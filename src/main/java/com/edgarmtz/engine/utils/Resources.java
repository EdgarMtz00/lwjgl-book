package com.edgarmtz.engine.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Resources{
    public static String loadResourceContent(String fileName) throws Exception{
        String resource;
        try(InputStream in = loadResource(fileName);
            Scanner scanner = new Scanner(in, StandardCharsets.UTF_8.name())){
            resource = scanner.useDelimiter("\\A").next();
        }
        return resource;
    }

    public static  InputStream loadResource(String fileName){
        return Resources.class.getResourceAsStream(fileName);
    }

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
