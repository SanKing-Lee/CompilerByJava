package com.compiler.syntaxAnalysis;

import java.io.*;
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
    private static final String nullRegx = "null";
    private static int productionId = 0;

    private List<Production> productions = new ArrayList<>();
    private List<String> nonTerminals = new ArrayList<>();
    private List<String> terminals = new ArrayList<>();

    private List<String> nullable = new ArrayList<>();
    private HashMap<String, List<String>> first = new HashMap<>();
    private HashMap<String, List<String>> follow = new HashMap<>();
    private HashMap<Integer, List<String>> select = new HashMap<>();

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
        grammar2Productions();
        initNullable();
        initFirst();
        initFollow();
        initSelect();
        writeToFile("information.txt");
    }

    /**
     * 从文法文件中读取产生生，传递给分析函数分析
     */
    private void grammar2Productions() {
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
                parseProduction(line);
            }
//            System.out.println("所有产生式：" + productions.size());
//            for (int i = 0; i < productions.size(); i++) {
//                System.out.println(productions.get(i));
//            }
//            System.out.println("所有非终结符: " + nonTerminals.size());
//            for (int i = 0; i < nonTerminals.size(); i++) {
//                System.out.println(nonTerminals.get(i));
//            }
//            System.out.println("所有终结符: " + terminals.size());
//            for (int i = 0; i < terminals.size(); i++) {
//                System.out.println(terminals.get(i));
//            }
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
    private void parseProduction(String production) {
        // 将产生式分解为左部和右部
        String[] leftAndRight = production.split("->");
        String left = leftAndRight[0];
        String right = leftAndRight[1];
        // 分解右部
        String[] results = right.split("\\|");

        // 遍历所有右部
        for (int i = 0; i < results.length; i++) {
            // 分解每个右部为符号串
            String[] symbols = results[i].split(" ");
            List<String> rightPart = new ArrayList<>();
            // 遍历符号串中的每个符号并进行匹配
            for (int j = 0; j < symbols.length; j++) {
                String symbol = symbols[j];
                if (isTerminal(symbol) || isNonTerminal(symbol) || isNull(symbol)) {
                    rightPart.add(symbol);
                }
                if (isTerminal(symbol)) {
                    if (!terminals.contains(symbol)) {
                        terminals.add(symbol);
                    }
                }
                if (isNonTerminal(symbol)) {
                    if (!nonTerminals.contains(symbol)) {
                        nonTerminals.add(symbol);
                    }
                }
            }
            Production cProduction = new Production(productionId++, left, rightPart);
            if (!productions.contains(cProduction)) {
                productions.add(cProduction);
            }
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

    private boolean unionList(List<String> list1, List<String> list2) {
        boolean flag = false;
        for (String str : list2) {
            if (!list1.contains(str)) {
                list1.add(str);
                flag = true;
            }
        }
        return flag;
    }

    private boolean addDistinct(List<String> list, String symbol) {
        boolean flag = false;
        if (!list.contains(symbol)) {
            list.add(symbol);
            flag = true;
        }
        return flag;
    }

    /**
     * 初始化nullable集
     * 非终结符为空的情况：
     * 1. 有一条产生空的产生式
     * 2. 产生式的符号全为非终结符，所有的非终结符都属于空集
     */
    private void initNullable() {
//        NULLABLE = {};
//        while (NULLABLE is still changing)
//            foreach (production p: X-> β)
//                if (β== ε)
//                    NULLABLE ∪= {X}
//                if (β== Y1 … Yn)
//                    if (Y1ϵ NULLABLE && … && Yn ϵ NULLABLE)
//                        NULLABLE ∪= {X}

        boolean changing = true;
        while (changing) {
            changing = false;
            // 遍历所有的非终结符的产生式
            for (Production production : productions) {
                String left = production.getLeft();
                // 如果该产生式为一条空产生式
                if (isNullProduction(production)) {
                    changing = (addDistinct(nullable, left)) || changing;
                }
                boolean allNullableNonTerminal = true;
                // 遍历产生式中的所有符号
                for (String symbol : production.getRight()) {
                    // 如果该符号为一个终结符
                    if (isTerminal(symbol)) {
                        // 说明该条产生式不是全为非终结符，肯定不适合第二种情况
                        allNullableNonTerminal = false;
                        break;
                    } else {
                        // 如果该符号为一个非终结符，但是该终结符并不属于空集，则不满足第二种情况也肯定不会满足第一种情况，
                        // 所以直接跳出循环
                        if (!nullable.contains(symbol)) {
                            allNullableNonTerminal = false;
                            break;
                        }
                    }
                }
                // 满足第二种情况
                if (allNullableNonTerminal) {
                    changing = (addDistinct(nullable, left)) || changing;
                }
            }
        }
//        System.out.println("所有的可空非终结符: " + nullable.size());
//        for (int i = 0; i < nullable.size(); i++) {
//            System.out.println(nullable.get(i));
//        }
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
    private void initFirst() {
//        foreach (nonterminal N)
//          FIRST(N) = {}
//        while(some set is changing)
//          foreach (production p: N->β1 … βn)
//              foreach (βi from β1 upto βn)
//                if (βi== a …)
//                    FIRST(N) ∪= {a}
//                    break
//                if (βi== M …)
//                    FIRST(N) ∪= FIRST(M)
//                if (M is not in NULLABLE)
//                    break;
        boolean changing = true;
        while (changing) {
            changing = false;
            // 遍历所有产生式
            for (Production production : productions) {
                String left = production.getLeft();
                List<String> symbols = production.getRight();
                List<String> proFirst = first.getOrDefault(left, new ArrayList<>());
                for (int j = 0; j < symbols.size(); j++) {
                    String symbol = symbols.get(j);
                    // 以终结符开头且该终结符没有在该非终结符的first集中出现
                    if (isTerminal(symbol)) {
                        changing = (addDistinct(proFirst, symbol)) || changing;
                        break;
                    } else {
                        List<String> otherSymbols = first.getOrDefault(symbol, new ArrayList<>());
                        changing = unionList(proFirst, otherSymbols) || changing;
                        if (!nullable.contains(symbol)) {
                            break;
                        }
                    }
                }
                first.put(left, proFirst);
            }
        }
//        System.out.println("所有非终结符的First集：");
//        for (Map.Entry<String, List<String>> entry : first.entrySet()) {
//            System.out.print(entry.getKey() + ": ");
//            List<String> symbols = entry.getValue();
//            for (int i = 0; i < symbols.size(); i++) {
//                System.out.print(symbols.get(i) + " ");
//            }
//            System.out.println();
//        }
    }

    /**
     * 初始化FOLLOW集
     */
    private void initFollow() {
        //        foreach (nonterminal N)
        //            FOLLOW(N) = { }
//            while(some set is changing)
//                foreach (production p: N->β1 … βn)
//                    temp = FOLLOW(N)
//                    foreach (βi from βn downto β1)  // 逆序！
//                        if (βi== a …)
//                            temp = {a}
//                        if (βi== M …)
//                            FOLLOW(M) ∪= temp
//                            if (M is not NULLABLE)
//                                temp = FIRST(M)
//                             else temp ∪= FIRST(M)

        // 遍历所有的非终结符及它们的产生式
        // foreach(nonterminal N)
//        System.out.println("所有非终结符的FOLLOW集：");

        for (String nonTerminal : nonTerminals) {
            follow.put(nonTerminal, new ArrayList<>());
        }

        List<String> programFollow = new ArrayList<>();
        programFollow.add("END");
        follow.put("<program>", programFollow);
        boolean changing = true;
        // while some set is changing
        while (changing) {
            changing = false;
            // 遍历所有产生式
            // foreach(production)
            for (Production production : productions) {
                List<String> temp = follow.getOrDefault(production.getLeft(), new ArrayList<>());
                List<String> symbols = production.getRight();
                // from n down to 0
                for (int k = symbols.size() - 1; k >= 0; k--) {
                    String symbol = symbols.get(k);
                    // terminal
                    if (isTerminal(symbol)) {
                        // temp = {symbol}
                        List<String> terminalTemp = new ArrayList<>();
                        terminalTemp.add(symbol);
                        temp = terminalTemp;
                    }
                    // nonTerminal
                    else {
                        List<String> nonTerSymbFollow = follow.getOrDefault(symbol, new ArrayList<>());
                        changing = (unionList(nonTerSymbFollow, temp)) || changing;
                        follow.put(symbol, nonTerSymbFollow);

                        List<String> nonTerSymbFirst = first.getOrDefault(symbol, new ArrayList<>());
                        if (!nullable.contains(symbol)) {
                            temp = nonTerSymbFirst;
                        } else {
                            unionList(temp, nonTerSymbFirst);
                        }
                    }
                }
                // FOLLOW.putIfAbsent(nonTerminal.getName(), temp);
            }
        }
//        System.out.println("所有非终结符的FOLLOW集：");
//        for (Map.Entry<String, List<String>> entry : follow.entrySet()) {
//            System.out.print(entry.getKey() + ": ");
//            for (String symbol : entry.getValue()) {
//                System.out.print(symbol + " ");
//            }
//            System.out.println();
//        }
    }

    //    calculte_FIRST_S(production p: N->β1 …βn)
//      foreach(βi from β1to βn)
//          if (βi== a …)
//              FIRST_S(p) ∪= {a}
//             return;
//          if (βi== M …)
//              FIRST_S(p) ∪= FIRST(M)
//             if (M is not NULLABLE)
//            return;
//      FIRST_S(p) ∪= FOLLOW(N)
    public void initSelect() {
        for (Production production : productions) {
            select.put(production.getId(), new ArrayList<>());
        }

        for (Production production : productions) {
            String left = production.getLeft();
            List<String> symbols = production.getRight();
            List<String> sel = select.get(production.getId());
            // 如果该产生式是空产生式，select = follow
            if (isNullProduction(production)) {
                sel = follow.getOrDefault(left, new ArrayList<>());
                select.put(production.getId(), sel);
                continue;
            }
            // 遍历产生式右部的符号
            for (int i = 0; i < symbols.size(); i++) {
                String symbol = symbols.get(i);
                // 如果遇到了一个终结符，加入sel并break;
                if (isTerminal(symbol)) {
                    if (!sel.contains(symbol)) {
                        sel.add(symbol);
                        select.put(production.getId(), sel);
                        break;
                    }
                } else {
                    // 遇到了一个非终结符，将该非终结符的first集加入sel中
                    List<String> nonTerFirst = first.getOrDefault(symbol, new ArrayList<>());
                    unionList(sel, nonTerFirst);
                    select.put(production.getId(), sel);
                    // 如果该非终结符不是可空的，则结束
                    if (!nullable.contains(symbol)) {
                        break;
                    }
                }
                // 如果所有的符号都为非终结符且都是可空的，则将产生式左部的非终结符的Follow集加入sel;
                unionList(sel, follow.getOrDefault(symbol, new ArrayList<>()));
                select.put(production.getId(), sel);
            }
//            List<String> sel = select.get(production.getId());
//            unionList(sel, follow.getOrDefault(production.getLeft(), new ArrayList<>()));
//            select.put(production.getId(), sel);
        }
//        for(Map.Entry<Integer, List<String>> entry: select.entrySet()){
//            System.out.print(getProductionById(entry.getKey()).toString() + ": ");
//            for(String symbol: entry.getValue()){
//                System.out.print(symbol + " ");
//            }
//            System.out.println();
//        }
    }

    private Production getProductionById(int id) {
        for (Production production : productions) {
            if (production.getId() == id) {
                return production;
            }
        }
        return null;
    }

    private boolean isNullProduction(Production production) {
        return production.getRight().get(0).equals(nullRegx);
    }

    public AnalyzeTable generateAnalyzeTable() {
        AnalyzeTable analyzeTable = new AnalyzeTable(nonTerminals, terminals);
        for (Map.Entry<Integer, List<String>> entry : select.entrySet()) {
            Production production = getProductionById(entry.getKey());
            List<String> symbols = entry.getValue();
            for (int i = 0; i < symbols.size(); i++) {
                analyzeTable.setProduction(production.getLeft(), symbols.get(i), production);
            }
        }

        // 将follow集和first集中的所有符号作为同步符号
        for (String nonTerminal : nonTerminals) {
            List<String> syncSymbols = new ArrayList<>();
            List<String> firstSymbols = first.getOrDefault(nonTerminal, new ArrayList<>());
            List<String> followSymbols = follow.getOrDefault(nonTerminal, new ArrayList<>());

//            unionList(syncSymbols, firstSymbols);
            unionList(syncSymbols, followSymbols);

            for(String syncSymbol:syncSymbols){
                analyzeTable.setProduction(nonTerminal, syncSymbol, new Production(true));
            }
        }

        return analyzeTable;
    }

    public void writeToFile(String informationFile) {
        File file = new File(informationFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("创建信息文件失败！");
                e.printStackTrace();
            }
        }
        try {
            PrintWriter printWriter = new PrintWriter(informationFile, "UTF-8");
            printWriter.println("所有的产生式：");
            for (Production production : productions) {
                printWriter.println(production.toString());
            }
            printWriter.println();
            printWriter.println("所有非终结符: ");
            for (String symbol : nonTerminals) {
                printWriter.println(symbol);
            }
            printWriter.println();
            printWriter.println("所有终结符: ");
            for (String symbol : terminals) {
                printWriter.println(symbol);
            }
            printWriter.println();
            printWriter.println("所有可空非终结符: ");
            for (String symbol : nullable) {
                printWriter.println(symbol);
            }
            printWriter.println();
            printWriter.println("FIRST集：");
            for (Map.Entry<String, List<String>> entry : first.entrySet()) {
                printWriter.print(entry.getKey() + ": ");
                for (String str : entry.getValue()) {
                    printWriter.print(str + " ");
                }
                printWriter.println();
            }
            printWriter.println();
            printWriter.println("FOLLOW集：");
            for (Map.Entry<String, List<String>> entry : follow.entrySet()) {
                printWriter.print(entry.getKey() + ": ");
                for (String str : entry.getValue()) {
                    printWriter.print(str + " ");
                }
                printWriter.println();
            }
            printWriter.println();
            printWriter.println("SELECT集: ");
            for (Map.Entry<Integer, List<String>> entry : select.entrySet()) {
                printWriter.print(getProductionById(entry.getKey()) + ": ");
                for (String str : entry.getValue()) {
                    printWriter.print(str + " ");
                }
                printWriter.println();
            }
            printWriter.println();
            printWriter.println("预测分析表：");
            printWriter.println(generateAnalyzeTable().toString());
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
