package com.edgarmtz.engine.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Provides utilities to load files
 */
public class Resources{
    /**
     * Loads a file's text in a String object
     * @param fileName file's directory
     * @return file's text
     * @throws Exception if file doesn't exist
     */
    public static String loadResourceContent(String fileName) throws Exception{
        String resource;
        try(InputStream in = loadResource(fileName);
            Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())){
            resource = scanner.useDelimiter("\\A").next();
        }
        return resource;
    }

    /**
     * Loads a file as a InputStream
     * @param fileName file's directory
     * @return file's InputStream
     */
    public static  InputStream loadResource(String fileName){
        return Resources.class.getResourceAsStream(fileName);
    }

    /**
     * Loads a file text separated by each line
     * @param fileName file's directory
     * @return List containing each line String
     * @throws Exception if file doesn't exist or is unreadable
     */
    public static List<String> loadResourceLines(String fileName) throws Exception{
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(loadResource(fileName)))){
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
