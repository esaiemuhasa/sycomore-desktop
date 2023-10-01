package com.sycomore.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utilitaire permettant d'interagir avec la configuration local de l'application
 */
public class Config {
    private static final Map<String, String> dictionary = formPropertiesFile("config");
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

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
}
