package org.example;

public class Instruction {
    private String opcode;//קובע את סוג הפעולה (למשל, חיבור, חיסור, קפיצה וכו').
    private String operand1, operand2, destination;

    public Instruction(String opcode, String destination, String operand1, String operand2) {
        this.opcode = opcode;
        this.destination = destination;
        this.operand1 = operand1;
        this.operand2 = operand2;
    }

    public String getOpcode() {
        return opcode;
    }

    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    public String getOperand1() {
        return operand1;
    }

    public void setOperand1(String operand1) {
        this.operand1 = operand1;
    }

    public String getOperand2() {
        return operand2;
    }

    public void setOperand2(String operand2) {
        this.operand2 = operand2;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "Instruction{" +
                "opcode='" + opcode + '\'' +
                ", operand1='" + operand1 + '\'' +
                ", operand2='" + operand2 + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
