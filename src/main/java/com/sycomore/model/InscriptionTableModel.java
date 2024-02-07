package com.sycomore.model;

import antlr.actions.python.CodeLexer;
import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.InscriptionRepository;
import com.sycomore.entity.Inscription;
import com.sycomore.entity.Promotion;

/**
 * Model de visualisation de la liste des inscriptions d'une promotion
 */
public class InscriptionTableModel extends AbstractPersistableEntityTableModel<Inscription> {

    private static final String [] TITLES = {"n°", "Matricule", "Nom, post-nom et prénom", "Payé", "Reste"};

    private Promotion promotion;

    private InscriptionRepository inscriptionRepository;

    public InscriptionTableModel() {
        super(DAOFactory.getInstance(InscriptionRepository.class));
        inscriptionRepository = DAOFactory.getInstance(InscriptionRepository.class);
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
        reload();
    }

    private void reload () {

    }

    @Override
    public int getColumnCount() {
        return TITLES.length;
    }

    @Override
    public String getColumnName(int column) {
        if (column < TITLES.length)
            return TITLES[column];

        return super.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Inscription inscription = getRow(rowIndex);

        switch (columnIndex) {
            case 0:
                return (rowIndex + 1)+"";
            case 1:
                return inscription.getStudent().getRegistrationNumber();
            case 2:
                return inscription.getStudent().getNames();

        }
        return null;
    }
}
