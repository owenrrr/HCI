package com.example.demo.vo;

import lombok.Data;

/**
 * @Author: Owen
 * @Date: 2021/3/5 21:55
 * @Description:
 */
@Data
public class ResponseVO {

    private Boolean success;

    private String message;

    private Object content;

    public static ResponseVO buildSuccess(){
        ResponseVO response=new ResponseVO();
        response.setSuccess(true);
        return response;
    }


    public static ResponseVO buildSuccess(Object content){
        ResponseVO response=new ResponseVO();
        response.setContent(content);
        response.setSuccess(true);
        return response;
    }

    public static ResponseVO buildFailure(String message){
        ResponseVO response=new ResponseVO();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }

    public static ResponseVO buildFailure(Object content){
        ResponseVO response=new ResponseVO();
        response.setContent(content);
        response.setSuccess(false);
        return response;
    }

}
