package com.sycomore.dao;

import com.sycomore.entity.Payment;

public class PaymentRepository extends BaseRepositoryJPA<Payment>{

    public PaymentRepository(DAOFactoryJPA factory) {
        super(factory);
    }

    @Override
    protected Class<Payment> getEntityClass() {
        return Payment.class;
    }

    @Override
    protected Payment[] createArray(int size) {
        return new Payment[size];
    }
}
