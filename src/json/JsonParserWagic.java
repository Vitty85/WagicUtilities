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

// @author Eduardo
public class JsonParserWagic {

    private static final String setCode = "BRO";
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

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
            JSONObject data = (JSONObject) jsonObject.get("data");
            JSONArray cards = (JSONArray) data.get("cards");

            // Metadata header
            if (createCardsDat) {
                Metadata.printMetadata(data.get("name"), data.get("releaseDate"), data.get("totalSetSize"), myWriter);
            }

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
                    if ((oracleText.trim().toLowerCase().contains("create") && oracleText.trim().toLowerCase().contains("creature token")) || 
                        (oracleText.trim().toLowerCase().contains("put") && oracleText.trim().toLowerCase().contains("token")) ||
                        (oracleText.trim().toLowerCase().contains("create") && oracleText.trim().toLowerCase().contains("blood token"))) {                  
                        String arrays[] = oracleText.trim().split(" ");
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
                        if(nametoken.equals("Zombie") && oracleText.trim().toLowerCase().contains("with decayed"))
                            nametoken = "Zombie Dec";
                        if(nametoken.isEmpty()){
                            System.err.println("Error reading token info for (-" + identifiers.get("multiverseId") + "), you have to manually fix it later into Dat file!");
                            nametoken = "Unknown";
                        }
                        //CardDat.generateCardDat(nametoken, "-"+identifiers.get("multiverseId"), "T", myWriter);
                    }
                    CardDat.generateCardDat(primitiveCardName, identifiers.get("multiverseId"), primitiveRarity, myWriter);
                    CardDat.generateCSV((String) card.get("setCode"), identifiers.get("multiverseId"), (String) identifiers.get("scryfallId"), myWriterImages, side);
                }
                // If card is a reprint, skip it                
                if (card.get("isReprint") != null || "[\"Siege\"]".equals(subtypes.toString())) {
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

                if (card.get("colorIndicator") != null) {
                    colorIndicator = "color=" + card.get("colorIndicator");
                }

                // CARD TAG
                System.out.println("[card]");
                System.out.println(nameHeader);

                if (type.contains("Planeswalker")) {
                    System.out.println(loyalty);
                }
                // ORACLE TEXT
                if (oracleText != null) {
                    OracleTextToWagic.parseOracleText(keywords, oracleText, cardName, type, subtype, (String) card.get("power"), manaCost);
                    System.out.println("text=" + oracleText.replace("\n", " -- "));
                }
                if (manaCost != null) {
                    mana = mana.replace("/", "");
                    System.out.println(mana);
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

                    System.out.println(colorIndicator);
                }
                System.out.println(type.trim());
                if (!subtype.isEmpty()) {
                    System.out.println(subtype.trim());
                }
                if (!power.isEmpty()) {
                    System.out.println(power);
                    System.out.println(toughness);
                }
                System.out.println("[/card]\n");
            }
            myWriter.close();
            myWriterImages.close();

        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException " + ex.getMessage());
        } catch (ParseException | NullPointerException ex) {
            System.out.println("NullPointerException " + ex.getMessage());
        }
    }
}
