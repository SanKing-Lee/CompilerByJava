package com.compiler;

import com.compiler.syntaxAnalysis.GrammarAnalysis;
import com.compiler.syntaxAnalysis.SyntaxAnalysis;
import sun.awt.Symbol;

import java.io.File;

import static com.compiler.lexicalAnalysis.Tag.RBRACE;

/**
 * Author: Sean
 * Date: Created In 17:00 2019/4/9
 * Title:
 * Description:
 * Version:
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class Compiler {
    public static void main(String[] args){
        SyntaxAnalysis syntaxAnalysis = new SyntaxAnalysis();
        syntaxAnalysis.analyze();
    }
}
