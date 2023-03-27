package tech.havier.stringBlockDelegate;

import java.io.FileWriter;
import java.io.IOException;

public class TxtFileAppender {
    public static void append(String appendedString) {
        try{
            FileWriter fileWriter = new FileWriter("RawString.txt", true);
            fileWriter.write(appendedString);
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
