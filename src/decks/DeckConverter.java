/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decks;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class DeckConverter {

    public static void main(String[] argv) throws IOException {
        argv = new String[] { "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\", "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\output.txt", "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\input.txt" };
        if (argv.length < 3) {
            System.err.println("Usage: java -jar DeckConverter.jar wagicPath outputDeck inputDeck");
            System.exit(-1);
        }
        String basePath = argv[0];
        String outputDeck = argv[1];
        String inputDeck = argv[2];
        File baseFolder = new File(basePath + "Res\\sets\\");
        File[] listOfSet = baseFolder.listFiles();
        Map<String, String> totalCardMap = new HashMap<>();
        Map<String, String> totalCardsSet = new HashMap<>();
        Map<String, Integer> deckCards = new HashMap<>();
        Map<String, Integer> deckSideboard = new HashMap<>();
        Map<String, Integer> deckCommander = new HashMap<>();
        Map<String, Integer> deckDungeon = new HashMap<>();
        Vector<String> metadata = new Vector<>();
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
                    if (primitive != null && id != null && !id.equalsIgnoreCase("null") && !primitive.equalsIgnoreCase("X/X")){
                        totalCardMap.put(id, primitive);
                        totalCardsSet.put(id, Set.replace("\\", ""));
                    }
                }
            }
        }
        String cards = primitives.PrimitiveDatabase.readLineByLineJava8(inputDeck);
        String listOfCards[] = cards.split("\n");
        for (int i = 0; i < listOfCards.length; i++) {
            String name = listOfCards[i];
            if(name.startsWith("#")){
                if(name.startsWith("#DNG:")){
                    name = name.replace("#DNG:", "");
                    String id = name;
                    name = totalCardMap.get(id) + ";" + totalCardsSet.get(id);
                    Integer occ = 1;
                    if(deckDungeon.containsKey(name))
                        occ = deckDungeon.get(name) + 1;
                    deckDungeon.put(name, occ);
                }
                else if(name.startsWith("#CMD:")){
                    name = name.replace("#CMD:", "");
                    String id = name;
                    name = totalCardMap.get(id) + ";" + totalCardsSet.get(id);
                    Integer occ = 1;
                    if(deckCommander.containsKey(name))
                        occ = deckCommander.get(name) + 1;
                    deckCommander.put(name, occ);
                }
                else if(name.startsWith("#SB:")){
                    name = name.replace("#SB:", "");
                    String id = name;
                    name = totalCardMap.get(id) + ";" + totalCardsSet.get(id);
                    Integer occ = 1;
                    if(deckSideboard.containsKey(name))
                        occ = deckSideboard.get(name) + 1;
                    deckSideboard.put(name, occ);
                }
                else{
                    metadata.add(name);
                }
            } else {
                String id = name;
                name = totalCardMap.get(id) + ";" + totalCardsSet.get(id);
                Integer occ = 1;
                if(deckCards.containsKey(name))
                    occ = deckCards.get(name) + 1;
                deckCards.put(name, occ);
            }
        }
        File cardlist = new File(outputDeck);
        FileWriter fw = new FileWriter(cardlist);
        for (int i = 0; i < metadata.size() ; i++){
            fw.append(metadata.get(i) + "\n");
        }
        String[] lista = new String[deckCards.size()];
        for (int i = 0; i < (deckCards.keySet()).toArray().length ; i++){
            if((deckCards.keySet()).toArray()[i] != null) {
                String name = (deckCards.keySet()).toArray()[i].toString().split(";")[0];
                String set = (deckCards.keySet()).toArray()[i].toString().split(";")[1];
                Integer occ = deckCards.get((deckCards.keySet()).toArray()[i].toString());
                fw.append(name + " (" + set + ")" + " *" + occ + "\n");
                fw.flush();
            }
        }
        lista = new String[deckSideboard.size()];
        for (int i = 0; i < (deckSideboard.keySet()).toArray().length ; i++){
            if((deckSideboard.keySet()).toArray()[i] != null) {
                String name = (deckSideboard.keySet()).toArray()[i].toString().split(";")[0];
                String set = (deckSideboard.keySet()).toArray()[i].toString().split(";")[1];
                Integer occ = deckSideboard.get((deckSideboard.keySet()).toArray()[i].toString());
                fw.append("#SB:" + name + " (" + set + ")" + " *" + occ + "\n");
                fw.flush();
            }
        }
        lista = new String[deckDungeon.size()];
        for (int i = 0; i < (deckDungeon.keySet()).toArray().length ; i++){
            if((deckDungeon.keySet()).toArray()[i] != null) {
                String name = (deckDungeon.keySet()).toArray()[i].toString().split(";")[0];
                String set = (deckDungeon.keySet()).toArray()[i].toString().split(";")[1];
                Integer occ = deckDungeon.get((deckDungeon.keySet()).toArray()[i].toString());
                fw.append("#DNG:" + name + " (" + set + ")" + " *" + occ + "\n");
                fw.flush();
            }
        }
        lista = new String[deckCommander.size()];
        for (int i = 0; i < (deckCommander.keySet()).toArray().length ; i++){
            if((deckCommander.keySet()).toArray()[i] != null) {
                String name = (deckCommander.keySet()).toArray()[i].toString().split(";")[0];
                String set = (deckCommander.keySet()).toArray()[i].toString().split(";")[1];
                Integer occ = deckCommander.get((deckCommander.keySet()).toArray()[i].toString());
                fw.append("#CMD:" + name + " (" + set + ")" + " *" + occ + "\n");
                fw.flush();
            }
        }
        fw.close();
        System.out.println("Deck has been converted in " + outputDeck);
    }
}