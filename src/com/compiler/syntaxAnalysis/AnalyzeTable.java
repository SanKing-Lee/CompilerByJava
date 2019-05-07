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
    private HashMap<Header, Production> anylzeTable = new HashMap<>();

    public AnalyzeTable(HashMap<String, NonTerminal> nonTerminalList, HashMap<String, Terminal> terminalList)
    {
        // initial table
        for(Map.Entry<String, NonTerminal> nonTerminalEntry: nonTerminalList.entrySet()){
            for(Map.Entry<String, Terminal> terminalEntry: terminalList.entrySet()){
                anylzeTable.put(new Header(nonTerminalEntry.getValue(), terminalEntry.getValue()), null);
            }
        }
    }

    public void setProduction(NonTerminal row, Terminal column, Production production){
        Header header = new Header(row, column);
        if(!anylzeTable.containsKey(header)){
            System.out.println("不存在该表头");
            return;
        }
        if(anylzeTable.putIfAbsent(header, production) != null){
            System.out.println(row.getName() + ", " + column.getName() + ": " + "该位置已存在产生式");
        }
    }

    public Production getProduction(NonTerminal row, Terminal column){
        Header header = new Header(row, column);
        return anylzeTable.getOrDefault(header, null);
    }

    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<Header, Production> entry : anylzeTable.entrySet()){
            Header header = entry.getKey();
            Production production = entry.getValue();
            String sPro;
            if(production == null){
                sPro = "null";
            }
            else{
                sPro = production.toString();
            }
            stringBuilder.append( "(" + header.getRow().getName() + ", "
                    + header.getColumn() + "): " + sPro + "\n");
        }
        return stringBuilder.toString();
    }

    class Header{
        NonTerminal row;
        Terminal column;

        public Header(NonTerminal row, Terminal column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object obj){
            if(obj == null){
                return false;
            }
            Header objHeader = (Header)obj;
            return (row.getName().equals(objHeader.getRow().getName())
                    && column.getName().equals(objHeader.getColumn().getName()));
        }

        public NonTerminal getRow() {
            return row;
        }

        public void setRow(NonTerminal row) {
            this.row = row;
        }

        public Terminal getColumn() {
            return column;
        }

        public void setColumn(Terminal column) {
            this.column = column;
        }
    }
}
