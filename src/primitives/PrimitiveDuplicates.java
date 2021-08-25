/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primitives;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class PrimitiveDuplicates {
    //Read file content into string with - Files.lines(Path path, Charset cs)
    private static String readLineByLineJava8(String filePath)
    {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.ISO_8859_1))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static void main(String[] argv) throws IOException {
        File duplicates = new File("C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\duplicates.txt");
        FileWriter fw = new FileWriter(duplicates);
        fw.append("name;file" + "\n");
        fw.flush();
        String basePath = "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\wagic-wagic-v0.23.1\\projects\\mtg\\bin\\Res\\sets\\primitives\\";
        File baseFolder = new File(basePath);
        File[] listOfSet = baseFolder.listFiles();
        Map<String, String> mappa = new HashMap<>();
        String set = "NOCHECK";
        int idx = -1;
        for (int y = 0; y < listOfSet.length; y++) {
            if (!listOfSet[y].getName().contains(set)) {
                String filePath = listOfSet[y].getAbsolutePath();
                String lines = readLineByLineJava8(filePath);
                while (lines.contains("[card]")) {
                    String findStr = "[card]";
                    int lastIndex = lines.indexOf(findStr);
                    String name = null;
                    int a = lines.indexOf("name=", lastIndex);
                    if (a > 0) {
                        if (lines.substring(a, lines.indexOf("\n", a)).replace("//", "-").split("=").length > 1)
                            name = lines.substring(a, lines.indexOf("\n", a)).replace("//", "-").split("=")[1];
                    }
                    int c = lines.indexOf("[/card]", lastIndex);
                    if (c > 0)
                        lines = lines.substring(c + 8);
                    if (name != null) {
                        if (mappa.get(name) == null) {
                            mappa.put(name, filePath);
                        } else {
                            if(set.equals("NOCHECK")) {
                                fw.append(name + ";" + filePath + "\n");
                                fw.append(name + ";" + mappa.get(name) + "\n");
                                fw.flush();
                            }
                        }
                    }
                }
            } else
                idx = y;
        }
        if(idx >= 0 && !set.equals("NOCHECK")) {
            String filePath = listOfSet[idx].getAbsolutePath();
            String lines = readLineByLineJava8(filePath);
            while (lines.contains("[card]")) {
                String findStr = "[card]";
                int lastIndex = lines.indexOf(findStr);
                String name = null;
                int a = lines.indexOf("name=", lastIndex);
                if (a > 0) {
                    if (lines.substring(a, lines.indexOf("\n", a)).replace("//", "-").split("=").length > 1)
                        name = lines.substring(a, lines.indexOf("\n", a)).replace("//", "-").split("=")[1];
                }
                int c = lines.indexOf("[/card]", lastIndex);
                if (c > 0)
                    lines = lines.substring(c + 8);
                if (name != null) {
                    if (mappa.get(name) == null) {
                        mappa.put(name, filePath);
                        if (filePath.contains(set) || mappa.get(name).contains(set)) {
                            fw.append("\n" + name + ";" + filePath + "\n\n");
                        }
                    } else {
                        if (filePath.contains(set) || mappa.get(name).contains(set)) {
                            fw.append(name + ";" + filePath + "\n");
                            fw.append(name + ";" + mappa.get(name) + "\n");
                            fw.flush();
                        }
                    }
                }
            }
        }
        fw.close();
        System.out.println("Check is done");
    }
}
