package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class FirstBracketsChecker {

    public static void main(String[] args) throws IOException {
        String folderName = "C:\\Users\\Eduardo_\\Documents\\WagicWindows-0231\\Res\\sets\\primitives"; // replace with the name of your folder
        File folder = new File(folderName);
        for (File file : folder.listFiles()) {
            String fileName = file.getAbsolutePath();

            System.out.println("Processing file: " + file.getName());

            try ( BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                int lineNumber = 0;
                int sequenceCount = 0;
                int andOpenCount = 0;
                Stack<Integer> sequenceStack = new Stack<>();
                Stack<Integer> andOpenStack = new Stack<>();

                while ((line = reader.readLine()) != null) {
                    lineNumber++;

                    if (line.startsWith("text=")) {
                        // Ignore this line, it starts with the prefix "text="
                        continue;
                    }

                    int i = 0;
                    while (i < line.length()) {
                        if (line.charAt(i) == '$') {
                            if (i + 1 < line.length() && line.charAt(i + 1) == '!') {
                                sequenceCount++;
                                sequenceStack.push(lineNumber);
                                i++;
                            } else if (i + 1 < line.length() && line.charAt(i + 1) == '$') {
                                i++; // Skip over the second '$'
                            }
                        } else if (line.charAt(i) == '!') {
                            if (i + 1 < line.length() && line.charAt(i + 1) == '$') {
                                if (sequenceCount > 0) {
                                    sequenceCount--;
                                    sequenceStack.pop();
                                } else {
                                    System.out.println("Error: closing sequence at line " + lineNumber + ", position " + (i + 1) + " has no matching opening sequence");
                                }
                                i++;
                            }
                        } else if (line.startsWith("and!(")) {
                            andOpenCount++;
                            andOpenStack.push(lineNumber);
                            i += 4; // Skip over the "and!(" opening sequence
                        } else if (line.startsWith(")!")) {
                            if (andOpenCount > 0) {
                                andOpenCount--;
                                andOpenStack.pop();
                            } else {
                                System.out.println("Error: closing sequence at line " + lineNumber + ", position " + (i + 1) + " has no matching opening sequence");
                            }
                            i += 1; // Skip over the ")" closing sequence
                        }

                        i++;
                    }
                }

                if (sequenceCount > 0) {
                    System.out.println("Error: opening sequence at line " + sequenceStack.pop() + " has no matching closing sequence");
                }
                if (andOpenCount > 0) {
                    System.out.println("Error: opening sequence at line " + andOpenStack.pop() + " has no matching closing sequence");
                }

            }
        }
    }
}
