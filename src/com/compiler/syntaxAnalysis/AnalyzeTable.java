package com.compiler.syntaxAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Sean
 * Date: Created In 15:31 2019/5/7
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class AnalyzeTable {
    private HashMap<String, HashMap<String, Production>> analyzeTable = new HashMap<>();

    public AnalyzeTable(List<String> nonTerminals, List<String> terminals)
    {
        for(int i = 0; i < nonTerminals.size(); i++){
            HashMap<String, Production> analyzeEntry = new HashMap<>();
            for(int j = 0; j < terminals.size(); j++){
                analyzeEntry.put(terminals.get(j), null);
            }
            analyzeTable.put(nonTerminals.get(i), analyzeEntry);
        }
    }

    public void setProduction(String nonTerminal, String terminal, Production production){
        HashMap<String, Production> analyzeEntry = analyzeTable.getOrDefault(nonTerminal, null);
        if(analyzeEntry == null){
            return;
        }
        analyzeEntry.putIfAbsent(terminal, production);
    }

    public Production getProduction(String row, String column){
        return analyzeTable.get(row).get(column);
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<String, HashMap<String, Production>> entry: analyzeTable.entrySet()){
            stringBuilder.append(String.format("%-14s: ", entry.getKey()));
            for(Map.Entry<String, Production> entry1 : entry.getValue().entrySet()){
                String sPro;
                Production production = entry1.getValue();
                if(production == null){
                    sPro = "null";
                }
                else{
                    sPro = production.getId().toString();
                }
                stringBuilder.append( String.format("%8s: %-4s\t", entry1.getKey(), sPro));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
