package com.sycomore.view.workspace.students;

import com.sycomore.entity.School;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.model.tree.SchoolTreeModel;
import com.sycomore.model.tree.SchoolsTreeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class Sidebar extends JPanel {

    private final YearDataModel yearDataModel;
    private final Header header = new Header();
    private final Container container = new Container();

    public Sidebar() {
        super(new BorderLayout(10, 10));

        yearDataModel = YearDataModel.getInstance();

        add(header, BorderLayout.NORTH);
        add(container, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
        yearDataModel.addYearDataListener(yearDataModelAdapter);
    }

    @Override
    protected void paintChildren(Graphics graphics) {
        super.paintChildren(graphics);

        super.paintChildren(graphics);

        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(UIManager.getColor("border_color"));

        g.drawLine(0, 0, 0, getHeight());
    }

    protected final YearDataModelAdapter yearDataModelAdapter = new YearDataModelAdapter() {
        @Override
        public void onLoadStart() {
            setEnabled(false);
        }

        @Override
        public void onLoadFinish() {
            header.reload();
            setEnabled(true);
        }
    };

    /**
     * Entête du menu de navigation de la section.
     * Permet de filtrer les promotions école
     */
    private class Header extends JPanel {
        private final DefaultComboBoxModel<School> schoolComboBoxModel = new DefaultComboBoxModel<>();
        private final JComboBox<School> schoolComboBox = new JComboBox<>(schoolComboBoxModel);
        private final JCheckBox filterBySchools = new JCheckBox("",true);
        public Header() {
            super(new BorderLayout());

            Box box = Box.createHorizontalBox();
            box.add(filterBySchools);
            box.add(Box.createHorizontalStrut(10));
            box.add(schoolComboBox);

            add(box, BorderLayout.CENTER);

            setBackground(UIManager.getColor("border_color_l10"));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            initEvents();
        }

        /**
         * Rechargement des donnees du model du combo box perméntent de choisir l'école
         */
        private void reload () {
            schoolComboBoxModel.removeAllElements();

            School [] schools = yearDataModel.getSchools();
            if (schools == null)
                return;

            for (School s : schools)
                schoolComboBoxModel.addElement(s);
        }

        private void initEvents () {
            filterBySchools.addActionListener(e -> {
                boolean filter = filterBySchools.isSelected();
                schoolComboBox.setEnabled(filter);

                if (filter) {
                    School school = schoolComboBoxModel.getElementAt(schoolComboBox.getSelectedIndex());
                    container.setSchool(school);
                } else
                    container.setSchool(null);
            });

            schoolComboBox.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.DESELECTED)
                    return;

                School school = (School) e.getItem();
                container.setSchool(school);
            });
        }
    }

    private static class Container extends JPanel {

        private final SchoolTreeModel schoolTreeModel = new SchoolTreeModel();
        private final SchoolsTreeModel schoolsTreeModel = new SchoolsTreeModel();
        private final JTree tree = new JTree(schoolTreeModel);

        public Container() {
            super(new BorderLayout());

            add(new JScrollPane(tree), BorderLayout.CENTER);
            tree.setRootVisible(false);
        }

        public void setSchool (School school) {
            if (school != null) {
                schoolTreeModel.setSchool(school);
                tree.setModel(schoolTreeModel);
            } else {
                tree.setModel(schoolsTreeModel);
            }
        }
    }
}
