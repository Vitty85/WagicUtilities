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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScryFallDownloader {
    public static void main(String[] args) throws IOException {
        String cardName = "Shorikai, Genesis Engine";
        String multiverseId = "553691";
        String imgFormat = "large";
        
        // Recupera le informazioni sulla carta in json mediante le API di ScryFall
        JsonObject cardJson = findCardJsonById(multiverseId);
        //JsonObject cardJson = findCardJsonByName(cardName);
        
        // Verifica se la carta ha un multiverse id associato
        if(multiverseId.isEmpty())
            multiverseId = findCardMultiverseId(cardJson);
        if(multiverseId.isEmpty())
            multiverseId = cardName;
        
        // Verifica se la carta ha una immagine associata
        String cardImageURL = findCardImageUrl(cardJson, imgFormat);    
        if(!cardImageURL.isEmpty()){
            URL cardImageURLObj = new URL(cardImageURL);
            String fileName = multiverseId + ".jpg";
            Path filePath = new File("C:\\Users\\alfieriv\\Desktop\\TODO\\" + fileName).toPath();
            Files.copy(cardImageURLObj.openStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Card image downloaded as: " + filePath.toAbsolutePath());
        }
        
        // Verifica se la carta ha un token associato
        String tokenImameURL = findTokenImageUrl(cardJson, imgFormat);
        if(!tokenImameURL.isEmpty()){
            URL tokenImageUrlObj = new URL(tokenImameURL);
            String tokenFileName = multiverseId + "t.jpg";
            Path tokenFilePath = new File("C:\\Users\\alfieriv\\Desktop\\TODO\\" + tokenFileName).toPath();
            Files.copy(tokenImageUrlObj.openStream(), tokenFilePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Token image downloaded as: " + tokenFilePath.toAbsolutePath());
        }
    }
    
    public static JsonObject findCardJsonById(String multiverseId) throws IOException{
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

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response.toString());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject;
    }
    
    public static JsonObject findCardJsonByName(String cardName) throws IOException{
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

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response.toString());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        return jsonObject;
    }
    
    public static String findCardMultiverseId(JsonObject jsonObject){
        String multiverseId = "";
        if (jsonObject.has("multiverse_ids")) {
            multiverseId = jsonObject.getAsJsonArray("multiverse_ids").get(0).getAsString();
        } else {
            System.err.println("Multiverse ID not found in the JSON response.");
        }
        return multiverseId;
    }
    
    public static String findCardImageUrl(JsonObject jsonObject, String format){
        Map<String, String> imageUris = new HashMap<>();
        if (jsonObject.has("image_uris")) {
            JsonObject imageUrisObject = jsonObject.getAsJsonObject("image_uris");
            for (Map.Entry<String, JsonElement> entry : imageUrisObject.entrySet()) {
                String type = entry.getKey();
                String imageUrl = entry.getValue().getAsString();
                if(imageUrl.indexOf(".jpg") < imageUrl.length())
                    imageUrl = imageUrl.substring(0, imageUrl.indexOf(".jpg")+4);
                imageUris.put(type, imageUrl);
            }
        } else {
            System.err.println("This card has no images...");
            return "";
        }
        return imageUris.get(format);
    }
    
    public static String findTokenImageUrl(JsonObject jsonObject, String format){
        String imageUrl = "";
        try {
            Document document = Jsoup.connect(jsonObject.get("scryfall_uri").getAsString()).get();
            if (document != null) {
                Element printsTable = document.selectFirst("table.prints-table");
                if (printsTable != null) {
                    Element tokenRow = null;
                    Elements rows = printsTable.select("tr");
                    for (Element row : rows) {
                        if (row.text().contains(" Token,") && !row.text().contains("Faces,")) {
                            tokenRow = row;
                        }
                    }
                    if (tokenRow != null) {
                        Element aElement = tokenRow.selectFirst("td > a");
                        if (aElement != null) {
                            String tokenName = aElement.text();
                            tokenName = tokenName.substring(0, tokenName.indexOf(" Token,"));
                            System.out.println("Token found: " + tokenName);
                            imageUrl = aElement.attr("data-card-image-front");
                            if(imageUrl.indexOf(".jpg") < imageUrl.length())
                                imageUrl = imageUrl.substring(0, imageUrl.indexOf(".jpg")+4);
                        }
                    } 
                } 
            } 
        } catch (IOException e) {
            System.err.println("There was an error while retrieving token image...");
            return null;
        }
        return imageUrl.replace("large", format);
    }

    public static String findTokenName(JsonObject jsonObject){
        String tokenName = "";
        try {
            Document document = Jsoup.connect(jsonObject.get("scryfall_uri").getAsString()).get();
            if (document != null) {
                Element printsTable = document.selectFirst("table.prints-table");
                if (printsTable != null) {
                    Element tokenRow = null;
                    Elements rows = printsTable.select("tr");
                    for (Element row : rows) {
                        if (row.text().contains(" Token,") && !row.text().contains("Faces,")) {
                            tokenRow = row;
                        }
                    }
                    if (tokenRow != null) {
                        Element aElement = tokenRow.selectFirst("td > a");
                        if (aElement != null) {
                            tokenName = aElement.text();
                            tokenName = tokenName.substring(0, tokenName.indexOf(" Token,"));
                            System.out.println("Token found: " + tokenName);
                        }
                    } 
                } 
            } 
        } catch (IOException e) {
            System.err.println("There was an error while retrieving token image...");
            return null;
        }
        return tokenName;
    }
}
