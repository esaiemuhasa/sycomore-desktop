package com.sycomore.view.workspace;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.InscriptionRepository;
import com.sycomore.entity.Inscription;
import com.sycomore.entity.Promotion;
import com.sycomore.entity.Student;
import com.sycomore.view.MainWindow;
import com.sycomore.view.componets.Workspace;
import com.sycomore.view.workspace.students.InscriptionForm;
import com.sycomore.view.workspace.students.Sidebar;
import com.sycomore.view.workspace.students.StudentsDataViewContainer;
import com.sycomore.view.workspace.students.Toolbar;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class StudentsPanel extends JPanel implements Workspace.WorkspaceItem {

    private JDialog dialogInscription;
    private InscriptionForm inscriptionForm;

    private final Toolbar toolbar;
    private final StudentsDataViewContainer viewContainer;
    private final Sidebar sidebar;

    private final InscriptionRepository inscriptionRepository;

    public StudentsPanel() {
        super(new BorderLayout());
        inscriptionRepository = DAOFactory.getInstance(InscriptionRepository.class);

        JPanel center = new JPanel(new BorderLayout());

        toolbar = new Toolbar(toolbarListener);
        viewContainer = new StudentsDataViewContainer();
        sidebar = new Sidebar();

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, viewContainer, sidebar);
        split.setDividerSize(10);
        split.setOneTouchExpandable(true);

        center.add(split, BorderLayout.CENTER);

        add(toolbar, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
    }

    private void handleAddInscription () {
        buildInscriptionForm();

        dialogInscription.setLocationRelativeTo(dialogInscription.getOwner());
        dialogInscription.setVisible(true);
    }

    private void buildInscriptionForm () {
        if (dialogInscription != null)
            return;

        dialogInscription = new JDialog(MainWindow.getInstance(), "Inscription");
        inscriptionForm = new InscriptionForm(inscriptionFormListener);
        inscriptionForm.reload();
        dialogInscription.setContentPane(inscriptionForm);
        dialogInscription.pack();
        dialogInscription.setSize(600, dialogInscription.getHeight());
        dialogInscription.setResizable(false);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public String getName() {
        return "students";
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }



    protected final Toolbar.ToolbarListener toolbarListener = new Toolbar.ToolbarListener() {
        @Override
        public void onInscriptionRequest() {
            handleAddInscription();
        }

        @Override
        public void onSearch(String value) {

        }
    };

    private final InscriptionForm.InscriptionFormListener inscriptionFormListener = new InscriptionForm.InscriptionFormListener() {
        @Override
        public void onValidate(String names, String registrationNumber, Date birthDate, Promotion promotion, Inscription inscription) {
            Date now = new Date();

            if (inscription == null) {
                inscription = new Inscription();
                inscription.setRecordingDate(now);
                inscription.setStudent(new Student());
                inscription.getStudent().setRecordingDate(now);
            } else {
                inscription.setUpdatingDate(now);
                inscription.getStudent().setUpdatingDate(now);
            }

            Student student = inscription.getStudent();
            student.setNames(names);
            student.setRegistrationNumber(registrationNumber);
            student.setBirthDate(new java.sql.Date(birthDate.getTime()));
            inscription.setPromotion(promotion);

            inscriptionRepository.persist(inscription);
        }

        @Override
        public void onCancel() {
            dialogInscription.setVisible(false);
            dialogInscription.dispose();
        }
    };
}
