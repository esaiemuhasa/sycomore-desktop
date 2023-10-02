package com.sycomore.model;

public interface YearDataModelListener {
    void onLoadStart();
    void onLoadFinish();

    void onError(Exception exception);
}
