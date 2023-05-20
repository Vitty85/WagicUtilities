package utilities;

/**
 *
 * @author Eduardo_
 */
import java.io.*;

public class SyntaxCheck {

    public static void main(String[] args) {
        // Replace the path below with the path to your folder
        String folderName = args[0];
        File folder = new File(folderName);
        for (File file : folder.listFiles()) {

            if (file.isFile()) {
                try {
                    // Open the file for reading
                    BufferedReader br = new BufferedReader(new FileReader(file));

                    String line;
                    int lineNumber = 1;
                    while ((line = br.readLine()) != null) {
                        if (line.startsWith("text=")) {
                            // Ignore this line, it starts with the prefix "text="
                            lineNumber++;
                            continue;
                        }
                        // Check if the line contains a comma between square brackets
                        //if (line.matches(".*\\[[^\\[\\]\\(\\)]*\\,[^\\[\\]\\(\\)]*\\].*")) {
                        if (line.matches("^(?!.*phaseaction\\[[^\\[\\]]*\\,[^\\[\\]]*\\])[^\\[\\]]*\\[[^\\[\\]\\(\\)]*\\,[^\\[\\]\\(\\)]*\\].*$")) {
                            System.out.println("Error in " + file.getName() + " line " + lineNumber + ": " + line);
                        }
                        lineNumber++;
                    }

                    // Close the file
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
