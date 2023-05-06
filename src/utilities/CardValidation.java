package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CardValidation {

    public static void main(String[] args) {
        String folderName = "C:\\Users\\Eduardo_\\Documents\\WagicWindows-0231\\Res\\sets\\primitives";
        File folder = new File(folderName);

        for (File file : folder.listFiles()) {
            String fileName = file.getAbsolutePath();

            System.out.println("Processing file: " + file.getName());

            try ( BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

                String line;
                String currentCard = "";
                int lineNum = 0;
                boolean isValid = true;
                boolean hasTarget = false;
                while ((line = reader.readLine()) != null) {
                    lineNum++;
                    line = line.trim();
                    if (line.startsWith("[card]")) {
                        currentCard = "";
                        hasTarget = false;
                    } else if (line.startsWith("name=")) {
                        currentCard = line.substring(5);
                        hasTarget = false;
                    } else if (line.startsWith("target=")) {
                        hasTarget = true;
                    } else if (line.startsWith("subtype=Aura") && !currentCard.isEmpty() && !currentCard.endsWith("*")) {
                        if (!hasTarget) {
                            System.out.println("ERROR: Aura card \"" + currentCard + "\" at line " + lineNum + " is missing a target line.");
                            isValid = false;
                        }
                    } else if (line.startsWith("type=") && line.contains("Creature") && !currentCard.isEmpty() && !currentCard.endsWith("*")) {
                        if (hasTarget) {
                            System.out.println("ERROR: creature card \"" + currentCard + "\" at line " + lineNum + " is a target line.");
                            isValid = false;
                        }
                    } else if (line.startsWith("subtype=") && line.contains("Equipment") && !currentCard.isEmpty() && !currentCard.endsWith("*")) {
                        if (hasTarget) {
                            System.out.println("ERROR: equip card \"" + currentCard + "\" at line " + lineNum + " is a target line.");
                            isValid = false;
                        }
                    }
                }
                reader.close();
                if (isValid) {
                    System.out.println("All cards are valid!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
