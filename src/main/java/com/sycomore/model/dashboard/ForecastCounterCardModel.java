package com.sycomore.model.dashboard;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.PromotionRepository;
import com.sycomore.dao.RepositoryAdapter;
import com.sycomore.entity.Promotion;
import com.sycomore.helper.Config;
import com.sycomore.model.YearDataModel;
import com.sycomore.model.YearDataModelAdapter;
import com.sycomore.view.components.swing.DefaultCardModel;

/**
 * Model utilitaire qui calcule le montant total prévu pour le budget.
 */
public class ForecastCounterCardModel extends DefaultCardModel<Double> {
    private static ForecastCounterCardModel instance;

    private final YearDataModel dataModel = YearDataModel.getInstance();

    private ForecastCounterCardModel() {}

    /**
     * Initialisation du model des données
     */
    private void init () {
        icon = Config.getIcon("dashboard/accounting");
        info = "Prévision budgétaire";
        title = "Budget";
        value = 0d;

        PromotionRepository promotionRepository = DAOFactory.getInstance(PromotionRepository.class);

        dataModel.addYearDataListener(new YearDataModelAdapter() {
            @Override
            public void onLoadFinish() {
               revalidate();
            }
        });

        promotionRepository.addRepositoryListener(new RepositoryAdapter<Promotion>() {
            @Override
            public void onUpdate(Promotion oldState, Promotion newState) {
                if (newState.getYear().equals(dataModel.getYear()))
                    revalidate();
            }
        });
    }

    /**
     * Action de revalidation de la valeur affichée par le card.
     */
    private void revalidate () {
        Promotion [] promotions = dataModel.getPromotions();
        if (promotions == null) {
            setValue(0d);
            return;
        }

        double sum = 0;
        for (Promotion promotion: promotions)
            if (promotion.getInscriptionsCount() != null && promotion.getTotalStudyFees() != null)
                sum += (promotion.getTotalStudyFees() * promotion.getInscriptionsCount());

        setValue(sum);
    }

    /**
     * Utilitaire de recuperation de l'instance unique du model des données
     */
    public static ForecastCounterCardModel getInstance() {
        if (instance == null) {
            instance = new ForecastCounterCardModel();
            instance.init();
        }
        return instance;
    }
}
