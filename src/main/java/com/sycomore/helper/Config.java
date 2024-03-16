package com.sycomore.helper;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utilitaire permettant d'interagir avec la configuration local de l'application
 */
public class Config {

    public static final int DEFAULT_H_GAP = 5, DEFAULT_V_GAP = 5;
    public static final Border DEFAULT_EMPTY_BORDER = BorderFactory.createEmptyBorder(DEFAULT_H_GAP, DEFAULT_H_GAP, DEFAULT_H_GAP, DEFAULT_H_GAP);
    private static final Map<String, String> dictionary = formPropertiesFile("config");
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
    public static SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    private Config(){}

    /**
     * Utilitaire de lecture des contenus d'un fichier properties
     */
    public static Map<String, String> formPropertiesFile (String filename) {
        Map<String, String> map = new HashMap<>();

        String path = "config/"+filename+".properties";

        File file = new File(path);

        if (!file.exists() || !file.isFile())
            return null;


        Properties properties = new Properties();
        try (FileInputStream stream = new FileInputStream(file)) {
            properties.load(stream);
            Enumeration<Object> keys = properties.keys();

            while (keys.hasMoreElements()) {
                String key = keys.nextElement().toString();
                map.put(key, properties.getProperty(key));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return map;
    }

    public static String get (String key) {
        if (dictionary != null && dictionary.containsKey(key))
            return dictionary.get(key);
        return null;
    }

    public static String getLogDirAt (Date date) {
        if (date == null)
            date = new Date();

        String dir = get("logg_dir")+"/"+DATE_FORMAT.format(date)+"/";
        File file = new File(dir);

        if (!file.isDirectory())
            file.mkdirs();

        return dir;
    }

    //couleurs
    private static final Color [] COLORS = new Color[] {
            new Color(0xFFCE30), new Color(0xE83845), new Color(0xE9669F),
            new Color(0x746AB0), new Color(0x2050FA), new Color(0x9A9A00),
            new Color(0xFA5050), new Color(0xF0CF6F), new Color(0xAFAFAF),
            new Color(0x20FF76), new Color(0x077B8A), new Color(0x5C3C92),
            new Color(0xE2D810), new Color(0xD9138A), new Color(0x12A4D9),
            new Color(0x22780F), new Color(0x381A3C), new Color(0x004C56),
            new Color(0x9B571D), new Color(0xB03468), new Color(0x4E5352),
            new Color(0x0081F8), new Color(0x00AFAF), new Color(0xF0F3FF)
    };

    public static Color getColorAt (int index) {
        return COLORS[index % (COLORS.length - 1)];
    }

    public static Color getAlphaColorAt (int index) {
        return COLORS_ALPHA[index % (COLORS_ALPHA.length - 1)];
    }

    private static final Color [] COLORS_ALPHA = new Color[] {
            new Color(0x55FFCE30, true), new Color(0x55E83845, true), new Color(0x55E9889F, true),
            new Color(0x55746AB0, true), new Color(0x55288BA8, true), new Color(0x55FF88FF, true),
            new Color(0x555F7FF0, true), new Color(0x5590C86F, true), new Color(0x550756FF, true),
            new Color(0x55A2D5C6, true), new Color(0x55077B8A, true), new Color(0x555C3C92, true),
            new Color(0x55E2D810, true), new Color(0x55D9138A, true), new Color(0x5512A4D9, true),
            new Color(0x5522780F, true), new Color(0x55381A3C, true), new Color(0x55004C56, true),
            new Color(0x559B571D, true), new Color(0x55B03468, true), new Color(0x554E5352, true)
    };

    /**
     * Renvoie le chemain vers l'icone dont le nom est en parametre
     * nous syposont que l'incone doit avoir l'extension .png
     */
    public static String getIcon (String name) {
        return "icons/"+name+".png";
    }

    public static String getIcon(String name, String ext) {
        return "icons/"+name+"."+ext;
    }

    /**
     * Renvoie une instance de date corresponant au dernier timestemp de la journee du date en parametre
     */
    public static Date toMaxTimestampOfDay (Date date) throws RuntimeException{
        String date2str = DATE_FORMAT.format(date);
        Date maxDate = null;
        try {
            maxDate = DATE_TIME_FORMAT.parse(date2str+" 23:59:59");
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return maxDate;
    }

    public static Date toMiddleTimestampOfDay (Date date) throws RuntimeException{
        String date2str = DATE_FORMAT.format(date);
        Date maxDate = null;
        try {
            maxDate = DATE_TIME_FORMAT.parse(date2str+" 12:00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return maxDate;
    }

    /**
     * Utilitaire de creation d'un scroll-pane, scrollable verticalement
     */
    public static JScrollPane createVerticalScrollPane(JComponent view) {
        final JScrollPane scroll = new JScrollPane(view);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setViewportBorder(null);
        scroll.setBorder(null);
        return scroll;
    }

    public static final JLabel createTitle (String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.PLAIN, 25));
        label.setForeground(Color.LIGHT_GRAY);
        label.setBorder(DEFAULT_EMPTY_BORDER);
        return label;
    }

    public static final JLabel createSubTitle (String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setForeground(Color.LIGHT_GRAY);
        label.setBorder(DEFAULT_EMPTY_BORDER);
        return label;
    }

}
