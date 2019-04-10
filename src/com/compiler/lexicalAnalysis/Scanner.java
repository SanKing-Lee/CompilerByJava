package com.compiler.lexicalAnalysis;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Author: Sean
 * Date: Created In 9:30 2019/4/10
 * Title: 扫描器
 * Description: 从文件中获取字符流
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class Scanner {
    private int bufferLength = 0;
    private int readPos = 0;
    private int lineNum = 1;
    private int colNum = 1;
    private File file;
    private char[] buffer;


    public Scanner(File file) {
        this.file = file;
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.out.println("创建文件失败！");
                }
            } catch (IOException e) {
                System.out.println("创建文件失败！");
                e.printStackTrace();
            }
        }
        try (FileReader reader = new FileReader(file)) {
            int len = (int) file.length();
            buffer = new char[len + 1];
            bufferLength = reader.read(buffer);
        } catch (IOException e) {
            System.out.println("创建文件阅读器失败！");
            e.printStackTrace();
        }
    }

    public char scan() {
        char ch = buffer[readPos++];
        if (ch == '\n') {
            lineNum++;
            colNum = 1;
        } else {
            colNum++;
        }
        return ch;
    }

    public char peek(){
        return buffer[readPos+1];
    }

    public int getReadPos() {
        return readPos;
    }

    public int getLineNum() {
        return lineNum;
    }

    public int getColNum() {
        return colNum;
    }

    public int getFileLength() {
        return (int) file.length();
    }

    public int getBufferLength() {
        return bufferLength;
    }

    public boolean isEOF() {
        return readPos >= bufferLength;
    }
}
