package com.compiler.syntaxAnalysis;

import com.compiler.lexicalAnalysis.Tag;

import java.util.List;
import java.util.Properties;

/**
 * Author: Sean
 * Date: Created In 23:42 2019/5/5
 * Title: 产生式类
 * Description: 描述产生式的类，包括了它的左部和右部
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class Production {
    private NonTerminal left;
    private List<Symbol> right;

    public Production(NonTerminal left, List<Symbol> right) {
        this.left = left;
        this.right = right;
    }

    public NonTerminal getLeft() {
        return left;
    }

    public void setLeft(NonTerminal left) {
        this.left = left;
    }

    public List<Symbol> getRight() {
        return right;
    }

    public void setRight(List<Symbol> right) {
        this.right = right;
    }

    public String toString() {
        String ret = left.getName() + "->";
        for (int i = 0; i < right.size(); i++) {
            ret += right.get(i).getName();
            if (i < right.size() - 1) {
                ret += " ";
            }
        }
        return ret;
    }
}

abstract class Symbol {
    private String name;
    private boolean terminal;

    public Symbol(String name, boolean terminal) {
        this.name = name;
        this.terminal = terminal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Object obj){
        if(obj == null){
            return false;
        }
        Symbol objSymbol = (Symbol)obj;
        return name.equals(objSymbol.getName());
    }
}

class NonTerminal extends Symbol {
    public NonTerminal(String name) {
        super(name, false);
    }
}

class Terminal extends Symbol {
    private Tag tag;

    public Terminal(String name) {
        super(name, true);
        this.tag = Enum.valueOf(Tag.class, name);
    }
}
