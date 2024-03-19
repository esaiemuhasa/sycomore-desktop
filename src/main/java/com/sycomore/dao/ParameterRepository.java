package com.sycomore.dao;

import com.sycomore.entity.helper.Parameter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParameterRepository extends BaseRepositoryJPA<Parameter> {

    private final List<Parameter> parameters = new ArrayList<>();//mis en cache des occurrences pour inviter de contacter la BDD tres souvent

    public ParameterRepository(DAOFactoryJPA factory) throws RuntimeException {
        super(factory);
        findAll();

        addRepositoryListener(new RepositoryAdapter<Parameter>() {
            @Override
            public void onCreate(Parameter parameter) {
                parameters.add(parameter);
            }

            @Override
            public void onUpdate(Parameter oldState, Parameter newState) {
                for (int i = 0, max = parameters.size(); i < max; i++) {
                    Parameter parameter = parameters.get(i);
                    if (parameter.getId().equals(newState.getId())) {
                        parameters.set(i, newState);
                        break;
                    }
                }
            }

            @Override
            public void onDelete(Parameter parameter) {
                parameters.remove(parameter);
            }
        });
    }

    @Override
    public Parameter find(int id) throws RuntimeException {
        for (Parameter parameter :parameters)
            if (parameter.getId().equals(id))
                return parameter;
        return super.find(id);
    }

    /**
     * Recuperation du paramètre dont le nom est en paramètre
     */
    public Parameter findOneByName (String name) {
        for (Parameter parameter :parameters)
            if (parameter.getName().equals(name))
                return parameter;
        return null;
    }

    @Override
    public Parameter[] findAll () throws RuntimeException {
        if (parameters.isEmpty()) {
            Parameter[] all = super.findAll();

            if (all != null)
                parameters.addAll(Arrays.asList(all));

            return all;
        }

        return parameters.toArray(createArray(parameters.size()));
    }

    @Override
    protected Class<Parameter> getEntityClass() {
        return Parameter.class;
    }

    @Override
    protected Parameter[] createArray(int size) {
        return new Parameter[size];
    }
}
