package com.compiler.lexicalAnalysis;

import java.io.File;

/**
 * Author: Sean
 * Date: Created In 10:00 2019/4/10
 * Title: 测试扫描器
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class TestScanner {
    public static void main(String[] args){
        File testFile = new File("test.c");
        Scanner test = new Scanner(testFile);
        while(test.getReadPos() < testFile.length()){
            System.out.printf("行：%d，列：%d，字符：%c\n", test.getLineNum(), test.getColNum(), test.scan());
        }
    }
}
