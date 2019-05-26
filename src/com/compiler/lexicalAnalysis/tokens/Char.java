package com.compiler.lexicalAnalysis.tokens;

import static com.compiler.lexicalAnalysis.Tag.CH;
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
public class Char extends Token {
    private char ch;

    public Char() {
    }

    public Char(char ch) {
        super();
        setTag(CH);
        this.ch = ch;
    }

    public Char(char ch, int line, int col){
        super(CH, line, col);
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
