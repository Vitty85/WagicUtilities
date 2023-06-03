/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

/**
 *
 * @author vitty85
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class PrimitiveDatabase {

    public static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.ISO_8859_1))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (Exception e)
        {
            System.err.println("Error parsing content of file: " + filePath);
        }

        return contentBuilder.toString();
    }
    
    public static Map<String, String> getFullDatabase(String wagicPath) throws IOException {
        File baseFolder = new File(wagicPath + "Res\\sets\\primitives\\");
        File[] listOfPrimitives = baseFolder.listFiles();
        Map<String, String> primitivesMap = new HashMap<>();
        for (int y = 0; y < listOfPrimitives.length; y++) {
            if (!listOfPrimitives[y].getName().contains("unsupported.txt") && !listOfPrimitives[y].getName().contains("_macros.txt")) {
                try {
                    System.out.println("Reading file: " + listOfPrimitives[y].getName());
                    File inputfile = listOfPrimitives[y];
                    String lines = readLineByLineJava8(inputfile.getAbsolutePath());
                    while(lines.contains("[card]")) {
                        String findStr = "[card]";
                        int lastIndex = lines.indexOf(findStr);
                        int endIndex = lines.indexOf("[/card]",lastIndex) + 8;
                        String content = lines.substring(lastIndex, endIndex);
                        String name = null;
                        int a = lines.indexOf("name=",lastIndex);
                        if (a > 0){
                            if(lines.substring(a, lines.indexOf("\n", a)).split("=").length > 1)
                                name = lines.substring(a, lines.indexOf("\n", a)).split("=")[1];
                        }
                        if (name != null)
                            primitivesMap.put(name.toLowerCase(), content);
                        else
                            System.err.println("\r\n" + "Error reading: " + content);
                        lines = lines.substring(endIndex);
                    }
                    System.out.println("All primitives contained in " + listOfPrimitives[y].getName() + " have been read");
                } catch (Exception e) {
                    System.err.println("Error while reading file: " + listOfPrimitives[y].getName());
                    System.err.println(e.getMessage());
                }
            }
        }
        return primitivesMap;
    }
    
    public static Map<String, String> getUnsupportedDatabase(String wagicPath) throws IOException {
        File baseFolder = new File(wagicPath + "Res\\sets\\primitives\\");
        File[] listOfPrimitives = baseFolder.listFiles();
        Map<String, String> primitivesMap = new HashMap<>();
        for (int y = 0; y < listOfPrimitives.length; y++) {
            if (listOfPrimitives[y].getName().contains("unsupported.txt")) {
                try {
                    System.out.println("Reading file: " + listOfPrimitives[y].getName());
                    File inputfile = listOfPrimitives[y];
                    String lines = readLineByLineJava8(inputfile.getAbsolutePath());
                    while(lines.contains("[card]")) {
                        String findStr = "[card]";
                        int lastIndex = lines.indexOf(findStr);
                        int endIndex = lines.indexOf("[/card]",lastIndex) + 8;
                        String content = lines.substring(lastIndex, endIndex);
                        String name = null;
                        int a = lines.indexOf("name=",lastIndex);
                        if (a > 0){
                            if(lines.substring(a, lines.indexOf("\n", a)).split("=").length > 1)
                                name = lines.substring(a, lines.indexOf("\n", a)).split("=")[1];
                        }
                        if (name != null)
                            primitivesMap.put(name.toLowerCase(), content);
                        else
                            System.err.println("\r\n" + "Error reading: " + content);
                        lines = lines.substring(endIndex);
                    }
                    System.out.println("All primitives contained in " + listOfPrimitives[y].getName() + " have been read");
                } catch (Exception e) {
                    System.err.println("Error while reading file: " + listOfPrimitives[y].getName());
                    System.err.println(e.getMessage());
                }
            }
        }
        return primitivesMap;
    }
}
