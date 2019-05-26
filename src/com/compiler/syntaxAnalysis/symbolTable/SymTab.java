package com.compiler.syntaxAnalysis.symbolTable;

import com.compiler.lexicalAnalysis.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Author: Sean
 * Date: Created In 14:03 2019/5/22
 * Title: 符号表
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class SymTab {
    private HashMap<String, ArrayList<Var>> varTab = new HashMap<>();     // 变量表
    private HashMap<String, Var> strTab = new HashMap<>();    // 字符串常量表
    private HashMap<String, Fun> funTab = new HashMap<>();    // 函数表
    private Fun currFun = null;    // 当前的函数
    private int scopeId = 0;    // 作用域编号
    private Stack<Integer> scopePath = new Stack<>();   // 作用域路径

    public void enter() {
        scopePath.push(scopeId++);
        if (currFun != null) {
            currFun.enterScope();
        }
    }

    public void leave() {
        scopePath.pop();
        if (currFun != null) currFun.leaveScope();
    }

    public String toString() {
        StringBuilder strBuil = new StringBuilder();
        strBuil.append("Var:\n");
        for (Map.Entry<String, ArrayList<Var>> entry : varTab.entrySet()) {
            ArrayList<Var> vars = entry.getValue();
            for(int i = 0; i < vars.size(); i++){
                strBuil.append(vars.get(i).toString() + "\n");
            }
        }
        strBuil.append("Str: \n");
        for(Map.Entry<String, Var> entry: strTab.entrySet()){
            Var var = entry.getValue();
            strBuil.append(var.toString()+"\n");
        }
        strBuil.append("Fun: \n");
        for(Map.Entry<String, Fun> entry: funTab.entrySet()){
            Fun fun = entry.getValue();
            strBuil.append(fun.toString() + "\n");
        }
        return strBuil.toString();
    }

    public void getVar(String name) {

    }

    public HashMap<String, ArrayList<Var>> getVarTab() {
        return varTab;
    }

    public void setVarTab(HashMap<String, ArrayList<Var>> varTab) {
        this.varTab = varTab;
    }

    public HashMap<String, Var> getStrTab() {
        return strTab;
    }

    public void setStrTab(HashMap<String, Var> strTab) {
        this.strTab = strTab;
    }

    public HashMap<String, Fun> getFunTab() {
        return funTab;
    }

    public void setFunTab(HashMap<String, Fun> funTab) {
        this.funTab = funTab;
    }

    public Fun getCurrFun() {
        return currFun;
    }

    public void setCurrFun(Fun currFun) {
        this.currFun = currFun;
    }

    public int getScopeId() {
        return scopeId;
    }

    public void setScopeId(int scopeId) {
        this.scopeId = scopeId;
    }

    public Stack<Integer> getScopePath() {
        return scopePath;
    }

    public void setScopePath(Stack<Integer> scopePath) {
        this.scopePath = scopePath;
    }
}

