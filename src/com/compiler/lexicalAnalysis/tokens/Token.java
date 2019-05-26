package com.compiler.lexicalAnalysis.tokens;

import com.compiler.lexicalAnalysis.Position;
import com.compiler.lexicalAnalysis.Tag;

import java.io.*;
import java.util.HashMap;

import static com.compiler.lexicalAnalysis.Tag.*;

/**
 * Author: Sean
 * Date: Created In 20:58 2019/4/9
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class Token {
    private Tag tag;
    private Position position;

    public Token() {
    }

    public Token(Tag tag) {
        this.tag = tag;
    }

    public Token(Tag tag, int rowNumber, int colNumber){
        this.tag = tag;
        this.position = new Position(rowNumber, colNumber);
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public String toString(){
        return "关键字：" + tag;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}


