package com.example.demo.util;

/**
 * @Author: Owen
 * @Date: 2021/6/7 0:09
 * @Description:
 */
public enum Symbol {

    COMMA(","), DOUBLE_QUOTATION("\""), COLON(":"), LEFT_BRACKET("["), RIGHT_BRACKET("]"), OPEN_BRACE("{"), CLOSE_BRACE("}");

    private String symbol;

    private Symbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
