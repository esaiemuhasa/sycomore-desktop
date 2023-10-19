package com.sycomore.model.tree;

import com.sycomore.entity.Option;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.School;
import com.sycomore.entity.Section;
import com.sycomore.model.YearDataModel;

import javax.swing.tree.DefaultMutableTreeNode;

public class SchoolTreeNode extends DefaultMutableTreeNode {

    private School school;
    private YearDataModel yearDataModel;

    public SchoolTreeNode() {
        super();
        yearDataModel = YearDataModel.getInstance();
    }


    @Override
    public void setUserObject(Object userObject) {
        if (!(userObject instanceof School))
            throw new RuntimeException("only instance of "+School.class.getName()+" are supported");

        super.setUserObject(userObject);
        setSchool(school);
    }

    public void setSchool(School school) {

        if (school == null) {
            removeAllChildren();
            return;
        }

        if (school.equals(this.school))
            return;

        removeAllChildren();

        this.school = school;
        Section [] sections = yearDataModel.getSections(school);
        if (sections != null) {
            for (Section s : sections) {
                SectionTreeNode node = new SectionTreeNode(s, school);
                add(node);
            }
        } else {
            //list de promotion directement
            Promotion [] promotions = yearDataModel.getPromotions(school);
            if (promotions != null) {
                for (Promotion p : promotions) {
                    PromotionTreeNode node = new PromotionTreeNode(p);
                    add(node);
                }
            }
        }
    }

    private void reload () {

    }
}
