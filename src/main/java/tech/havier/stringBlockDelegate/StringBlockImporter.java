package tech.havier.stringBlockDelegate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StringBlockImporter {

    private String stringBlock;
    private static boolean databaseNeedRefresh = false;
    private static boolean importAtLeastOnce = false;

    public StringBlockImporter() {
        if (importAtLeastOnce) {
            databaseNeedRefresh = true;
        }
        importAtLeastOnce = true;
        this.stringBlock = importStringBlock();

    }
    public String getStringBlock() {
        return stringBlock;
    }

    public static boolean isDatabaseNeedRefresh() {
        return databaseNeedRefresh;
    }

    private String importStringBlock() {

        StringBuilder stringBuilder = new StringBuilder();
        try{
            File file = new File("rawString.txt");
            Scanner scanner = new Scanner(file);

            while(scanner.hasNextLine()){
                stringBuilder.append(" ");
                stringBuilder.append(scanner.nextLine());

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }


}
