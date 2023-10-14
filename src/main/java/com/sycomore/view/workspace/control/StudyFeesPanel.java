package com.sycomore.view.workspace.control;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.PromotionStudyFeesRepository;
import com.sycomore.dao.StudyFeesConfigRepository;
import com.sycomore.entity.PromotionStudyFees;
import com.sycomore.entity.School;
import com.sycomore.entity.StudyFeesConfig;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.model.YearDataModelListener;

import javax.swing.*;
import java.awt.*;

/**
 * Panel de configuration des frais d'étude pour une année scolaire
 */
public class StudyFeesPanel extends JPanel {

    private final DefaultListModel<StudyFeesConfig> listModelConfig = new DefaultListModel<>();
    private final DefaultListModel<PromotionStudyFees> listModelPromotion = new DefaultListModel<>();
    private final JList<StudyFeesConfig> listConfig = new JList<>(listModelConfig);
    private final JList<PromotionStudyFees> listPromotion = new JList<>(listModelPromotion);

    private final DefaultComboBoxModel<School> comboBoxModel = new DefaultComboBoxModel<>();
    private final JComboBox<School> comboBox = new JComboBox<>(comboBoxModel);
    private final JButton buttonAddingConfig = new JButton("Ajouter un autre frais");
    private final JButton buttonAddingPromotion = new JButton("Associer d'autre(s) promotion(s)");

    private final YearDataModel dataModel;
    private final StudyFeesConfigRepository studyFeesConfigRepository;
    private final PromotionStudyFeesRepository promotionStudyFeesRepository;

    public StudyFeesPanel() {
        super(new BorderLayout());
        dataModel = YearDataModel.getInstance();
        studyFeesConfigRepository = DAOFactory.getInstance(StudyFeesConfigRepository.class);
        promotionStudyFeesRepository = DAOFactory.getInstance(PromotionStudyFeesRepository.class);

        init();

        dataModel.addYearDataListener(dataModelListener);
    }

    private void init () {
        JPanel left = new JPanel(new BorderLayout());
        JPanel right = new JPanel(new BorderLayout());

        JPanel leftContainer = new JPanel(new BorderLayout(10, 10));
        JPanel rightContainer = new JPanel(new BorderLayout(10, 10));

        leftContainer.add(new JScrollPane(listPromotion), BorderLayout.CENTER);
        leftContainer.add(buttonAddingPromotion, BorderLayout.SOUTH);
        leftContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rightContainer.add(comboBox, BorderLayout.NORTH);
        rightContainer.add(new JScrollPane(listConfig), BorderLayout.CENTER);
        rightContainer.add(buttonAddingConfig, BorderLayout.SOUTH);
        rightContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        left.setBorder(BorderFactory.createLineBorder(UIManager.getColor("border_color")));
        right.setBorder(BorderFactory.createLineBorder(UIManager.getColor("border_color")));

        left.add(leftContainer, BorderLayout.CENTER);
        right.add(rightContainer, BorderLayout.CENTER);

        JSplitPane split = new JSplitPane();
        split.setLeftComponent(left);
        split.setRightComponent(right);
        split.setDividerLocation(400);
        split.setDividerSize(10);

        add(split, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void revalidateSchools () {
        School [] schools = dataModel.getSchools();
        comboBoxModel.removeAllElements();
        if (schools == null)
            return;

        for (School s : schools)
            comboBoxModel.addElement(s);
    }

    protected final YearDataModelListener dataModelListener = new YearDataModelAdapter() {
        @Override
        public void onLoadFinish() {
            revalidateSchools();
        }
    };

}
