package com.sycomore.model;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.PromotionRepository;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.School;

import java.util.Arrays;

public class PromotionTableModel extends AbstractPersistableEntityTableModel <Promotion> {

    private static final String [] TITLES = {"Abbreviation", "Appellation complete"};

    private School school;
    private final YearDataModel dataModel;

    public PromotionTableModel () {
        super(DAOFactory.getInstance(PromotionRepository.class));
        dataModel = YearDataModel.getInstance();
    }

    private void reload () {
        rows.clear();
        if (school != null) {
            Promotion [] ps = dataModel.getPromotions(school);
            if (ps != null)
                rows.addAll(Arrays.asList(ps));
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
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Promotion p = getRow(rowIndex);
        if (columnIndex == 0)
            return p.getShortName();
        return p.getFullName();
    }
}
