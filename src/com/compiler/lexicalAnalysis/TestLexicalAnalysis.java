package com.compiler.lexicalAnalysis;

import java.io.File;

/**
 * Author: Sean
 * Date: Created In 18:21 2019/4/9
 * Title: 测试词法分析器
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class TestLexicalAnalysis {
    public static void main(String[] args){
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis("test.c");
        lexicalAnalysis.analyze();
    }
}
