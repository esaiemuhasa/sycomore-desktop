package com.sycomore.model;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.PromotionRepository;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.School;

import java.util.Arrays;

/**
 * Model du Table de visualisation de la liste des promotions d'une école.
 */
public class PromotionTableModel extends AbstractPersistableEntityTableModel <Promotion> {

    private static final String [] TITLES = {"Abbreviation", "Appellation complete", "Frais d'étude"};

    private final PromotionRepository promotionRepository;

    private School school;
    private final YearDataModel dataModel;

    public PromotionTableModel () {
        super(DAOFactory.getInstance(PromotionRepository.class));
        dataModel = YearDataModel.getInstance();
        promotionRepository = DAOFactory.getInstance(PromotionRepository.class);
    }

    private void reload () {
        rows.clear();
        if (school != null) {
            Promotion [] ps = dataModel.getPromotions(school);
            if (ps != null) {
                for(Promotion pro : ps) {//verification du calcul de frais d'étude
                    if (pro.getTotalStudyFees() == null) {
                        pro.setTotalStudyFees(dataModel.getSumPromotionStudyFees(pro));
                        promotionRepository.persist(pro);
                    }
                }
                rows.addAll(Arrays.asList(ps));
            }
        }
        fireTableDataChanged();
    }

    public void setSchool(School school) {
        this.school = school;
        reload();
    }

    @Override
    public void onCreate(Promotion promotion) {
        if (promotion.getSchool().equals(school) && promotion.getYear().equals(dataModel.getYear()))
            super.onCreate(promotion);
    }

    @Override
    public String getColumnName(int column) {
        if (column < TITLES.length)
            return TITLES[column];

        return super.getColumnName(column);
    }

    @Override
    public int getColumnCount() {
        return TITLES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Promotion p = getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return p.getShortName();
            case 1:
                return p.getFullName();

            case 2:
                return p.getTotalStudyFees()+" USD";
        }


        return  "";
    }
}
