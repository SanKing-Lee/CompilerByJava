package com.compiler.lexicalAnalysis.tokens;

import com.compiler.lexicalAnalysis.Tag;

import java.util.HashMap;

import static com.compiler.lexicalAnalysis.Tag.*;

/**
 * Author:
 * Date: Created In 17:08 2019/5/26
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */
public class KeyWords {
    private HashMap<String, Tag> keyWords = new HashMap<>();

    private String keyWordsFileLocation = "./keyWords.txt";

    public Tag getTag(String name) {
        return keyWords.getOrDefault(name, ID);
    }

    public KeyWords() {
        keyWords.put("int", KW_INT);
        keyWords.put("double", KW_DOUBLE);
        keyWords.put("char", KW_CHAR);
        keyWords.put("string", KW_STRING);
        keyWords.put("void", KW_VOID);
        keyWords.put("extern", KW_EXTERN);
        keyWords.put("if", KW_IF);
        keyWords.put("else", KW_ELSE);
        keyWords.put("switch", KW_SWITCH);
        keyWords.put("case", KW_CASE);
        keyWords.put("default", KW_DEFAULT);
        keyWords.put("while", KW_WHILE);
        keyWords.put("do", KW_DO);
        keyWords.put("for", KW_FOR);
        keyWords.put("break", KW_BREAK);
        keyWords.put("continue", KW_CONTINUE);
        keyWords.put("return", KW_RETURN);
//        keyWords.put("main", KW_MAIN);
    }

    public HashMap<String, Tag> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(HashMap<String, Tag> keyWords) {
        this.keyWords = keyWords;
    }
}
