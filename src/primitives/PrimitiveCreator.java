/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package primitives;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class PrimitiveCreator {
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

        String set = "J21";

        String basePath = "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\Res\\sets\\";
        File baseFolder = new File(basePath + "primitives\\");
        File[] listOfPrimitives = baseFolder.listFiles();
        Map<String, String> mappa2 = new HashMap<>();
        int numprimitives = 0;
        for (int y = 0; y < listOfPrimitives.length; y++) {
            String filePath = listOfPrimitives[y].getAbsolutePath();
            String primitives = readLineByLineJava8(filePath);
            String findStr2 = "name=";
            int lastIndex2 = 0;
            while (lastIndex2 != -1) {
                lastIndex2 = primitives.indexOf(findStr2, lastIndex2);
                if (lastIndex2 != -1) {
                    numprimitives++;
                    lastIndex2 += findStr2.length();
                }
            }
            for (int i = 0; i < numprimitives; i++) {
                int a = primitives.indexOf("name=") + "name=".length();
                int b = primitives.indexOf("\n", a) + "\n".length();
                if (a < b && primitives.length() > b) {
                    String name = primitives.substring(a, b - "\n".length()).replace("//", "-");
                    primitives = primitives.substring(b);
                    if (!mappa2.containsKey(name))
                        mappa2.put(name, name);
                }
            }
        }
        baseFolder = new File(basePath);
        File[] listOfSet = baseFolder.listFiles();
        Map<String, String> mappa = new HashMap<>();
        for (int y = 0; y < listOfSet.length; y++) {
            if (listOfSet[y].isDirectory() && listOfSet[y].getName().equalsIgnoreCase(set)) {
                String Set = listOfSet[y].getName() + "\\";
                File folder = new File(basePath + Set);
                File[] listOfFiles = folder.listFiles();
                String filePath = folder.getAbsolutePath() + "\\_cards.dat";
                String lines = readLineByLineJava8(filePath);
                int totalcards = 0;
                String findStr = "primitive=";
                int lastIndex = 0;
                while (lastIndex != -1) {
                    lastIndex = lines.indexOf(findStr, lastIndex);
                    if (lastIndex != -1) {
                        totalcards++;
                        lastIndex += findStr.length();
                    }
                }
                for (int i = 0; i < totalcards; i++) {
                    int a = lines.indexOf("primitive=") + "primitive=".length();
                    int b = lines.indexOf("id=") + "id=".length();
                    int c = lines.indexOf("rarity=");
                    String name = lines.substring(a, b - "id=".length() - 1).replace("//", "-");
                    String id = lines.substring(b, c - 1);
                    lines = lines.substring(c + "rarity=".length() + 2, lines.length());
                    if(name!=null && id!= null && !id.contains("-") && !mappa2.containsKey(name))
                        mappa.put(id, name);
                }
            }
        }

        String base = "https://gatherer.wizards.com/Pages/Card/";
        String baseurl = base + "Details.aspx?multiverseid=";
        String head = "grade=unsupported\n";
        File cards = new File("C:\\Users\\alfieriv\\Desktop\\TODO\\" + set + ".txt");
        FileWriter fw2 = new FileWriter(cards);
        fw2.append(head);
        File missing = new File("C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\WTH 0.23.1\\TODO_" + set + ".csv");
        FileWriter fw = new FileWriter(missing);
        fw.append("id;name" + "\n");
        for (int i = 0; i < (mappa.keySet()).toArray().length ; i++){
            String id = (mappa.keySet()).toArray()[i].toString();
            if(id != null && !id.isEmpty()) {
                String card = "[card]\n";
                String Name = mappa.get(id);
                fw.append(id + ";" + Name + "\n");
                fw.flush();
                Document doc = null;
                try {
                    doc = Jsoup.connect(baseurl + id).get();
                }catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                if(doc == null)
                    continue;
                Elements divs = doc.select("body div");
                int k;
                for (k = 0; k < divs.size(); k++)
                    if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("card name"))
                        break;
                String parsedname = divs.get(k+1).childNode(0).attributes().get("#text").replace("\r\n","").trim();
                card = card + "name="+parsedname.trim()+"\n";

                String text = "";
                for (k = 0; k < divs.size(); k++)
                    if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("card text"))
                        break;
                if (k < divs.size()) {
                    Element tex = divs.get(k + 1);
                    for (int z = 0; z < divs.get(k + 1).childNodes().size(); z++) {
                        for (int u = 0; u < divs.get(k + 1).childNode(z).childNodes().size(); u++) {
                            if(divs.get(k + 1).childNode(z).childNode(u).childNodes().size() > 1) {
                                for (int w = 0; w < divs.get(k + 1).childNode(z).childNode(u).childNodes().size(); w++) {
                                    if (divs.get(k + 1).childNode(z).childNode(u).childNode(w).hasAttr("alt")) {
                                        String newtext = divs.get(k + 1).childNode(z).childNode(u).childNode(w).attributes().get("alt").trim();
                                        newtext = newtext.replace("Green", "{G}");
                                        newtext = newtext.replace("White", "{W}");
                                        newtext = newtext.replace("Black", "{B}");
                                        newtext = newtext.replace("Blue", "{U}");
                                        newtext = newtext.replace("Red", "{R}");
                                        newtext = newtext.replace("Tap", "{T}");
                                        text = text + newtext;
                                    } else
                                        text = text + " " + divs.get(k + 1).childNode(z).childNode(u).childNode(w).toString().replace("\r\n", "").trim() + " ";
                                    text = text.replace("} .", "}.");
                                    text = text.replace("} :", "}:");
                                    text = text.replace("} ,", "},");
                                }
                            } else {
                                if (divs.get(k + 1).childNode(z).childNode(u).hasAttr("alt")) {
                                    String newtext = divs.get(k + 1).childNode(z).childNode(u).attributes().get("alt").trim();
                                    newtext = newtext.replace("Green", "{G}");
                                    newtext = newtext.replace("White", "{W}");
                                    newtext = newtext.replace("Black", "{B}");
                                    newtext = newtext.replace("Blue", "{U}");
                                    newtext = newtext.replace("Red", "{R}");
                                    newtext = newtext.replace("Tap", "{T}");
                                    text = text + newtext;
                                } else
                                    text = text + " " + divs.get(k + 1).childNode(z).childNode(u).toString().replace("\r\n", "").trim() + " ";
                                text = text.replace("} .", "}.");
                                text = text.replace("} :", "}:");
                                text = text.replace("} ,", "},");
                            }
                            if (z > 0 && z < divs.get(k + 1).childNodes().size() - 1)
                                text = text + " -- ";
                            text = text.replace("<i>", "");
                            text = text.replace("</i>", "");
                            text = text.replace("<b>", "");
                            text = text.replace("</b>", "");
                            text = text.replace(" -- (", " (");
                            text = text.replace("  ", " ");
                        }
                    }
                }
                if(!text.trim().isEmpty())
                    card = card + "text="+text.trim()+"\n";

                String mana = "";
                for (k = 0; k < divs.size(); k++)
                    if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("mana cost"))
                        break;
                if (k < divs.size()) {
                    for (int z = 1; z < divs.get(k+1).childNodes().size(); z++)
                        mana = mana + "{" + divs.get(k+1).childNode(z).attributes().get("alt").trim() + "}";
                }
                mana = mana.replace("Green", "G");
                mana = mana.replace("White", "W");
                mana = mana.replace("Black", "B");
                mana = mana.replace("Blue", "U");
                mana = mana.replace("Red", "R");
                if(!mana.trim().isEmpty())
                    card = card + "mana="+mana.trim()+"\n";

                String type = "";
                String subtype = "";
                for (k = 0; k < divs.size(); k++)
                    if (divs.get(k).childNodes().size() > 0 && divs.get(k).childNode(0).toString().toLowerCase().contains("types"))
                        break;
                if (k < divs.size()) {
                    type = divs.get(k+1).childNode(0).toString().replace("\r\n","").trim();
                }
                if(type.contains("—")) {
                    subtype = type.split("—")[1];
                    type = type.split("—")[0];
                    card = card + "type="+type.trim()+"\n";
                    card = card + "subtype="+subtype.trim()+"\n";
                } else {
                    card = card + "type="+type.trim()+"\n";
                }

                String pt = "";
                for (k = 0; k < divs.size(); k++)
                    if (divs.get(k).childNodes().size() > 1 && divs.get(k).childNode(1).toString().equals("<b>P/T:</b>"))
                        break;
                if (k < divs.size()) {
                    pt = divs.get(k + 1).childNode(0).toString().replace("\r\n", "").trim();
                    card = card + "power=" + pt.split("/")[0].trim() + "\n";
                    card = card + "toughness=" + pt.split("/")[1].trim() + "\n";
                }

                card = card + "[/card]"+"\n";
                fw2.append(card);
                fw2.flush();
            }
        }
        fw.close();
        fw2.close();
        System.out.println("check is done");
    }
}