import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class AVLTreeTest {
    public static void main(String[] args) {
        var tree = new AVLTree<Integer>();

        tree.insert(0);
        tree.insert(1);
        tree.insert(2);
        tree.print();
    }

    @Test
    public void testInsertLL() {
        var tree = new AVLTree<Integer>();

        tree.insert(0);
        tree.insert(1);
        tree.insert(2);

        assertDoesNotThrow(tree::validate);
    }

    @Test
    public void testInsertRR() {
        var tree = new AVLTree<Integer>();

        tree.insert(2);
        tree.insert(1);
        tree.insert(0);

        assertDoesNotThrow(tree::validate);
    }

    @Test
    public void testInsertLR() {
        var tree = new AVLTree<Integer>();

        tree.insert(2);
        tree.insert(0);
        tree.insert(1);

        assertDoesNotThrow(tree::validate);
    }

    @Test
    public void testInsertRL() {
        var tree = new AVLTree<Integer>();

        tree.insert(0);
        tree.insert(2);
        tree.insert(1);

        assertDoesNotThrow(tree::validate);
    }

    @Test
    public void testInsertRandom() {
        var rng = new Random();

        var tree = new AVLTree<Integer>();

        IntStream.generate(rng::nextInt).limit(1000).forEach(tree::insert);

        assertDoesNotThrow(tree::validate);
    }
}
