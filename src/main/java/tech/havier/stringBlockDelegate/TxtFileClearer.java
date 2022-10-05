package tech.havier.stringBlockDelegate;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TxtFileClearer {


    public static void txtFileClearer() {
        try {
            FileWriter fw = new FileWriter("rawString.txt", false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
