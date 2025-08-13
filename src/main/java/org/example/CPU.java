package org.example;
import java.util.LinkedList;
import java.util.Queue;

public class CPU {
    private int PC = 0; // Program Counter
    private Queue<Instruction> instructionQueue = new LinkedList<>();//תור פקודות לביצוע
    private final java.util.Map<String, Integer> registers = new java.util.HashMap<>();//מיפוי השמת תוצאות לרגיסטרים

    public CPU() {
        registers.put("R1", 0);
        registers.put("R2", 0);
        registers.put("R3", 0);
    }

    //מתודה להכנסת שורת פקודה "באסמבלי" לתור שורות פקודה
    public void loadInstruction(Instruction instr) {
        instructionQueue.offer(instr);
    }

    //פונקצייה לביצוע שורות הפקודה(מהתור פקודות)
    public void run() {
        while (!instructionQueue.isEmpty()) {
            Instruction instr = fetch();
            //אם המשימה לא ריקה ביצוע המשימה
            if (instr != null) {
                execute(instr);
            }
        }
    }

    //פונקציה פרטית לשליפת פקודה מתור
    private Instruction fetch() {
        System.out.println("Fetching instruction at PC=" + PC);
        PC++;//קידום מונה תוכנית
        return instructionQueue.poll();
    }

    //פונקציה לביצוע פקודה שנשלפה מתור
    private void execute(Instruction instr) {
        //הדפסה תיאור הפקודה
        System.out.println("Decoding: " + instr.getOpcode() + " " + instr.getDestination() + ", " + instr.getOperand1() + ", " + instr.getOperand2());
        //קבלת ערכים ממפת הרגסטרים
        int op1 = registers.getOrDefault(instr.getOperand1(), 0);
        int op2 = registers.getOrDefault(instr.getOperand2(), 0);
        //קריאה למתודת סטטית ממחלקת ALU לביצוע פעולה חשבונית
        int result = ALU.execute(instr.getOpcode(), op1, op2);
        //השמת התוצאה ברגיסטר המתאים
        registers.put(instr.getDestination(), result);
        System.out.println("Executed: " + instr.getOpcode() + " -> " + instr.getDestination() + " = " + result);
    }

    //הדפסת מפת הרגיסטרים
    public void printRegisters() {
        System.out.println("Registers: " + registers);
    }
}
