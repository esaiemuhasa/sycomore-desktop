package com.sycomore.view.components.swing;

import java.awt.Color;

public abstract class CardModelAdapter implements CardModelListener{
    @Override
    public void onChange(CardModel<?> model) {}

    @Override
    public void onValueChange(CardModel<?> model, Object oldValue) {}

    @Override
    public void onTitleChange(CardModel<?> model, int index, String oldTitle) {}

    @Override
    public void onColorChange(CardModel<?> model, int index, Color oldColor) {}

    @Override
    public void onIconChange(CardModel<?> model, String oldIcon) {}
}
