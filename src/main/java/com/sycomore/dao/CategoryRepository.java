package com.sycomore.dao;

import com.sycomore.entity.Category;

public class CategoryRepository extends BaseRepositoryJPA<Category> {

    public CategoryRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<Category> getEntityClass() {
        return Category.class;
    }

    @Override
    protected Category[] createArray(int size) {
        return new Category[size];
    }
}
