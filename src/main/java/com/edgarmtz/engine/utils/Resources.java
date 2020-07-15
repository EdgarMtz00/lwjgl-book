package com.edgarmtz.engine.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

    public static  InputStream loadResource(String fileName) throws Exception{
        InputStream in = Resources.class.getResourceAsStream(fileName);
        return in;
    }
}
