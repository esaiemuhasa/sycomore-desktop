package com.sycomore.dao;

import com.sycomore.entity.SchoolYear;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DAOFactoryTest {

    @Test
    public void testUseBegin () {
        DAOFactory factory = DAOFactory.getInstance();
        factory.begin();
        EntityManager manager = factory.getManager();
        SchoolYear year = new SchoolYear();
        Date now = new Date();

        year.setRecordingDate(now);
        year.setArchived(false);
        year.setLabel("2023-2023");
        manager.persist(year);
        manager.close();
        assertTrue(true, "Démarrage transaction en globale ok");
    }

    @Test()
    public void testNoUseBegin () {

        DAOFactory factory = DAOFactory.getInstance();
        EntityManager manager = factory.getManager();

        SchoolYear year = new SchoolYear();
        Date now = new Date();
        year.setRecordingDate(now);
        year.setArchived(false);
        year.setLabel("2023-2023");

        TransactionRequiredException exception = assertThrows(
                TransactionRequiredException.class,
                () -> {
                    manager.persist(year);
                    manager.flush();
                    manager.close();
                },
                "Exception dans le cas ou aucune transaction n'a été démarrer d'avance"
        );

        if (exception == null) {
            fail("Aucune exception lancé lors la transaction globale n'est pas démarré");
        }

    }
}
