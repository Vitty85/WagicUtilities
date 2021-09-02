/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decks;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class DeckCardsStats {

    public static void main(String[] argv) throws IOException {
        argv = new String[] { "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\", "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\DeckCardsStats.csv", "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\deck151.txt" };
        if (argv.length < 3) {
            System.err.println("Usage: java -jar DeckCardsStats.jar wagicPath outputCsvFile inputDeck");
            System.exit(-1);
        }
        String basePath = argv[0];
        FileWriter fw = new FileWriter(new File(argv[1]));
        fw.append("CARD NAME;PRIMITIVE FILE\n");
        String inputDeck = argv[2];
        File baseFolder = new File(basePath + "Res\\sets\\primitives\\");
        File[] listOfPrimitives = baseFolder.listFiles();
        Map<String, String> totalCards = new HashMap<>();
        int numprimitives = 0;
        for (int y = 0; y < listOfPrimitives.length; y++) {
            String filePath = listOfPrimitives[y].getAbsolutePath();
            String primitive = primitives.PrimitiveDatabase.readLineByLineJava8(filePath);
            String findStr2 = "name=";
            int lastIndex2 = 0;
            while (lastIndex2 != -1) {
                lastIndex2 = primitive.indexOf(findStr2, lastIndex2);
                if (lastIndex2 != -1) {
                    numprimitives++;
                    lastIndex2 += findStr2.length();
                }
            }
            for (int i = 0; i < numprimitives; i++) {
                int a = primitive.indexOf("name=") + "name=".length();
                int b = primitive.indexOf("\n", a) + "\n".length();
                if (a < b && primitive.length() > b) {
                    String name = primitive.substring(a, b - "\n".length()).replace("//", "-");
                    primitive = primitive.substring(b);
                    if (!totalCards.containsKey(name))
                        totalCards.put(name, listOfPrimitives[y].getName());
                }
            }
        }
        String cards = primitives.PrimitiveDatabase.readLineByLineJava8(inputDeck);
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
            if(name.contains("#"))
                continue;
            if(totalCards.containsKey(name))
                fw.append(name + ";" + totalCards.get(name) + "\n");
            else
                fw.append(name + ";NOT FOUND\n");
        }
        fw.close();
        System.out.println("Stats file has been saved in: " + argv[1]);
    }
}