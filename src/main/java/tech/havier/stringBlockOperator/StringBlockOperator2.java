package tech.havier.stringBlockOperator;

import tech.havier.Dictionaries;
import tech.havier.PerformanceMonitor;
import tech.havier.databaseOperator.DatabaseFactory;
import tech.havier.databaseOperator.DatabaseOperator;
import tech.havier.stringBlockDelegate.StringBlockImporter;
import tech.havier.timeToolkit.HavierTimer;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * the operator2 is using hibernate operator.
 */
public class StringBlockOperator2 implements StringBlockOperator{

    private List<String> ignoredWords = new ArrayList<>();
    private final List<String> toRemove = new ArrayList<>();
    private final List<String> foundWords = new ArrayList<>();
    private final ConcurrentHashMap<String, String> convertedWords = new ConcurrentHashMap<>();
    private List<String> unknownWords = new ArrayList<>();

    private HavierTimer havierTimer = new HavierTimer();

    private Dictionaries dictionaries;
    private DatabaseOperator databaseOperator;

    private PerformanceMonitor performanceMonitor = new PerformanceMonitor();

    public StringBlockOperator2(StringBlockImporter stringBlockImporter) throws Exception {

        dictionaries = new Dictionaries();
        databaseOperator = new DatabaseFactory().createDatabaseOperator();
        String string = stringBlockImporter.getStringBlock();
        var words = preProcess(string);
        validateWords(dictionaries.transitions(words));
//        finalChecking();
    }

    private void validateWords(List<String> transitionedWords) {
        var temp = dictionaries.getTemporarySelfTransWords();
        foundWords.addAll(temp);
        var process = 0;
        for (String rawWord : transitionedWords) {
            process++;
            if(process % 100 == 0) System.out.println("validating process: " + process + " / " + transitionedWords.size());
            if(dictionaries.doesIgnore(rawWord)) continue;
            if(dictionaries.hasSaved(rawWord)){foundWords.add(rawWord); continue;}
            if(isOnWeb(rawWord)){foundWords.add(rawWord); continue;}
            var convertedCount = Arrays.stream(dictionaries.getSuffixConvertDictionary()).anyMatch(e->doesEndWith(rawWord, e));
            if (convertedCount) continue;
            if(unknownWords.contains(rawWord)) continue;
            unknownWords.add(rawWord);
        }
    }

    /**
     * if true, will save into AasB dictionary automatically inside the method.
     */
    private boolean doesEndWith(String checkedWord, String[] suffixPair) {
        int charCount = suffixPair[0].length();
        if(!checkedWord.endsWith(suffixPair[0])){
            return false;
        }
        String afterWord = checkedWord.substring(0, checkedWord.length() - charCount) + suffixPair[1];
        if (dictionaries.hasSaved(afterWord) || isOnWeb(afterWord)) {
            convertedWordsPut(checkedWord, afterWord);
            return true;
        }
        return false;
    }
    private boolean isOnWeb(String word) {
        String url = "https://www.oxfordlearnersdictionaries.com/definition/english/" + word;
        try {
            (new java.net.URL(url)).openStream().close();
            performanceMonitor.countOneSearch();
            return true;
        } catch (Exception ex) { }
        return false;
    }
    private void convertedWordsPut(String key, String value) {
        if (convertedWords.containsKey(key)) {
            return;
        }
        convertedWords.put(key, value);
    }


    @Override
    public int savedWordsNumber() {
        return foundWords.size();
    }

    @Override
    public int needCheckWordsNumber() {
        return convertedWords.size();
    }

    @Override
    public int unknownWordsNumber() {
        return unknownWords.size();
    }

    @Override
    public void printSavedWords() {
        var index = 0;
        for (String e : foundWords) {
            System.out.println(index + ". " + e);
        }
    }

    @Override
    public void printConvertedWords() {
        int index = 0;
        for (Map.Entry<String, String> e : convertedWords.entrySet()) {
            System.out.println(index + ". " + e.getKey() + " : " + e.getValue());
            index++;
        }
    }

    @Override
    public void printUnknownWords() {
        int index = 0;
        for (String e : unknownWords) {
            System.out.println(index + ". " + e);
            index++;
        }
    }

    @Override
    public void uploadFoundWords() {
        havierTimer.start(0);
        var index = 0;
        databaseOperator.uploadNewWords(foundWords);
        databaseOperator.uploadNewRecords(foundWords);
        havierTimer.cancel("Uploading found words ");
        foundWords.clear();
        System.out.println("All found words upload successfully! Found-word-list has been cleared.");
    }

    @Override
    public void uploadCurrentAasBDictionary() {
        databaseOperator.uploadNewAasBs(convertedWords);
        System.out.println("Current converted words uploaded successfully!");
        convertedWords.clear();
    }

    @Override
    public void checkConvertedWords(Scanner scanner, String[] correctingWords) {
        for (String word : correctingWords) {
            if(!convertedWords.containsKey(word)) throw new RuntimeException("wrong entered word\"" + word + "\"");
            System.out.println("Treat { " + word + " } as? enter correct word OR d for discard");
            String yn = scanner.nextLine().toLowerCase().replaceAll(" ","" );
            if (yn.equals("d")) {
                convertedWords.remove(word);
                continue;
            }
            confirmAnAasBPair(scanner, word, yn);
        }
        System.out.println("All converted words are checked!");
        System.out.println("Enter anything to continue ...");
        scanner.nextLine();
    }
    private void confirmAnAasBPair(Scanner scanner, String word, String yn) {
        System.out.println("Confirm the pair { " + word + ":" + yn + " }? y / other correct word" );
        String confirmPair = scanner.nextLine();
        if (confirmPair.equals("")) {
            convertedWords.replace(word, yn);
            return;
        }
        confirmAnAasBPair(scanner, word, confirmPair);
    }

    @Override
    public void checkUnknownWords(Scanner scanner) throws SQLException, ClassNotFoundException {
        for(String unknownWord : unknownWords){
            System.out.println("Treat {" + unknownWord + "} as?  input word or / d (discard and ignore this word)");
            String input = scanner.nextLine().toLowerCase().replaceAll(" ", "");
            if (input.equals("d")) {
                toRemove.add(unknownWord);
                databaseOperator.uploadNewIgnoredWord(unknownWord);
                continue;
            }
            correctUnknownWord(scanner, unknownWord, input );
        }
        if (unknownWords.size() != toRemove.size()) {
            throw new RuntimeException("Something wrong! cannot clear unknown word list! Size Mismatch!");
        }
        unknownWords.removeAll(toRemove);
        toRemove.clear();
        System.out.println("All unknown words are operated!");
        System.out.println("Enter anything to continue");
        scanner.nextLine();
    }
    private void correctUnknownWord(Scanner scanner, String unknownWord, String input ) throws SQLException, ClassNotFoundException {
        System.out.println("Treat {" + unknownWord + "} as {" + input + "} ? y or / input new word or / d(discard and ignore this word)" );
        String input2 = scanner.nextLine();

        if (input2.equals("d")) {
            toRemove.add(unknownWord);
            databaseOperator.uploadNewIgnoredWord(unknownWord);
            return;
        }
        if ((input2.equals(""))) {
            databaseOperator.uploadNewAasB(unknownWord, input);
            toRemove.add(unknownWord);
            return;
        }
        correctUnknownWord(scanner, unknownWord, input2);
    }
}
