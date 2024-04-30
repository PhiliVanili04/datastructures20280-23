package wordle;
import project20280.tree.LinkedBinaryTree;
import java.util.Map;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;



class HuffmanTest {

    @Test
    public void testCalculateFreq() {
        Huffman huffman = new Huffman();
        Map<Character, Integer> frequency = huffman.calculateFreq("hello");
        assertEquals(1,  frequency.get('e'));
        assertEquals(1, frequency.get('h'));
        assertEquals(2, frequency.get('l'));
        assertEquals(1, frequency.get('o'));
    }

    @Test
    void buildTree() {
        Huffman huffman = new Huffman();
        LinkedBinaryTree tree = huffman.buildTree("hello my name is Philip");
        assertNotNull(tree);
    }

    @Test
    void generateCodes() {
        Huffman huffman = new Huffman();
        LinkedBinaryTree tree = huffman.buildTree("hello");
        Map codes = huffman.generateCodes(tree);
        assertTrue(codes.containsKey('h'));
        assertTrue(codes.containsKey('e'));
        assertTrue(codes.containsKey('l'));
        assertTrue(codes.containsKey('o'));
    }

    @Test
    void encodedecode() {
        Huffman huffman = new Huffman();
        String input = "hello world";
        LinkedBinaryTree tree = huffman.buildTree(input);
        Map codes = huffman.generateCodes(tree);
        String encoded = huffman.encode(input, codes);
        String decoded = huffman.decode(encoded, tree);

        assertEquals(input, decoded);
    }



    @Test
    public void testHints() {
        ChainWordle wordleA = new ChainWordle();
        String target, guess;
        String[] hints;

        target = "abbey";
        guess = "keeps";
        hints = wordleA.getHint(target, guess);
        assertEquals("[_, o, _, _, _]", Arrays.toString(hints));

        target = "abbey";
        guess = "kebab";
        hints = wordleA.getHint(target, guess);
        assertEquals("[_, o, +, o, o]", Arrays.toString(hints));

        target = "abbey";
        guess = "babes";
        hints = wordleA.getHint(target, guess);
        assertEquals("[o, o, +, +, _]", Arrays.toString(hints));

        target = "lobby";
        guess = "table";
        hints = wordleA.getHint(target, guess);
        assertEquals("[_, _, +, o, _]", Arrays.toString(hints));

        target = "ghost";
        guess = "pious";
        hints = wordleA.getHint(target, guess);
        assertEquals("[_, _, +, _, o]", Arrays.toString(hints));

        target = "ghost";
        guess = "slosh";
        hints = wordleA.getHint(target, guess);
        assertEquals("[_, _, +, +, o]", Arrays.toString(hints));


        target = "kayak";
        guess = "aorta";
        hints = wordleA.getHint(target, guess);
        assertEquals("[o, _, _, _, o]", Arrays.toString(hints));

        target = "kayak";
        guess = "kayak";
        hints = wordleA.getHint(target, guess);
        System.out.println(target + ", " + guess + ", " + Arrays.toString(hints));
        assertEquals("[+, +, +, +, +]", Arrays.toString(hints));

        target = "kayak";
        guess = "fungi";
        hints = wordleA.getHint(target, guess);
        System.out.println(target + ", " + guess + ", " + Arrays.toString(hints));
        assertEquals("[_, _, _, _, _]", Arrays.toString(hints));


    }
}




