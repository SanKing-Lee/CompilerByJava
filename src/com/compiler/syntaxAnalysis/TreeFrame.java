package com.compiler.syntaxAnalysis;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.awt.geom.Dimension2D;

/**
 * Author: Sean
 * Date: Created In 16:28 2019/5/8
 * Title:
 * Description:
 * Version: 0.1
 * Update History:
 * [Date][Version][Author] What has been done;
 */

public class TreeFrame extends JFrame {
    private DefaultTreeModel model;
    private JTree tree;
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 450;

    public TreeFrame(DefaultMutableTreeNode root){
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension2D screen = toolkit.getScreenSize();
        setLocation((int)(screen.getWidth() - DEFAULT_WIDTH)/2, (int)(screen.getHeight()-DEFAULT_HEIGHT)/2);
        model = new DefaultTreeModel(root);
        tree = new JTree(model);
        add(new JScrollPane(tree));

    }
}
