package com.compiler.syntaxAnalysis;

import com.compiler.lexicalAnalysis.LexicalAnalysis;
import com.compiler.lexicalAnalysis.Tag;
import com.compiler.lexicalAnalysis.tokens.Id;
import com.compiler.lexicalAnalysis.tokens.Int;
import com.compiler.lexicalAnalysis.tokens.Token;
import com.compiler.syntaxAnalysis.symbolTable.Fun;
import com.compiler.syntaxAnalysis.symbolTable.SymTab;
import com.compiler.syntaxAnalysis.symbolTable.Var;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.compiler.lexicalAnalysis.Tag.*;
import static com.compiler.lexicalAnalysis.Tag.STR;

/**
 * Author: Sean
 * Date: Created In 23:58 2019/5/26
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class SymTabGenerate {
    private SymTab symTab;
    private ArrayList<Token> tokens;
    private LexicalAnalysis lexicalAnalysis;
    private int Pos = 0;
    private Token token;
    private int errorCount = 0;

    public SymTabGenerate() {
        symTab = new SymTab();
        lexicalAnalysis = new LexicalAnalysis("test.c");
        tokens = (ArrayList<Token>) lexicalAnalysis.analyze();
    }

    /**
     * 扫描词法记号
     */
    public void scan() {
        if (Pos < tokens.size()) {
            token = tokens.get(Pos++);
        }
    }

    /**
     * 比较当前的词法记号是否与传入的tag匹配
     *
     * @param ter 需要比较的Tag
     * @return 是否匹配
     */
    private boolean compare(Tag ter) {
        return token.getTag().equals(ter);
    }

    /**
     * 获得当前词法记号变量类型
     *
     * @return 如果当前的词法记号为一个类型，则返回该类型，否则返回空
     */
    private Tag matchType() {
        Tag type;
        switch (token.getTag()) {
            case KW_INT:
                type = INT;
                break;
            case KW_DOUBLE:
                type = DOUBLE;
                break;
            case KW_CHAR:
                type = CH;
                break;
            case KW_STRING:
                type = STR;
                break;
            default:
                type = null;
                break;
        }
        if (type != null) {
            scan();
        }
        return type;
    }

    /**
     * 匹配Tag并扫描，如果匹配则扫描，否则就不继续扫描
     *
     * @param ter 传入的Tag
     * @return 是否匹配
     */
    private boolean match(Tag ter) {
        boolean ret = token.getTag().equals(ter);
        if (ret) {
            scan();
        }
        return ret;
    }

    /**
     * 输出错误信息
     *
     * @param error 错误信息
     */
    private void errorMessage(String error) {
        System.out.println("行：" + token.getPosition().getRowNumber() +
                ", 列：" + token.getPosition().getColNumber() +
                " : " + error);
        errorCount++;
    }

    /**
     * 根据词法记号产生一个符号表
     *
     * @return 获得的符号表
     */
    public SymTab generateSymTab() {
//        generateSymList();
//        while (hasNextSym()) {
//            if (currSymbol instanceof Token) {
//                Token ter = (Token) currSymbol;
//                if (match(Tag.LBRACE)) {
//                    symTab.enter();
//                }
//                if (match(Tag.RBRACE)) {
//                    symTab.leave();
//                }
//            } else if (currSymbol instanceof String) {
//                String nonTer = (String) currSymbol;
//                boolean externed;
//                switch (nonTer) {
//                    case "<segment>":
//                        move();
//                        if (match(Tag.KW_EXTERN)) {
//                            move();
//                            externed = true;
//                        }
//                        if (match("<segment'>"))
//                }
//            }
//        }
        Pos = 0;
        // 读入一个词法记号
        scan();
        symTab.enter();
        // 读所有的词法记号
        while (!token.getTag().equals(END)) {
            // 变量声明的类型
            Tag type = null;
            // 左花括号进入一个新的作用域
            if (match(LBRACE)) {
                symTab.enter();
            }
            // 右花括号离开当前的作用域
            if (match(RBRACE)) {
                symTab.leave();
            }
            // extern声明
            if (match(KW_EXTERN)) {
                type = matchType();
                if (type != null) {
                    defdata(true, type);
                } else {
                    errorMessage("");
                }
            }
            // 普通的显式声明
            type = matchType();
            if (type != null) {
                defdata(false, type);
            }
            // 更新当前的词法记号
            scan();
        }
        System.out.println("finish!");
        return symTab;
    }

    //-----------------------------------------------变量处理----------------------------------------------//

    /**
     * 完整的全局变量或局部变量的形式
     * @param ext   是否为extern声明
     * @param type  变量类型
     * @return  变量对应的对象
     */
    private Var defdata(boolean ext, Tag type) {
        String name = "";
        // 添加普通变量或函数
        if (compare(ID)) {
            Id id = (Id) token;
            name = id.getName();
            scan();
            // 变量
            if(!compare(LPAREN)) {
                return varrdef(ext, type, false, name);
            }
            else{
                scan();
                idtail(ext, type, name);
            }
        }
        // 添加数组
        else if (match(MUL)) {
            if (compare(ID)) {
                name = ((Id) token).getName();
                scan();
                return varrdef(ext, type, true, name);
            }
        }
        return null;
    }

    /**
     * 变量声明的后半部分
     *
     * @param ext  是否为extern声明
     * @param type 变量类型
     * @param ptr  是否为一个指针
     * @param name 变量名称
     * @return 该变量对应的Var对象
     */
    private Var varrdef(boolean ext, Tag type, boolean ptr, String name) {
        // 数组的声明
        if (match(LBRACK)) {
            // 数组长度
            int len = 0;
            if (match(INT)) {
                len = ((Int) token).getVal();
            }
            if (match(RBRACK)) {
                return init(ext, type, ptr, name);
            }
        }
        // 普通变量
        else {
            return init(ext, type, ptr, name);
        }
        return null;
    }

    /**
     * 变量初始化部分
     *
     * @param ext  是否为extern声明
     * @param type 变量类型
     * @param ptr  是否为指针
     * @param name 变量名称
     * @return 变量对应的对象
     */
    private Var init(boolean ext, Tag type, boolean ptr, String name) {
        Var initVal = null;
        // 后面跟着ASSIGN，说明有对该变量进行声明时初始化
        if (match(ASSIGN)) {
            initVal = expr();
        }
        // 添加变量
        Var var = new Var(symTab.getScopePath(), ext, type, name, ptr, initVal);
        addVarToSymTab(var);
        return var;
    }

    //-----------------------------------------------函数处理----------------------------------------------//

    /**
     * 函数标识符之后的部分
     * @param ext   是否为extern声明
     * @param type  返回类型
     * @param name  函数名称
     */
    private void idtail(boolean ext, Tag type, String name) {
        // 进入新的作用域
        symTab.enter();
        // 分析形参列表
        ArrayList<Var> paraList = para();
        Fun fun = new Fun(ext, type, name, paraList);
        funtail(fun);
        symTab.leave();
    }

    /**
     * 函数的参数列表处理
     * @return  参数列表
     */
    private ArrayList<Var> para() {
        ArrayList<Var> paraList = new ArrayList<>();
        while(!match(RPAREN)) {
            Tag type = matchType();
            if (type != null) {
                paraList.add(defdata(false, type));
            }
            if(compare(COMMA)){
                scan();
            }
        }
        return paraList;
    }

    /**
     * 函数声明之后的尾部
     *
     * @param fun
     */
    private void funtail(Fun fun) {
        // 后面的符号为分号，说明是一个函数声明
        if (match(SEMICON)) {
            decFun(fun);
        }
        // 函数定义
        else {
            defFun(fun);
        }
    }

    /**
     * 添加函数声明
     *
     * @param fun 要声明的函数
     */
    private void decFun(Fun fun) {
        // 将externed字段设置为true，表示函数的声明
        fun.setExterned(true);
        HashMap<String, Fun> funTab = symTab.getFunTab();
        Fun presFun = funTab.getOrDefault(fun.getName(), null);
        // 未找到该函数
        if (presFun == null) {
            // 添加该函数
            funTab.put(fun.getName(), fun);
        } else {
            // 匹配失败，即这两个重名函数有相同的形式，导致冲突
            if (!fun.match(presFun)) {
                errorMessage("函数声明冲突！");
            }
        }
    }

    /**
     * 添加函数定义
     * @param fun   函数头
     */
    private void defFun(Fun fun) {
        // 函数声明不能为extern
        if(fun.isExterned()){
            fun.setExterned(false);
        }
        HashMap<String, Fun> funTab = symTab.getFunTab();
        Fun presFun = funTab.getOrDefault(fun.getName(), null);
        // 未找到该函数
        if (presFun == null) {
            // 添加该函数
            funTab.put(fun.getName(), fun);
        }
        // 该函数头已存在
        else{
            // 存在的函数头为声明
            if(presFun.isExterned()){
                // 存在的函数头和该函数头不匹配
                if(!presFun.match(fun)){
                    return;
                }
                // 将声明信息替换为定义信息
                presFun.define(fun);
            }
            // 函数重定义了
            else{
                return;
            }
            fun = presFun;
        }
        symTab.setCurrFun(fun);
        funTab.put(fun.getName(), fun);
    }


    // 处理常量
    private Var literal() {
        Var v = null;
        if (compare(DOUBLE) || compare(INT) || compare(STR) || compare(CH)) {
            v = new Var(token);
            if (compare(STR)) {
                addStrToSymTab(v);
            } else {
                addVarToSymTab(v);
            }
            scan();
        }
        return v;
    }

    /**
     * 将变量添加到符号表中
     * @param var   要添加的变量
     */
    private void addVarToSymTab(Var var) {
        HashMap<String, ArrayList<Var>> varTab = symTab.getVarTab();
        ArrayList<Var> vars = varTab.getOrDefault(var.getName(), new ArrayList<>());
        boolean existed = false;
        // 遍历已有的变量列表
        for (Var var1 : vars) {
            // 判断作用域是否相同，如果相同就不添加且报错
            if (var1.getScopePath().peek().equals(var.getScopePath().peek())) {
                existed = true;
                break;
            }
        }

        if (!existed || var.getName().startsWith("<")) {
            vars.add(var);
            varTab.put(var.getName(), vars);
        } else {
            errorMessage("重复定义!");
            return;
        }
    }

    /**
     * 将常量字符串添加到符号表中
     * @param var   要添加的常量字符串
     */
    private void addStrToSymTab(Var var) {
        HashMap<String, Var> strTab = symTab.getStrTab();
        for (Map.Entry<String, Var> entry : strTab.entrySet()) {
            Var value = entry.getValue();
            // 该字符串常量已存在
            if (value.getStrVal().equals(var.getStrVal())) {
                return;
            }
        }
        strTab.put(var.getName(), var);
    }

    /**
     * 获得名称对应的变量
     * @param name  变量名
     * @return  变量名对应的变量对象
     */
    private Var getVar(String name) {
        Var select = null;
        HashMap<String, ArrayList<Var>> varTab = symTab.getVarTab();
        ArrayList<Var> vars = varTab.get(name);
        if (vars != null) {
            // 获取当前的作用域路径长度
            int pathLen = symTab.getScopePath().size();
            // 最大匹配的作用域路径长度
            int maxLen = 0;
            // 遍历符号表中的所有变量
            for (Var var : vars) {
                // 该变量的作用域路径长度
                int len = var.getScopePath().size();
                // 变量的作用域路径长度小于当前的作用域路径
                // 且为了找到最近的作用域，需要按照最长匹配的原则来进行寻找
                if ((len <= pathLen) &&
                        (var.getScopePath().get(len - 1).equals(symTab.getScopePath().get(len - 1)))) {
                    if (len > maxLen) {
                        maxLen = len;
                        select = var;
                    }
                }
            }
        }
        if (select == null) {
            errorMessage("变量未声明");
        }
        return select;
    }


    public Var expr() {
        return new Var();
    }

//    private void generateSymList() {
//        DefaultMutableTreeNode currentNode = root;
//        Stack<DefaultMutableTreeNode> treeNodeStack = new Stack<>();
//        Pos = 0;
//        treeNodeStack.push(currentNode);
//        while (!treeNodeStack.empty()) {
//            // 当前节点
//            DefaultMutableTreeNode tempNode = treeNodeStack.pop();
//            symList.add(tempNode);
//            // 遍历孩子节点，并按照逆序压入栈中
//            for (int i = tempNode.getChildCount() - 1; i >= 0; i--) {
//                treeNodeStack.push((DefaultMutableTreeNode) (tempNode.getChildAt(i)));
//            }
//        }
//    }

}
