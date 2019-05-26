package com.compiler.lexicalAnalysis.tokens;

import static com.compiler.lexicalAnalysis.Tag.STR;

/**
 * Author:
 * Date: Created In 17:08 2019/5/26
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */
public class Str extends Token {
    private String str;

    public Str() {
    }

    public Str(String str) {
        super();
        setTag(STR);
        this.str = str;
    }

    public Str(String str, int line, int col){
        super(STR, line, col);
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
