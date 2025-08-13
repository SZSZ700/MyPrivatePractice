package org.example;

public class ALU {
    public static int execute(String opcode, int op1, int op2) {
        switch (opcode) {
            case "ADD": return op1 + op2;
            case "SUB": return op1 - op2;
            case "MUL": return op1 * op2;
            case "DIV": return (op2 != 0) ? op1 / op2 : 0;
            default: throw new IllegalArgumentException("Invalid Opcode: " + opcode);
        }
    }
}
