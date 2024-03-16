package com.sycomore.helper;


import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.PromotionRepository;
import com.sycomore.dao.PromotionStudyFeesRepository;
import com.sycomore.dao.RepositoryAdapter;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.PromotionStudyFees;
import com.sycomore.model.YearDataModel;

/**
 * Utilitaire de verification de l'état des occurrences et/ou de valeurs mise en cache lors des changements des certaines occurrences dans la BDD.
 * Cette classe implement le modèle singleton.
 * Pour avoir l'instance de ladite classe, vous devez passer par la methode getInstance().
 */
public final class StateChecker {

    private static StateChecker instance;


    private final YearDataModel yearDataModel = YearDataModel.getInstance();
    private final PromotionRepository promotionRepository = DAOFactory.getInstance(PromotionRepository.class);
    private final PromotionStudyFeesRepository promotionStudyFeesRepository = DAOFactory.getInstance(PromotionStudyFeesRepository.class);

    private StateChecker () {}

    /**
     * Initialisation du vérificateur d'états, et interception des événements du DAO.
     */
    private void init () {
        promotionStudyFeesRepository.addRepositoryListener(new RepositoryAdapter<PromotionStudyFees>() {
            @Override
            public void onCreate(PromotionStudyFees promotionStudyFees) {
                revalidatePromotion(promotionStudyFees.getPromotion());
            }

            @Override
            public void onUpdate(PromotionStudyFees oldState, PromotionStudyFees newState) {
                revalidatePromotion(oldState.getPromotion());
                if (!oldState.getPromotion().equals(newState.getPromotion())) {
                    revalidatePromotion(newState.getPromotion());
                }
            }

            @Override
            public void onDelete(PromotionStudyFees promotionStudyFees) {
                revalidatePromotion(promotionStudyFees.getPromotion());
            }
        });
    }

    /**
     * Utilitaire du checker d'état
     */
    public static void setup () {
        if (instance != null)
            return;

        instance = new StateChecker();
        instance.init();
    }

    public static StateChecker getInstance() {
        if (instance == null) {
            instance = new StateChecker();
            instance.init();
        }
        return instance;
    }

    /**
     * Action de revalidation des quelques valeurs mise en cache qui touche une promotion
     */
    public void revalidatePromotion (Promotion promotion) {
        double sum = promotionStudyFeesRepository.sumAllByPromotion(promotion);
        if (promotion.getTotalStudyFees() == null || sum != promotion.getTotalStudyFees()) {
            promotion.setTotalStudyFees(sum);
            promotionRepository.persist(promotion);
        }
    }
}
