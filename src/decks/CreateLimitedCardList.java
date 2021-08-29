/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decks;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CreateLimitedCardList {

    public static void main(String[] argv) throws IOException {
        argv = new String[] { "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\", "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\", "true" };
        if (argv.length < 1 || argv.length > 3) {
            System.err.println("Usage: java -jar CreateLimitedCardList.jar wagicPath outputPath createCollection");
            System.exit(-1);
        }
        String basePath = argv[0];
        String outputPath = (argv.length == 1)?(argv[0]+"\\User\\"):argv[1];
        boolean createColl = (argv.length < 3)?false:Boolean.parseBoolean(argv[2]);
        File baseFolder = new File(basePath + "Res\\sets\\");
        File[] listOfSet = baseFolder.listFiles();
        Map<String, String> totalCardMap = new HashMap<>();
        Map<String, String> restrictedCardMap = new HashMap<>();
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
                        if (rarity != null && rarity.equalsIgnoreCase("T"))
                            restrictedCardMap.put(primitive, primitive);
                        else
                            totalCardMap.put(id, primitive);
                    }
                }
            }
        }
        baseFolder = new File(basePath + "User\\player\\");
        File[] listOfDecks = baseFolder.listFiles();
        for (int y = 0; y < listOfDecks.length; y++) {
            String filePath = listOfDecks[y].getAbsolutePath();
            if(!filePath.contains("deck"))
                continue;
            String cards = primitives.PrimitiveDatabase.readLineByLineJava8(filePath);
            String listOfCards[] = cards.split("\n");
            for (int i = 0; i < listOfCards.length; i++) {
                String name = listOfCards[i];
                int a = name.indexOf("(");
                if (a > 0 && name.length() > a)
                    name = name.substring(0, a);
                a = name.indexOf("*");
                if (a > 0 && name.length() > a)
                    name = name.substring(0, a);
                name = name.replace("#DNG:", "");
                name = name.replace("#CMD:", "");
                name = name.replace("#SB:", "");
                name = name.trim();
                if(totalCardMap.containsKey(name))
                    name = totalCardMap.get(name);
                if (!restrictedCardMap.containsKey(name) && !name.contains("#"))
                    restrictedCardMap.put(name, name);
            }
        }
        baseFolder = new File(basePath + "User\\ai\\baka\\");
        listOfDecks = baseFolder.listFiles();
        for (int y = 0; y < listOfDecks.length; y++) {
            int numoflines = 0;
            String filePath = listOfDecks[y].getAbsolutePath();
            if(!filePath.contains("deck"))
                continue;
            String cards = primitives.PrimitiveDatabase.readLineByLineJava8(filePath);
            String listOfCards[] = cards.split("\n");
            for (int i = 0; i < listOfCards.length; i++) {
                String name = listOfCards[i];
                int a = name.indexOf("(");
                if (a > 0 && name.length() > a)
                    name = name.substring(0, a);
                a = name.indexOf("*");
                if (a > 0 && name.length() > a)
                    name = name.substring(0, a);
                name = name.replace("#DNG:", "");
                name = name.replace("#CMD:", "");
                name = name.replace("#SB:", "");
                name = name.trim();
                if(totalCardMap.containsKey(name))
                    name = totalCardMap.get(name);
                if (!restrictedCardMap.containsKey(name) && !name.contains("#"))
                    restrictedCardMap.put(name, name);
            }
        }
        baseFolder = new File(basePath + "Res\\ai\\baka\\");
        listOfDecks = baseFolder.listFiles();
        for (int y = 0; y < listOfDecks.length; y++) {
            int numoflines = 0;
            String filePath = listOfDecks[y].getAbsolutePath();
            if(!filePath.contains("deck"))
                continue;
            String cards = primitives.PrimitiveDatabase.readLineByLineJava8(filePath);
            String listOfCards[] = cards.split("\n");
            for (int i = 0; i < listOfCards.length; i++) {
                String name = listOfCards[i];
                int a = name.indexOf("(");
                if (a > 0 && name.length() > a)
                    name = name.substring(0, a);
                a = name.indexOf("*");
                if (a > 0 && name.length() > a)
                    name = name.substring(0, a);
                name = name.replace("#DNG:", "");
                name = name.replace("#CMD:", "");
                name = name.replace("#SB:", "");
                name = name.trim();
                if(totalCardMap.containsKey(name))
                    name = totalCardMap.get(name);
                if (!restrictedCardMap.containsKey(name) && !name.contains("#"))
                    restrictedCardMap.put(name, name);
            }
        }
        baseFolder = new File(basePath + "Res\\player\\premade\\");
        listOfDecks = baseFolder.listFiles();
        for (int y = 0; y < listOfDecks.length; y++) {
            int numoflines = 0;
            String filePath = listOfDecks[y].getAbsolutePath();
            if(!filePath.contains("deck"))
                continue;
            String cards = primitives.PrimitiveDatabase.readLineByLineJava8(filePath);
            String listOfCards[] = cards.split("\n");
            for (int i = 0; i < listOfCards.length; i++) {
                String name = listOfCards[i];
                int a = name.indexOf("(");
                if (a > 0 && name.length() > a)
                    name = name.substring(0, a);
                a = name.indexOf("*");
                if (a > 0 && name.length() > a)
                    name = name.substring(0, a);
                name = name.replace("#DNG:", "");
                name = name.replace("#CMD:", "");
                name = name.replace("#SB:", "");
                name = name.trim();
                if(totalCardMap.containsKey(name))
                    name = totalCardMap.get(name);
                if (!restrictedCardMap.containsKey(name) && !name.contains("#"))
                    restrictedCardMap.put(name, name);
            }
        }
        File cardlist = new File(outputPath + "LimitedCardList.txt");
        File primitivelist = new File(outputPath + "database.txt");
        Map<String,String> fullDatabase = primitives.PrimitiveDatabase.getFullDatabase(basePath);
        FileWriter fw = new FileWriter(cardlist);
        FileWriter fw2 = new FileWriter(primitivelist);
        String[] lista = new String[restrictedCardMap.size()];
        for (int i = 0; i < (restrictedCardMap.keySet()).toArray().length ; i++){
            if((restrictedCardMap.keySet()).toArray()[i] !=null) {
                try {
                    lista[i] = restrictedCardMap.get((restrictedCardMap.keySet()).toArray()[i]);
                } catch (Exception e) {
                    lista[i] = "";
                }
            }
        }
        java.util.Arrays.sort(lista);
        for(int i = 0; i < lista.length; i++){
            if(!lista[i].isEmpty()) {
                fw.append(lista[i] + "\n");
                String primitive = fullDatabase.get(lista[i].toLowerCase());
                if(primitive != null && !primitive.isEmpty())
                    fw2.append(primitive);
            }
            fw.flush();
            fw2.flush();
        }
        fw.close();
        fw2.close();
        System.out.println("The files LimitedCardList.txt and database.txt have been created in " + outputPath);
        if(createColl){
            File collection = new File(outputPath + "collection.dat");
            fw = new FileWriter(collection);
            fw.append("#NAME:collection" + "\n");
            Integer[] lista2 = new Integer[totalCardMap.size()];
            for (int i = 0; i < (totalCardMap.keySet()).toArray().length ; i++){
                if((totalCardMap.keySet()).toArray()[i] != null) {
                    try {
                        lista2[i] = Integer.parseInt((totalCardMap.keySet()).toArray()[i].toString());
                    } catch (Exception e) {
                        lista2[i] = 0;
                    }
                }
            }
            java.util.Arrays.sort(lista2);
            for(int i = 0; i < lista2.length; i++){
                if(lista2[i] > 0 && restrictedCardMap.containsKey(totalCardMap.get(lista2[i].toString()))) {
                    fw.append(lista2[i] + "\n");
                }
                fw.flush();
            }
            fw.close();
            System.out.println("File collection.dat has been created in " + outputPath);
        }
    }
}