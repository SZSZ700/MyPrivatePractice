import org.example.Node;
import org.example.NodeWrapper;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class NodeWrapperTest {

    private Node<Integer> headNode;
    private NodeWrapper<Integer> wrapper;

    @BeforeEach
    public void init() {
        // Arrange
        headNode = new Node<>(10);
        wrapper = new NodeWrapper<>(headNode);
    }

    @AfterEach
    public void cleanup() {
        wrapper = null;
        headNode = null;
    }

    @Test
    public void testConstructorAndGetHead_AAA() {
        // Arrange done in init()

        // Act
        Node<Integer> result = wrapper.getHead();

        // Assert
        assertEquals(headNode, result);
        assertEquals(10, result.getValue());
    }

    @Test
    public void testSetHead_AAA() {
        // Arrange
        Node<Integer> newHead = new Node<>(99);

        // Act
        wrapper.setHead(newHead);

        // Assert
        assertEquals(newHead, wrapper.getHead());
        assertEquals(99, wrapper.getHead().getValue());
    }

    @Test
    public void testSetHeadToNull_AAA() {
        // Arrange
        wrapper.setHead(null);

        // Act
        Node<Integer> result = wrapper.getHead();

        // Assert
        assertNull(result);
    }
}

