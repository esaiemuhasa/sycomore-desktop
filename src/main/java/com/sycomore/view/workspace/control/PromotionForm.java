package com.sycomore.view.workspace.control;

import com.sycomore.dao.*;
import com.sycomore.entity.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PromotionForm extends JPanel {

    private final DefaultComboBoxModel<School> schoolBoxModel = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<Section> sectionBoxModel = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<Category> categoryBoxModel = new DefaultComboBoxModel<>();
    private final DefaultListModel<Option> optionListModel = new DefaultListModel<>();
    private final DefaultListModel<Level> levelListModel = new DefaultListModel<>();

    private final JComboBox<School> schoolComboBox = new JComboBox<>(schoolBoxModel);
    private final JComboBox<Section> sectionComboBox = new JComboBox<>(sectionBoxModel);
    private final JComboBox<Category> categoryComboBox = new JComboBox<>(categoryBoxModel);

    private final JList<Option> optionList = new JList<>(optionListModel);
    private final JList<Level> levelList = new JList<>(levelListModel);

    private final JCheckBox boxSection = new JCheckBox("Sections", true);
    private final JCheckBox boxCategory = new JCheckBox("Classification", true);
    private final JButton buttonValidate = new JButton("Valider");
    private final JButton buttonCancel = new JButton("Annuler");

    private final List<Option> options = new ArrayList<>();
    private final PromotionFormListener formListener;

    private final SchoolRepository schoolRepository;
    private final LevelRepository levelRepository;
    private final SectionRepository sectionRepository;
    private final OptionRepository optionRepository;
    private final CategoryRepository categoryRepository;

    public PromotionForm(PromotionFormListener formListener) {
        super(new BorderLayout(10, 10));
        this.formListener = formListener;
        schoolRepository = DAOFactory.getInstance(SchoolRepository.class);
        levelRepository = DAOFactory.getInstance(LevelRepository.class);
        sectionRepository = DAOFactory.getInstance(SectionRepository.class);
        optionRepository = DAOFactory.getInstance(OptionRepository.class);
        categoryRepository = DAOFactory.getInstance(CategoryRepository.class);
        init();
    }

    /**
     * Action d'initialisation des models des données des composants graphiques
     */
    public void setup () {
        School [] schools = schoolRepository.findAll();
        Level [] levels = levelRepository.findAll();
        Section [] sections = sectionRepository.findAll();
        Option [] ops = optionRepository.findAll();
        Category [] categories = categoryRepository.findAll();

        if (ops != null)
            options.addAll(Arrays.asList(ops));

        if (schools != null)
            for (School s : schools)
                schoolBoxModel.addElement(s);

        if (sections != null)
            for (Section s: sections)
                sectionBoxModel.addElement(s);

        if (categories != null)
            for (Category c: categories)
                categoryBoxModel.addElement(c);
        else {
            boxCategory.setSelected(false);
            checkCategory();
        }

        if (levels != null)
            for (Level l : levels)
                levelListModel.addElement(l);
    }

    private void filterOptions (ItemEvent event) {
        if (event == null || event.getStateChange() != ItemEvent.SELECTED)
            return;

        Section section = sectionBoxModel.getElementAt(sectionComboBox.getSelectedIndex());
        optionListModel.clear();
        for (Option op : options)
            if (Objects.equals(op.getSection().getId(), section.getId()))
                optionListModel.addElement(op);
    }

    private void handleClickValidate (ActionEvent event) {
        List<Promotion> promotions = new ArrayList<>();

        int [] levels = levelList.getSelectedIndices();
        School school = schoolBoxModel.getElementAt(schoolComboBox.getSelectedIndex());

        for (int l : levels) {

            Promotion p = new Promotion();
            p.setLevel(levelListModel.getElementAt(l));
            p.setSchool(school);

            if (boxSection.isSelected()) {
                int [] options = optionList.getSelectedIndices();
                for (int o: options) {
                    p.setOption(optionListModel.getElementAt(o));
                }
            }

            if (boxCategory.isSelected())
                p.setCategory(categoryBoxModel.getElementAt(categoryComboBox.getSelectedIndex()));

            promotions.add(p);
        }

        if (promotions.isEmpty()){
            JOptionPane.showMessageDialog(this, "Impossible d'effectuer cette operation", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        int count = promotions.size();
        formListener.onValidate(promotions.toArray(new Promotion[count]));
    }

    private void checkSectionAction () {
        boolean check = boxSection.isSelected();
        sectionComboBox.setEnabled(check);
        optionList.setEnabled(check);
    }

    private void checkCategory () {
        boolean check = boxSection.isSelected();
        categoryComboBox.setEnabled(check);
    }

    private void init () {
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));
        JPanel bottom = new JPanel();

        center.add(initSection());
        center.add(initLevelAndCategory());

        bottom.add(buttonValidate);
        bottom.add(buttonCancel);

        add(initSchool(), BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        boxSection.addActionListener(e -> checkSectionAction());
        boxCategory.addActionListener(e -> checkCategory());

        buttonValidate.addActionListener(this::handleClickValidate);
        buttonCancel.addActionListener(event -> formListener.onCancel());
    }

    private JPanel initSchool () {
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Écoles");

        panel.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));
        panel.add(label, BorderLayout.NORTH);
        panel.add(schoolComboBox, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel initSection () {
        JPanel container = new JPanel(new BorderLayout(10, 10));

        JPanel label = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Box top = Box.createVerticalBox();

        label.add(boxSection);
        top.add(label);
        top.add(sectionComboBox);
        top.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));

        container.add(top, BorderLayout.NORTH);
        container.add(new JScrollPane(optionList), BorderLayout.CENTER);

        JPanel content = new JPanel(new BorderLayout());
        content.add(container, BorderLayout.CENTER);
        content.setBorder(BorderFactory.createLineBorder(UIManager.getColor("border_color")));
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sectionComboBox.addItemListener(this::filterOptions);

        return content;
    }

    private JPanel initLevelAndCategory () {
        JPanel container = new JPanel(new BorderLayout(10, 10));

        JPanel label = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Box top = Box.createVerticalBox();

        label.add(boxCategory);
        top.add(label);
        top.add(categoryComboBox);
        top.setMaximumSize(new Dimension(Short.MAX_VALUE, 80));

        container.add(top, BorderLayout.SOUTH);
        container.add(new JScrollPane(levelList), BorderLayout.CENTER);


        JPanel content = new JPanel(new BorderLayout());
        content.add(container, BorderLayout.CENTER);
        content.setBorder(BorderFactory.createLineBorder(UIManager.getColor("border_color")));
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return content;
    }

    /**
     * Interface d'écoute des événements du formulaire d'ajout des promotions.
     */
    public interface PromotionFormListener {

        /**
         * Lors de la validation d'enregistrement des nouvelles promotions
         */
        void onValidate(Promotion ... promotions);

        /**
         * Lors de l'annulation de l'opération
         */
        void onCancel();
    }
}
