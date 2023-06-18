/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primitives;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class PrimitiveMissing {

    public static void main(String[] argv) throws IOException {
        argv = new String[] { "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\wagic-wagic-v0.23.1_win\\projects\\mtg\\", "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\OUT\\" };
        if (argv.length < 1 || argv.length > 3) {
            System.err.println("Usage: java -jar FindMissingPrimitives.jar wagicPath outputPath");
            System.exit(-1);
        }
        String basePath = argv[0];
        String outputPath = (argv.length == 1)?(argv[0]+"\\User\\"):argv[1];
        File baseFolder = new File(basePath + "Res\\sets\\");
        File[] listOfSet = baseFolder.listFiles();
        HashMap<String,HashMap<String, String>> totalCardMap = new HashMap<>();
        for (int y = 0; y < listOfSet.length; y++) {
            if (listOfSet[y].isDirectory() && !listOfSet[y].getName().equalsIgnoreCase("primitives")) {
                String Set = listOfSet[y].getName() + "\\";
                File folder = new File(baseFolder.getAbsolutePath() + "\\" + Set);
                String filePath = folder.getAbsolutePath() + "\\_cards.dat";
                String lines = primitives.PrimitiveDatabase.readLineByLineJava8(filePath);
                while (lines.contains("[card]")) {
                    String findStr = "[card]";
                    int lastIndex = lines.indexOf(findStr);
                    String id = null;
                    String primitive = null;
                    String rarity = null;
                    int a = lines.indexOf("primitive=", lastIndex);
                    if (a > 0) {
                        if (lines.substring(a, lines.indexOf("\n", a)).split("=").length > 1)
                            primitive = lines.substring(a, lines.indexOf("\n", a)).split("=")[1];
                    }
                    int b = lines.indexOf("id=", lastIndex);
                    if (b > 0) {
                        if (lines.substring(b, lines.indexOf("\n", b)).split("=").length > 1)
                            id = lines.substring(b, lines.indexOf("\n", b)).split("=")[1];
                    }
                    int r = lines.indexOf("rarity=", lastIndex);
                    if (r > 0) {
                        if (lines.substring(r, lines.indexOf("\n", r)).split("=").length > 1)
                            rarity = lines.substring(r, lines.indexOf("\n", r)).split("=")[1];
                    }
                    int c = lines.indexOf("[/card]", lastIndex);
                    if (c > 0)
                        lines = lines.substring(c + 8);
                    if (primitive != null && id != null && !id.equalsIgnoreCase("null")){
                        if (rarity != null && !rarity.equalsIgnoreCase("T")){
                            HashMap<String, String> currentMap = totalCardMap.get(listOfSet[y].getName());
                            if(currentMap == null)
                                currentMap = new HashMap<>();
                            if(!currentMap.containsKey(id))
                                currentMap.put(id, primitive);
                            totalCardMap.put(listOfSet[y].getName(),currentMap);
                        }
                    }
                }
            }
        }
        Map<String,String> fullDatabase = primitives.PrimitiveDatabase.getFullDatabase(basePath);
        Map<String,String> unsupportedDatabase = primitives.PrimitiveDatabase.getUnsupportedDatabase(basePath);
        FileWriter missingCards = null;
        Set<String> listOfSets = totalCardMap.keySet();
        Vector<String> listOfAddedCards = new Vector<>();
        java.util.Arrays.sort(listOfSets.toArray());
        for(int i = 0; i < listOfSets.toArray().length; i++){
            if(!listOfSets.toArray()[i].toString().isEmpty()) {
                FileWriter unsupportedSetCards = null;
                HashMap<String,String> currentMap = totalCardMap.get(listOfSets.toArray()[i].toString());
                String[] lista = new String[currentMap.size()];
                for (int k = 0; k < (currentMap.keySet()).toArray().length ; k++){
                    if((currentMap.keySet()).toArray()[k] != null) {
                        try {
                            lista[k] = currentMap.get((currentMap.keySet()).toArray()[k]).toString();
                        } catch (Exception e) {
                            lista[k] = "";
                        }
                    }
                    if(!lista[k].isEmpty()){
                        String primitive = fullDatabase.get(lista[k].toLowerCase());
                        if(primitive == null || primitive.isEmpty()){
                            primitive = unsupportedDatabase.get(lista[k].toLowerCase());
                            if(primitive != null && !primitive.isEmpty()){
                                if(unsupportedSetCards == null)
                                    unsupportedSetCards = new FileWriter(new File(outputPath + listOfSets.toArray()[i].toString() + ".txt"));
                                unsupportedSetCards.append(primitive);
                                unsupportedSetCards.flush();
                            } else if(!listOfAddedCards.contains(lista[k].toLowerCase())){
                                listOfAddedCards.add(lista[k].toLowerCase());
                                if(missingCards == null)
                                    missingCards = new FileWriter(new File(outputPath + "missingCardList.txt"));
                                missingCards.append(lista[k] + "\n");
                                missingCards.flush();
                            }
                        }
                    }
                }
                if(unsupportedSetCards != null)
                    unsupportedSetCards.close();
            }
        }
        if(missingCards != null)
            missingCards.close();
        System.out.println("The files have been created in " + outputPath);
    }
}