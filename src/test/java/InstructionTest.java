import org.example.Instruction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class InstructionTest {

    private Instruction instr;

    @BeforeEach
    public void init() {
        // Arrange
        instr = new Instruction("ADD", "R1", "R2", "R3");
    }

    @Test
    public void testConstructorAndGetters_AAA() {
        // Act
        String opcode = instr.getOpcode();
        String dest = instr.getDestination();
        String op1 = instr.getOperand1();
        String op2 = instr.getOperand2();

        // Assert
        assertEquals("ADD", opcode);
        assertEquals("R1", dest);
        assertEquals("R2", op1);
        assertEquals("R3", op2);
    }

    @Test
    public void testSetters_AAA() {
        // Act
        instr.setOpcode("SUB");
        instr.setDestination("R4");
        instr.setOperand1("R5");
        instr.setOperand2("R6");

        // Assert
        assertEquals("SUB", instr.getOpcode());
        assertEquals("R4", instr.getDestination());
        assertEquals("R5", instr.getOperand1());
        assertEquals("R6", instr.getOperand2());
    }

    @Test
    public void testToString_AAA() {
        // Act
        String output = instr.toString();

        // Assert
        assertTrue(output.contains("opcode='ADD'"));
        assertTrue(output.contains("destination='R1'"));
        assertTrue(output.contains("operand1='R2'"));
        assertTrue(output.contains("operand2='R3'"));
    }
}

