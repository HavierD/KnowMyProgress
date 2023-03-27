package tech.havier;

import tech.havier.stringBlockDelegate.StringBlockImporter;
import tech.havier.stringBlockDelegate.TxtFileClearer;
import tech.havier.stringBlockOperator.StringBlockOperator;
import tech.havier.stringBlockOperator.StringBlockOperatorFactory;
import tech.havier.timeToolkit.HavierTimer;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Welcome to Know Your Progress!");
        System.out.println("Author: Havier.");
        showMenu1();
    }

    private static void showMenu1() throws Exception {
        System.out.println("-------------------");
        System.out.println("MAIN MENU: ");
        System.out.println("Please choose: ");
        System.out.println("1. Retrieve words from rawString.txt.");
        System.out.println("2. Words export into rawString.txt");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        switch (userInput) {
            case "1":
                retrieveWords();
                break;
            case "2":
                exportWords();
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid Input!");
                System.out.println("------------------------");
                showMenu1();
        }
    }

    private static void exportWords() throws Exception {
        File file = new File("rawString.txt");
        Scanner scanner = new Scanner(file);
        if (scanner.hasNextLine()) {
            System.out.println("please import the rawString.txt first. This .txt is not empty.");
            showMenu1();
            return;
        }
        Dictionaries dictionaries = new Dictionaries();
        List<String> words = dictionaries.getWordDictionaryByRepetitions();
        try (FileWriter writer = new FileWriter("rawString.txt")) {
            StringBuilder builder = new StringBuilder();
            words.forEach((word) -> {
                builder.append(" ");
                builder.append(word);
            });
            writer.write(builder.toString());
            System.out.println("Export words successfully!");
        }catch (Exception e){
            System.out.println("cannot create file writer!!");
        }
        showMenu1();
    }

    private static void retrieveWords() throws Exception {

        HavierTimer timer = new HavierTimer();
        timer.start(5);
        System.out.println("Importing ... ");
        System.out.println("Time may be long. Please be patient... ");
        StringBlockOperator stringBlockOperator = new StringBlockOperatorFactory().createStringBlockOperator(new StringBlockImporter());
        System.out.println("Imported successfully!");
        System.out.println();
        timer.cancel();

        showMenu2(stringBlockOperator);
    }

    private static void showMenu2(StringBlockOperator stringBlockOperator) throws Exception {
        System.out.println("-------------------");
        System.out.println("WORDS OPERATION MENU:");
        Scanner scanner = new Scanner(System.in);
        int total = stringBlockOperator.needCheckWordsNumber() + stringBlockOperator.savedWordsNumber() + stringBlockOperator.unknownWordsNumber();
        if (total == 0) {
            System.out.println("No words needed to operation!");
            clearTxtConfirmation(scanner);
            System.out.println("Enter anything back to main menu.");
            scanner.nextLine();
            showMenu1();

        }

        System.out.println("Found words: " + stringBlockOperator.savedWordsNumber());
        System.out.println("Found converted words: " + stringBlockOperator.needCheckWordsNumber());
        System.out.println("Unknown words: " + stringBlockOperator.unknownWordsNumber());
        System.out.println();
        System.out.println("Please choose next step:");
        System.out.println("1. Read found word list.");
        System.out.println("2. Check converted word list.");
        System.out.println("3. Operate unknown words.");

        String option = scanner.nextLine();

        switch (option) {
            case "1":
                stringBlockOperator.printSavedWords();
                System.out.println("Upload all found words? Y/N ");
                String input = scanner.nextLine().toLowerCase();
                String yn = input.replaceAll(" ", "");
                if (yn.equals("")) {
                    System.out.println("Uploading ...");
                    stringBlockOperator.uploadFoundWords();
                    showMenu2(stringBlockOperator);
                } else if (yn.equals("n")) {
                    showMenu2(stringBlockOperator);
                } else {
                    System.out.println("invalid input!!!!!!");
                    showMenu2(stringBlockOperator);
                }
                break;
            case "2":
                stringBlockOperator.printConvertedWords();
                System.out.println("Do you want to confirm all the transitions? y/ or enter word(s) to correct");
                String transConfirm = scanner.nextLine();
                if (transConfirm.equals("")) {
                    System.out.println("uploading... ");
                    stringBlockOperator.uploadCurrentAasBDictionary();
                } else {
                    String[] correctingWords = transConfirm.split(" ");
                    stringBlockOperator.checkConvertedWords(scanner, correctingWords);
                }
                showMenu2(stringBlockOperator);
                break;
            case "3":
                stringBlockOperator.printUnknownWords();
                System.out.println("Enter anything start to operate");
                scanner.nextLine();
                stringBlockOperator.checkUnknownWords(scanner);
                showMenu2(stringBlockOperator);
                break;
            default:
                System.out.println("Invalid Input!");
                System.out.println("------------------------");
                showMenu2(stringBlockOperator);
        }
    }

    private static void clearTxtConfirmation(Scanner scanner) {
        System.out.println("do you want to clear importing txt file? y/n");
        String yn = scanner.nextLine().toLowerCase().replaceAll(" ", "");
        if (yn.equals("")) {
            System.out.println("clearing...");
            TxtFileClearer.clean();
            System.out.println("importing txt file clear successfully!");
        } else if (yn.equals("n")) {

        } else {
            System.out.println("unknown type in!");
            clearTxtConfirmation(scanner);
        }
    }

    private static int[] parseIndexes(String string) {
        String[] indexes = string.split(" ");
        int[] intIndexes = new int[indexes.length];
        try {
            for (int i = 0; i < indexes.length; i++) {
                intIndexes[i] = Integer.parseInt(indexes[i]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return intIndexes;
    }

}