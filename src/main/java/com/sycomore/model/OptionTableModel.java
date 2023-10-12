package com.sycomore.model;

import com.sycomore.dao.DAOFactory;
import com.sycomore.dao.OptionRepository;
import com.sycomore.entity.Option;
import com.sycomore.entity.Section;

import java.util.Arrays;
import java.util.Objects;

public class OptionTableModel extends AbstractClassifiableTableModel<Option> {
    private Section section;

    private final OptionRepository optionRepository;

    public OptionTableModel() {
        super(DAOFactory.getInstance(OptionRepository.class));
        optionRepository = DAOFactory.getInstance(OptionRepository.class);
    }

    public void setSection(Section section) {
        this.section = section;
        reload();
    }

    private void reload () {
        rows.clear();
        if (section != null) {
            Option [] options = optionRepository.findAllBySection(section);
            if (options != null)
                rows.addAll(Arrays.asList(options));
        }
        fireTableDataChanged();
    }

    @Override
    public void onCreate(Option option) {
        if (section == null || !Objects.equals(option.getSection().getId(), section.getId()))
            return;

        super.onCreate(option);
    }
}
