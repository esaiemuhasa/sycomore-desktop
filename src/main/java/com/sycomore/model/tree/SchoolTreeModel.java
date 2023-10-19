package com.sycomore.model.tree;

import com.sycomore.entity.School;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class SchoolTreeModel extends DefaultTreeModel {

    private final SchoolTreeNode rootNode;

    public SchoolTreeModel() {
        super(new SchoolTreeNode());
        rootNode = (SchoolTreeNode) getRoot();
    }

    public void setSchool (School school) {
        rootNode.setSchool(school);

        fireTreeStructureChanged(this, new Object[]{rootNode}, new int[] {0}, null);
    }
}
