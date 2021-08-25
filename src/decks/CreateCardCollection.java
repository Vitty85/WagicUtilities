/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CreateCardCollection {
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
        String basePath = "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\Res\\sets\\";
        File baseFolder = new File(basePath);
        File[] listOfSet = baseFolder.listFiles();
        Map<String, String> mappa = new HashMap<>();
        for (int y = 0; y < listOfSet.length; y++) {
            if (listOfSet[y].isDirectory() && !listOfSet[y].getName().equalsIgnoreCase("primitives")) {
                String Set = listOfSet[y].getName() + "\\";
                File folder = new File(basePath + Set);
                String filePath = folder.getAbsolutePath() + "\\_cards.dat";
                String lines = readLineByLineJava8(filePath);
                while (lines.contains("[card]")) {
                    String findStr = "[card]";
                    int lastIndex = lines.indexOf(findStr);
                    String id = null;
                    String primitive = null;
                    int a = lines.indexOf("primitive=", lastIndex);
                    if (a > 0) {
                        if (lines.substring(a, lines.indexOf("\n", a)).replace("//", "-").split("=").length > 1)
                            primitive = lines.substring(a, lines.indexOf("\n", a)).replace("//", "-").split("=")[1];
                    }
                    int b = lines.indexOf("id=", lastIndex);
                    if (b > 0) {
                        if (lines.substring(b, lines.indexOf("\n", b)).replace("-", "").split("=").length > 1)
                            id = lines.substring(b, lines.indexOf("\n", b)).replace("-", "").split("=")[1];
                    }
                    int c = lines.indexOf("[/card]", lastIndex);
                    if (c > 0)
                        lines = lines.substring(c + 8);
                    if (primitive != null && id != null && !id.equalsIgnoreCase("null"))
                        mappa.put(id, id);
                }
            }
        }
        File collection = new File(basePath + "collection.dat");
        FileWriter fw = new FileWriter(collection);
        fw.append("#NAME:collection" + "\n");
        Integer[] lista = new Integer[mappa.size()];
        for (int i = 0; i < (mappa.keySet()).toArray().length ; i++){
            if((mappa.keySet()).toArray()[i] !=null) {
                try {
                    lista[i] = Integer.parseInt(mappa.get((mappa.keySet()).toArray()[i]));
                } catch (Exception e) {
                    lista[i] = 0;
                }
            }
        }
        Arrays.sort(lista);
        for(int i = 0; i < lista.length; i++){
            if(lista[i] > 0) {
                fw.append(lista[i] + "\n");
                fw.append(lista[i] + "\n");
                fw.append(lista[i] + "\n");
                fw.append(lista[i] + "\n");
            }
            fw.flush();
        }
        fw.close();
        System.out.println("collection is ready");
    }
}