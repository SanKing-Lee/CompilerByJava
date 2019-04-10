package com.compiler.lexicalAnalysis;

/**
 * Author: Sean
 * Date: Created In 20:55 2019/4/9
 * Title: 词法记号类
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public enum Tag {
    ERR,
    ID, PLACEHOLDER,
    KW_INT, KW_DOUBLE, KW_CHAR, KW_VOID, KW_STRING, KW_MAIN,
    KW_EXTERN,
    INT, DOUBLE, CH, STR,
    NOT, LEA,
    ADD, SUB, MUL, DIV, MOD,
    INC, DEC,
    GT, GE, LT, LE, EQU, NEQU,
    AND, OR,
    LPAREN, RPAREN,
    LBRACK, RBRACK,
    LBRACE, RBRACE,
    COMMA, COLON, SEMICON,
    ASSIGN,
    KW_IF, KW_ELSE,
    KW_SWITCH, KW_CASE, KW_DEFAULT,
    KW_WHILE, KW_DO, KW_FOR,
    KW_BREAK, KW_CONTINUE, KW_RETURN
}
