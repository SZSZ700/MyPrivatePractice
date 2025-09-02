package org.example;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import java.nio.charset.StandardCharsets;

public class JnaTry {

    public static void main(String[] args) {
        // נתיב הקובץ (אפשר יחסי או מוחלט)
        String path = "C:\\temp\\example_jna.txt";

        // המחרוזת שנרצה לכתוב לקובץ
        byte[] data = "Hello from JNA + WinAPI!\r\n".getBytes(StandardCharsets.UTF_8);

        // פתיחת/יצירת קובץ: GENERIC_WRITE, ללא שיתוף, CREATE_ALWAYS, מאפיין FILE_ATTRIBUTE_NORMAL
        WinNT.HANDLE hFile = Kernel32.INSTANCE.CreateFile(
                path,
                WinNT.GENERIC_WRITE,
                0,
                null,
                Kernel32.CREATE_ALWAYS,
                WinNT.FILE_ATTRIBUTE_NORMAL,
                null
        );

        // בדיקה אם קיבלנו Handle לא תקין (INVALID_HANDLE_VALUE)
        if (WinBase.INVALID_HANDLE_VALUE.equals(hFile)) {
            int err = Kernel32.INSTANCE.GetLastError();
            System.err.println("CreateFile failed. GetLastError=" + err);
            return;
        }

        try {
            // כתיבה לקובץ בעזרת WriteFile
            IntByReference bytesWritten = new IntByReference(0);
            boolean ok = Kernel32.INSTANCE.WriteFile(
                    hFile,
                    data,
                    data.length,
                    bytesWritten,
                    null
            );

            if (!ok) {
                int err = Kernel32.INSTANCE.GetLastError();
                System.err.println("WriteFile failed. GetLastError=" + err);
                return;
            }

            System.out.println("Wrote " + bytesWritten.getValue() + " bytes to: " + path);

            // (לא חובה) הבטחת ריקון באפרים לדיסק
            if (!Kernel32.INSTANCE.FlushFileBuffers(hFile)) {
                int err = Kernel32.INSTANCE.GetLastError();
                System.err.println("FlushFileBuffers failed. GetLastError=" + err);
            }

        } finally {
            // סגירת ה־Handle בקפדנות
            boolean closed = Kernel32.INSTANCE.CloseHandle(hFile);
            if (!closed) {
                int err = Kernel32.INSTANCE.GetLastError();
                System.err.println("CloseHandle failed. GetLastError=" + err);
            }
        }
    }
}
