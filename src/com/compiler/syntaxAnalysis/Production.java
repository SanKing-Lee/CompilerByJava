package com.compiler.syntaxAnalysis;

import com.compiler.lexicalAnalysis.Tag;

import java.util.List;
import java.util.Properties;

/**
 * Author: Sean
 * Date: Created In 23:42 2019/5/5
 * Title: 产生式类
 * Description: 描述产生式的类，包括了它的左部和右部
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class Production {
    private Integer id;
    private String left;
    private List<String> right;

    public Production(Integer id, String left, List<String> right) {
        this.id = id;
        this.left = left;
        this.right = right;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public List<String> getRight() {
        return right;
    }

    public void setRight(List<String> right) {
        this.right = right;
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(id + ": " + left + "->");
        for(int i = 0; i < right.size(); i++){
            stringBuilder.append(right.get(i));
            if(i < right.size()-1){
                stringBuilder.append(" ");
            }
        }
        return stringBuilder.toString();
    }

    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        Production proObj = (Production)obj;
        return toString().equals(proObj.toString());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
