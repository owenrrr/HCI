package com.example.demo.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/6/3 14:54
 * @Description:
 */
@Data
public class Characters {

    @JSONField(name = "出生")
    private String birth;

    @JSONField(name = "血统")
    private String blood;

    @JSONField(name = "婚姻状况")
    private String marry;

    @JSONField(name = "物种")
    private String species;

    @JSONField(name = "性别")
    private String gender;

    @JSONField(name = "逝世")
    private String dead;

    @JSONField(name = "头发颜色")
    private String hairColor;

    @JSONField(name = "皮肤颜色")
    private String skinColor;

    @JSONField(name = "眼睛颜色")
    private String eyeColor;

    @JSONField(name = "学院")
    private String college;

    @JSONField(name = "职业")
    private String[] jobs;
}
