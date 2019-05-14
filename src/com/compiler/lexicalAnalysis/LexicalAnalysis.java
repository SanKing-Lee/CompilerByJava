package com.compiler.lexicalAnalysis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.compiler.lexicalAnalysis.Tag.*;

/**
 * Author: Sean
 * Date: Created In 18:00 2019/4/9
 * Title: 词法分析器
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class LexicalAnalysis {
    private KeyWords keyWords = new KeyWords();
    private char currChar;
    private Scanner scanner;
    private HashMap<String, Id> Ids = new HashMap<>();
    private ArrayList<Error> errors = new ArrayList<>();

    public LexicalAnalysis(String fileName) {
        File file = new File(fileName);
        scanner = new Scanner(file);
    }

    private boolean isLetter(char ch) {
        return Character.isLetter(ch);
    }

    private boolean isDigit(char ch) {
        return Character.isDigit(ch);
    }

    private boolean isHex(char ch) {
        return isDigit(ch) || (ch >= 'a' && ch <= 'f')
                || (ch >= 'A' && ch <= 'F');
    }

    private boolean isBin(char ch) {
        return ch >= '0' && ch <= '1';
    }

    private boolean isOctal(char ch) {
        return ch >= '0' && ch <= '7';
    }

    private void scan() {
        currChar = scanner.scan();
    }

    private char peek() {
        return scanner.peek();
    }

    private boolean scanNeed(char needChar) {
        char ch = scanner.scan();
        if (ch == needChar) {
            return true;
        } else {
            currChar = ch;
            return false;
        }
    }

    private void errorMessage(String error) {
        System.out.println("行：" + scanner.getLineNum() +
                ", 列：" + scanner.getColNum() +
                " : " + error);
        errors.add(new Error(error, scanner.getLineNum(), scanner.getColNum()));
    }

    public List<Token> analyze() {
        List<Token> tokens = new ArrayList<>();
        scan();
        while (!scanner.isEOF()) {
            Token t = null;
            // 忽略所有的无效字符
            while (currChar == ' ' || currChar == '\n' || currChar == '\t' || currChar == '\r') {
                scan();
            }
            if (scanner.isEOF()) {
                break;
            }
            // 标识符或关键字
            if (isLetter(currChar) || currChar == '_') {
                StringBuilder name = new StringBuilder();
                do {
                    name.append(currChar);
                    scan();
                } while (isLetter(currChar) || currChar == '_' || isDigit(currChar));

                // 判断是否为关键字
                Tag tag = keyWords.getTag(name.toString());
                if (tag == ID) {
                    t = new Id(name.toString());
                } else {
                    t = new Token(tag, scanner.getLineNum(), scanner.getColNum());
                }
            }
            // 数字
            else if (isDigit(currChar)) {
                StringBuilder num = new StringBuilder();
                double val = 0;
                if (currChar != '0') {
                    // 以数字1-9开始的十进制数字
                    do {
                        num.append(currChar);
                        scan();
                    } while (isDigit(currChar));
                    val = java.lang.Double.valueOf(num.toString());
                    t = new Int((int) val);
                    if (currChar == '.') {
                        num.append('.');
                        scan();
                        double fra = 0;
                        do {
                            num.append(currChar);
                            scan();
                        } while (isDigit(currChar));
                        val = java.lang.Double.valueOf(num.toString());
                        t = new Double(val);
                    }
                    if (currChar == 'e' || currChar == 'E') {
                        int exp = 0;
                        boolean isNegative = false;
                        scan();
                        if (currChar == '-') {
                            isNegative = true;
                            scan();
                        }
                        do {
                            exp = exp * 10 + currChar - '0';
                            scan();
                        } while (isDigit(currChar));
                        if (isNegative) {
                            val /= Math.pow(10, exp);
                        } else {
                            val *= Math.pow(10, exp);
                        }
                        t = new Double(val);
                    }
                } else {
                    scan();
                    if (currChar == 'x' || currChar == 'X') {
                        // 以0x开始的十六进制数字
                        scan();
                        if (isHex(currChar)) {
                            do {
                                val = val * 16 + currChar;
                                if (currChar >= '0' && currChar <= '9') {
                                    val -= '0';
                                } else if (currChar >= 'a' && currChar <= 'f') {
                                    val += 10 - 'a';
                                } else {
                                    val += 10 - 'A';
                                }
                                scan();
                            } while (isHex(currChar));
                        } else {
                            errorMessage("十六进制数字错误：0x后无数据");
                            t = new Token(ERR, scanner.getLineNum(), scanner.getColNum());
                        }
                    } else if (currChar == 'b' || currChar == 'B') {
                        // 以0b开始的二进制数字
                        scan();
                        if (isBin(currChar)) {
                            do {
                                val = val * 2 + currChar - '0';
                                scan();
                            } while (isBin(currChar));
                        } else {
                            errorMessage("二进制数字错误：0b后无数据");
                            t = new Token(ERR, scanner.getLineNum(), scanner.getColNum());
                        }
                    } else if (isOctal(currChar)) {
                        // 以0开始的八进制数字
                        do {
                            val = val * 8 + currChar - '0';
                            scan();
                        } while (isOctal(currChar));
                    }
                }
                if (t == null) {
                    t = new Int((int) val);
                }
            }
            // 字符常量
            else if (currChar == '\'') {
                // 以'开头的字符常量
                char c = ' ';
                scan();
                // 读入一个转义字符
                if (currChar == '\\') {
                    scan();
                    if (currChar == '\n' || scanner.isEOF()) {
                        errorMessage("读入转义字符时遇到换行或文件结尾！");
                        t = new Token(ERR, scanner.getLineNum(), scanner.getColNum());
                    }
                    switch (currChar) {
                        case 'n':
                            c = '\n';
                            break;
                        case '\\':
                            c = '\\';
                            break;
                        case 't':
                            c = '\t';
                            break;
                        case '0':
                            c = '\0';
                            break;
                        case '\'':
                            c = '\'';
                            break;
                        default:
                            errorMessage("无效的转义字符！");
                            t = new Token(ERR, scanner.getLineNum(), scanner.getColNum());
                    }
                }
                // 读入字符常量时遇到换行符或者文件结尾
                else if (currChar == '\n' || scanner.isEOF()) {
                    errorMessage("读入字符常量时遇到换行或者文件结尾！");
                    t = new Token(ERR, scanner.getLineNum(), scanner.getColNum());
                }
                // 遇到了后单引号，字符常量没有数据
                else if (currChar == '\'') {
                    errorMessage("字符常量里没有数据！");
                    t = new Token(ERR, scanner.getLineNum(), scanner.getColNum());
                }
                // 读入一个正常的字符常量
                else c = currChar;
                // 如果没有出现错误
                if (t == null) {
                    // 如果下一个字符是右单引号
                    if (scanner.scan() == '\'') {
                        t = new Char(c);
                    } else {
                        errorMessage("读入字符常量错误：没有相应的右单引号！");
                        t = new Token(ERR, scanner.getLineNum(), scanner.getColNum());
                    }
                }
                scan();
            }
            // 字符串常量
            else if (currChar == '\"') {
                StringBuilder str = new StringBuilder();
                while ((currChar = scanner.scan()) != '\"') {
                    if (currChar == '\\') {
                        scan();
                        if (scanner.isEOF()) {
                            errorMessage("读入字符串遇到文件结尾！");
                            t = new Token(ERR, scanner.getLineNum(), scanner.getColNum());
                            break;
                        }
                        switch (currChar) {
                            case 'n':
                                str.append('\n');
                                break;
                            case '\\':
                                str.append('\\');
                                break;
                            case 't':
                                str.append('\t');
                                break;
                            case '\"':
                                str.append('\"');
                                break;
                            case '0':
                                str.append('\0');
                                break;
                            case '\n':
                                break;
                            default:
                                errorMessage("未定义的转义符！");
                                break;
                        }
                    } else if (currChar == '\n' || scanner.isEOF()) {
                        errorMessage("读取字符串常量时遇到换行符或文件结尾！");
                        t = new Token(ERR, scanner.getLineNum(), scanner.getColNum());
                        break;
                    } else {
                        str.append(currChar);
                    }
                }
                if (t == null) {
                    t = new Str(str.toString());
                }
                scan();
            }
            // 界符
            else {
                if (scanner.isEOF()) {
                    break;
                }
                switch (currChar) {
                    case '+':
                        t = new Token((scanNeed('+')) ? INC : ADD, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '-':
                        t = new Token((scanNeed('-')) ? DEC : SUB, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '*':
                        t = new Token(MUL, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '/':
                        scan();
                        if (currChar == '/') {
                            scan();
                            while (!(currChar == '\n' || scanner.isEOF())) {
                                scan();
                                t = new Token(ERR, scanner.getLineNum(), scanner.getColNum());
                            }
                        } else if (currChar == '*') {
                            scan();
                            while (!scanner.isEOF()) {
                                scan();
                                if (currChar == '*') {
                                    while (currChar == '*') scan();
                                    if (currChar == '/') {
                                        t = new Token(PLACEHOLDER, scanner.getLineNum(), scanner.getColNum());
                                        break;
                                    }
                                }
                                if (t == null && scanner.isEOF()) {
                                    errorMessage("多行注释出错!");
                                    t = new Token(ERR, scanner.getLineNum(), scanner.getColNum());
                                }
                            }
                        } else {
                            t = new Token(DIV, scanner.getLineNum(), scanner.getColNum());
                        }
                        break;
                    case '%':
                        t = new Token(MOD, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '>':
                        t = new Token((scanNeed('=')) ? GE : GT, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '<':
                        t = new Token((scanNeed('=')) ? LE : LT, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '=':
                        t = new Token((scanNeed('=')) ? EQU : ASSIGN, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '&':
                        t = new Token((scanNeed('&')) ? AND : LEA, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '|':
                        t = new Token((scanNeed('|')) ? OR : ERR, scanner.getLineNum(), scanner.getColNum());
                        if (t.getTag() == ERR) {
                            errorMessage("||未定义！");
                        }
                        break;
                    case '!':
                        t = new Token((scanNeed('=')) ? NEQU : NOT, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case ',':
                        t = new Token(COMMA, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case ':':
                        t = new Token(COLON, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case ';':
                        t = new Token(SEMICON, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '(':
                        t = new Token(LPAREN, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case ')':
                        t = new Token(RPAREN, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '[':
                        t = new Token(LBRACK, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case ']':
                        t = new Token(RBRACK, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '{':
                        t = new Token(LBRACE, scanner.getLineNum(), scanner.getColNum());
                        break;
                    case '}':
                        t = new Token(RBRACE, scanner.getLineNum(), scanner.getColNum());
                        break;
                    default:
                }
                scan();
            }
            // System.out.println(t.toString());
             tokens.add(t);
        }
        tokens.add(new Token(END, scanner.getLineNum(), scanner.getColNum()));
        return tokens;
    }
}