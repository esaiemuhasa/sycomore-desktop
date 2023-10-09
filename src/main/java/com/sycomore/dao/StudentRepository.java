package com.sycomore.dao;

import com.sycomore.entity.Student;

public class StudentRepository extends BaseRepositoryJPA<Student> {

    public StudentRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<Student> getEntityClass() {
        return Student.class;
    }

    @Override
    protected Student[] createArray(int size) {
        return new Student[size];
    }
}
