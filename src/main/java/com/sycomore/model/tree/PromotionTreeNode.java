package com.sycomore.model.tree;

import com.sycomore.entity.Promotion;

import javax.swing.tree.DefaultMutableTreeNode;

public class PromotionTreeNode extends DefaultMutableTreeNode {
    public PromotionTreeNode(Promotion promotion) {
        super(promotion, true);
    }
}
