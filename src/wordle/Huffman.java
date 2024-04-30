package wordle;

import project20280.interfaces.Entry;
import project20280.interfaces.Position;
import project20280.priorityqueue.HeapPriorityQueue;
import project20280.tree.LinkedBinaryTree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Huffman {


    HeapPriorityQueue<Integer, LinkedBinaryTree> queue = new HeapPriorityQueue<>();

    public LinkedBinaryTree buildTree(String input) {
        Map<Character, Integer> charFreq = calculateFreq(input);//get the freq of each character in the dictionary

        for (Map.Entry<Character, Integer> entry : charFreq.entrySet())//create an initial tree for each character and add it to priority queue
        {
            LinkedBinaryTree<Character> tree = new LinkedBinaryTree<>();
            tree.addRoot(entry.getKey());//each char becomes a root
            queue.insert(entry.getValue(), tree);//insert into the queue based on feq
        }
        while (queue.size() > 1)//combine trees until only one remains
        {
            Entry<Integer, LinkedBinaryTree> T1 = queue.removeMin();//remove tree with least freq
            Entry<Integer, LinkedBinaryTree> T2 = queue.removeMin();//remove the next least freq

            Integer combined = T1.getKey() + T2.getKey();// combine the frequencies of the two trees
            LinkedBinaryTree child1 = T1.getValue();
            LinkedBinaryTree child2 = T2.getValue();

            LinkedBinaryTree<Character> parent = new LinkedBinaryTree<>();//make new parent tree
            parent.addRoot(null);//make the node null
            parent.attach(parent.root(), child1, child2);//attach the smaller trees as children

            queue.insert(combined, parent);//insert the new tree into the queue
        }
        Entry<Integer, LinkedBinaryTree> root = queue.removeMin();
        return root.getValue();
    }


    public Map<Character, Integer> calculateFreq(String input) {
        Map<Character, Integer> frequencyMap = new HashMap<>(); //hashMap to store the frequency of each character
        for (char c : input.toCharArray()) {
            if (frequencyMap.containsKey(c)) {
                frequencyMap.put(c, frequencyMap.get(c) + 1);
            } else {
                frequencyMap.put(c, 1);//if the character is not in the map, add it with count 1
            }
        }
        return frequencyMap;
    }



    public Map<Character, String> generateCodes(LinkedBinaryTree<Character> tree) {// Generates Huffman codes for each character in the tree.
        Map<Character, String> codes = new HashMap<>();
        generateCodesHelper(tree, tree.root(), "", codes);//Start the recursive generation of codes from the root of the Huffman tree .The initial code is an empty string since we have not traversed any path yet
        return codes;
    }

    private void generateCodesHelper(LinkedBinaryTree<Character> tree, Position<Character> node, String code, Map<Character, String> codes) {//recursive helper method to generate huffman codes.
        if (tree.isExternal(node)) {//check if current node is leaf
            codes.put(node.getElement(), code);//it is unique assign code
        } else {//if not a leaf
            generateCodesHelper(tree, tree.left(node), code + "0", codes);//traverse left
            generateCodesHelper(tree, tree.right(node), code + "1", codes);//traverse right
        }
    }

    // Encode a message
    public String encode(String word, Map<Character, String> codes) {
        StringBuilder encoded = new StringBuilder();
        for (char c : word.toCharArray()) {//for each character get huffman code
            encoded.append(codes.get(c));
        }
        return encoded.toString();//return the code as a string
    }

    // Decode a message
    public String decode(String encoded, LinkedBinaryTree<Character> tree) {
        StringBuilder decoded = new StringBuilder();
        Position<Character> node = tree.root();//start at root
        for (char bit : encoded.toCharArray()) {
            //if the current bit is 0, move to the left child of the current node.
            //if the current bit is 1, move to the right child.
            node = bit == '0' ? tree.left(node) : tree.right(node);
            if (tree.isExternal(node)) {//if its a leaf node
                decoded.append(node.getElement());//add to the code
                node = tree.root();//reset
            }
        }
        return decoded.toString();//return as string
    }

    public static void main(String[] args) throws IOException {


        String input = getInput();
        Huffman huffman = new Huffman();
        LinkedBinaryTree tree = huffman.buildTree(input);
        Map<Character, String> codes = huffman.generateCodes(tree);
        String encoded = huffman.encode(input, codes);
        String decoded = huffman.decode(encoded, tree);


        int totalAsciiBits = input.length() * 8;
        int totalHuffmanBits = encoded.length();
        float compressionRatio = ((float) totalHuffmanBits / totalAsciiBits) * 100;


        System.out.println("Encoded: " + encoded);
        System.out.println("Decoded: " + decoded);
        System.out.println("Compression Ratio: " + compressionRatio);
        System.out.println(tree.toBinaryTreeString());
        Map<String, Integer> wordCodeLengths = new HashMap<>();
        for (String word : input.split(" ")) {
            int length = 0;
            for (char c : word.toCharArray()) {
                length += codes.get(c).length();
            }
            wordCodeLengths.put(word, length);
        }

        String longestWord = null;
        String shortestWord = null;
        int longest = Integer.MIN_VALUE;
        int shortest = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : wordCodeLengths.entrySet()) {
            if (entry.getValue() > longest) {
                longest = entry.getValue();
                longestWord = entry.getKey();
            }
            if (entry.getValue() < shortest) {
                shortest = entry.getValue();
                shortestWord = entry.getKey();
            }
        }


        System.out.println("Longest Huffman Code Word: " + longestWord);
        System.out.println("Shortest Huffman Code Word: " + shortestWord);
    }

    private static String getInput() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\phili\\Documents\\GitHub\\datastructures20280-23\\src\\wordle\\resources\\dictionary.txt"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append(" ");
        }
        reader.close();
        return sb.toString().trim();


    }
}





















