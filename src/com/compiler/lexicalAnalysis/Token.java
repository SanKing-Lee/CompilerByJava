package com.compiler.lexicalAnalysis;

import java.io.*;
import java.util.HashMap;

import static com.compiler.lexicalAnalysis.Tag.*;

/**
 * Author: Sean
 * Date: Created In 20:58 2019/4/9
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class Token {
    private Tag tag;

    Token() {
    }

    Token(Tag tag) {
        this.tag = tag;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String toString(){
        return "关键字：" + tag;
    }
}

class Id extends Token {
    private String name;

    Id() {
    }

    Id(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "标识符：" + name;
    }
}

class Int extends  Token{
    private int val;

    Int(){

    }

    Int(int val){
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String toString(){
        return "整型数字：" + val;
    }
}

class Double extends Token {
    private double val;

    Double() {
    }

    Double(double val) {
        this.val = val;
    }

    public double getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String toString() {
        return "浮点数：" + val;
    }

}

class Char extends Token {
    private char ch;

    Char() {
    }

    Char(char ch) {
        this.ch = ch;
    }

    public char getCh() {
        return ch;
    }

    public void setCh(char ch) {
        this.ch = ch;
    }

    public String toString() {
        return "字符常量：" + ch;
    }
}

class Str extends Token {
    private String str;

    Str() {
    }

    Str(String str) {
        this.str = str;
    }


    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String toString() {
        return "字符串常量：" + str;
    }
}

class KeyWords {
    private HashMap<String, Tag> keyWords = new HashMap<>();

    private String keyWordsFileLocation = "./keyWords.txt";

    public Tag getTag(String name) {
        return keyWords.getOrDefault(name, ID);
    }

    public KeyWords() {
        keyWords.put("int", KW_INT);
        keyWords.put("double", KW_DOUBLE);
        keyWords.put("char", KW_CHAR);
        keyWords.put("string", KW_STRING);
        keyWords.put("void", KW_VOID);
        keyWords.put("extern", KW_EXTERN);
        keyWords.put("if", KW_IF);
        keyWords.put("else", KW_ELSE);
        keyWords.put("switch", KW_SWITCH);
        keyWords.put("case", KW_CASE);
        keyWords.put("default", KW_DEFAULT);
        keyWords.put("while", KW_WHILE);
        keyWords.put("do", KW_DO);
        keyWords.put("for", KW_FOR);
        keyWords.put("break", KW_BREAK);
        keyWords.put("continue", KW_CONTINUE);
        keyWords.put("return", KW_RETURN);
    }

    public HashMap<String, Tag> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(HashMap<String, Tag> keyWords) {
        this.keyWords = keyWords;
    }
}