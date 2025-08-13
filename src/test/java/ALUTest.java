import org.example.ALU;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ALUTest {

    @Test
    public void testAdd_AAA() {
        // Arrange
        int a = 5, b = 3;

        // Act
        int result = ALU.execute("ADD", a, b);

        // Assert
        assertEquals(8, result);
    }

    @Test
    public void testSub_AAA() {
        // Arrange
        int a = 10, b = 4;

        // Act
        int result = ALU.execute("SUB", a, b);

        // Assert
        assertEquals(6, result);
    }

    @Test
    public void testMul_AAA() {
        // Arrange
        int a = 7, b = 6;

        // Act
        int result = ALU.execute("MUL", a, b);

        // Assert
        assertEquals(42, result);
    }

    @Test
    public void testDiv_AAA() {
        // Arrange
        int a = 20, b = 4;

        // Act
        int result = ALU.execute("DIV", a, b);

        // Assert
        assertEquals(5, result);
    }

    @Test
    public void testDivByZero_AAA() {
        // Arrange
        int a = 10, b = 0;

        // Act
        int result = ALU.execute("DIV", a, b);

        // Assert
        assertEquals(0, result); // לפי הקוד: מחזיר 0 במקום לזרוק חריגה
    }

    @Test
    public void testInvalidOpcode_AAA() {
        // Arrange
        String invalidOp = "MOD";

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ALU.execute(invalidOp, 1, 1);
        });

        // Optional: Verify the message
        assertTrue(exception.getMessage().contains("Invalid Opcode"));
    }
}

