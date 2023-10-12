package com.sycomore.view.moreoption;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.OptionRepository;
import com.sycomore.dao.SectionRepository;
import com.sycomore.entity.Classifiable;
import com.sycomore.entity.Option;
import com.sycomore.entity.Section;
import com.sycomore.model.OptionTableModel;
import com.sycomore.model.SectionComboBoxModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Date;

public class SectionsPanel extends JPanel {

    private final OptionTableModel tableModel = new OptionTableModel();
    private final JTable table = new JTable(tableModel);

    private final SectionComboBoxModel boxModel = new SectionComboBoxModel();
    private final JComboBox<Section> comboBox = new JComboBox<>(boxModel);

    private final JButton buttonAddingSection = new JButton("Ajouter une section");
    private final JButton buttonAddingOption = new JButton("Ajouter une option");

    private JDialog dialog;
    private ClassifiableForm <Classifiable> classifiableForm;

    private final SectionRepository sectionRepository;
    private final OptionRepository optionRepository;

    public SectionsPanel() {
        super(new BorderLayout(10, 10));

        sectionRepository = DAOFactory.getInstance(SectionRepository.class);
        optionRepository = DAOFactory.getInstance(OptionRepository.class);
        init();


        boxModel.init();
    }

    private void init () {
        JPanel buttons = new JPanel(new GridLayout(1, 2,5, 5));
        JPanel head =  new JPanel(new BorderLayout(10, 10));

        buttons.add(buttonAddingSection);
        buttons.add(buttonAddingOption);

        head.add(buttons, BorderLayout.EAST);
        head.add(comboBox, BorderLayout.CENTER);
        head.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(head, BorderLayout.NORTH);
        add(table, BorderLayout.CENTER);


        buttonAddingSection.addActionListener(e -> {
            buildDialog();

            dialog.setLocationRelativeTo(this);
            classifiableForm.setData(new Section());

            dialog.setVisible(true);
        });

        buttonAddingOption.addActionListener(e -> {
            buildDialog();

            dialog.setLocationRelativeTo(this);
            classifiableForm.setData(new Option());

            dialog.setVisible(true);
        });

        comboBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                tableModel.setSection(boxModel.getElementAt(comboBox.getSelectedIndex()));
            }
        });
    }

    private void buildDialog () {
        if (dialog != null)
            return;

        dialog = new JDialog();
        classifiableForm = new ClassifiableForm<>(formListener);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(classifiableForm);
        dialog.pack();
        dialog.setSize(dialog.getWidth() + 200, dialog.getHeight());
        dialog.setResizable(false);
    }

    private final ClassifiableForm.ClassifiableFormListener formListener = new ClassifiableForm.ClassifiableFormListener() {
        @Override
        public void onValidate(String shortName, String fullName, Classifiable data) {
            if (data == null)
                return;

            dialog.setVisible(false);
            dialog.dispose();

            data.setShortName(shortName);
            data.setFullName(fullName);

            if (data.getRecordingDate() == null)
                data.setRecordingDate(new Date());
            else
                data.setUpdatingDate(new Date());

            if (data instanceof Option) {
                Option option = (Option) data;
                Section section = boxModel.getElementAt(comboBox.getSelectedIndex());
                option.setSection(section);
                optionRepository.persist(option);
            } else {
                Section section = (Section) data;
                sectionRepository.persist(section);
            }
        }

        @Override
        public void onCancel() {
            dialog.setVisible(false);
            dialog.dispose();
        }
    };
}
