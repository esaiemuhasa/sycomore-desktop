package com.sycomore.model.tree;

import com.sycomore.entity.Option;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.School;
import com.sycomore.model.YearDataModel;

import javax.swing.tree.DefaultMutableTreeNode;

public class OptionTreeNode extends DefaultMutableTreeNode {
    private final Option option;
    private final School school;

    private final YearDataModel yearDataModel;

    public OptionTreeNode(Option option, School school) {
        super(option);
        yearDataModel = YearDataModel.getInstance();

        this.option = option;
        this.school = school;

        reload();
    }

    private  void reload () {
        Promotion[] promotions = yearDataModel.getPromotions(option, school);
        if (promotions != null) {
            for (Promotion p : promotions) {
                PromotionTreeNode node = new PromotionTreeNode(p);
                add(node);
            }
        }
    }
}
