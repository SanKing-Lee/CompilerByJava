package com.compiler.syntaxAnalysis;

import javax.swing.*;
import java.awt.*;

/**
 * Author: Sean
 * Date: Created In 16:32 2019/5/8
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class SyntaxAnalysisTest {
    public static void main(String[] args){
        SyntaxAnalysis syntaxAnalysis = new SyntaxAnalysis();

        EventQueue.invokeLater(()->{
            JFrame frame = new TreeFrame(syntaxAnalysis.analyze());
            frame.setTitle("抽象语法树");
            frame.setVisible(true);
        });
    }
}
