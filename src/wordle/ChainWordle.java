package wordle;

import project20280.hashtable.ChainHashMap;
import project20280.interfaces.Entry;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ChainWordle {
    

        String fileName = "wordle/resources/dictionary.txt";
        //String fileName = "wordle/resources/extended-dictionary.txt";
        static List<String> dictionary = null;
        final int num_guesses = 5;
        final long seed = 42;
        //Random rand = new Random(seed);
        Random rand = new Random();
        public String Guess;
        ChainHashMap<Character, Integer> characterFreq = new ChainHashMap<>();
        ChainHashMap<String, Integer> possibleWords = new ChainHashMap<>();


        static final String winMessage = "CONGRATULATIONS! YOU WON! :)";
        static final String lostMessage = "YOU LOST :( THE WORD CHOSEN BY THE GAME IS: ";
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
        public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
        public static final String ANSI_GREY_BACKGROUND = "\u001B[100m";

        ChainWordle() {

            this.dictionary = readDictionary(fileName);
            System.out.println("dict length: " + this.dictionary.size());
            System.out.println("dict: " + dictionary);

        }

        public static void main(String[] args) {
            ChainWordle chaingame = new ChainWordle();

            String target = chaingame.getRandomTargetWord();
            //String target = "cigar";
            // Print the frequency of each character in the dictionary

            //System.out.println("target: " + target);
            chaingame.Guess = "blush";
            chaingame.wordleSolver(target);



            //chaingame.play(target);

        }

        public void wordleSolver(String target)
        {
            getPossibleWords();//get possible words
            if (!processGuesses(target))//process the guesses
            {
                lost(target);//if all the guesses are used and the target is not found you lose
            }
            System.out.println("Collisions: " + possibleWords.getCollisions());
        }



    private void getPossibleWords() {
        for (String word : dictionary) { // Iterate over each word in the dictionary
            possibleWords.put(word, wordValue(word, characterFreq)); // Store the word and its calculated value in the map
        }
    }




    private boolean processGuesses(String target)
    {
        String guess = Guess;//first guess
        int guessNo = 0;
        System.out.println("loadfactor :" + possibleWords.loadfactor());
        for (int i = 0; i < num_guesses; i++) {//iterate 5 times
            if (Objects.equals(guess, target)) //check if the current guess matches the target word
            {
                win(target);
                return true;
            }
            String[] hint = getHint(guess, target);//get hints
            System.out.println("Guess " + guessNo++ +" "+ guess);
            System.out.println("Hint: " + Arrays.toString(hint));
            System.out.println("Number of words before filter: " + possibleWords.size());
            filterPossibleWords(possibleWords, guess, hint);//filter words based on hint
            System.out.println("Number of words after filter: " + possibleWords.size());
            if (possibleWords.isEmpty())//check for words left in map
            {
                System.out.println("No possible words left.");
                break;
            }
            guess = bestGuess(possibleWords);//find best guess
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
        private String bestGuess(ChainHashMap<String, Integer> possibleWords) {
            String bestGuess = null;
            Integer maxValue = null;
            for (Entry<String, Integer> entry : possibleWords.entrySet()) {
                if (maxValue == null || entry.getValue() > maxValue) {
                    maxValue = entry.getValue();
                    bestGuess = entry.getKey(); //make sure the guess with the highest score is chosen
                }
            }
            return bestGuess;
        }
//changed filterpossiblewords
    public void filterPossibleWords(ChainHashMap<String, Integer> possibleWords, String guess, String[] hint) {

        //create a new hashmap to store the filtered results
        ChainHashMap<String, Integer> filteredWords = new ChainHashMap<>();
        for (Entry<String, Integer> entry : possibleWords.entrySet()) {
            if (matchHint(entry.getKey(), guess, hint)) {
                filteredWords.put(entry.getKey(), entry.getValue());
            }
        }
        possibleWords.clear();
        possibleWords.putAll(filteredWords);
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

        //changed gethint
    String[] getHint(String target, String guess) {
        String [] hint = {"_", "_", "_", "_", "_"};
        boolean[] usedCharacters = new boolean[target.length()]; //track characters in target that have been used

        //check fo r characters in the correct position
        for (int i = 0; i < target.length(); i++) {
            if (guess.charAt(i) == target.charAt(i)) {
                hint[i] = "+";
                usedCharacters[i] = true;
            }
        }
        //check for character in wrong position
        for (int i = 0; i < target.length(); i++) {
            if (!hint[i].equals("+"))
            {//if guessed correctly skip
                for (int j = 0; j < target.length(); j++) {
                    if (guess.charAt(i) == target.charAt(j) && !usedCharacters[j] && !hint[i].equals("+")) {
                        hint[i] = "o";
                        usedCharacters[j] = true;
                        break;
                    }
                }
            }
        }

        return hint;
    }

    //find value of a word based on sum of freq
        private int wordValue(String word, ChainHashMap<Character, Integer> valueMap)
        {
            int sum = 0;
            for (char c : word.toCharArray())//loop through word
            {
                if (valueMap.containsKey(c))
                {
                    sum += valueMap.get(c);
                }
            }
            return sum;
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
            //generate random values from 0 to dictionary size
            return dictionary.get(rand.nextInt(dictionary.size()));
        }


    }