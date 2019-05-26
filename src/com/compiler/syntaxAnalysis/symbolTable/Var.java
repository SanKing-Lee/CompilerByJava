package com.compiler.syntaxAnalysis.symbolTable;

import com.compiler.lexicalAnalysis.Tag;
import com.compiler.lexicalAnalysis.tokens.Char;
import com.compiler.lexicalAnalysis.tokens.Double;
import com.compiler.lexicalAnalysis.tokens.Int;
import com.compiler.lexicalAnalysis.tokens.Str;
import com.compiler.lexicalAnalysis.tokens.Token;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.compiler.lexicalAnalysis.Tag.KW_DOUBLE;

/**
 * Author:
 * Date: Created In 17:12 2019/5/26
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */
public class Var {
    boolean literal = false;        // 是否常量
    Stack<Integer> scopePath = new Stack<>();      // 作用域路径
    boolean externed = false;       // 是否为extern声明
    Tag type;               // 变量的类型
    String name;            // 变量名称
    boolean isPtr = false;          // 是否为指针

    boolean isArray = false;        // 是否为数组
    int arraySize = 0;          // 数组长度

    boolean isLeft = true;         // 是否可以作为左值

    Var initData = null;           // 初始数据
    boolean inited = false;         // 是否初始化

    int intVal;             // 初始值
    char charVal;
    double doubleVale;

    String strVal;  // 字符串常量初值

    String ptrVal;  // 字符指针初值
    Var ptr;        // 指针指向的变量

    int size = 0;       // 变量的大小
    int offset;     // 偏移量

    private String genLb(){
        String token = (System.currentTimeMillis() + new Random().nextInt(9999))+"";
        try{
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] md5 = md.digest(token.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            String code = encoder.encode(md5);
            String regEx = "[+%& /?#&=]";
            Pattern p = Pattern.compile(regEx);
            Matcher matcher = p.matcher(code);
            return matcher.replaceAll("").trim();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    public Var() {

    }

    // 创建常量型变量
    public Var(Token lt){
        literal = true;
        isLeft = false;
        switch(lt.getTag()){
            case INT:
                type = Tag.KW_INT;
                name = "<int>";
                intVal = ((Int)lt).getVal();
                break;
            case DOUBLE:
                type = KW_DOUBLE;
                name = "<double>";
                doubleVale = ((Double)lt).getVal();
                break;
            case CH:
                type = Tag.KW_CHAR;
                name = "<char>";
                intVal = 0;
                charVal = ((Char)lt).getCh();
                break;
            case STR:
                type = Tag.KW_CHAR;
                name = genLb();
                strVal = ((Str)lt).getStr();
                isArray = true;
                arraySize = strVal.length()+1;
                break;
        }
    }

    public Var(Stack<Integer> scopePath, boolean externed, Tag type, String name, boolean isPtr, Var initData) {
        for(int i = 0; i < scopePath.size(); i++){
            this.scopePath.push(scopePath.get(i));
        }
        this.externed = externed;
        this.type = type;
        this.name = name;
        this.isPtr = isPtr;
        this.initData = initData;
        inited = true;
    }

    @Override
    public String toString() {
        StringBuilder sbScopeEsp = new StringBuilder();
        for(int i = 0; i < scopePath.size(); i++){
            sbScopeEsp.append(scopePath.get(i));
        }
        return "Var{" +
                "literal=" + literal +
                ", scopePath=" + sbScopeEsp.toString() +
                ", externed=" + externed +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", isPtr=" + isPtr +
                ", isArray=" + isArray +
                ", arraySize=" + arraySize +
                ", isLeft=" + isLeft +
                ", initData=" + initData +
                ", inited=" + inited +
                ", intVal=" + intVal +
                ", charVal=" + charVal +
                ", doubleVale=" + doubleVale +
                ", strVal='" + strVal + '\'' +
                ", ptrVal='" + ptrVal + '\'' +
                ", ptr=" + ptr +
                ", size=" + size +
                ", offset=" + offset +
                '}';
    }

    public boolean isLiteral() {
        return literal;
    }

    public void setLiteral(boolean literal) {
        this.literal = literal;
    }

    public Stack<Integer> getScopePath() {
        return scopePath;
    }

    public void setScopePath(Stack<Integer> scopePath) {
        this.scopePath = scopePath;
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

    public boolean isPtr() {
        return isPtr;
    }

    public void setPtr(boolean ptr) {
        isPtr = ptr;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public int getArraySize() {
        return arraySize;
    }

    public void setArraySize(int arraySize) {
        this.arraySize = arraySize;
    }

    public boolean isLeft() {
        return isLeft;
    }

    public void setLeft(boolean left) {
        isLeft = left;
    }

    public Var getInitData() {
        return initData;
    }

    public void setInitData(Var initData) {
        this.initData = initData;
    }

    public boolean isInited() {
        return inited;
    }

    public void setInited(boolean inited) {
        this.inited = inited;
    }

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    public char getCharVal() {
        return charVal;
    }

    public void setCharVal(char charVal) {
        this.charVal = charVal;
    }

    public String getStrVal() {
        return strVal;
    }

    public void setStrVal(String strVal) {
        this.strVal = strVal;
    }

    public String getPtrVal() {
        return ptrVal;
    }

    public void setPtrVal(String ptrVal) {
        this.ptrVal = ptrVal;
    }

    public Var getPtr() {
        return ptr;
    }

    public void setPtr(Var ptr) {
        this.ptr = ptr;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
