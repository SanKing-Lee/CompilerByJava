package com.compiler.lexicalAnalysis;

/**
 * Author:
 * Date: Created In 21:04 2019/4/10
 * Title:
 * Description:
 * Version:
 * Update History:
 * [Date][Version][Author] What has been done;
 */
public class Error {
    private String error;
    private int lineNumber;
    private int colNumber;
    public Error(){

    }

    public Error(String error, int lineNumber, int colNumber){
        this.error = error;
        this.lineNumber = lineNumber;
        this.colNumber = colNumber;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getColNumber() {
        return colNumber;
    }

    public void setColNumber(int colNumber) {
        this.colNumber = colNumber;
    }

    public String toString(){
        return "行：" + lineNumber +
                ", 列：" + colNumber +
                " : " + error;
    }
}
