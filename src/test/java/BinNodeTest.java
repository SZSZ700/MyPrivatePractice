import org.example.BinNode;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BinNodeTest {

    private BinNode<Integer> node;

    @BeforeEach
    public void init() {
        // Arrange
        node = new BinNode<>(10);
    }

    @AfterEach
    public void cleanup() {
        node = null;
    }

    @Test
    public void testSingleConstructor_AAA() {
        // Arrange done in init()

        // Act – אין פעולה

        // Assert
        assertEquals(10, node.getValue());
        assertNull(node.getLeft());
        assertNull(node.getRight());
        assertTrue(node.isLeaf());
    }

    @Test
    public void testFullConstructor_AAA() {
        // Arrange
        BinNode<Integer> left = new BinNode<>(5);
        BinNode<Integer> right = new BinNode<>(15);

        // Act
        BinNode<Integer> root = new BinNode<>(left, 10, right);

        // Assert
        assertEquals(10, root.getValue());
        assertEquals(left, root.getLeft());
        assertEquals(right, root.getRight());
        assertFalse(root.isLeaf());
    }

    @Test
    public void testSetLeft_AAA() {
        // Arrange
        BinNode<Integer> left = new BinNode<>(5);

        // Act
        node.setLeft(left);

        // Assert
        assertEquals(left, node.getLeft());
        assertTrue(node.hasLeft());
    }

    @Test
    public void testSetRight_AAA() {
        // Arrange
        BinNode<Integer> right = new BinNode<>(15);

        // Act
        node.setRight(right);

        // Assert
        assertEquals(right, node.getRight());
        assertTrue(node.hasRight());
    }

    @Test
    public void testSetValue_AAA() {
        // Arrange
        node.setValue(42);

        // Act
        int value = node.getValue();

        // Assert
        assertEquals(42, value);
    }

    @Test
    public void testIsLeaf_True_AAA() {
        // Arrange – no children

        // Act
        boolean result = node.isLeaf();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsLeaf_False_AAA() {
        // Arrange
        node.setLeft(new BinNode<>(5));

        // Act
        boolean result = node.isLeaf();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasLeftAndRight_AAA() {
        // Arrange
        BinNode<Integer> left = new BinNode<>(1);
        BinNode<Integer> right = new BinNode<>(2);
        node.setLeft(left);
        node.setRight(right);

        // Act
        boolean hasLeft = node.hasLeft();
        boolean hasRight = node.hasRight();

        // Assert
        assertTrue(hasLeft);
        assertTrue(hasRight);
    }
}
