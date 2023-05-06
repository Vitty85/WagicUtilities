package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class ParenthesesChecker {

    public static void main(String[] args) throws IOException {
        String folderName = "C:\\Users\\Eduardo_\\Documents\\WagicWindows-0231\\Res\\sets\\primitives"; // replace with the name of your folder
        File folder = new File(folderName);
        for (File file : folder.listFiles()) {
            String fileName = file.getAbsolutePath();

            System.out.println("Processing file: " + file.getName());

            try ( BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                int lineNumber = 0;
                Stack<Integer> parenStack = new Stack<>();
                Stack<Integer> bracketStack = new Stack<>();
                Stack<Integer> curlyStack = new Stack<>();

                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    if (line.startsWith("text=")) {
                        // Ignore this line, it starts with the prefix "text="
                        continue;
                    }
                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);
                        switch (c) {
                            case '(':
                                if (line.contains("this(cantargetcard(")) {
                                    // Ignore this line, it contains "cantargetcard("
                                    //System.out.println("cantargetcard line " + lineNumber + ", position " + (i + 1));
                                    continue;
                                }
                                parenStack.push(lineNumber);
                                break;
                            case ')':
                                if (line.contains("this(cantargetcard(")) {
                                    // Ignore this line, it contains "cantargetcard("
                                    continue;
                                }
                                if (parenStack.isEmpty()) {
                                    System.out.println("Error: no opening parenthesis found for closing parenthesis at line " + lineNumber + ", position " + (i + 1));
                                } else {
                                    parenStack.pop();
                                }
                                break;
                            case '[':
                                bracketStack.push(lineNumber);
                                break;
                            case ']':
                                if (bracketStack.isEmpty()) {
                                    System.out.println("Error: no opening bracket found for closing bracket at line " + lineNumber + ", position " + (i + 1));
                                } else {
                                    bracketStack.pop();
                                }
                                break;
                            case '{':
                                curlyStack.push(lineNumber);
                                break;
                            case '}':
                                if (curlyStack.isEmpty()) {
                                    System.out.println("Error: no opening curly bracket found for closing curly bracket at line " + lineNumber + ", position " + (i + 1));
                                } else {
                                    curlyStack.pop();
                                }
                                break;

                            default:
                                break;
                        }
                    }
                }

                if (!parenStack.isEmpty()) {
                    int lastLineNumber = parenStack.pop();
                    System.out.println("Error: opening parenthesis at line " + lastLineNumber + " has no matching closing parenthesis");
                }
                if (!bracketStack.isEmpty()) {
                    int lastLineNumber = bracketStack.pop();
                    System.out.println("Error: opening bracket at line " + lastLineNumber + " has no matching closing bracket");
                }
                if (!curlyStack.isEmpty()) {
                    int lastLineNumber = curlyStack.pop();
                    System.out.println("Error: opening curly bracket at line " + lastLineNumber + " has no matching closing curly bracket");
                }
            }
        }
    }
}
