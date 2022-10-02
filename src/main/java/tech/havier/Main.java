package tech.havier;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import tech.havier.stringBlockOperator.StringBlockOperator;
import tech.havier.userInterface.StringBlockImporter;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Know Your Progress!");
        System.out.println("Author: Havier.");
        showMenu1();
    }

    private static void showMenu1() throws IOException {
        System.out.println("Please choose: ");
        System.out.println("1. Retrieve words from .txt.");
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        switch (userInput){
            case "1":
                retrieveWords();
                break;
            case "2":
                break;
            case "3":
                break;
            default:
                System.out.println("Invalid Input!");
                System.out.println("------------------------");
                showMenu1();
        }
    }

    private static void retrieveWords() throws IOException {


        UniqueTimer timer = new UniqueTimer();
        timer.start(5);
        System.out.println("Importing ... ");
        System.out.println("Time may be long. Please be patient... ");
        StringBlockOperator stringBlockOperator = new StringBlockOperator(new StringBlockImporter());
        System.out.println("Imported successfully!");
        System.out.println("Time used: " + timer.getCurrentTimer());
        System.out.println();
        timer.cancel();
        System.out.println("Found words: " + stringBlockOperator.savedWordsNumber());
        System.out.println("Found converted words: " + stringBlockOperator.needCheckWordsNumber());
        System.out.println("Unknown words: " + stringBlockOperator.unknownWordsNumber());
        System.out.println("-------------------");
        showMenu2(stringBlockOperator);
    }

    private static void showMenu2(StringBlockOperator stringBlockOperator) throws IOException {
        System.out.println("Please choose next step:");
        System.out.println("1. Read found word list.");
        System.out.println("2. Check converted word list.");
        System.out.println("3. Operate unknown words.");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.nextLine();
        switch (option){
            case "1":
                stringBlockOperator.printSavedWords();
                System.out.println("Enter anything back to manu.");
                showMenu2(stringBlockOperator);
                break;
            case "2":
                stringBlockOperator.printConvertedWords();
                System.out.println("Enter anything start to confirm.");
                scanner.nextLine();
                break;
            case "3":
                stringBlockOperator.printUnknownWords();
                System.out.println("Enter anything start to operate");
                scanner.nextLine();
                break;
            default:
                System.out.println("Invalid Input!");
                System.out.println("------------------------");
                showMenu1();
        }
    }




}