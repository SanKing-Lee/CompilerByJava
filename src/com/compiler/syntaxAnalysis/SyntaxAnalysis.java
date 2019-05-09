package com.compiler.syntaxAnalysis;

import com.compiler.lexicalAnalysis.LexicalAnalysis;
import com.compiler.lexicalAnalysis.Token;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Sean
 * Date: Created In 19:44 2019/4/24
 * Title: 语法分析
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class SyntaxAnalysis {
    private static final String START_SYMBOL = "<program>";             // 开始符
    private static final String noneTerminalRegx = "<[a-z]+'*>";        // 非终结符的匹配模式
    private static final String terminalRegx = "([A-Z])+(_[A-Z]+)*";    // 终结符的匹配模式
    private static final String nullRegx = "null";                      // 空符的匹配模式
    private ArrayList<Token> tokens;                                    // 词法记号流
    private AnalyzeTable analyzeTable;                                  // 预测分析表
    private Token token;                                                // 当前的词法记号
    private Stack<String> analyzeStack;                                 // 预测分析栈
    private static int Pos = 0;                                         // 词法记号的位置
    private DefaultMutableTreeNode root;                                // 生成抽象语法树的根结点
    private DefaultMutableTreeNode currentNode;                         // 当前的结点
    private Stack<DefaultMutableTreeNode> treeStack;                    // 用于生成语法树的栈

    /**
     * 构造函数，完成了私有变量的定义
     */
    public SyntaxAnalysis() {
        // 调用文法分析并获得一个文法分析表
        GrammarAnalysis grammarAnalysis = new GrammarAnalysis("Grammar.txt");
        analyzeTable = grammarAnalysis.generateAnalyzeTable();
        // 调用词法分析并获得所有的词法记号
        LexicalAnalysis lexicalAnalysis = new LexicalAnalysis("test.c");
        lexicalAnalysis.analyze();
        tokens = lexicalAnalysis.getTokens();
        // 初始化分析栈、根结点、当前结点和树栈
        analyzeStack = new Stack<>();
        root = new DefaultMutableTreeNode(START_SYMBOL);
        currentNode = root;
        treeStack = new Stack<>();

        analyzeStack.push("END");
        analyzeStack.push(START_SYMBOL);
        treeStack.push(new DefaultMutableTreeNode("END"));
        treeStack.push(root);
    }

    /**
     * 分析程序，t为当前的栈顶符号，token为当前的词法记号
     * 1.   如果当前的词法记号是一个终结符，则将它和栈顶符号进行比较，如果是一样的，则将栈顶符号弹出，如果不匹配，则报错
     * 2.   如果当前的词法记号是一个非终结符，则将它从栈顶弹出，并从分析表中找到对应的产生式，将产生式的右部符号全部压入栈内
     *      如果没有找到产生式，则报错，并弹出当前的栈顶符号，令token等于下一个词法记号，直到找到匹配的产生式为止
     * 3.   分析栈为空，则说明成功分析完了所有的词法记号，accept
     * @return  生成的语法树的根结点
     */
    public DefaultMutableTreeNode analyze() {
        scan();
        while (!analyzeStack.empty()) {
            String t = analyzeStack.peek();
            if (isTerminal(t)) {
                if (t.equals(token.getTag().name())) {
                    scan();
                    analyzeStack.pop();
                    treeStack.pop();
                } else {
                    System.out.println(token.getTag() + " does not match " + t);
                    scan();
                }
            } else if (isNonTerminal(t)) {
                analyzeStack.pop();
                currentNode = treeStack.pop();

//                System.out.println(t);
//                System.out.println(token.getTag().name());
                Production production = analyzeTable.getProduction(t, token.getTag().name());
//                System.out.println(production);
                // 错误恢复，如果当前的产生式为空，则将当前词法记号前移
                if (production == null) {
                    System.out.println("Error: Null Production!");
                    scan();
                    continue;
                }
                // 如果当前的产生式为一条同步产生式，则将当前分析栈顶的终结符弹出
                if (production.isSync()){
                    System.out.println("Error: Synchronized");
                    analyzeStack.pop();
                    continue;
                }
                List<String> symbols = production.getRight();
                Stack<DefaultMutableTreeNode> nodeStack = new Stack<>();
                for (int i = symbols.size() - 1; i >= 0; i--) {
                    String symbol = symbols.get(i);
                    if (!symbol.equals("null")) {
                        analyzeStack.push(symbol);
                        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(symbol);
                        treeStack.push(newNode);
                        nodeStack.push(newNode);
                    }
                }
                while(!nodeStack.empty()){
                    currentNode.add(nodeStack.pop());
                }
            }
        }
        System.out.println("accept!");
        return root;
    }

    public void scan() {
        if (Pos < tokens.size()) {
            token = tokens.get(Pos++);
        }
    }

    /**
     * 根据regx匹配字符串，并返回匹配到的字符串
     *
     * @param str  待匹配的字符串
     * @param regx 匹配模式
     * @return 在待匹配字符串中找到的匹配的字符串
     */
    private String match(String str, String regx) {
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    private boolean isTerminal(String str) {
        return match(str, terminalRegx) != null;
    }

    private boolean isNonTerminal(String str) {
        return match(str, noneTerminalRegx) != null;
    }

    private boolean isNull(String str) {
        return match(str, nullRegx) != null;
    }
}
