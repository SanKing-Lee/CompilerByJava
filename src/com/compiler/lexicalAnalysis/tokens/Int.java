package com.compiler.lexicalAnalysis.tokens;

import static com.compiler.lexicalAnalysis.Tag.INT;

/**
 * Author:
 * Date: Created In 17:07 2019/5/26
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */
public class Int extends Token {
    private int val;

    public Int() {

    }

    public Int(int val) {
        super();
        setTag(INT);
        this.val = val;
    }

    public Int(int val, int line, int col) {
        super(INT, line, col);
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String toString() {
        return "整型数字：" + val;
    }
}
