package json;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// @author vitty85
public class JsonParserWagic {

    private static final String setCode = "SIR";
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

                primitiveCardName = (String) card.get("faceName") != null ? (String) card.get("faceName") : (String) card.get("name");
                primitiveRarity = card.get("side") != null && "b".equals(card.get("side")) ? "T" : (String) card.get("rarity");
                String side = card.get("side") != null && "b".equals(card.get("side").toString()) ? "back/" : "front/";
                String id = (String) identifiers.get("multiverseId");
                boolean scryId = false;
                if(((String) card.get("name")).contains("//") && side.equals("front/")){
                    if(id != null && !onlyToken)
                        System.out.println("The card " + primitiveCardName + " is the front face of a double card (" + (String) card.get("name") + ") with id: " + id);
                } else if(((String) card.get("name")).contains("//") && side.equals("back/")){
                    if((id == null) || addedId.containsKey(id)){
                        String prevId = (id != null)?id:"null";    
                        id = (String) identifiers.get("scryfallId");
                        if(!onlyToken)
                            System.out.println("The card " + primitiveCardName + " is the backside of a double card (" + (String) card.get("name") + ") with null or duplicated id (" + prevId + "), i will use the scryfallId: " + id + "\n");
                        scryId = true;
                    }
                }
                String oracleText = "";
                if (card.get("text") != null) {
                    oracleText = card.get("text").toString();
                }
                if (createCardsDat && id != null) {
                    // If card is already in database, skip it   
                    if(addedId.containsKey(id))
                        continue;
                    addedId.put(id, primitiveCardName);
                    
                    JSONObject cardJson = findCardJsonById(id, scryId);
                    if(!withoutToken){
                        boolean canCreateToken = !oracleText.trim().toLowerCase().contains("nontoken") && 
                                (oracleText.trim().toLowerCase().contains("investigate") || 
                                (oracleText.trim().toLowerCase().contains("create") && oracleText.trim().toLowerCase().contains("creature token")) || 
                                (oracleText.trim().toLowerCase().contains("put") && oracleText.trim().toLowerCase().contains("token")));
                        String nametoken = findTokenName(cardJson, id, "");
                        if(nametoken.equals("Copy")){ 
                            nametoken = findTokenName(cardJson, id, "Copy");
                            if(nametoken.isEmpty()) 
                                nametoken = "Copy";
                        }
                        if(canCreateToken && nametoken.isEmpty()){
                            System.err.println("Error reading token info for " + primitiveCardName + " (-" + id + "), you have to manually fix it later into CSV file!");
                            nametoken = "Unknown:" + primitiveCardName;
                        }
                        if(!canCreateToken && !nametoken.isEmpty()){
                            System.out.println("Warning reading token info for " + primitiveCardName + " (-" + id + "), i found the token " + nametoken + " but the card text is ambiguos, you have to manually check later!");
                        }
                        String tokenUrl = findTokenImageUrl(cardJson, id, "large", "Copy");                    
                        if(canCreateToken && !nametoken.equals("Copy") && (tokenUrl == null || tokenUrl.isEmpty())){
                            System.err.println("Error reading token image url for " + primitiveCardName + " (-" + id + "), you have to manually fix it later into CSV file!");
                            tokenUrl = "Unknown:" + primitiveCardName;
                        }
                        if(!nametoken.isEmpty() && !nametoken.equals("Copy") && !tokenUrl.isEmpty()){
                            System.out.println("Writing token " + nametoken + " (" + tokenUrl + ") for " + primitiveCardName + " (" + id + ")");
                            CardDat.generateCardDat(nametoken, "-" + id, "T", myWriter);
                            myWriterImages.write((String) card.get("setCode") + ";" + id + "t;" + tokenUrl + "\n");
                        }
                    }
                    if(!onlyToken){
                        CardDat.generateCardDat(primitiveCardName, id, primitiveRarity, myWriter);
                        String imageUrl = findCardImageUrl(cardJson, primitiveCardName, id, "large");
                        if(side.equals("back/") && imageUrl.contains("front/"))
                            System.err.println("The card " + primitiveCardName + " could be a backside, but the url cointains front: " + imageUrl + "\n");
                        myWriterImages.write((String) card.get("setCode") + ";" + id + ";" + imageUrl + "\n");
                    }
                    myWriter.flush();
                    myWriterImages.flush();
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

                if (subtypes.size() > 0) {
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
                    defense = "auto=counter(0/0," + card.get("defense") + ",defense)";
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
        } catch (Exception ex) {
            System.out.println("Exception " + ex.getMessage());
        }
    }
    
    public static JSONObject findCardJsonById(String id, boolean scryId) throws Exception{
        String apiUrl = "https://api.scryfall.com/cards/multiverse/" + id;
        if(scryId)
            apiUrl = "https://api.scryfall.com/cards/" + id;
        
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.toString());
        return jsonObject;
    }
    
    public static JSONObject findCardJsonByName(String cardName) throws Exception{
        String apiUrl = "https://api.scryfall.com/cards/named?exact=" + cardName.replace(" ", "+");

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(response.toString());
        return jsonObject;
    }
    
    public static String findCardMultiverseId(JSONObject jsonObject){
        String multiverseId = "";
        if (jsonObject.get("multiverse_ids") != null) {
            multiverseId = (String) ((JSONArray) jsonObject.get("multiverse_ids")).get(0);
        } else {
            System.err.println("Multiverse ID not found in the JSON response.");
        }
        return multiverseId;
    }
    
    public static String findCardImageUrl(JSONObject jsonObject, String primitiveCardName, String multiverseId, String format){
        Map<String, String> imageUris = new HashMap<>();
        if (jsonObject.get("image_uris") != null) {
            JSONObject imageUrisObject = (JSONObject) jsonObject.get("image_uris");
            if(imageUrisObject != null && jsonObject.get("name").equals(primitiveCardName))
                imageUris = (HashMap) imageUrisObject;
        } else if (jsonObject.get("card_faces") != null) {
            JSONArray faces = (JSONArray) jsonObject.get("card_faces");
            if(faces != null){
                for (Object o : faces) {
                    JSONObject imageUrisObject = (JSONObject) o;
                    if(imageUrisObject != null && imageUrisObject.get("name").equals(primitiveCardName)){
                        if (imageUrisObject.get("image_uris") != null) {
                            imageUrisObject = (JSONObject) imageUrisObject.get("image_uris");
                            if(imageUrisObject != null)
                                imageUris = (HashMap) imageUrisObject;
                        }
                    }
                }
            }
        } else {
            System.err.println("Cannot retrieve image url for card: " + primitiveCardName + " (" + multiverseId + ")");
            return "";
        }
        String imageUrl = imageUris.get(format);
        if(imageUrl == null){
            System.err.println("Cannot retrieve image url for card: " + primitiveCardName + " (" + multiverseId + ")");
            return "";
        }
        if(imageUrl.indexOf(".jpg") < imageUrl.length())
            imageUrl = imageUrl.substring(0, imageUrl.indexOf(".jpg")+4);
        return imageUrl;
    }
    
    public static String findTokenImageUrl(JSONObject jsonObject, String multiverseId, String format, String filterName){
        String imageUrl = "";
        try {
            Document document = Jsoup.connect((String )jsonObject.get("scryfall_uri")).get();
            if (document != null) {
                Element printsTable = document.selectFirst("table.prints-table");
                if (printsTable != null) {
                    Elements rows = printsTable.select("tr");
                    int howmany = 0;
                    for (Element row : rows) {
                        if (row.text().contains(" Token,") && !row.text().contains("Faces,")) {
                            Element aElement = row.selectFirst("td > a");
                            String tokenName = aElement.text();
                            tokenName = tokenName.substring(0, tokenName.indexOf(" Token,"));
                            if(tokenName.equals(filterName)){
                                System.out.println("The token " + tokenName + " has been filtered for card: " + (String)jsonObject.get("name") + " (" + multiverseId + ")");
                            } else {
                                howmany++;
                                imageUrl = aElement.attr("data-card-image-front");
                                if(imageUrl != null){
                                    if(imageUrl.indexOf(".jpg") < imageUrl.length())
                                        imageUrl = imageUrl.substring(0, imageUrl.indexOf(".jpg")+4);
                                }
                            }
                        }
                    }
                    if (howmany > 1) {
                        System.out.println("Warning: found " + howmany  + " valid image urls for token created by: " + (String)jsonObject.get("name") + " (" + multiverseId + ")");
                    } 
                } 
            } 
        } catch (IOException e) {
            System.err.println("There was an error while retrieving the token image for card: " + (String)jsonObject.get("name") + " (" + multiverseId + ")");
            return "";
        }
        if(imageUrl == null){
            System.err.println("There was an error while retrieving token image for card: " + (String)jsonObject.get("name") + " (" + multiverseId + ")");
            return "";
        }
        return imageUrl.replace("large", format);
    }
    
    public static String findTokenName(JSONObject jsonObject, String multiverseId, String filterName){
        String tokenName = "";
        try {
            Document document = Jsoup.connect((String) jsonObject.get("scryfall_uri")).get();
            if (document != null) {
                Element printsTable = document.selectFirst("table.prints-table");
                if (printsTable != null) {
                    Elements rows = printsTable.select("tr");
                    int howmany = 0;
                    for (Element row : rows) {
                        if (row.text().contains(" Token,") && !row.text().contains("Faces,")) {
                            Element aElement = row.selectFirst("td > a");
                            String tok = aElement.text();
                            if(tok != null) {
                                tok = tok.substring(0, tok.indexOf(" Token,"));
                                if (tok.equals(filterName)) {
                                    System.out.println("The token " + tok + " has been filtered for card: " + (String) jsonObject.get("name") + " (" + multiverseId + ")");
                                } else {
                                    howmany++;
                                    tokenName = tok;
                                }
                            }
                        }
                    }
                    if (howmany > 1) {
                        System.out.println("Warning: found " + howmany  + " valid token name created by: " + (String)jsonObject.get("name") + " (" + multiverseId + ")");
                    } 
                }
            } 
        } catch (IOException e) {
            System.err.println("There was an error while retrieving the token name for card: " + (String)jsonObject.get("name") + " (" + multiverseId + ")");
            return "";
        }
        return tokenName;
    }
}
