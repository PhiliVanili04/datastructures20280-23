package project20280.exercises;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import project20280.hashtable.ChainHashMap;
import project20280.interfaces.Entry;

public class WordFrequencyCounter{
    public static void main(String[] args) throws FileNotFoundException
    {
        ChainHashMap<String, Integer> wordFreq = new ChainHashMap<>();//create a hashmap to store word frequencies
        File file = new File("C:\\Users\\phili\\Documents\\GitHub\\datastructures20280-23\\src\\wordle\\resources\\sample_text.txt");//load file
        Scanner scanner = new Scanner(file);//initialize scanner to read from the file

        while (scanner.hasNext()) {
            String word = scanner.next().replaceAll("[^a-zA-Z]", "").toLowerCase();//read the next word and convert to lower case
            if (word.isEmpty())//check if the word is empty
            {
                continue;
            }
            Integer count = wordFreq.get(word);//get the current count of the word from the map
            if (count == null)//check if the word has not been seen before
            {
                wordFreq.put(word, 1);
            }else {
                wordFreq.put(word, count + 1);
            }
        }
        scanner.close();
    //arrays to store the top 10 most frequent words and their counts
    String[] mostFrequentWords = new String[10];
    int[] freq = new int[10];
    for (Entry<String, Integer> entry : wordFreq.entrySet()) {
        int count = entry.getValue();//counter for number of times word appears
        //place the current word in the correct position
        for (int i = 0; i < mostFrequentWords.length; i++) {//loops through top woeds to fin the correct position
            if (freq[i] < count) {//if the word is higher than the count at the top frequency it should be placed at or before the index
                for (int j = mostFrequentWords.length - 1; j > i; j--) {//shift words and counts to make space for the new word
                    mostFrequentWords[j] = mostFrequentWords[j - 1];
                    freq[j] = freq[j - 1];
                }
                //insert the new word and count
                mostFrequentWords[i] = entry.getKey();
                freq[i] = count;
                break;
            }
        }
    }
     System.out.println("Top 10 most frequently used words:");
            for (int i = 0; i < mostFrequentWords.length && mostFrequentWords[i] != null; i++) {
        System.out.println(mostFrequentWords[i] + ": " + freq[i]);
    }
}
    }









