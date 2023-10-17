package com.sycomore.model;

import com.sycomore.dao.*;
import com.sycomore.entity.*;
import com.sycomore.helper.event.ProgressEmitter;
import com.sycomore.helper.event.ProgressListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Utilitaire de mis memoire des touts les données relatifs a une année scolaire
 */
public class YearDataModel implements ProgressEmitter {

    private static YearDataModel instance;

    private boolean started = false;

    private final List<ProgressListener> progressListeners = new ArrayList<>();
    private final List<YearDataModelListener> yearDataModelListeners = new ArrayList<>();

    private SchoolYear year;

    private Thread thread;

    //donnees mis en cache
    private final List<SchoolYear> years = new ArrayList<>();
    private final List<StudyFeesConfig> studyFeesConfigs = new ArrayList<>();
    private final List<PromotionStudyFees> promotionStudyFees = new ArrayList<>();
    private final List<RelatedFeesConfig> relatedFeesConfigs = new ArrayList<>();
    private final List<Promotion> promotions = new ArrayList<>();//liste des promotions d'une année
    private final List<School> schools = new ArrayList<>();//liste des écoles pour une année
    private final List<Inscription> inscriptions = new ArrayList<>();//Liste des élèves inscrits pour l'année en cours de consultation
    //==================

    private final SchoolYearRepository yearRepository;
    private final SchoolRepository schoolRepository;

    private final PromotionRepository promotionRepository;
    private final StudyFeesConfigRepository studyFeesConfigRepository;
    private final PromotionStudyFeesRepository promotionStudyFeesRepository;

    private YearDataModel () {
        yearRepository = DAOFactory.getInstance(SchoolYearRepository.class);
        schoolRepository = DAOFactory.getInstance(SchoolRepository.class);
        promotionRepository = DAOFactory.getInstance(PromotionRepository.class);
        studyFeesConfigRepository = DAOFactory.getInstance(StudyFeesConfigRepository.class);
        promotionStudyFeesRepository = DAOFactory.getInstance(PromotionStudyFeesRepository.class);

        promotionRepository.addRepositoryListener(new RepositoryAdapter<Promotion>() {
            @Override
            public void onCreate(Promotion promotion) {
                if (promotion.getYear().equals(year)) {
                    promotions.add(promotion);

                    if (schools.size() != schoolRepository.countAll()) {
                        schools.clear();
                        getSchools();
                    }

                    firePromotionTreeChange();
                }
            }

            @Override
            public void onDelete(Promotion promotion) {
                if (!promotion.getYear().equals(year))
                    return;

                for (int i = 0; i < promotions.size(); i++) {
                    Promotion p = promotions.get(i);
                    if (p.equals(promotion)) {
                        promotions.remove(i);
                        schools.clear();
                        break;
                    }
                }

                getSchools();
                firePromotionTreeChange();
            }
        });

        yearRepository.addRepositoryListener(new RepositoryAdapter<SchoolYear>() {
            @Override
            public void onCreate(SchoolYear year) {
                //lors de la creation d'une nouvelle année scolaire, on charge directement l'année en question
                instance.year = year;
                reload();
            }

            @Override
            public void onUpdate(SchoolYear oldState, SchoolYear newState) {
                if (year == null)
                    return;

                if (Objects.equals(newState.getId(), year.getId()))
                    instance.year = newState;
            }


            @Override
            public void onDelete(SchoolYear year) {
                if (instance.year == null)
                    return;

                //lors de la suppression si l'année actuel vient d'étre supprimé, alors on recharge la dernière année qui existe en BDD
                if (Objects.equals(year.getId(), instance.year.getId())) {
                    SchoolYear current = yearRepository.findOneLatest();
                    if (current != null) {
                        setYear(current);
                    }
                }
            }
        });

        studyFeesConfigRepository.addRepositoryListener(new RepositoryAdapter<StudyFeesConfig>() {
            @Override
            public void onCreate(StudyFeesConfig config) {
                if (config.getYear().equals(year))
                    studyFeesConfigs.add(config);
            }

            @Override
            public void onUpdate(StudyFeesConfig oldState, StudyFeesConfig newState) {
                if (!newState.getYear().equals(year))
                    return;

                for (int i = 0; i < studyFeesConfigs.size(); i++) {
                    if (newState.equals(studyFeesConfigs.get(i))) {
                        studyFeesConfigs.set(i, newState);
                        break;
                    }
                }
            }

            @Override
            public void onDelete(StudyFeesConfig config) {
                if (!config.getYear().equals(year))
                    return;

                for (int i = 0; i < studyFeesConfigs.size(); i++) {
                    if (config.equals(studyFeesConfigs.get(i))) {
                        studyFeesConfigs.remove(i);
                        break;
                    }
                }
            }
        });

        promotionStudyFeesRepository.addRepositoryListener(new RepositoryAdapter<PromotionStudyFees>() {
            @Override
            public void onCreate(PromotionStudyFees p) {
                if (p.getPromotion().getYear().equals(year))
                    promotionStudyFees.add(p);
            }

            @Override
            public void onDelete(PromotionStudyFees p) {
                if (!p.getPromotion().getYear().equals(year))
                    return;

                for (int i = 0; i < promotionStudyFees.size(); i++)
                    if (promotionStudyFees.get(i).equals(p)){
                        promotionStudyFees.remove(i);
                        break;
                    }
            }
        });
    }

    public static YearDataModel getInstance() {
        if (instance == null)
            instance = new YearDataModel();

        return instance;
    }

    public SchoolYear getYear() {
        return year;
    }

    /**
     * Mutation de l'année scolaire actuel
     */
    public synchronized void setYear(SchoolYear year) {
        if (!started) {
            doSetup();
            return;
        }

        if (Objects.equals(year, this.year))
            return;

        this.year = year;
        reload();
    }

    /**
     * Action de demande d'initialisation du model des données annuel
     */
    public synchronized void setup () {
        doSetup();
    }

    /**
     * Initialisation du model des données annuel
     */
    protected void doSetup () {
        if (started)
            return;

        started = true;

        //chargement de la liste des années scolaires
        SchoolYear [] years = yearRepository.findAll();
        if (years != null) {
            this.years.addAll(Arrays.asList(years));

            for (SchoolYear y : years) {
                if (!y.isArchived()) {
                    year = y;
                    break;
                }
            }

            if (year == null){
                year = years[0];
            }
        }

        fireSetup();
        reload();
    }

    /**
     * Renvoie la liste des années scolaires
     */
    public SchoolYear [] getYears () {
        if (years.isEmpty())
            return null;

        int size = years.size();
        return years.toArray(new SchoolYear[size]);
    }

    /**
     * Recuperation des écoles
     */
    public School [] getSchools () {
        if (schools.isEmpty()) {
            for (Promotion p: promotions) {
                boolean exists = false;

                for (School s: schools) {
                    if (s.equals(p.getSchool())) {
                        exists = true;
                        break;
                    }
                }

                if (!exists)
                    schools.add(p.getSchool());

                if (schools.size() == schoolRepository.countAll())
                    break;
            }
        }

        if (schools.isEmpty())
            return null;

        int size = schools.size();
        return schools.toArray(new School[size]);
    }

    /**
     * Recuperation de la liste des promotions d'une école pour une année
     */
    public Promotion [] getPromotions (School school) {
        List<Promotion> ps = new ArrayList<>();
        for (Promotion p : promotions) {
            if (p.getSchool().equals(school))
                ps.add(p);
        }

        int count = ps.size();
        return ps.toArray(new Promotion[count]);
    }

    /**
     * Filtrage des promotions d'une option
     */
    public Promotion [] getPromotions (Option option) {
        List<Promotion> ps = new ArrayList<>();
        for (Promotion p : promotions) {
            if (p.getOption().equals(option))
                ps.add(p);
        }

        int count = ps.size();
        return ps.toArray(new Promotion[count]);
    }

    /**
     * Recuperation de la liste des promotions, associer a une configuration des frais.
     */
    public Promotion [] getPromotions (StudyFeesConfig config) {
        List<Promotion> ps = new ArrayList<>();

        for (PromotionStudyFees p : promotionStudyFees) {
            if (p.getConfig().equals(config))
                ps.add(p.getPromotion());
        }

        if (ps.isEmpty())
            return null;

        int count = ps.size();
        return ps.toArray(new Promotion[count]);
    }

    /**
     * Renvoie la configuration des frais d'études pour l'école en paramètre
     */
    public StudyFeesConfig [] getStudyFeesConfigs (School school) {
        List<StudyFeesConfig> configs = new ArrayList<>();

        for (StudyFeesConfig c : studyFeesConfigs)
            if (c.getSchool().equals(school))
                configs.add(c);

        if (configs.isEmpty())
            return null;

        int size = configs.size();
        return configs.toArray(new StudyFeesConfig[size]);
    }


    /**
     * Renvoie lq configuration des frais d'étude pour l'année scolaire en cours de consultation
     */
    public StudyFeesConfig [] getStudyFeesConfigs () {
        if (studyFeesConfigs.isEmpty())
            return null;

        int size = studyFeesConfigs.size();
        return studyFeesConfigs.toArray(new StudyFeesConfig[size]);
    }

    /**
     * Renvoie la liste des configs des frais d'étude associé à une promotion
     */
    public PromotionStudyFees [] getPromotionStudyFees (Promotion promotion) {
        List<PromotionStudyFees> configs = new ArrayList<>();

        for (PromotionStudyFees pf : promotionStudyFees)
            if (pf.getPromotion().equals(promotion))
                configs.add(pf);

        if (configs.isEmpty())
            return null;

        int size = configs.size();
        return configs.toArray(new PromotionStudyFees[size]);
    }

    public PromotionStudyFees [] getPromotionStudyFees (StudyFeesConfig config) {
        List<PromotionStudyFees> configs = new ArrayList<>();

        for (PromotionStudyFees pf : promotionStudyFees)
            if (pf.getConfig().equals(config))
                configs.add(pf);

        if (configs.isEmpty())
            return null;

        int size = configs.size();
        return configs.toArray(new PromotionStudyFees[size]);
    }

    /**
     * Recuperation de la liste des élèves inscrit dans une promotion.
     */
    public Inscription [] getInscriptions (Promotion promotion) {
        List<Inscription> is = new ArrayList<>();

        for (Inscription i : inscriptions) {
            if (i.getPromotion().equals(promotion))
                is.add(i);
        }

        if (is.isEmpty())
            return null;

        int count = is.size();
        return is.toArray(new Inscription[count]);
    }

    /**
     * Action de rechargement de donnee du model
     */
    private synchronized void doReload () {
        promotions.clear();
        promotionStudyFees.clear();
        schools.clear();

        fireLoadStart();

        Promotion [] ps = promotionRepository.findAllByYear(getYear());
        if (ps != null)
            promotions.addAll(Arrays.asList(ps));

        StudyFeesConfig [] configs = studyFeesConfigRepository.findAllByYear(year);
        if (configs!= null)
            studyFeesConfigs.addAll(Arrays.asList(configs));

        PromotionStudyFees [] fees = promotionStudyFeesRepository.findAllByYear(year);
        if (fees != null)
            promotionStudyFees.addAll(Arrays.asList(fees));

        fireLoadFinish();
    }

    /**
     * Demande de rechargement des donnees du model.
     * L'opération de chargement des donnees est faite dans un nouveau thread.
     */
    public synchronized void reload () {
        if (thread == null || thread.getState() == Thread.State.TERMINATED) {
            thread = new Thread(this::doReload);
            thread.start();
        }
    }

    /**
     * Emission de l'évènement de debut de chargement des donnees du model
     */
    protected void fireLoadStart() {
        Object [] listeners = yearDataModelListeners.toArray();
        for (Object ls : listeners)
            ((YearDataModelListener) ls).onLoadStart();
    }

    /**
     * Emission de l'évènement de fin de chargement des données du model
     */
    protected void fireLoadFinish() {
        Object [] listeners = yearDataModelListeners.toArray();
        for (Object ls : listeners)
            ((YearDataModelListener) ls).onLoadFinish();
    }

    protected void fireSetup () {
        Object [] listeners = yearDataModelListeners.toArray();
        for (Object ls : listeners)
            ((YearDataModelListener) ls).onSetup();
    }

    protected void firePromotionTreeChange() {
        Object [] listeners = yearDataModelListeners.toArray();

        int size = promotions.size();
        if (size != 0) {
            Promotion [] ps = promotions.toArray(new Promotion[size]);
            for (Object ls : listeners)
                ((YearDataModelListener) ls).onPromotionTreeChange(ps);
        } else
            for (Object ls : listeners)
                ((YearDataModelListener) ls).onPromotionTreeChange();
    }


    public void addYearDataListener (YearDataModelListener listener) {
        if (!yearDataModelListeners.contains(listener))
            yearDataModelListeners.add(listener);
    }

    public void removeYearDataListener (YearDataModelListener listener) {
        yearDataModelListeners.remove(listener);
    }

    @Override
    public void addProgressListener(ProgressListener listener) {
        if (!progressListeners.contains(listener))
            progressListeners.add(listener);
    }

    @Override
    public void removeProgressListener(ProgressListener listener) {
        progressListeners.remove(listener);
    }

}
