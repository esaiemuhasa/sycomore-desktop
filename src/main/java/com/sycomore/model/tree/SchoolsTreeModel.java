package com.sycomore.model.tree;

import com.sycomore.entity.School;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

public class SchoolsTreeModel extends DefaultTreeModel {

    private final YearDataModel yearDataModel;

    public SchoolsTreeModel() {
        super(new DefaultMutableTreeNode());

        yearDataModel = YearDataModel.getInstance();
        yearDataModel.addYearDataListener(yearDataModelAdapter);
    }

    private void reloadStructure () {

        School [] schools = yearDataModel.getSchools();
        if (schools == null)
            return;

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
        root.removeAllChildren();

        for (School s : schools) {
            SchoolTreeNode node = new SchoolTreeNode(s);
            node.reload();
            root.add(node);
        }
    }

    protected final YearDataModelAdapter yearDataModelAdapter = new YearDataModelAdapter() {
        @Override
        public void onLoadFinish() {
            reloadStructure();
        }
    };
}
