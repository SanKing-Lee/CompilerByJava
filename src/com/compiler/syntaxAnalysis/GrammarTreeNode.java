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
    private int id;
    private String name;
    private GrammarTreeNode parent;
    private List<GrammarTreeNode> children;
}
