package utilities;

import java.io.*;

public class FileRead {
    public static void main(String[] args) {
        try {
            File file = new File("C:\\Users\\Eduardo_\\Documents\\WagicWindows-0231\\Res\\sets\\primitives\\one.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("name=")) {
                    System.out.println(line.substring(5));
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}