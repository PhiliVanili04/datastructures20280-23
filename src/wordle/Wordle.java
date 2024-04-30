package wordle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Wordle {

    String fileName = "wordle/resources/dictionary.txt";
    //String fileName = "wordle/resources/extended-dictionary.txt";
    static List<String> dictionary = null;
    final int num_guesses = 5;
    final long seed = 42;
    //Random rand = new Random(seed);
    Random rand = new Random();
    public String Guess;
    //HashMap<Character, Integer> characterFreq = new HashMap<>();
    HashMap<String, Integer> possibleWords = new HashMap<>();
    HashMap<Character, Integer> valuemap = new HashMap<>();


    static final String winMessage = "CONGRATULATIONS! YOU WON! :)";
    static final String lostMessage = "YOU LOST :( THE WORD CHOSEN BY THE GAME IS: ";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_GREY_BACKGROUND = "\u001B[100m";

    Wordle() {

        this.dictionary = readDictionary(fileName);
        System.out.println("dict length: " + this.dictionary.size());
        System.out.println("dict: " + dictionary);

    }

    public static void main(String[] args) {
        Wordle game = new Wordle();

        String target = game.getRandomTargetWord();
        //String target = "abbey";
        // Print the frequency of each character in the dictionary

        //System.out.println("target: " + target);
        game.Guess = "keeps";
        game.wordleSolver(target);



        //game.play(target);

    }

    public void wordleSolver(String target)
       {
          // getCharacterFrequencies();//get character freq
           getPossibleWords();//get possible words
           if (!processGuesses(target))//process the guesses
           {
               lost(target);//if all the guesses are used and the target is not found you lose
           }
       }
    //find value of a word based on sum of freq
    private int wordValue(String word) {
        int sum = 0;
        for (char c : word.toCharArray()) {
            if (valuemap.containsKey(c)) {
                sum += valuemap.get(c);
            }
        }
        return sum;
    }


    private void getPossibleWords()
       {
           for (String word : dictionary) //iterate over each word
           {
               possibleWords.put(word, wordValue(word));//find the value of the words based on the freq and store it in the map
           }
       }

       private boolean processGuesses(String target)
       {
           String guess = Guess;//first guess
           int guessNo = 0;

           for (int i = 0; i < num_guesses; i++) {//iterate 5 times
               if (Objects.equals(guess, target)) //check if the current guess matches the target word
               {
                   win(target);
                   return true;
               }
               String[] hint = getHint(guess, target);//get hints
               System.out.println("Guess " + guessNo++ +" "+ guess);
               System.out.println("Hint: " + Arrays.toString(hint));

               filterPossibleWords(guess, hint);//filter words based on hint
               if (possibleWords.isEmpty())//check for words left in map
               {
                   System.out.println("No possible words left.");
                   break;
               }
               guess = bestGuess();// find best guess
               if (guess == null)//if there isnt one break out the loop
               {
                   System.out.println("Couldn't make the next guess");
                   break;
               }
               if (Arrays.equals(hint, new String[]{"+", "+", "+", "+", "+"}))//if the hints are all + you win
               {
                   win(target);
                   return true;
               }
           }
           return false;
       }


    //select best word to guess based on highest score
    private String bestGuess()
    {
        String bestGuess = "";//intiliase empty string
        Integer maxValue = null;//
        for (Map.Entry<String, Integer> entry : possibleWords.entrySet())//iterate over each entry
        {
            if (maxValue == null || entry.getValue() > maxValue)//check if the current entry is greater than current maxVlaue
            {
                maxValue = entry.getValue();
                bestGuess = entry.getKey();
            }
        }

        return bestGuess;
    }
    private void filterPossibleWords(String guess, String[] hint)
    {//remove if words do not match the hints
        possibleWords.entrySet().removeIf(entry -> !matchHint(entry.getKey(), guess, hint));
    }

    private boolean matchHint(String word, String guess, String[] hints) {
        //Loop over each character in the word
           for(int i = 0 ; i < word.length(); i++)
        {
            char Char = word.charAt(i);//char from the word
            char charGuess = guess.charAt(i);//char from the guess
            switch (hints[i])
            {
                case "_"://does not exist
                    if (Char == charGuess){
                        return false;
                    }
                    break;

                case "o"://exists but in wrong place
                    if (Char == charGuess || !word.contains(String.valueOf(charGuess))){
                        return false;

                    }
                    break;
                case "+"://right place right letter
                    if (Char != charGuess)
                    {
                        return false;

                    }
                    break;

            }
        }
        return true;
    }
    public String[] getHint(String guess, String target) {
        String[] hint = new String[] {"_", "_", "_", "_", "_"};

        Map<Character, Integer> unmatched = new HashMap<>();//stores the unmatched character and ther frequenceies

        //check for exact matches
        for (int i = 0; i < target.length(); i++) {
            char guessChar = guess.charAt(i);
            char targetChar = target.charAt(i);

            if (guessChar == targetChar) {
                //correct character in the correct position.
                hint[i] = "+";
            } else {
                //populate the frequency map for target characters not yet matched exactly.
                if (unmatched.containsKey(targetChar)) {
                    unmatched.put(targetChar, unmatched.get(targetChar) + 1);
                } else {
                    unmatched.put(targetChar, 1);
                }

            }
        }
        //check for correct characters in wrong positions.
        for (int i = 0; i < guess.length(); i++) {
            char guessChar = guess.charAt(i);

            if (hint[i].equals("_") && unmatched.containsKey(guessChar))
            {
                hint[i] = "o";//correct character in the wrong position.

                unmatched.put(guessChar, unmatched.get(guessChar) - 1);//update the frequency map
            }
        }
        return hint;
    }




    public List<String> readDictionary(String fileName) {
        List<String> wordList = new ArrayList<>();

        try {
            //read the dictionary file
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            assert in != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String strLine;

            //Read file line By line
            while ((strLine = reader.readLine()) != null) {
                wordList.add(strLine.toLowerCase());
            }
            //Close the input stream
            in.close();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return wordList;
    }

    public void lost(String target) {
        System.out.println();
        System.out.println(lostMessage + target.toUpperCase() + ".");
        System.out.println();

    }
    public void win(String target) {
        System.out.println(ANSI_GREEN_BACKGROUND + target.toUpperCase() + ANSI_RESET);
        System.out.println();
        System.out.println(winMessage);
        System.out.println();
    }


    public void play(String target) {
        // TODO
        // TODO: You have to fill in the code
        for(int i = 0; i < num_guesses; ++i) {
            String guess = getGuess();

            if(Objects.equals(guess, target)) { // you won!
                win(target);
                return;
            }
            // the hint is a string where green="+", yellow="o", grey="_"
            // didn't win ;(
            String [] hint = {"_", "_", "_", "_", "_"};
           boolean[] accountedFor = new boolean[target.length()];

            for (int k = 0; k < 5; k++) {
                // TODO:
                if(guess.charAt(k) == target.charAt(k))
                {
                    hint[k] = "+";
                    accountedFor[k] = true;
                }
            }
            // set the arrays for yellow (present but not in right place), grey (not present)
            // loop over each entry:
            //  if hint == "+" (green) skip it
            //  else check if the letter is present in the target word. If yes, set to "o" (yellow)
            for (int k = 0; k < 5; k++) {
                // TODO:
                if (!hint[k].equals("+")) {
                    for (int j = 0; j < target.length(); j++) {
                        if (!accountedFor[j] && guess.charAt(k) == target.charAt(j)) {
                            hint[k] = "o"; // Yellow
                            accountedFor[j] = true;
                            break;
                        }
                    }
                }
            }
            // after setting the yellow and green positions, the remaining hint positions must be "not present" or "_"
            System.out.println("hint: " + Arrays.toString(hint));
            // check for a win
            int num_green = 0;
            for(int k = 0; k < 5; ++k) {
                if(hint[k] == "+") num_green += 1;
            }
            if(num_green == 5) {
                 win(target);
                 return;
            }

        }
        lost(target);
    }






    public String getGuess() {

        Scanner myScanner = new Scanner(System.in, StandardCharsets.UTF_8.displayName());
        System.out.println("Guess:");

        String userWord = myScanner.nextLine();  // Read user input
        userWord = userWord.toLowerCase(); // covert to lowercase

        // check the length of the word and if it exists
        while ((userWord.length() != 5) || !(dictionary.contains(userWord))) {
            if ((userWord.length() != 5)) {
                System.out.println("The word " + userWord + " does not have 5 letters.");
            } else {
                System.out.println("The word " + userWord + " is not in the word list.");
            }
            // Ask for a new word
            System.out.println("Please enter a new 5-letter word.");
            userWord = myScanner.nextLine();
        }
        return userWord;
    }

    public String getRandomTargetWord() {
        // generate random values from 0 to dictionary size
        return dictionary.get(rand.nextInt(dictionary.size()));
    }




}
