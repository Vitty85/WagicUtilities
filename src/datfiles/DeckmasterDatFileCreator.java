/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datfiles;

/**
 *
 * @author alfieriv
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.*;
import java.util.ArrayList;

public class DeckmasterDatFileCreator {

    public static void main(String[] argv) throws IOException {
        
        argv = new String[] { "Commander 2020", "2020-04-17", "C:\\Users\\alfieriv\\Desktop\\_cards.dat", "COM-L.C20" };
        if (argv.length != 4) {
            System.err.println("Usage: java -jar DatFileCreator.jar setName setDate outputFilePath orderindex");
            System.exit(-1);
        }
        try {
            String setName = argv[0];
            String setDate = argv[1];
            String outputFilePath = argv[2];
            String orderIndex = argv[3];
            System.out.println("Creating Dat file for " + setName + " in " + outputFilePath);

            String gathererUrl = "https://gatherer.wizards.com/Pages/Card/Details.aspx?multiverseid=";
            String deckmasterUrl = "https://deckmaster.info/";

            ArrayList<String> ids = new ArrayList<>();

            Document docu = Jsoup.connect(deckmasterUrl).get();
            Elements links = docu.select("body a");
            int k;
            for (k = 0; k < links.size(); k++) {
                if (links.get(k).childNodes().size() > 1 && links.get(k).childNode(0).childNodes().size() > 0) {
                    if (links.get(k).childNode(1).childNodes().size() > 1) {
                        String deckname = links.get(k).childNode(1).childNode(0).toString() + " " + links.get(k).childNode(1).childNode(2).toString();
                        if (deckname.toLowerCase().contains(setName.toLowerCase())) {
                            break;
                        }
                    } else if (links.get(k).childNode(1).childNode(0).toString().toLowerCase().contains(setName.toLowerCase())) {
                        break;
                    }
                }
            }
            if (k >= links.size()) {
                System.err.println("The set " + setName + " could not be found on " + deckmasterUrl);
                System.exit(-1);
            }
            String setUrl = links.get(k).attributes().get("href");
            docu = Jsoup.connect(setUrl).get();
            links = docu.select("body a");
            Integer sequence = 0;
            boolean finished = false;
            for (k = 0; k < links.size() && !finished; k++) {
                if (links.get(k).childNodes().size() > 1 && links.get(k).childNode(0).childNodes().size() > 0 && links.get(k).attributes().get("href").contains("multiverseid")) {
                    String[] tokens = links.get(k).childNode(1).childNode(0).toString().split(" ");
                    for (int i = 0; i < tokens.length; i++) {
                        if (tokens[i].contains("(") && !(tokens[i].contains("Green") || tokens[i].contains("Blue") || 
                                tokens[i].contains("Black") || tokens[i].contains("White") || tokens[i].contains("Red"))) {
                            String currentseq = tokens[i].replace("(", "").replace(")", "");
                            if (Integer.parseInt(currentseq) > sequence) {
                                String id = links.get(k).attributes().get("href").split("=")[1];
                                ids.add(id);
                                sequence = Integer.parseInt(currentseq);
                            } else
                                finished = true;
                        }
                    }
                }
            }
            String head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=" + setName + "\n" +
                    "orderindex=" + orderIndex + "\n" +
                    "year=" + setDate + "\n" +
                    "total=" + ids.size() + "\n" +
                    "[/meta]";

            File cards = new File(outputFilePath);
            FileWriter fw = new FileWriter(cards);
            fw.append(head + "\n");
            fw.flush();

            for (int y = 0; y < ids.size(); y++) {
                String id = ids.get(y);
                Document doc = Jsoup.connect(gathererUrl + id.toString()).get();
                Elements divs = doc.select("body div");
                k = 0;
                for (k = 0; k < divs.size(); k++)
                    if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("card name"))
                        break;
                if (k >= divs.size())
                    continue;
                String name = divs.get(k + 1).childNode(0).attributes().get("#text").replace("\r\n", "").trim();

                for (k = 0; k < divs.size(); k++)
                    if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().contains("Rarity"))
                        break;
                String rare = divs.get(k + 1).childNode(1).childNodes().get(0).toString().replace("\r\n", "").trim();

                if (rare.toLowerCase().contains("uncommon"))
                    rare = "U";
                else if (rare.toLowerCase().contains("mythic"))
                    rare = "M";
                else if (rare.toLowerCase().contains("rare"))
                    rare = "R";
                else if (rare.toLowerCase().contains("land"))
                    rare = "L";
                else if (rare.toLowerCase().contains("token"))
                    rare = "T";
                else if (rare.toLowerCase().contains("common"))
                    rare = "C";

                String card = "[card]\n" +
                        "primitive=" + name + "\n" +
                        "id=" + id + "\n" +
                        "rarity=" + rare + "\n" +
                        "[/card]";
                fw.append(card + "\n");
                fw.flush();
                System.out.println("Added " + name + "(" + id + ") to Dat file");
            }
            fw.close();
            System.out.println("Dat file correctly created!");
        } catch (Exception e) {
            System.err.println("Error while creating dat file...");
            System.exit(-1);
        }
    }
}
