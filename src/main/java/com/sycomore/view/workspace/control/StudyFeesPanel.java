package com.sycomore.view.workspace.control;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.PromotionStudyFeesRepository;
import com.sycomore.dao.RepositoryAdapter;
import com.sycomore.dao.StudyFeesConfigRepository;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.PromotionStudyFees;
import com.sycomore.entity.School;
import com.sycomore.entity.StudyFeesConfig;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.model.YearDataModelListener;
import com.sycomore.view.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
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
    private final JPopupMenu promotionPopup = new JPopupMenu();
    private final JMenuItem itemDeleteConfig = new JMenuItem("Supprimer");
    private final JMenuItem itemUpdateConfig = new JMenuItem("Modifier");
    private final JMenuItem itemDeletePromotion = new JMenuItem("Détacher la promotion");

    private JDialog configDialog;
    private StudyFeesConfigForm configForm;
    private JDialog promotionDialog;
    private PromotionStudyFeesForm promotionStudyFeesForm;

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

        promotionPopup.add(itemDeletePromotion);

        buttonAddingConfig.addActionListener(e -> handleAddingConfiguration());
        buttonAddingPromotion.addActionListener(e -> handleAddPromotion());
        itemUpdateConfig.addActionListener(e -> handleUpdateConfiguration());
        itemDeletePromotion.addActionListener(e -> handleDeletePromotion());

        listConfig.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (listConfig.getSelectedIndex() != -1 && e.isPopupTrigger())
                    configPopup.show(listConfig, e.getX(), e.getY());
            }
        });

        listPromotion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && listPromotion.getSelectedIndex() != -1)
                    promotionPopup.show(listPromotion, e.getX(), e.getY());
            }
        });

        listConfig.addListSelectionListener(event -> reloadPromotions());
        comboBox.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED)
                loadConfigs();
        });
    }

    private void revalidateSchools () {
        School [] schools = dataModel.getSchools();
        comboBoxModel.removeAllElements();
        if (schools == null)
            return;

        for (School s : schools)
            comboBoxModel.addElement(s);

        loadConfigs();
    }

    /**
     * Rechargement des donnees du model du JList visualisant la liste des configurations des frais scolaire
     * d'une école.
     */
    private void loadConfigs () {
        listModelConfig.removeAllElements();
        School school = comboBoxModel.getElementAt(comboBox.getSelectedIndex());
        StudyFeesConfig [] configs = dataModel.getStudyFeesConfigs(school);

        if (configs != null) {
            for (StudyFeesConfig c: configs)
                listModelConfig.addElement(c);

            listConfig.setSelectedIndex(0);
        }
    }

    /**
     * Rechargement des elements du model du JList visualisant la liste des promotions associé aux frais scolaire
     */
    private void reloadPromotions () {
        listModelPromotion.removeAllElements();
        StudyFeesConfig config = getSelectedConfig();

        if (config == null)
            return;

        PromotionStudyFees [] fees = dataModel.getPromotionStudyFees(config);
        if (fees == null)
            return;

        for (PromotionStudyFees f : fees)
            listModelPromotion.addElement(f);
    }

    private StudyFeesConfig getSelectedConfig () {
        int index = listConfig.getSelectedIndex();
        if (index == -1 && !listModelConfig.isEmpty())
            index = 0;

        if (index == -1)
            return null;

        return listModelConfig.getElementAt(index);
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

    private void handleAddPromotion () {
        buildPromotionDialog();

        StudyFeesConfig config = getSelectedConfig();
        promotionDialog.setLocationRelativeTo(promotionDialog.getOwner());
        promotionStudyFeesForm.setConfig(config);
        promotionDialog.setVisible(true);
    }

    private void handleDeletePromotion () {
        StudyFeesConfig config = getSelectedConfig();
        PromotionStudyFees p = listModelPromotion.getElementAt(listPromotion.getSelectedIndex());

        assert config != null;

        int status = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment détacher la promotion "+p.getPromotion().toString()
                +" du "+config.getAmount()+" USD ("+config.getCaption()+")", "Détachement de la promotion", JOptionPane.OK_CANCEL_OPTION);

        if (status == JOptionPane.OK_OPTION) {
            promotionStudyFeesRepository.remove(p);
        }
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

    /**
     * Utilitaire de chargement de la boite de dialogue permettant l'association des promotions
     * aux frais scolaires.
     */
    private void buildPromotionDialog () {
        if (promotionDialog != null)
            return;

        promotionDialog = new JDialog(MainWindow.getInstance());
        promotionStudyFeesForm = new PromotionStudyFeesForm(promotionStudyFeesFormListener);
        promotionDialog.setContentPane(promotionStudyFeesForm);
        promotionDialog.setTitle("Ajout des promotions au frais d'étude");

        promotionDialog.pack();
        promotionDialog.setSize(promotionDialog.getWidth() + 150, promotionDialog.getHeight());
        promotionDialog.setResizable(false);
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
            configForm.setConfig(null);
            configDialog.setVisible(false);
            configDialog.dispose();
        }
    };

    protected final PromotionStudyFeesForm.PromotionStudyFeesFormListener promotionStudyFeesFormListener = new PromotionStudyFeesForm.PromotionStudyFeesFormListener() {
        @Override
        public void onValidate(Promotion[] promotions, StudyFeesConfig config) {
            onCancel();

            for (Promotion p : promotions) {
                PromotionStudyFees fees = new PromotionStudyFees();

                fees.setConfig(config);
                fees.setPromotion(p);
                fees.setRecordingDate(new Date());

                promotionStudyFeesRepository.persist(fees);
            }
        }

        @Override
        public void onCancel() {
            promotionDialog.setVisible(false);
            promotionDialog.dispose();
        }
    };

}
