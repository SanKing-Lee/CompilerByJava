//package com.compiler.syntaxAnalysisByRecursiveSubProgram;
//
//import com.compiler.lexicalAnalysis.LexicalAnalysis;
//import com.compiler.lexicalAnalysis.Tag;
//import com.compiler.lexicalAnalysis.tokens.Token;
//
//import java.util.ArrayList;
//
//import static com.compiler.lexicalAnalysis.Tag.*;
//
///**
// * Author: Sean
// * Date: Created In 15:56 2019/5/25
// * Title:
// * Description:
// * Version: 0.1
// * Update History:
// * [Date][Version][Author] What has been done;
// */
//
//public class Parser {
//    private ArrayList<Token> tokens;                                    // 词法记号流
//    private Token look;                                                // 当前的词法记号
//    private static int Pos = 0;                                         // 词法记号的位置
//
//    public Parser() {
//        // 调用词法分析并获得所有的词法记号
//        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis("test.c");
//        tokens = (ArrayList<Token>) lexicalAnalysis.analyze();
//    }
//
//    public void analyze(){
//        move();
//        program();
//    }
//
//    public void program(){
//        if(look.getTag() == END){
//            return;
//        }
//        else{
//            segment();
//            program();
//        }
//    }
//
//    public void whilestate() {
//        match(KW_WHILE);
//        if (!match(LPAREN)) {
//            recovery(EXPR_FIRST || F(RPAREN), LPAREN_LOST, LPAREN_WRONG);
//        }
//        altexpr();
//        if (!match(RPAREN)) {
//            recovery(F(LBRACE), RPAREN_LOST, RPAREN_WRONG)；
//        }
//        block();
//    }
//
//    public void init(){
//        if(match(ASSIGN)){
//            expr();
//        }
//    }
//
//    public void forstate() {
//
//    }
//
//    public void dowhilestat() {
//
//    }
//
//    public void ifstat() {
//
//    }
//
//    public void switchstat() {
//
//    }
//
//    public void statement() {
//        switch (look.getTag()) {
//            case KW_WHILE:
//                whilestate();
//                break;
//            case KW_FOR:
//                forstate();
//                break;
//            case KW_DO:
//                dowhilestat();
//                break;
//            case KW_IF:
//                ifstat();
//                break;
//            case KW_SWITCH:
//                switchstat();
//                break;
//            case KW_BREAK:
//                move();
//                if (!match(SEMICON)) {
//                    recovery(TYPE_FIRST || STATEMENT_FIRST || F(RBRACE), SEMICON_LOST, SEMICON_WRONG);
//                }
//                break;
//            case KW_CONTINUE:
//                move();
//                if (!match(SEMICON)) {
//                    recovery(TYPE_FIRST || STATEMENT_FIRST || F(RBRACE), SEMICON_LOST, SEMICON_WRONG);
//                }
//                break;
//            case KW_RETURN:
//                move();
//                altexpr();
//                if (!match(SEMICON)) {
//                    recovery(TYPE_FIRST || STATEMENT_FIRST || F(RBRACE), SEMICON_LOST, SEMICON_WRONG);
//                }
//                break;
//            default:
//                altexpr();
//                if (!match(SEMICON)) {
//                recovery(TYPE_FIRST || STATEMENT_FIRST || F(RBRACE), SEMICON_LOST, SEMICON_WRONG);
//            }
//        }
//    }
//
//
//
//    public void recovery() {
//
//    }
//
//    public void altexpr() {
//
//    }
//
//    public void block() {
//
//    }
//
//    public void F() {
//
//    }
//
//    public void move() {
//        if (Pos < tokens.size()) {
//            look = tokens.get(Pos++);
//        }
//    }
//
//    public boolean match(Tag need) {
//        if (look.getTag() == need) {
//            move();
//            return true;
//        } else {
//            return false;
//        }
//    }
//}
