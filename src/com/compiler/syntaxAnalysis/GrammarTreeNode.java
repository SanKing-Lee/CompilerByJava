package com.compiler.syntaxAnalysis;

import java.util.HashMap;
import java.util.List;

/**
 * Author: Sean
 * Date: Created In 11:40 2019/5/7
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class GrammarTreeNode {
    private Symbol symbol;
    private List<GrammarTreeNode> Childrens;
    private HashMap<NonTerminal, List<Production>> productions;

    public GrammarTreeNode(Symbol symbol, HashMap<NonTerminal, List<Production>> productions) {
        this.symbol = symbol;
        this.productions = productions;
        if(!symbol.isTerminal()){
            NonTerminal nonTerminal = (NonTerminal)symbol;
        }
    }


}
