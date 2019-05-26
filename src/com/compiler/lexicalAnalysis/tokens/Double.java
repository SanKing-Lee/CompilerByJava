package com.compiler.lexicalAnalysis.tokens;

import static com.compiler.lexicalAnalysis.Tag.DOUBLE;
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
public class Double extends Token {
    private double val;

    public Double() {
    }

    public Double(double val) {
        super();
        setTag(DOUBLE);
        this.val = val;
    }

    public Double(double val, int line, int col){
        super(DOUBLE, line, col);
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
