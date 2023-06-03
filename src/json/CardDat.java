package json;

// @author vitty85
import java.io.FileWriter;
import java.io.IOException;

public class CardDat {

    //[card]
    //primitive=Abomination of Gudul 
    //id=386463
    //rarity=C
    //[/card]
    public static void generateCardDat(String primitive, Object id, String rarity, FileWriter myWriter) {

        switch (rarity) {
            case "common":
                rarity = "C";
                break;
            case "uncommon":
                rarity = "U";
                break;
            case "rare":
                rarity = "R";
                break;
            case "mythic":
                rarity = "M";
                break;
            case "special":
                rarity = "S";
                break;
        }

        String[] basics = {"Plains", "Island", "Swamp", "Mountain", "Forest"};

        for (String basic : basics) {
            if (primitive.equals(basic)) {
                rarity = "L";
            }
        }
        try {
            myWriter.write("[card]\n");
            myWriter.write("primitive=" + primitive + "\n");
            myWriter.write("id=" + id + "\n");
            myWriter.write("rarity=" + rarity + "\n");
            myWriter.write("[/card]\n");

        } catch (IOException e) {

        }
    }

    static void generateCSV(String string, Object id, String scryfallId, FileWriter myWriterImages, String side) {
        try {
            myWriterImages.write(string + ";" + id + ";" + "https://cards.scryfall.io/large/" + side + scryfallId.substring(0, 1) + "/" + scryfallId.substring(1, 2) + "/" + scryfallId + ".jpg\n");
        } catch (IOException e) {
            System.out.println("Error in generateCSV");
        }
    }
}
