package com.compiler.syntaxAnalysis;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: Sean
 * Date: Created In 23:40 2019/5/5
 * Title: 文法分析器
 * Description: 从文件中读取文法，获得它的终结符、非终结符、FIRST集、FOLLOW集和SELECT集并判断该文法是否为一个LL(1)文法
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class GrammarAnalysis {
    private static final String noneTerminalRegx = "<[a-z]+'*>";  // 非终结符的匹配模式
    private static final String terminalRegx = "([A-Z])+(_[A-Z]+)*";    // 终结符的匹配模式

    private HashMap<NonTerminal, List<Production>> productions = new HashMap<>();   // 从文法中读取到的所有产生式
    private HashMap<String, NonTerminal> nonTerminals = new HashMap<>();
    private HashMap<String, Terminal> terminals = new HashMap<>();

    private HashMap<String, HashMap<String, Symbol>> FIRST = new HashMap<>();    // 所有非终结符的FIRST集
    private HashMap<String, List<Symbol>> FOLLOW = new HashMap<>();   // 非终结符的FOLLOW集
    private HashMap<String, List<Symbol>> SELECT = new HashMap<>();   // 非终结符的SELECT集
    private HashMap<String, NonTerminal> NULLABLE = new HashMap<>();                 // 空集

    private String sGrammarFile;                          // 文法文件名
    private File grammarFile;                               // 文法文件

    /**
     * constructor
     *
     * @param file 文法对应文件
     */
    public GrammarAnalysis(String file) {
        this.sGrammarFile = file;
        this.grammarFile = new File(file);
    }

    /**
     * 从文法文件中读取产生生，传递给分析函数分析
     */
    public void grammar2Productions() {
        if (!grammarFile.exists()) {
            try {
                if (!grammarFile.createNewFile()) {
                    System.out.println("创建文件失败！");
                }
            } catch (IOException e) {
                System.out.println("创建文件失败！");
                e.printStackTrace();
            }
        }
        try {
            Scanner scanner = new Scanner(new FileInputStream(sGrammarFile));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println("产生式: " + line);
                parseProduction(line);
                System.out.println();
            }
            System.out.println("所有产生式：");
            for (Map.Entry<NonTerminal, List<Production>> entry : productions.entrySet()) {
                List<Production> productions = entry.getValue();
                for(Production production:productions){
                    System.out.println(production.toString());
                }
            }
            System.out.println("\n所有非终结符：");
            for (Map.Entry<String, NonTerminal> entry : nonTerminals.entrySet()) {
                System.out.println(entry.getValue().toString() + " ");
            }
            System.out.println("\n所有终结符：");
            for (Map.Entry<String, Terminal> entry : terminals.entrySet()) {
                System.out.println(entry.getValue().toString() + " ");
            }
            System.out.println();
        } catch (FileNotFoundException e) {
            System.out.println("未找到该文件！");
            e.printStackTrace();
        }
    }

    /**
     * 分析产生式
     *
     * @param production 产生式字符串
     */
    public void parseProduction(String production) {
        // 将产生式分解为左部和右部
        String[] leftAndRight = production.split("->");
        String left = leftAndRight[0];
        String right = leftAndRight[1];
        // 分解右部
        String[] results = right.split("\\|");

        String sLeft = match(left, noneTerminalRegx);
        System.out.println("左部: " + sLeft);
        NonTerminal leftNoneTerminal = new NonTerminal(sLeft);
        nonTerminals.putIfAbsent(sLeft, leftNoneTerminal);

        List<Production> nonTerminalProductions =
                productions.getOrDefault(leftNoneTerminal.getName(), new ArrayList<>());
        // 遍历所有右部
        for (int i = 0; i < results.length; i++) {
            System.out.print("右部: ");
            // 分解每个右部为符号串
            String[] symbols = results[i].split(" ");
            List<Symbol> lSymbols = new ArrayList<>();
            // 遍历符号串中的每个符号并进行匹配
            for (int j = 0; j < symbols.length; j++) {
                String symbol = match(symbols[j], noneTerminalRegx);
                if (symbol != null) {
                    System.out.print("非终结符: " + symbol + " ");
                    NonTerminal rightPartNoneTerimal = new NonTerminal(symbol);
                    lSymbols.add(rightPartNoneTerimal);
                    nonTerminals.putIfAbsent(symbol, rightPartNoneTerimal);
                }
                symbol = match(symbols[j], terminalRegx);
                if (symbol != null) {
                    System.out.print("终结符: " + symbol + " ");
                    Terminal rightPartTerminal = new Terminal(symbol);
                    lSymbols.add(rightPartTerminal);
                    terminals.putIfAbsent(symbol, rightPartTerminal);
                }
            }
            System.out.println();
            Production cProduction = new Production(leftNoneTerminal, lSymbols);

            nonTerminalProductions.add(cProduction);
            System.out.println(cProduction.toString());
        }
        productions.putIfAbsent(leftNoneTerminal, nonTerminalProductions);
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

    /**
     * 初始化nullable集
     * 非终结符为空的情况：
     * 1. 有一条产生空的产生式
     * 2. 产生式的符号全为非终结符，所有的非终结符都属于空集
     */
    public void initNullable() {
        boolean changing = true;
        while (changing) {
            changing = false;
            // 遍历所有的非终结符及它们的产生式
            for (Map.Entry<NonTerminal, List<Production>> entry : productions.entrySet()) {
                NonTerminal noneTerminal = entry.getKey();
                List<Production> productions = entry.getValue();
                // 遍历所有的非终结符的产生式
                for (Production production : productions) {
                    boolean allNullableNonTerminal = true;
                    // 遍历产生式中的所有符号
                    for (Symbol symbol : production.getRight()) {
                        // 如果该符号为一个终结符
                        if (symbol.isTerminal()) {
                            // 说明该条产生式不是全为非终结符，肯定不适合第二种情况
                            allNullableNonTerminal = false;
                            // 该终结符是一个空符，就把该非终结符加入NULLABLE集合中并置changing为true
                            if (symbol.getName().equals("NULL")) {
                                if (!NULLABLE.containsKey(noneTerminal.getName())) {
                                    NULLABLE.putIfAbsent(noneTerminal.getName(), noneTerminal);
                                    changing = true;
                                }
                            }
                            // 说明该条产生式也不符合第一种情况，跳出循环
                            break;
                        } else {
                            // 如果该符号为一个非终结符，但是该终结符并不属于空集，则不满足第二种情况也肯定不会满足第一种情况，
                            // 所以直接跳出循环
                            if (!NULLABLE.containsKey(symbol.getName())) {
                                allNullableNonTerminal = false;
                                break;
                            }
                        }
                    }
                    // 满足第二种情况
                    if (allNullableNonTerminal) {
                        if (!NULLABLE.containsKey(noneTerminal.getName())) {
                            NULLABLE.putIfAbsent(noneTerminal.getName(), noneTerminal);
                            changing = true;
                        }
                    }
                }
            }
        }
        System.out.println("所有的可空非终结符: ");
        for (Map.Entry<String, NonTerminal> entry : NULLABLE.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    /**
     * 基本情况： X -> a
     * FIRST ( X)  ∪=  {a}
     * 归纳情况：X -> Y1 Y2  	… Yn
     * FIRST ( X)  ∪=  FIRST(Y1)
     * if Y1ϵNULLABLE, FIRST (X) ∪=  FIRST(Y2)
     * if Y1，Y2 ϵ NULLABLE, FIRST (X) ∪=  FIRST(Y3)
     * ……
     */
    public void initFirst(){
//        foreach (nonterminal N)
//          FIRST(N) = {}
//        while(some set is changing)
//          foreach (production p: N->β1 … βn)
//              foreach (βi from β1 upto βn)
//                if (βi== a …)
//                    FIRST(N) ∪= {a}
//                break
//                if (βi== M …)
//                    FIRST(N) ∪= FIRST(M)
//                if (M is not in NULLABLE)
//                    break;
        boolean changing = true;
        while (changing) {
            changing = false;
            // 遍历所有的非终结符及它们的产生式
            for (Map.Entry<NonTerminal, List<Production>> entry : productions.entrySet()) {
                NonTerminal noneTerminal = entry.getKey();
                List<Production> productions = entry.getValue();
                HashMap<String, Symbol> first = FIRST.getOrDefault(noneTerminal.getName(), new HashMap<>());
                // 遍历所有产生式
                for (Production production : productions) {
                    List<Symbol> symbols = production.getRight();
                    for(int i = 0; i < symbols.size(); i++){
                        Symbol symbol = symbols.get(i);
                        // 以终结符开头且该终结符没有在该非终结符的first集中出现
                        if(symbol.isTerminal()){
                            if(!first.containsKey(symbol.getName())){
                                first.put(symbol.getName(), symbol);
                                changing = true;
                            }
                            break;
                        }
                        else{
                            HashMap<String, Symbol> otherSymbols = FIRST.getOrDefault(symbol.getName(), null);
                            if(otherSymbols != null){
                                for(Map.Entry<String, Symbol> entry1 : otherSymbols.entrySet()){
                                    if(!first.containsKey(entry1.getKey())){
                                        first.put(entry1.getKey(), entry1.getValue());
                                        changing = true;
                                    }
                                }
                            }
                            if(!NULLABLE.containsKey(symbol.getName())){
                                break;
                            }
                        }
                    }
                }
                FIRST.putIfAbsent(noneTerminal.getName(), first);
            }
        }
        System.out.println("\n所有非终结符的FIRST集：");
        for(Map.Entry<String, HashMap<String, Symbol>> entry : FIRST.entrySet()){
            System.out.print(entry.getKey() + ": ");
            for(Map.Entry<String, Symbol> entry1:entry.getValue().entrySet()){
                System.out.print(entry1.getKey() + " ");
            }
            System.out.println();
        }
    }
}
