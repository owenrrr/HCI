
## 项目目录

```
src/main/resources
--data
|	|
|	--dictionary
|   |     |
|   |     --characters_dic.txt(Project: data)
|	--model
--jar
|	|
|	--hanlp-1.8.1.jar
|	--hanlp-1.8.1-sources.jar
--voc
|	|
|	--characters.txt
|	--relations.txt
|   --property.txt
--application.yml
|
--clilin.txt
|
--hanlp.properties
|
--data.sql
|
--schema.sql	

```



## 版本迭代
    
| 日期             | 功能                                | 版本 |
| ---------------- | ----------------------------------- | ---- |
| 2021/06/02 14:00 | 基本问答功能完成，无优化            | v1.0 |
| 2021/06/02 23:21 | 实体和关系使用Jaccard相似度进行筛选 | v1.1 |
| 2021/06/03       | 1. 关系使用同义词进行筛选           |      |
| 2021/06/05       | 添加个性化推荐controller, 在RecommendServiceImplTest中写了一些改良智能问答相关的东西 | v1.2 |                                     |      |

## 接口文档
- 采用 swagger2
- 本地测试，访问 http://localhost:8088/swagger-ui.html
- 接口、类型、参数、返回类型
- 采用JSON格式，支持在线测试数据格式
