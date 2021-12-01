package com.example.demo.controller;

import com.example.demo.bl.QAService;
import com.example.demo.vo.ResponseVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Owen
 * @Date: 2021/6/2 14:25
 * @Description:
 */
@RestController
@RequestMapping("/api/qa")
@Api(value = "测试接口", tags="问答系统接口")
public class QAController {

    @Autowired
    QAService qaService;

    @GetMapping("/simple_question")
    @ApiOperation(value = "基本问答", notes = "基本问答")
    ResponseVO simpleQA(@RequestParam("question") String qs, @RequestParam("pid") Integer pid){
        String result = qaService.execute(qs, pid);
        return ResponseVO.buildSuccess(result);
    }
}
