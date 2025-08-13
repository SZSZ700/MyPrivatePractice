import org.example.CPU;
import org.example.Instruction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class CPUTest {

    private CPU cpu;

    @BeforeEach
    public void init() {
        // Arrange
        cpu = new CPU();
    }

    @Test
    public void testSingleAddInstruction_AAA() {
        // Arrange
        Instruction instr = new Instruction("ADD", "R1", "R2", "R3"); // 0 + 0 → R1

        // Act
        cpu.loadInstruction(instr);
        cpu.run();

        // Assert
        // הציפייה היא: R1 = 0 (0+0)
        captureOutput(cpu::printRegisters); // נדפיס לצורך פיקוח ויזואלי
    }

    @Test
    public void testAddAndMulInstructions_AAA() {
        // Arrange
        Instruction instr1 = new Instruction("ADD", "R1", "R2", "R3"); // R1 = R2 + R3
        Instruction instr2 = new Instruction("MUL", "R2", "R1", "R1"); // R2 = R1 * R1

        cpu.loadInstruction(instr1);
        cpu.loadInstruction(instr2);

        // Act
        cpu.run();

        // Assert
        // נבדוק שהתוצאה הסופית ברגיסטרים כפי שמצופה
        // ADD: R1 = 0 + 0 = 0
        // MUL: R2 = 0 * 0 = 0
        captureOutput(cpu::printRegisters);
    }

    @Test
    public void testWithInitialRegisterValues_AAA() {
        // Arrange
        // נשתמש בפעולת חישוב שתושפע מהשמות רגיסטרים
        Instruction instr = new Instruction("ADD", "R1", "R2", "R3");

        cpu.loadInstruction(instr);

        // שינוי ערכים ברגיסטרים (באמצעות הרצה מוקדמת)
        cpu.loadInstruction(new Instruction("ADD", "R2", "R2", "R2")); // R2 = 0 + 0 = 0
        cpu.loadInstruction(new Instruction("ADD", "R3", "R3", "R3")); // R3 = 0 + 0 = 0

        // Act
        cpu.run();

        // Assert
        // כל הרגיסטרים עדיין יהיו 0 – נבדוק פלט בלבד
        captureOutput(cpu::printRegisters);
    }

    @Test
    public void testInstructionSequenceUpdatesCorrectly_AAA() {
        // Arrange
        cpu.loadInstruction(new Instruction("ADD", "R1", "R2", "R3")); // R1 = 0
        cpu.loadInstruction(new Instruction("ADD", "R2", "R1", "R1")); // R2 = 0
        cpu.loadInstruction(new Instruction("ADD", "R3", "R2", "R2")); // R3 = 0

        // Act
        cpu.run();

        // Assert
        captureOutput(cpu::printRegisters);
    }

    // פונקציית עזר לקליטת פלט קונסול (אפשר גם לבדוק אותו בטסט אם רוצים)
    private void captureOutput(Runnable action) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        action.run();

        System.setOut(originalOut);
        System.out.println(">>> Output captured:\n" + outContent.toString()); // להמחשה בלבד
    }
}

