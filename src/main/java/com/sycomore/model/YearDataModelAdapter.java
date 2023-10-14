package com.sycomore.model;

import com.sycomore.entity.Promotion;

public abstract class YearDataModelAdapter implements YearDataModelListener{
    @Override
    public void onSetup() {}

    @Override
    public void onLoadStart() {}

    @Override
    public void onLoadFinish() {}

    @Override
    public void onPromotionTreeChange(Promotion... promotions) {}
}
