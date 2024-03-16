package com.sycomore.helper.chart;


import com.sycomore.helper.Config;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Utilitaire qui gere la poule des PiePart
 */
public class PiePartBuilder implements DisposableItemListener {

    private static PiePartBuilder instance;

    private final Queue<DefaultPiePart> disposes = new LinkedList<>(); //liste des parts disponibles

    private PiePartBuilder() {}

    private static void init () {
        if (instance == null)
            instance = new PiePartBuilder();
    }

    private DefaultPiePart poll (int index) {
        DefaultPiePart part = null;

        if (disposes.isEmpty()) {
            part = new DefaultPiePart();
            part.addDisposableItemListener(this);
        } else {
            part = disposes.poll();
            part.resume();
        }

        Color bkg = Config.getColorAt(index);
        Color frg = UIManager.getColor("Component.foreground");
        Color brd = UIManager.getColor("Component.borderColor");

        part.setBackgroundColor(bkg);
        part.setForegroundColor(frg);
        part.setBorderColor(brd);

        return part;
    }

    public static DefaultPiePart build (int index, double value, String label) {
        init();
        DefaultPiePart part = instance.poll(index);
        part.setValue(value);
        part.setLabel(label);
        return part;
    }

    public static DefaultPiePart build (int index) {
        init();
        return  instance.poll(index);
    }

    public static DefaultPiePart build () {
        init();
        return  instance.poll(instance.disposes.size());
    }

    @Override
    public void onDispose(DisposableItem source) {
        DefaultPiePart part = (DefaultPiePart) source;
        if (!disposes.contains(part))
            disposes.add(part);
    }

    @Override
    public void onResume(DisposableItem item) {
        DefaultPiePart part = (DefaultPiePart) item;
        disposes.remove(part);
    }
}
