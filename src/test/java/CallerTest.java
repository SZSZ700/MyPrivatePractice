import org.example.Caller;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CallerTest {

    private Caller caller;

    @BeforeEach
    public void init() {
        // Arrange
        caller = new Caller("Alice", "050-1234567");
    }

    @AfterEach
    public void cleanup() {
        caller = null;
    }

    @Test
    public void testConstructor_AAA() {
        // Arrange done in init()

        // Act
        String name = caller.getName();
        String phone = caller.getPhoneNumber();

        // Assert
        assertEquals("Alice", name);
        assertEquals("050-1234567", phone);
    }

    @Test
    public void testSetName_AAA() {
        // Arrange
        caller.setName("Bob");

        // Act
        String name = caller.getName();

        // Assert
        assertEquals("Bob", name);
    }

    @Test
    public void testSetPhoneNumber_AAA() {
        // Arrange
        caller.setPhoneNumber("052-9998888");

        // Act
        String phone = caller.getPhoneNumber();

        // Assert
        assertEquals("052-9998888", phone);
    }

    @Test
    public void testToString_AAA() {
        // Arrange (already done)

        // Act
        String output = caller.toString();

        // Assert
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("050-1234567"));
        assertTrue(output.contains("Caller"));
    }

    @Test
    public void testToString_ChangedValues_AAA() {
        // Arrange
        caller.setName("Charlie");
        caller.setPhoneNumber("053-1112222");

        // Act
        String result = caller.toString();

        // Assert
        assertTrue(result.contains("Charlie"));
        assertTrue(result.contains("053-1112222"));
    }
}

