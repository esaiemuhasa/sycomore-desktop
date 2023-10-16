package com.sycomore.view.workspace.control;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.PromotionStudyFeesRepository;
import com.sycomore.dao.RepositoryAdapter;
import com.sycomore.dao.StudyFeesConfigRepository;
import com.sycomore.entity.PromotionStudyFees;
import com.sycomore.entity.School;
import com.sycomore.entity.StudyFeesConfig;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.model.YearDataModelListener;
import com.sycomore.view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

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

    private final JPopupMenu configPopup = new JPopupMenu();
    private final JMenuItem itemDeleteConfig = new JMenuItem("Supprimer");
    private final JMenuItem itemUpdateConfig = new JMenuItem("Modifier");

    private JDialog configDialog;
    private StudyFeesConfigForm configForm;

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

        studyFeesConfigRepository.addRepositoryListener(new RepositoryAdapter<StudyFeesConfig>() {
            @Override
            public void onCreate(StudyFeesConfig config) {
                if (config.getYear().equals(dataModel.getYear()))
                    listModelConfig.addElement(config);
            }

            @Override
            public void onUpdate(StudyFeesConfig oldState, StudyFeesConfig newState) {
                if (!newState.getYear().equals(dataModel.getYear()))
                    return;

                for (int i = 0; i < listModelConfig.getSize(); i++) {
                    StudyFeesConfig item = listModelConfig.getElementAt(i);
                    if (item.equals(newState)){
                        listModelConfig.setElementAt(item, i);
                        break;
                    }
                }
            }

            @Override
            public void onDelete(StudyFeesConfig config) {
                if (!config.getYear().equals(dataModel.getYear()))
                    return;

                for (int i = 0; i < listModelConfig.getSize(); i++) {
                    StudyFeesConfig item = listModelConfig.getElementAt(i);
                    if (item.equals(config)){
                        listModelConfig.remove(i);
                        break;
                    }
                }
            }
        });
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

        configPopup.add(itemUpdateConfig);
        configPopup.add(itemDeleteConfig);

        buttonAddingConfig.addActionListener(e -> handleAddingConfiguration());
        itemUpdateConfig.addActionListener(e -> handleUpdateConfiguration());
        listConfig.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (listConfig.getSelectedIndex() != -1 && e.isPopupTrigger())
                    configPopup.show(listConfig, e.getX(), e.getY());
            }
        });
    }

    private void revalidateSchools () {
        School [] schools = dataModel.getSchools();
        comboBoxModel.removeAllElements();
        if (schools == null)
            return;

        for (School s : schools)
            comboBoxModel.addElement(s);
    }

    private StudyFeesConfig getSelectedConfig () {
        return listModelConfig.getElementAt(listConfig.getSelectedIndex());
    }

    private void handleAddingConfiguration () {
        buildConfigDialog();

        configDialog.setLocationRelativeTo(configDialog.getOwner());
        configDialog.setTitle("Ajout d'une configuration des frais scolaire");
        configDialog.setVisible(true);
    }

    private void handleUpdateConfiguration () {
        buildConfigDialog();

        StudyFeesConfig config = getSelectedConfig();
        configForm.setConfig(config);
        configDialog.setLocationRelativeTo(configDialog.getOwner());
        configDialog.setTitle("Modification de la configuration des frais scolaire");
        configDialog.setVisible(true);
    }

    private void buildConfigDialog () {
        if (configDialog != null)
            return;

        configDialog = new JDialog(MainWindow.getInstance());
        configForm = new StudyFeesConfigForm(configFormListener);

        configDialog.setContentPane(configForm);
        configDialog.pack();
        configDialog.setSize(configDialog.getWidth() + 200, configDialog.getHeight());
        configDialog.setResizable(false);
        configDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    protected final YearDataModelListener dataModelListener = new YearDataModelAdapter() {
        @Override
        public void onLoadFinish() {
            revalidateSchools();
        }
    };

    protected final StudyFeesConfigForm.StudyFeesConfigFormListener configFormListener = new StudyFeesConfigForm.StudyFeesConfigFormListener() {
        @Override
        public void onValidate(double amount, Date startDate, Date endDate, String caption, StudyFeesConfig config) {
            onCancel();

            if (config == null)
                config = new StudyFeesConfig();

            config.setAmount(amount);
            config.setStart(new java.sql.Date(startDate.getTime()));
            config.setEnd(new java.sql.Date(endDate.getTime()));
            config.setCaption(caption);

            if (config.getId() == null || config.getId() <= 0) {
                config.setYear(dataModel.getYear());
                config.setSchool(comboBoxModel.getElementAt(comboBox.getSelectedIndex()));
                config.setRecordingDate(new Date());
            } else {
                config.setUpdatingDate(new Date());
            }

            studyFeesConfigRepository.persist(config);
        }

        @Override
        public void onCancel() {
            configDialog.setVisible(false);
            configDialog.dispose();
        }
    };

}
