package com.sycomore.view.workspace.control;

import com.sycomore.entity.Promotion;
import com.sycomore.entity.StudyFeesConfig;
import com.sycomore.model.YearDataModel;

import javax.swing.*;
import java.awt.*;

public class PromotionStudyFeesForm extends JPanel {

    private final DefaultListModel<Promotion> listModelPromotion = new DefaultListModel<>();
    private final JList<Promotion> listPromotion = new JList<>(listModelPromotion);

    private final JButton buttonValidate = new JButton("Valider");
    private final JButton buttonCancel = new JButton("Annuler");

    private final PromotionStudyFeesFormListener formListener;

    private final YearDataModel dataModel;

    private StudyFeesConfig config;

    public PromotionStudyFeesForm(PromotionStudyFeesFormListener formListener) {
        super(new BorderLayout());
        this.formListener = formListener;
        dataModel = YearDataModel.getInstance();
        init();
    }

    public void setConfig(StudyFeesConfig config) {
        this.config = config;

        listModelPromotion.removeAllElements();

        if (config == null)
            return;

        Promotion [] promotions = dataModel.getPromotions(config.getSchool());
        Promotion [] usedPromotions = dataModel.getPromotions(config);

        for (Promotion promotion: promotions) {
            boolean check = false;
            if (usedPromotions != null) {
                for (Promotion p : usedPromotions)
                    if (promotion.equals(p)) {
                        check = true;
                        break;
                    }
            }

            if (!check) {
                listModelPromotion.addElement(promotion);
            }
        }
    }

    private void init() {
        JPanel center = new JPanel(new BorderLayout());
        JPanel footer = new JPanel();

        center.add(new JScrollPane(listPromotion), BorderLayout.CENTER);

        footer.add(buttonValidate);
        footer.add(buttonCancel);

        buttonValidate.addActionListener(event -> handleClickValidation());
        buttonCancel.addActionListener(event -> handleClickCancel());

        add(center, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void handleClickValidation () {
        int [] indices = listPromotion.getSelectedIndices();
        if (indices == null || indices.length == 0)
            return;

        Promotion [] promotions = new Promotion[indices.length];
        for (int i = 0; i < indices.length; i++)
            promotions[i] = listModelPromotion.elementAt(indices[i]);

        formListener.onValidate(promotions, config);
        config = null;
    }

    private void handleClickCancel () {
        formListener.onCancel();
    }


    public interface PromotionStudyFeesFormListener {
        /**
         * Événement de validation. Transmission des promotions choisie par l'utilisateur
         */
        void onValidate(Promotion [] promotions, StudyFeesConfig config);

        /**
         * Annulation de l'opération d'association des promotions aux frais d'étude
         */
        void onCancel();
    }
}
