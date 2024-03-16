package com.sycomore.model.dashboard;

import com.sycomore.entity.Inscription;
import com.sycomore.entity.Promotion;
import com.sycomore.helper.Config;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.view.components.swing.DefaultCardModel;

/**
 * Model des données chargées du calcul du montant total déjà payé par les élèves.
 */
public class CashCounterCardModel extends DefaultCardModel<Double> {
    private static CashCounterCardModel instance;
    private final YearDataModel dataModel = YearDataModel.getInstance();

    private CashCounterCardModel() {}

    /**
     * Initialisation du model des données
     */
    private void init () {
        icon = Config.getIcon("dashboard/caisse");
        info = "Cash déjà payé";
        title = "Cash";
        value = 0d;

        dataModel.addYearDataListener(new YearDataModelAdapter() {
            @Override
            public void onLoadFinish() {
                revalidate();
            }
        });
    }

    /**
     * Action de revalidation de la valeur affichée par le card.
     */
    private void revalidate () {

        Promotion[] promotions = dataModel.getPromotions();
        if (promotions == null) {
            setValue(0d);
            return;
        }

        double sum = 0;
        for (Promotion promotion: promotions) {
            Inscription [] inscriptions = dataModel.getInscriptions(promotion);
            if (inscriptions == null)
                continue;

            for (Inscription i : inscriptions)
                if (i.getTotalPaidCash() !=  null)
                    sum += i.getTotalPaidCash();
        }

        setValue(sum);
    }

    /**
     * Utilitaire de recuperation de l'instance unique du model des données
     */
    public static CashCounterCardModel getInstance() {
        if (instance == null) {
            instance = new CashCounterCardModel();
            instance.init();
        }
        return instance;
    }
}
