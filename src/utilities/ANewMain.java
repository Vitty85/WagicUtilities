package utilities;

import java.io.IOException;

/**
 * @author vitty85
 */
public class ANewMain {
    public static void main(String[] args) throws IOException {
        String folderName = "C:\\Program Files (x86)\\Emulatori\\Sony\\PSVita\\Games\\PSP\\Wagic\\wagic-wagic-v0.23.1_win\\projects\\mtg\\Res\\sets\\primitives";
        
        CardValidation.main(new String[]{folderName});
        FirstBracketsChecker.main(new String[]{folderName});
        Keywords.main(new String[]{folderName});
        ParenthesesChecker.main(new String[]{folderName});
        SyntaxCheck.main(new String[]{folderName});
    }
}
