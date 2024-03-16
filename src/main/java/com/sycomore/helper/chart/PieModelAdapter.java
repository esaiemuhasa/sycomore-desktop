package com.sycomore.helper.chart;

public abstract class PieModelAdapter implements PieModelListener{
    @Override
    public void refresh(PieModel model) {}

    @Override
    public void repaintPart(PieModel model, int partIndex) {}

    @Override
    public void onSelectedIndex(PieModel model, int oldIndex, int newIndex) {}

    @Override
    public void onTitleChange(PieModel model, String title) {}
}
