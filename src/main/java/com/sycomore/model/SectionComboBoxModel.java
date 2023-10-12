package com.sycomore.model;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.RepositoryAdapter;
import com.sycomore.dao.SectionRepository;
import com.sycomore.entity.Section;

import javax.swing.*;
import java.util.Arrays;
import java.util.Objects;

public class SectionComboBoxModel extends DefaultComboBoxModel<Section> {

    private final SectionRepository sectionRepository;

    public SectionComboBoxModel() {
        sectionRepository = DAOFactory.getInstance(SectionRepository.class);
        sectionRepository.addRepositoryListener(repositoryAdapter);
    }

    public void init () {
        Section [] sections = sectionRepository.findAll();
        if (sections != null) {
            for (Section s : sections)
                addElement(s);
        }
    }

    public void dispose () {
        sectionRepository.removeRepositoryListener(repositoryAdapter);
    }


    private final RepositoryAdapter<Section> repositoryAdapter = new RepositoryAdapter<Section>() {
        @Override
        public void onCreate(Section section) {
            addElement(section);
        }

        @Override
        public void onUpdate(Section oldState, Section newState) {
            for (int i = 0; i < getSize(); i++) {
                Section section = getElementAt(i);
                if (Objects.equals(section.getId(), newState.getId())){
                    removeElement(section);
                    insertElementAt(newState, i);
                    break;
                }
            }
        }

        @Override
        public void onDelete(Section section) {
            for (int i = 0; i < getSize(); i++) {
                Section s = getElementAt(i);
                if (Objects.equals(section.getId(), s.getId())){
                    removeElement(s);
                    break;
                }
            }
        }
    };

}
