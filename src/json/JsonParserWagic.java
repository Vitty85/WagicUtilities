package json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

// @author Eduardo
public class JsonParserWagic {

    private static final String setCode = "NEC";
    private static String filePath = "C:\\Users\\alfieriv\\Desktop\\TODO\\" + setCode;
    private static Map<String, String> mappa2;
    private static Map<String, String> addedId;
    private static String basePath = "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\wagic-wagic-v0.23.1_win\\projects\\mtg\\Res\\sets\\";
    
    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String aFilePath) {
        filePath = aFilePath;
    }

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
    
    public static void buildDatabase(){
        File baseFolder = new File(basePath + "primitives\\");
        File[] listOfPrimitives = baseFolder.listFiles();
        mappa2 = new HashMap<>();
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
    }
    
    public static void main(String[] args) {

        boolean createCardsDat = true;
        boolean onlyToken = true;
        boolean withoutToken = false;
        
        File directorio = new File(getFilePath());
        directorio.mkdir();
        buildDatabase();
        addedId = new HashMap<>();
        
        try {
            FileReader reader = new FileReader(getFilePath() + ".json");
            File myObj = new File(getFilePath() + "\\_cards.dat");
            myObj.createNewFile();
            FileWriter myWriter;
            myWriter = new FileWriter(myObj.getCanonicalPath());
            FileWriter myWriterImages;
            myWriterImages = new FileWriter("C:\\Users\\alfieriv\\Desktop\\TODO\\" + setCode + ".csv", true);
            FileWriter myWriterPrimitives;
            myWriterPrimitives = new FileWriter("C:\\Users\\alfieriv\\Desktop\\TODO\\" + setCode + "_TODO.txt", true);
            
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONArray cards = (JSONArray) data.get("cards");

            // Metadata header
            if (createCardsDat) {
                Metadata.printMetadata(data.get("name"), data.get("releaseDate"), data.get("totalSetSize"), myWriter);
            }
            
            // Adding unsupported grade to primitive file.
            myWriterPrimitives.append("grade=unsupported\n");
                    
            for (Object o : cards) {
                JSONObject card = (JSONObject) o;
                JSONArray subtypes = (JSONArray) card.get("subtypes");

                JSONObject identifiers = (JSONObject) card.get("identifiers");
                String primitiveCardName;
                String primitiveRarity;
                String side;

                primitiveCardName = (String) card.get("faceName") != null ? (String) card.get("faceName") : (String) card.get("name");
                primitiveRarity = card.get("side") != null && "b".equals(card.get("side").toString()) ? "T" : (String) card.get("rarity");
                side = card.get("side") != null && "b".equals(card.get("side").toString()) ? "back/" : "front/";
                String oracleText = "";
                if (card.get("text") != null) {
                    oracleText = card.get("text").toString();
                }
                if (createCardsDat && identifiers.get("multiverseId") != null) {
                                    // If card is already in database, skip it   
                    if(addedId.containsKey(identifiers.get("multiverseId").toString()))
                        continue;
                    addedId.put(identifiers.get("multiverseId").toString(), primitiveCardName);
                    if (!withoutToken && !oracleText.trim().toLowerCase().contains("nontoken") && (oracleText.trim().toLowerCase().contains("create") && oracleText.trim().toLowerCase().contains("creature token")) || 
                        (oracleText.trim().toLowerCase().contains("put") && oracleText.trim().toLowerCase().contains("token")) ||
                        (oracleText.trim().toLowerCase().contains("create") && oracleText.trim().toLowerCase().contains("blood token"))) {                  
                        String arrays[] = oracleText.trim().split(" ");
                        String nametoken = "";
                        String tokenUrl = "";
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
                        if(nametoken.equals("Zombie") && oracleText.trim().toLowerCase().contains("with decayed"))
                            nametoken = "Zombie Dec";
                        if(nametoken.isEmpty() && oracleText.trim().toLowerCase().contains("powerstone token"))
                            nametoken = "Powerstone";
                        if(nametoken.isEmpty()){
                            System.err.println("Error reading token info for " + primitiveCardName + " (-" + identifiers.get("multiverseId") + "), you have to manually fix it later into Dat file!");
                            nametoken = "Unknown:" + primitiveCardName;
                            tokenUrl = "Unknown:" + primitiveCardName;
                        } else {
                            tokenUrl = findtokenurl(oracleText.trim(), primitiveCardName, (String) identifiers.get("multiverseId"));
                            if(tokenUrl.isEmpty()){
                                System.err.println("Error reading token image url for " + primitiveCardName + " (-" + identifiers.get("multiverseId") + "), you have to manually fix it later into CSV file!");
                                tokenUrl = "Unknown:" + primitiveCardName;
                            }
                        }
                        CardDat.generateCardDat(nametoken, "-"+identifiers.get("multiverseId"), "T", myWriter);
                        myWriterImages.write((String) card.get("setCode") + ";" + identifiers.get("multiverseId") + "t;" + tokenUrl + "\n");
                    }
                    if(!onlyToken){
                        CardDat.generateCardDat(primitiveCardName, identifiers.get("multiverseId"), primitiveRarity, myWriter);
                        CardDat.generateCSV((String) card.get("setCode"), identifiers.get("multiverseId"), (String) identifiers.get("scryfallId"), myWriterImages, side);
                    }
                }
                // If card is a reprint, skip it                
                if (card.get("isReprint") != null) {
                    continue;
                }

                // If card is already in database, skip it   
                if(mappa2.containsKey(primitiveCardName))
                    continue;
                mappa2.put(primitiveCardName, primitiveCardName);
                
                String nameHeader = "name=" + primitiveCardName;
                String cardName = primitiveCardName;

                JSONArray keywords = (JSONArray) card.get("keywords");
                String manaCost = (String) card.get("manaCost");
                String mana = "mana=" + manaCost;
                String type = "type=";
                String subtype = "";
                String power = "";
                String toughness = "";
                String loyalty = "";
                String defense = "";
                String colorIndicator = "";

                if (card.get("supertypes") != null) {
                    JSONArray supertypes = (JSONArray) card.get("supertypes");
                    Iterator supertypesIter = supertypes.iterator();
                    while (supertypesIter.hasNext()) {
                        String supertype = (String) supertypesIter.next();
                        type += supertype + " ";
                    }
                }

                JSONArray types = (JSONArray) card.get("types");
                Iterator typesIter = types.iterator();
                while (typesIter.hasNext()) {
                    String typeStr = (String) typesIter.next();
                    type += typeStr + " ";
                }

                if (!subtypes.isEmpty()) {
                    subtype = "subtype=";
                    Iterator subtypesIter = subtypes.iterator();
                    while (subtypesIter.hasNext()) {
                        String subtypeStr = (String) subtypesIter.next();
                        subtype += subtypeStr + " ";
                    }
                }

                if (card.get("power") != null) {
                    power = "power=" + card.get("power");
                    toughness = "toughness=" + card.get("toughness");
                }

                if (card.get("loyalty") != null) {
                    loyalty = "auto=counter(0/0," + card.get("loyalty") + ",loyalty)";
                }

                if (card.get("defense") != null) {
                    loyalty = "auto=counter(0/0," + card.get("defense") + ",defense)";
                }
                
                if (card.get("colorIndicator") != null) {
                    colorIndicator = "color=" + card.get("colorIndicator");
                }

                // CARD TAG
                myWriterPrimitives.append("[card]\n");
                myWriterPrimitives.append(nameHeader + "\n");

                if (type.contains("Planeswalker")) {
                    myWriterPrimitives.append(loyalty + "\n");
                } else if (type.contains("Battle")) {
                    myWriterPrimitives.append(defense + "\n");
                }
                // ORACLE TEXT
                if (oracleText != null) {
                    OracleTextToWagic.parseOracleText(keywords, oracleText, cardName, type, subtype, (String) card.get("power"), manaCost, myWriterPrimitives);
                    oracleText = oracleText.replace("\n", " -- ");
                    oracleText = oracleText.replace("—", "-");
                    oracleText = oracleText.replace("•", "");
                    myWriterPrimitives.append("text=" + oracleText + "\n");
                }
                if (manaCost != null) {
                    mana = mana.replace("/", "");
                    myWriterPrimitives.append(mana + "\n");
                }
                if (!colorIndicator.isEmpty()) {
                    colorIndicator = colorIndicator.replace("W", "white");
                    colorIndicator = colorIndicator.replace("U", "blue");
                    colorIndicator = colorIndicator.replace("B", "black");
                    colorIndicator = colorIndicator.replace("R", "red");
                    colorIndicator = colorIndicator.replace("G", "green");
                    colorIndicator = colorIndicator.replace("[", "");
                    colorIndicator = colorIndicator.replace("]", "");
                    colorIndicator = colorIndicator.replace("\"", "");

                    myWriterPrimitives.append(colorIndicator + "\n");
                }
                myWriterPrimitives.append(type.trim() + "\n");
                if (!subtype.isEmpty()) {
                    myWriterPrimitives.append(subtype.trim() + "\n");
                }
                if (!power.isEmpty()) {
                    myWriterPrimitives.append(power + "\n");
                    myWriterPrimitives.append(toughness + "\n");
                }
                myWriterPrimitives.append("[/card]\n");
                myWriterPrimitives.flush();
            }
            myWriter.close();
            myWriterImages.close();
            myWriterPrimitives.close();
            String primitives = readLineByLineJava8("C:\\Users\\alfieriv\\Desktop\\TODO\\" + setCode + "_TODO.txt");
            File f = new File("C:\\Users\\alfieriv\\Desktop\\TODO\\" + setCode + "_TODO.txt");
            f.delete();
            myWriterPrimitives = new FileWriter("C:\\Users\\alfieriv\\Desktop\\TODO\\" + setCode + "_TODO.txt", true);
            myWriterPrimitives.write(primitives);
            myWriterPrimitives.flush();
            myWriterPrimitives.close();
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException " + ex.getMessage());
        } catch (ParseException | NullPointerException ex) {
            System.out.println("NullPointerException " + ex.getMessage());
        }
    }
    
    public static String findtokenurl(String text, String primitiveName, String id){
        String CardImageToken = "";
        String imageurl = "https://scryfall.com/sets/";
        if (((text.trim().toLowerCase().contains("create") && text.trim().toLowerCase().contains("creature token")) || 
           (text.trim().toLowerCase().contains("put") && text.trim().toLowerCase().contains("token")))) {
            boolean tokenfound;
            String arrays[] = text.trim().split(" ");
            String nametoken = "";
            String nametocheck = "";
            String tokenstats = "";
            String color = "";
            String color1 = "";
            String color2 = "";
            for (int l = 1; l < arrays.length - 1; l++) {
                if (arrays[l].equalsIgnoreCase("creature") && arrays[l + 1].toLowerCase().contains("token")) {
                    nametoken = arrays[l - 1];
                    if(l - 3 > 0){
                        tokenstats = arrays[l - 3];
                        color1 = arrays[l - 2];
                    }
                    if(!tokenstats.contains("/")){
                        if(l - 4 > 0){
                            tokenstats = arrays[l - 4];
                            color1 = arrays[l - 3];
                        }
                    }
                    if(!tokenstats.contains("/")){
                        if(l - 5 > 0){
                            tokenstats = arrays[l - 5];
                            color1 = arrays[l - 4];
                            color2 = arrays[l - 2];
                        }
                    }
                    if(!tokenstats.contains("/")){
                        if(l - 6 > 0){
                            tokenstats = arrays[l - 6];
                            color1 = arrays[l - 5];
                            color2 = arrays[l - 3];
                        }
                    }
                    if(!tokenstats.contains("/")){
                        if(l - 7 > 0){
                            tokenstats = arrays[l - 7];
                            color1 = arrays[l - 6];
                            color2 = arrays[l - 4];
                        }
                    }
                    if(nametoken.equalsIgnoreCase("artifact")){
                        if(l - 2 > 0)
                            nametoken = arrays[l - 2];
                        if(l - 4 > 0){
                            tokenstats = arrays[l - 4];
                            color1 = arrays[l - 3];
                        }
                        if(!tokenstats.contains("/")){
                            if(l - 5 > 0){
                                tokenstats = arrays[l - 5];
                                color1 = arrays[l - 4];
                            }
                        }
                        if(!tokenstats.contains("/")){
                            if(l - 6 > 0){
                                tokenstats = arrays[l - 6];
                                color1 = arrays[l - 5];
                                color2 = arrays[l - 3];
                            }
                        }
                        if(!tokenstats.contains("/")){
                            if(l - 7 > 0){
                                tokenstats = arrays[l - 7];
                                color1 = arrays[l - 6];
                                color2 = arrays[l - 4];
                            }
                        }
                        if(!tokenstats.contains("/")){
                            if(l - 8 > 0) {
                                tokenstats = arrays[l - 8];
                                color1 = arrays[l - 7];
                                color2 = arrays[l - 5];
                            }
                        }    
                    }
                    if(!tokenstats.contains("/"))
                        tokenstats = "";

                    if(color1.toLowerCase().contains("white"))
                        color1 = "W";
                    else if(color1.toLowerCase().contains("blue"))
                        color1 = "U";
                    else if(color1.toLowerCase().contains("black"))
                        color1 = "B";
                    else if(color1.toLowerCase().contains("red"))
                        color1 = "R";
                    else if(color1.toLowerCase().contains("green"))
                        color1 = "G";
                    else if (color1.toLowerCase().contains("colorless"))
                        color1 = "C";
                    else 
                        color1 = "";

                    if(color2.toLowerCase().contains("white"))
                        color2 = "W";
                    else if(color1.toLowerCase().contains("blue"))
                        color2 = "U";
                    else if(color1.toLowerCase().contains("black"))
                        color2 = "B";
                    else if(color1.toLowerCase().contains("red"))
                        color2 = "R";
                    else if(color1.toLowerCase().contains("green"))
                        color2 = "G";
                    else 
                        color2 = "";

                    if(!color1.isEmpty()){
                        color = "(" + color1 + color2 + ")";
                    }
                    break;
                } else if (arrays[l].equalsIgnoreCase("put") && arrays[l + 3].toLowerCase().contains("token")) {
                    nametoken = arrays[l + 2];
                    for (int j = 1; j < arrays.length - 1; j++) {
                        if (arrays[j].contains("/")){
                            tokenstats = arrays[j];
                            color = arrays[j+1];
                        }
                    }
                    if(color.toLowerCase().contains("white"))
                        color = "(W)";
                    else if(color.toLowerCase().contains("blue"))
                        color = "(U)";
                    else if(color.toLowerCase().contains("black"))
                        color = "(B)";
                    else if(color.toLowerCase().contains("red"))
                        color = "(R)";
                    else if(color.toLowerCase().contains("green"))
                        color = "(G)";
                    else if (color.toLowerCase().contains("colorless"))
                        color = "(C)";
                    else 
                        color = "";
                    break;
                }
                else if (arrays[l + 1].toLowerCase().equalsIgnoreCase("token") || arrays[l + 1].toLowerCase().equalsIgnoreCase("token.")) {
                    nametoken = arrays[l];
                    tokenstats = "";
                    color = "";
                    break;
                }
            }
            Elements imgstoken;
            Document doc;
            try{
                 doc = Jsoup.connect(imageurl + setCode.toLowerCase()).maxBodySize(0)
                    .timeout(100000*5)
                    .get();
                if (nametoken.isEmpty()) {
                    tokenfound = false;
                    return CardImageToken;
                } else {
                    try {
                        tokenfound = true;
                        nametocheck = nametoken;
                        doc = findTokenPage(imageurl, nametoken, setCode, tokenstats, color);
                    } catch(Exception e) {
                        tokenfound = false;
                        return CardImageToken;
                    }
                }
            } catch(Exception e){
                return CardImageToken;
            }
            if(doc == null)
                return CardImageToken;
            imgstoken = doc.select("body img");
            if(imgstoken == null)
                return CardImageToken;
            if(tokenstats.isEmpty() && color.isEmpty())
                System.out.println("Warning reading token image url for " + nametoken + " (-" + id + ")" + " created by " + primitiveName + ", maybe you have to manually fix it later into CSV file!");
            for (int p = 0; p < imgstoken.size(); p++) {
                String titletoken = imgstoken.get(p).attributes().get("alt");
                if(titletoken.isEmpty())
                    titletoken = imgstoken.get(p).attributes().get("title");
                if (titletoken.toLowerCase().contains(nametocheck.toLowerCase())) {
                    CardImageToken = imgstoken.get(p).attributes().get("src");
                    if (CardImageToken.isEmpty())
                        CardImageToken = imgstoken.get(p).attributes().get("data-src");
                    CardImageToken = CardImageToken.replace("/normal/", "/large/");
                    if(CardImageToken.indexOf(".jpg") < CardImageToken.length())
                        CardImageToken = CardImageToken.substring(0, CardImageToken.indexOf(".jpg")+4);
                    return CardImageToken;
                }
            }
        }
        return CardImageToken;
    }
    
    public static Document findTokenPage(String imageurl, String name, String set, String tokenstats, String color) throws Exception {
        Document doc;
        Elements outlinks;
        try {
            if(set.equalsIgnoreCase("DBL"))
                set = "VOW";
            doc = Jsoup.connect(imageurl + "t" + set.toLowerCase()).get();
            if(doc != null) {
                outlinks = doc.select("body a");
                if(outlinks != null){
                    for (int k = 0; k < outlinks.size(); k++){
                        String linktoken = outlinks.get(k).attributes().get("href");
                        if(linktoken != null && !linktoken.isEmpty()){
                            try {
                                Document tokendoc = Jsoup.connect(linktoken).get();
                                if(tokendoc == null)
                                    continue;
                                Elements stats = tokendoc.select("head meta");
                                if(stats != null) {
                                    for (int j = 0; j < stats.size(); j++){
                                        if(stats.get(j).attributes().get("content").contains(tokenstats.replace("X/X", "*/*")) && 
                                                stats.get(j).attributes().get("content").toLowerCase().contains(name.toLowerCase())){
                                            return tokendoc;
                                        }
                                    }
                                }
                            } catch (Exception e) {}
                        }
                    }
                }
            }
        } catch (Exception e){}
        
        return null;
    }
}
