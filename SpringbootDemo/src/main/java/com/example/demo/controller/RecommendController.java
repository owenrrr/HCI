package com.example.demo.controller;


import com.example.demo.bl.RecommendService;
import com.example.demo.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/recommend")
@Api(value = "测试接口", tags="个性化推荐接口")
public class RecommendController {

    @Autowired
    RecommendService recommendService;

    @GetMapping("/simple_question")
    @ApiOperation(value = "个性化推荐", notes = "个性化推荐")
    ResponseVO simpleRecommend(@RequestParam("question") String qs, @RequestParam("pid") Integer pid){
        HashMap<String, Integer> result = recommendService.recommend(qs, pid);
        return ResponseVO.buildSuccess(result);
    }

}
