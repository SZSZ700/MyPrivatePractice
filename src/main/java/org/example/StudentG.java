package org.example;

public class StudentG {
    private int studentId;
    private int grade;

    public StudentG(int studentId, int grade) {
        this.studentId = studentId;
        this.grade = grade;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "StudentG{" +
                "studentId=" + studentId +
                ", grade=" + grade +
                '}';
    }

    public int getCode() {
        String code = Integer.toString(this.studentId);
        int len = code.length();

        // IF מקוצר: אם אורך זוגי → קח שתי ספרות מהאמצע, אחרת → קח אחת
        String middle = (len % 2 == 0)
                ? code.substring(len / 2 - 1, len / 2 + 1)
                : code.substring(len / 2, len / 2 + 1);

        return Integer.parseInt(middle);
    }
}
