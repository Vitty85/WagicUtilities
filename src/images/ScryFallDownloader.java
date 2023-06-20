/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package images;

/**
 *
 * @author alfieriv
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScryFallDownloader {
    public static void main(String[] args) throws Exception {
        String cardName = "Fable of the Mirror-Breaker";
        String multiverseId = "553026";
        String imgFormat = "large";
        
        // Recupera le informazioni sulla carta in json mediante le API di ScryFall
        JSONObject cardJson = findCardJsonById(multiverseId);
        //JSONObject cardJson = findCardJsonByName(cardName);
        
        // Verifica se la carta ha un multiverse id associato
        if(multiverseId.isEmpty())
            multiverseId = findCardMultiverseId(cardJson);
        if(multiverseId.isEmpty())
            multiverseId = cardName;
        
        // Verifica se la carta ha una immagine associata
        String cardImageURL = findCardImageUrl(cardJson, cardName, multiverseId, imgFormat);    
        if(!cardImageURL.isEmpty()){
            URL cardImageURLObj = new URL(cardImageURL);
            String fileName = multiverseId + ".jpg";
            Path filePath = new File("C:\\Users\\alfieriv\\Desktop\\TODO\\" + fileName).toPath();
            Files.copy(cardImageURLObj.openStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Card image downloaded as: " + filePath.toAbsolutePath());
        }
        
        // Verifica se la carta ha un token associato
        String tokenName = findTokenName(cardJson, multiverseId, "Copy");
        String tokenImameURL = findTokenImageUrl(cardJson, multiverseId, imgFormat, "Copy");
        if(!tokenImameURL.isEmpty()){
            URL tokenImageUrlObj = new URL(tokenImameURL);
            String tokenFileName = multiverseId + "t.jpg";
            Path tokenFilePath = new File("C:\\Users\\alfieriv\\Desktop\\TODO\\" + tokenFileName).toPath();
            Files.copy(tokenImageUrlObj.openStream(), tokenFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Token image downloaded as: " + tokenFilePath.toAbsolutePath());
        }
    }
    
    public static JSONObject findCardJsonById(String multiverseId) throws Exception{
        String apiUrl = "https://api.scryfall.com/cards/multiverse/" + multiverseId;

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
            System.err.println("There was an error while retrieving token image for card: " + (String)jsonObject.get("name") + " (" + multiverseId + ")");
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
            System.err.println("There was an error while retrieving token name for card: " + (String)jsonObject.get("name") + " (" + multiverseId + ")");
            return "";
        }
        return tokenName;
    }
}
