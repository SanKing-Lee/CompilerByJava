package com.compiler.lexicalAnalysis;

import java.io.*;

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

    private boolean scanNeed(char needChar) {
        char ch = scanner.scan();
        if (ch == needChar) {
            currChar = scanner.scan();
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
    }

    public void analyze() {
        while (!scanner.isEOF()) {
            scan();
            Token t = null;
            // 忽略所有的无效字符
            while(currChar == ' ' || currChar == '\n' || currChar == '\t'){
                scan();
            }
            if(scanner.isEOF()){
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
                    t = new Token(tag);
                }
            }
            // 数字
            else if (isDigit(currChar)) {
                int val = 0;
                if (currChar != '0') {
                    // 以数字1-9开始的十进制数字
                    do {
                        val = val * 10 + currChar - '0';
                        scan();
                    } while (isDigit(currChar));
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
                            t = new Token(ERR);
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
                            t = new Token(ERR);
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
                    t = new Num(val);
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
                        t = new Token(ERR);
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
                            c = currChar;
                    }
                }
                // 读入字符常量时遇到换行符或者文件结尾
                else if (currChar == '\n' || scanner.isEOF()) {
                    errorMessage("读入字符常量时遇到换行或者文件结尾！");
                    t = new Token(ERR);
                }
                // 遇到了后单引号，字符常量没有数据
                else if (currChar == '\'') {
                    errorMessage("字符常量里没有数据！");
                    t = new Token(ERR);
                    // 忽略掉该引号
                    scan();
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
                        t = new Token(ERR);
                    }
                }
            }
            // 字符串常量
            else if (currChar == '\"') {
                StringBuilder str = new StringBuilder();
                while ((currChar = scanner.scan()) != '\"') {
                    if (currChar == '\\') {
                        scan();
                        if (scanner.isEOF()) {
                            errorMessage("读入字符串遇到文件结尾！");
                            t = new Token(ERR);
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
                                str.append(currChar);
                                break;
                        }
                    } else if (currChar == '\n' || scanner.isEOF()) {
                        errorMessage("读取字符串常量时遇到文件结尾！");
                        t = new Token(ERR);
                        break;
                    } else {
                        str.append(currChar);
                    }
                }
                if (t == null) {
                    t = new Str(str.toString());
                }
            }
            // 界符
            else {
                if(scanner.isEOF()){
                    break;
                }
                switch (currChar) {
                    case '+':
                        t = new Token((scanNeed('+')) ? INC : ADD);
                        break;
                    case '-':
                        t = new Token((scanNeed('-')) ? DEC : SUB);
                        break;
                    case '*':
                        t = new Token(MUL);
                        scan();
                        break;
                    case '/':
                        t = new Token(DIV);
                        scan();
                        break;
                    case '%':
                        t = new Token(MOD);
                        scan();
                        break;
                    case '>':
                        t = new Token((scanNeed('=')) ? GE : GT);
                        break;
                    case '<':
                        t = new Token((scanNeed('=')) ? LE : LT);
                        break;
                    case '=':
                        t = new Token((scanNeed('=')) ? EQU : ASSIGN);
                        break;
                    case '&':
                        t = new Token((scanNeed('&')) ? AND : LEA);
                        break;
                    case '|':
                        t = new Token((scanNeed('|')) ? OR : ERR);
                        if (t.getTag() == ERR) {
                            errorMessage("||未定义！");
                        }
                        break;
                    case '!':
                        t = new Token((scanNeed('='))?NEQU:NOT); break;
                    case ',':
                        t = new Token(COMMA); scan(); break;
                    case ':': t = new Token(COLON); scan(); break;
                    case ';': t = new Token(SEMICON); scan(); break;
                    case '(': t = new Token(LPAREN); scan(); break;
                    case ')': t = new Token(RPAREN); scan(); break;
                    case '[': t = new Token(LBRACK); scan(); break;
                    case ']': t = new Token(RBRACK); scan(); break;
                    case '{': t = new Token(LBRACE); scan();break;
                    case '}': t = new Token(RBRACE); scan(); break;
                    default:
                        t = new Token(ERR);
                        errorMessage("该词法记号未定义！");
                        scan();
                }
            }
            System.out.println(t.toString());
        }

    }

}
