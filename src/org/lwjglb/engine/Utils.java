package org.lwjglb.engine;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String fileName) throws Exception {
        String result;
        try (InputStream in = Utils.class.getResourceAsStream(fileName);
             Scanner scanner = new Scanner(new File(fileName))) {
            result = scanner.useDelimiter("\\A").next();
        }
        return result;
    }

}