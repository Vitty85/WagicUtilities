/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datfiles;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author alfieriv
 */
public class ScryfallDatFileCreator {
    
     public static void main(String[] argv) {
        argv = new String[] { "Y22", "Alchemy: Innistrad", "2021-12-09", "C:\\Users\\alfieriv\\Desktop\\_cards.dat", "297480", "ONL-E.Y22" };
        if (argv.length != 6) {
            System.err.println("Usage: java -jar ScryDatFileCreator.jar setCode setName setDate outputFilePath startingID orderindex");
            System.exit(-1);
        }
        
        String setCode = argv[0];
        String setName = argv[1];
        String setDate = argv[2];
        String outputFilePath = argv[3];
        Integer currentId = Integer.parseInt(argv[4]);
        String orderIndex = argv[5];
        System.out.println("Creating Dat file for " + setName + " in " + outputFilePath);
        HashMap<Integer, String[]> cards = new HashMap<>();
        String scryFallUrl = "https://scryfall.com/sets/" + setCode.toLowerCase();
        Document doc = null;
        try {
            doc = Jsoup.connect(scryFallUrl).maxBodySize(0)
                .timeout(100000*5)
                .get();
            
        } catch (Exception e) {
            try {
                doc = Jsoup.connect(scryFallUrl).maxBodySize(0)
                    .timeout(100000*5)
                    .get();
            } catch (Exception e2) {
                try {
                    doc = Jsoup.connect(scryFallUrl).maxBodySize(0)
                        .timeout(100000*5)
                        .get();
                } catch (Exception e3) {
                    System.err.println("Error while searching for " + setName + " on " + scryFallUrl);
                    System.exit(-1);
                }
            }
        }
        if(doc == null){
            System.err.println("Error while searching for " + setName + " on " + scryFallUrl);
            System.exit(-1);
        }
        Elements outlinks = doc.select("body a");
        if(outlinks != null){
            for (int h = 0; h < outlinks.size(); h++){
                String linkcard = outlinks.get(h).attributes().get("href");
                if(linkcard == null || !linkcard.contains("https://scryfall.com/card/" + setCode.toLowerCase()))
                    continue;
                try{
                    doc = Jsoup.connect(linkcard).get();
                } catch (Exception ex) {
                    try{
                        doc = Jsoup.connect(linkcard).get();
                    } catch (Exception ex2) {
                        try{
                            doc = Jsoup.connect(linkcard).get();
                        } catch (Exception ex3) {
                            System.err.println("Error while reading card infos from, " + linkcard + ", you have to manually add it later into Dat file!");
                            continue;
                        }
                    }
                }
                if(doc == null){
                    System.err.println("Error while reading card infos from, " + linkcard + ", you have to manually add it later into Dat file!");
                    continue;
                }
                Elements metadata = doc.select("head meta");
                Elements span = doc.select("span");
                String text = "";
                String name = "";
                String rare = "";
                String imageurl = "";
                if(metadata != null) {
                    for (int j = 0; j < metadata.size(); j++){
                        if(metadata.get(j).attributes().get("property").equals("og:title")){
                            name = metadata.get(j).attributes().get("content");
                            if(name == null || name.isEmpty() || name.equals("Scryfall")){
                                name = "";
                            }
                            name = name.replace("&#39;", "'");
                        }
                        else if(metadata.get(j).attributes().get("property").equals("og:description")){
                            if(metadata.get(j).attributes().get("content").split("•").length > 3){           
                                text = metadata.get(j).attributes().get("content").split("•")[3].trim();
                                if (text.contains("(" + setCode + ")"))
                                    text = metadata.get(j).attributes().get("content").split("•")[2].trim();
                                if (text.contains("Illustrated by"))
                                    text = metadata.get(j).attributes().get("content").split("•")[1].trim();
                                text = text.replace("&#39;", "'");
                            }
                        } else if(metadata.get(j).attributes().get("property").equals("og:image")){
                            imageurl = metadata.get(j).attributes().get("content");
                        } else if(metadata.get(j).attributes().get("name").equals("scryfall:multiverse:id")){
                            currentId = Integer.parseInt(metadata.get(j).attributes().get("content"));
                        }
                    }
                }
                if(span != null){
                    for (int k = 0; k < span.size(); k++){
                        rare = span.get(k).attributes().get("class");
                        if(rare.equals("prints-current-set-details")){
                            rare = span.get(k).childNode(0).toString();
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
                            else if (rare.toLowerCase().contains("special"))
                                rare = "S";
                            break;
                        }
                    }
                }
                if(!name.isEmpty()){
                    System.out.println("Reading infos for " + name + " (" + currentId + ") from " + linkcard);
                    if(rare.isEmpty()){
                        System.err.println("Error reading rarity info for " + name + "(" + currentId + "), you have to manually fix it later into Dat file!");
                    }
                    /*if(name.contains(" // ")){
                        cards.put(currentId, new String[] { name.split(" // ")[0], rare, imageurl });
                    } else {
                        cards.put(currentId, new String[] { name, rare, imageurl });
                    }*/
                    cards.put(currentId, new String[] { name, rare, imageurl });
                } else {
                    System.err.println("Error Reading card infos from " + linkcard);
                    continue;
                }
                if ((text.trim().toLowerCase().contains("create") && text.trim().toLowerCase().contains("creature token")) || 
                        (text.trim().toLowerCase().contains("put") && text.trim().toLowerCase().contains("token")) ||
                        (text.trim().toLowerCase().contains("create") && text.trim().toLowerCase().contains("blood token"))) {                  
                    String arrays[] = text.trim().split(" ");
                    String nametoken = "";
                    for (int l = 1; l < arrays.length - 1; l++) {
                        if (arrays[l].equalsIgnoreCase("creature") && arrays[l + 1].toLowerCase().contains("token")) {
                            nametoken = arrays[l - 1];
                            if(nametoken.equalsIgnoreCase("artifact")){
                                if(l - 2 > 0)
                                    nametoken = arrays[l - 2];
                                break;
                            } 
                        } else if ((arrays[l].toLowerCase().contains("put") || arrays[l].toLowerCase().contains("create")) && arrays[l + 3].toLowerCase().contains("token")) {
                            nametoken = arrays[l + 2];
                            break;
                        }
                    }
                    if(nametoken.equals("Zombie") && text.trim().toLowerCase().contains("with decayed"))
                        nametoken = "Zombie Dec";
                    if(nametoken.isEmpty()){
                        System.err.println("Error reading token info for (-" + currentId + "), you have to manually fix it later into Dat file!");
                        nametoken = "Unknown";
                    }
                    cards.put(-currentId, new String[] { nametoken, "T", "" });
                }
                /*if(name.contains(" // ")){
                    System.out.println("DBL;" + currentId + ";" + imageurl);
                    System.out.println("else if(id.equals(\"" + currentId + "\"))\n    cardurl = \"" + imageurl + "\";");
                    currentId++;
                    System.out.println("DBL;" + currentId + ";" + imageurl.replace("/front/", "/back/"));
                    System.out.println("else if(id.equals(\"" + currentId + "\"))\n    cardurl = \"" + imageurl.replace("/front/", "/back/") + "\";");
                    cards.put(currentId, new String[] { name.split(" // ")[1], "T", imageurl.replace("/front/", "/back/") });
                }*/
                currentId++;
            }
        } else {
            System.err.println("Error while searching for " + setName + " on " + scryFallUrl);
            System.exit(-1);
        }
        System.out.println("All card infos have been read from  " + scryFallUrl + ", now i will try to create Dat file.");
        try {
            String head = "[meta]\n" +
                    "author=Wagic Team\n" +
                    "name=" + setName + "\n" +
                    "orderindex=" + orderIndex + "\n" +
                    "year=" + setDate + "\n" +
                    "total=" + cards.size() + "\n" +
                    "[/meta]";

            File cardDat = new File(outputFilePath);
            FileWriter fw = new FileWriter(cardDat);
            fw.append(head + "\n");
            fw.flush();

            Set<Integer> ids = cards.keySet();
            List<Integer> numbersList = new ArrayList<>(ids);
            Collections.sort(numbersList);
            ids = new LinkedHashSet<>(numbersList);
            for(Integer id : ids){
                String card = "[card]\n" +
                        "primitive=" + cards.get(id)[0] + "\n" +
                        "id=" + id + "\n" +
                        "rarity=" + cards.get(id)[1] + "\n" +
                        "[/card]";
                fw.append(card + "\n");
                fw.flush();
                System.out.println("Added " + cards.get(id)[0] + "(" + id + ") to Dat file.");
            }
            fw.close();
            System.out.println("Dat file correctly created in " + outputFilePath);    
        }catch (IOException e) {
            System.err.println("Error while creating Dat file: " + e.getMessage());
        }
    }
}
