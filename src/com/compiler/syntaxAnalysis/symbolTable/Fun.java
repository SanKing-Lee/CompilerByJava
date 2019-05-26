package com.compiler.syntaxAnalysis.symbolTable;

import com.compiler.lexicalAnalysis.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Author:
 * Date: Created In 17:12 2019/5/26
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */
public class Fun {
    boolean externed = false;   // 是否为extern声明
    Tag type;       // 返回类型
    String name;    // 函数名称
    ArrayList<Var> paraVar = new ArrayList<>();  // 参数
    int maxDepth = 0;   // 栈的最大深度
    int currEsp = 0;     // 当前栈指针的位置
    Stack<Integer> scopeEsp = new Stack<>();    // 作用域栈指针位置

    /**
     * default construtor
     */
    public Fun() {

    }

    /**
     * constructor used
     *
     * @param externed 是否为extern声明
     * @param type     返回类型
     * @param name     函数名称
     * @param paraVar  参数列表
     */
    public Fun(boolean externed, Tag type, String name, ArrayList<Var> paraVar) {
        this.externed = externed;
        this.type = type;
        this.name = name;
        this.paraVar = paraVar;
        currEsp = 0;
        maxDepth = 0;
        // 为参数计算偏移量
        for (int i = 0, argOff = 8; i < paraVar.size(); i++, argOff += 4) {
            paraVar.get(i).setOffset(argOff);
        }
    }

    /**
     * 比较该函数和传入的函数是否为同一个函数声明
     *
     * @param fun 传入需要比较的函数
     * @return 比较的结果
     */
    public boolean match(Fun fun) {
        if (!(name.equals(fun.getName()))){
            return false;
        }
        if (paraVar.size() != fun.getParaVar().size()) {
            return false;
        }
        // 逐个比较参数
        int len = paraVar.size();
        for (int i = 0; i < len; i++){
            // 任意参数类型不同，则视为函数声明不同
            if(!(paraVar.get(i).getType().equals(fun.getParaVar().get(i).getType()))){
                return false;
            }
        }
        // 比较返回类型
        if(!type.equals(fun.getType())){
            return false;
        }
        return true;
    }

    public void define(Fun def){
        externed = false;
        paraVar = def.getParaVar();
    }

    public void enterScope() {
        scopeEsp.push(0);
    }

    public void leaveScope() {
        maxDepth = (currEsp > maxDepth) ? currEsp : maxDepth;
//        currEsp -= scopeEsp.pop();
    }

    public void locate(Var var) {
        int size = var.getSize();
        size += (4 - size % 4) % 4;
        scopeEsp.push(scopeEsp.pop() + size);
        currEsp += size;
        var.setOffset(-currEsp);
    }

    @Override
    public String toString() {
        StringBuilder sbScopeEsp = new StringBuilder();
        for (int i = 0; i < scopeEsp.size(); i++) {
            sbScopeEsp.append(scopeEsp.get(i).toString());
        }
        return "Fun{" +
                "externed=" + externed +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", paraVar=" + Arrays.toString(paraVar.toArray()) +
                ", maxDepth=" + maxDepth +
                ", currEsp=" + currEsp +
                ", scopeEsp=" + sbScopeEsp.toString() +
                '}';
    }

    public boolean isExterned() {
        return externed;
    }

    public void setExterned(boolean externed) {
        this.externed = externed;
    }

    public Tag getType() {
        return type;
    }

    public void setType(Tag type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Var> getParaVar() {
        return paraVar;
    }

    public void setParaVar(ArrayList<Var> paraVar) {
        this.paraVar = paraVar;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getCurrEsp() {
        return currEsp;
    }

    public void setCurrEsp(int currEsp) {
        this.currEsp = currEsp;
    }

    public Stack<Integer> getScopeEsp() {
        return scopeEsp;
    }

    public void setScopeEsp(Stack<Integer> scopeEsp) {
        this.scopeEsp = scopeEsp;
    }
}
