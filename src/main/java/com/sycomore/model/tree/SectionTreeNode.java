package com.sycomore.model.tree;

import com.sycomore.entity.Option;
import com.sycomore.entity.School;
import com.sycomore.entity.Section;
import com.sycomore.model.YearDataModel;

import javax.swing.tree.DefaultMutableTreeNode;

public class SectionTreeNode extends DefaultMutableTreeNode {

    private final Section section;
    private final School school;
    private final YearDataModel yearDataModel;

    public SectionTreeNode(Section section, School school) {
        super(section);

        yearDataModel = YearDataModel.getInstance();

        this.section = section;
        this.school = school;

        reload();
    }

    private void reload () {
        Option[] options = yearDataModel.getOptions(section, school);
        for (Option option : options) {
            OptionTreeNode node = new OptionTreeNode(option, school);
            add(node);
        }
    }
}
