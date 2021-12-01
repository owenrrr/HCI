package com.example.demo.blImpl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.bl.QAService;
import com.example.demo.bl.SynonymChecking;
import com.example.demo.dao.EntityMapper;
import com.example.demo.dao.RelationMapper;
import com.example.demo.po.Entity;
import com.example.demo.po.Relation;
import com.example.demo.util.aliyunOSS.PropertyReader;
import com.example.demo.vo.Characters;
import com.hankcs.hanlp.HanLP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.*;

/**
 * @Author: Owen
 * @Date: 2021/6/2 10:42
 * @Description:
 */
@Service
public class QAServiceImpl implements QAService {

    @Autowired
    EntityMapper entityMapper;
    @Autowired
    RelationMapper relationMapper;

    @Autowired
    SynonymChecking synonymChecking;

    private final double JACCARD_THRESHOLD = 0.5;

    private final double SIMILARITY_THRESHOLD = 0.7;

//    private final String RESOURCE_PATH = "/var/lib/jenkins/workspace/seiii/SpringbootDemo/src/main/resources";
    private final String RESOURCE_PATH = String.valueOf(Paths.get(System.getProperty("user.dir"), "src", "main", "resources"));

    /*
    * TODO 智能问答步骤
    *  1. 先将问题进行分词/关键词提取，得到候选的实体或关系
    *  2. 将候选的实体或关系通过同义词映射为已存在的实体或关系
    *  3. 将候选实体或关系算出优先级
    *  4. 根据characters.txt 和 relations.txt 可以得到确定的、已存在的关系和实体列表
    *  5. 将优先级高到低的关系和实体根据匹配规则得出结果
    *
    * TODO 匹配规则
    *  ps 匹配规则内也有优先级，下面按照数字先后排序
    *  1. 拥有关系和实体，得出另一个实体
    *  2. 拥有两个实体，得出实体间的关系
    *  3. 拥有一个实体，默认返回实体的属性
    * */

    /**
     * 内部嵌套类，用来表示匹配字符串和相似度
     * content: 是原问题中的文本； match_content: 是与之匹配的、已存在确定的文本
     */
    private class MatchWord {

        public float degree;

        public String content;

        public String match_content;

        public MatchWord(float degree, String content, String match_content){
            this.degree = degree;
            this.content = content;
            this.match_content = match_content;
        }

    }

    @Override
    public String execute(String question, Integer pid) {

        // step 1
        List<String> keywords = HanLP.extractKeyword(question, 5);
        System.out.println("keywords: " + keywords);

        List<MatchWord> entities = new LinkedList<>();
        List<MatchWord> relations = new LinkedList<>();

        try{
            // step 4
            File file = new File(RESOURCE_PATH + "/voc/characters.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String result = br.readLine();
            String[] character_array = null;
            if (result != null && result.length() > 0){
                character_array = result.split(",");
            }else{
                character_array = new String[0];
            }


            file = new File(RESOURCE_PATH + "/voc/relations.txt");
            br = new BufferedReader(new FileReader(file));
            result = br.readLine();
            String[] relations_array = null;
            if (result != null && result.length() > 0){
                relations_array = result.split(",");
            }else{
                relations_array = new String[0];
            }


            //step 5 (includes step 2: using on relation)
            float degree = 0;
            double similarity = 0.0;
            for (String element: keywords){
                for (String s: character_array){
                    degree = JaccardDegree(s, element);
                    if (degree > JACCARD_THRESHOLD){
                        entities.add(new MatchWord(degree, element, s));
                    }
                }
                for (String s: relations_array){
                    similarity = synonymChecking.getSimilarity(s, element);
                    if (similarity > SIMILARITY_THRESHOLD){
                        relations.add(new MatchWord((float)similarity, element, s));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        // step 6
        if (!entities.isEmpty() && !relations.isEmpty()){
            // 拿确定且必定存在的数据进行匹配(ac开头为确定的,没有ac开头是问题本身的)
            MatchWord ent_candidate = new MatchWord(0, "", "");
            MatchWord rel_candidate = new MatchWord(0, "", "");
            for (MatchWord m: entities){
                if (m.degree > ent_candidate.degree){
                    ent_candidate = m;
                }
            }
            for (MatchWord m: relations){
                if (m.degree > rel_candidate.degree){
                    rel_candidate = m;
                }
            }

            String ac_ent = ent_candidate.match_content;
            String ac_rel = rel_candidate.match_content;
            String ent = ent_candidate.content;
            String rel = rel_candidate.content;

            List<Relation> match = new ArrayList<>();

            // 假定实体是source
            match = relationMapper.getRelationsBySourceAndRelation(ac_ent, ac_rel, pid);
            if (match.size() > 0){
                return String.format("是%s", match.get(0).getTarget());
            }
            // 假定实体是target
            match = relationMapper.getRelationsByTargetAndRelation(ac_ent, ac_rel, pid);
            if (match.size() > 0){
                return String.format("是%s", match.get(0).getSource());
            }
            // 由relation查找，遍历结果并再次使用相似度进行匹配
            // 假定任意相似度 > 0.5即可
            match = relationMapper.getRelationsByRelation(ac_rel, pid);
            float a = 0, b = 0;
            for (Relation r: match){
                a = JaccardDegree(r.getSource(), ent);
                b = JaccardDegree(r.getTarget(), ent);
                if (a > 0.5){
                    return String.format("%s和%s之间的关系是%s", ent,r.getTarget(), rel);
                }
                if (b > 0.5){
                    return String.format("%s和%s之间的关系是%s", ent,r.getSource(), rel);
                }
            }
        }

        if (entities.size() > 1 && keywords.size() > 1){
            MatchWord candidate1 = new MatchWord(0, "", "");
            MatchWord candidate2 = new MatchWord(0, "", "");
            MatchWord tmp = null;

            for (MatchWord m: entities){
                if (candidate1.degree > candidate2.degree){
                    // 确保 candidate2.degree > candidate1.degree
                    tmp = candidate1;
                    candidate1 = candidate2;
                    candidate2 = tmp;
                }
                if (m.degree >= candidate2.degree){
                    candidate1 = candidate2;
                    candidate2 = m;
                }else if (m.degree >= candidate1.degree){
                    candidate1 = m;
                }
            }

            String ent1 = candidate1.match_content;
            String ent2 = candidate2.match_content;

            List<Relation> match = new ArrayList<>();

            match = relationMapper.getRelationsByTwoEntities(ent1, ent2, pid);
            if (match.size() > 0){
                return String.format("%s和%s之间的关系是%s", candidate1.content, candidate2.content, match.get(0).getRelation());
            }
        }

        if (entities.size() > 0){
            MatchWord candidate = new MatchWord(0, "", "");
            for (MatchWord m: entities){
                if (m.degree >= candidate.degree){
                    candidate = m;
                }
            }

            Entity ent = entityMapper.getEntityByName(candidate.match_content);
            if (ent != null){
                Characters tmp = jsonToObject(ent.getProperty());
                if (tmp != null){
                    return propertyPattern(tmp, candidate.content, candidate.match_content);
                }
            }
        }


        return "无匹配的资料!";
    }

    /**
     * @param: src, dst
     * @description: Jaccard相似度
     */
    @Override
    public float JaccardDegree(String a, String b){

        Set<Character> s1 = new HashSet<>();//set元素不可重复
        Set<Character> s2 = new HashSet<>();

        for (int i = 0; i < a.length(); i++) {
            s1.add(a.charAt(i));//将string里面的元素一个一个按索引放进set集合
        }
        for (int j = 0; j < b.length(); j++) {
            s2.add(b.charAt(j));
        }

        float mergeNum = 0;//并集元素个数
        float commonNum = 0;//相同元素个数（交集）

        for(Character ch1:s1) {
            for(Character ch2:s2) {
                if(ch1.equals(ch2)) {
                    commonNum++;
                }
            }
        }

        mergeNum = s1.size() + s2.size() - commonNum;

        return (float)(commonNum / mergeNum);

    }


    /**
     * @description: 解析JSON字符串成Java Object
     */
    @Override
    public Characters jsonToObject(String jsonString){
        Characters character = JSONObject.parseObject(jsonString, Characters.class);
        return character;
    }

    private String propertyPattern(Characters character, String source ,String match){
        StringBuilder builder = new StringBuilder();

        // 0: 它； 1：他； 2：她
        String it = "它";
        if (character.getGender() != null){
            if (character.getGender().equals("男")){
                it = "他";
            }else{
                it = "她";
            }
        }

        if (character.getBirth() != null){
            builder.append(String.format("%s出生在%s", match ,character.getBirth()));
        }
        if (character.getBlood() != null){
            if (builder.length() > 0){
                builder.append(String.format(",%s的血统是%s", it, character.getBlood()));
            }else{
                builder.append(String.format("%s的血统是%s", it, character.getBlood()));
            }
        }
        if (character.getMarry() != null){
            builder.append(String.format(",%s%s", it, character.getMarry()));
        }
        if (character.getSpecies() != null){
            builder.append(String.format("且%s是个%s。", it, character.getSpecies()));
        }
        if (character.getJobs() != null && character.getJobs().length > 0){
            builder.append(String.format("%s当过", it));
            String[] jobs = character.getJobs();
            for (int i = 0; i < jobs.length; ++i){
                if (i != jobs.length - 1){
                    builder.append(String.format("%s、", jobs[i]));
                }else{
                    builder.append(String.format("%s。", jobs[i]));
                }
            }
        }
        if (builder.length() < 10){
            builder.append(String.format("'%s' 匹配到 '%s',且无更多的信息...", source, match));
        }
        return String.valueOf(builder);
    }
}
