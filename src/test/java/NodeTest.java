import org.example.Node;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.stream.Stream;

public class NodeTest {

    private Node<Integer> node;

    @BeforeEach
    public void init() {
        // Arrange
        node = new Node<>(10);
    }

    @AfterEach
    public void cleanup() {
        // Arrange
        node = null;
    }

    @Test
    public void testSingleConstructor_AAA() {
        // Arrange (כבר נעשה ב־init)

        // Act – אין פעולה מיוחדת

        // Assert
        assertEquals(10, node.getValue());
        assertNull(node.getNext());
    }

    @Test
    public void testConstructorWithNext_AAA() {
        // Arrange
        Node<Integer> nextNode = new Node<>(20);

        // Act
        Node<Integer> current = new Node<>(15, nextNode);

        // Assert
        assertEquals(15, current.getValue());
        assertEquals(nextNode, current.getNext());
    }

    @Test
    public void testHasNextTrue_AAA() {
        // Arrange
        Node<Integer> next = new Node<>(99);
        node.setNext(next);

        // Act
        boolean result = node.hasNext();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasNextFalse_AAA() {
        // Arrange (אין next)

        // Act
        boolean result = node.hasNext();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testToString_AAA() {
        // Arrange (node כבר קיים)

        // Act
        String result = node.toString();

        // Assert
        assertTrue(result.contains("value=10"));
    }

    @Test
    public void testSetValue_AAA() {
        // Arrange
        Node<String> n = new Node<>("start");

        // Act
        n.setValue("new");

        // Assert
        assertEquals("new", n.getValue());
    }

    @Test
    public void testSetNext_AAA() {
        // Arrange
        Node<Integer> n = new Node<>(1);
        Node<Integer> next = new Node<>(2);

        // Act
        n.setNext(next);

        // Assert
        assertEquals(next, n.getNext());
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "B", "Test", "Node"})
    public void testStringNodeValue_AAA(String value) {
        // Arrange + Act
        Node<String> n = new Node<>(value);

        // Assert
        assertEquals(value, n.getValue());
    }

    static Stream<org.junit.jupiter.params.provider.Arguments> nodeData() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(5, 6),
                org.junit.jupiter.params.provider.Arguments.of(100, 200),
                org.junit.jupiter.params.provider.Arguments.of(-1, -99)
        );
    }

    @ParameterizedTest
    @MethodSource("nodeData")
    public void testChainedNodes_AAA(int val1, int val2) {
        // Arrange
        Node<Integer> second = new Node<>(val2);

        // Act
        Node<Integer> first = new Node<>(val1, second);

        // Assert
        assertEquals(val1, first.getValue());
        assertEquals(val2, first.getNext().getValue());
        assertTrue(first.hasNext());
    }
}


