package com.example.demo.controller;

import com.example.demo.bl.GrammarAnalyseService;
import com.example.demo.vo.GrammarToken;
import com.example.demo.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/grammar")
@Api(value = "测试接口", tags="语义分析接口")
public class GrammarAnalyseController {
    @Autowired
    GrammarAnalyseService grammarAnalyseService;

    @GetMapping("/grammar_analyse")
    @ApiOperation(value = "语义分析", notes = "语义分析")
    ResponseVO grammarAnalyse(@RequestParam("question") String qs, @RequestParam("pid") Integer pid){
        GrammarToken result = grammarAnalyseService.grammarAnalyse(qs, pid);
        return ResponseVO.buildSuccess(result);
    }
}

