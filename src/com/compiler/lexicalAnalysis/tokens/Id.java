package com.compiler.lexicalAnalysis.tokens;

import static com.compiler.lexicalAnalysis.Tag.ID;

/**
 * Author: Sean
 * Date: Created In 17:07 2019/5/26
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class Id extends Token {
    private String name;

    public Id() {
    }

    public Id(String name) {
        super();
        setTag(ID);
        this.name = name;
    }

    public Id(String name, int line, int col){
        super(ID, line, col);
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
