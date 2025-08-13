//imports
package org.example;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;

/**
 * The {@code MyEncrypt} class is a utility class that provides various
 * encryption and decryption functionalities for strings, integers, and files.
 * <p>
 * It supports the following algorithms:
 * <ul>
 *     <li><b>XOR Cipher:</b> Lightweight symmetric encryption for integers and strings</li>
 *     <li><b>Caesar Cipher:</b> Letter-shifting cipher for simple obfuscation of text</li>
 *     <li><b>AES Cipher:</b> Strong symmetric encryption for any file using AES algorithm</li>
 *     <li><b>Base64 Encoding:</b> Converts binary files to text and vice versa using Base64</li>
 * </ul>
 *
 * <p>
 * All methods in this class are static and exception-safe. The class is not meant
 * to be instantiated.
 * </p>
 *
 * <p><b>Example Usage:</b></p>
 * <pre>{@code
 *     String encrypted = MyEncrypt.encryptString("hello", 'k');
 *     MyEncrypt.encryptTxtFile("input.txt", "output.txt", 'x');
 *     MyEncrypt.EncryptOrDecryptAnyFileUsingCipher(Cipher.ENCRYPT_MODE, "1234567812345678", inputFile, outputFile);
 * }</pre>
 *
 * <p><b>Note:</b> AES requires a key length of 16 bytes.</p>
 *
 * @author SharbelZarzour
 * @version 1.0
 */
public class MyEncrypt {

    //בנאי פרטי – מונע יצירת מופע מהמחלקה
    private MyEncrypt() {
        throw new UnsupportedOperationException("class cannot be instantiated");
    }
    //------------------------------------------------------------------------------------------//
    //xor encryption:
    /**
     * Encrypts an integer using XOR with a key.
     * @param number the number to encrypt
     * @param key the XOR key
     * @return encrypted number
     */
    public static int encryptInt(int number, int key) {
        return number ^ key;
    }

    /**
     * Decrypts an encrypted integer using XOR with a key.
     * @param encryptedNumber the encrypted number
     * @param key the XOR key
     * @return original number
     */
    public static int decryptInt(int encryptedNumber, int key) {
        return encryptedNumber ^ key;
    }

    /**
     * Encrypts a string using XOR cipher.
     * @param input original string
     * @param key the XOR key
     * @return encrypted string
     */
    public static String encryptString(String input, char key) {
        //המרת המחרוזת למערך תווים
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            chars[i] = (char)(chars[i] ^ key);
        }
        // החזרת המערך חזרה כמחרוזת
        return new String(chars);
    }

    /**
     * Decrypts a string encrypted with XOR cipher.
     * @param encrypted the encrypted string
     * @param key the XOR key used
     * @return decrypted string
     */
    public static String decryptString(String encrypted, char key) {
        return encryptString(encrypted, key);
    }

    /**
     * Encrypts a text file line-by-line using XOR and writes to another file.
     * @param inputPath path of input file
     * @param outputPath path to save encrypted output file
     * @param key XOR key for encryption
     * @return true if encryption is successful, false otherwise
     */
    public static boolean encryptTxtFile(String inputPath, String outputPath, char key) {
        try {
            //פויינטר לקובץ יעד לביצוע הצפנה עליו
            File inputFile = new File(inputPath);
            //פויינטר לקובץ מוצפן
            File outputFile = new File(outputPath);

            // בדיקה האם קובץ המקור קיים
            if (!inputFile.exists()) {return false;}

            // אם קובץ הפלט לא קיים - צור אותו בשולחן העבודה
            if (!outputFile.exists()) {
                String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
                outputFile = new File(desktopPath, outputPath);
                System.out.println("file will be created in desktop!");
            }

            //קורא מקובץ..
            Scanner reader = new Scanner(inputFile);
            //כותב לקובץ...
            FileWriter writer = new FileWriter(outputFile, true);

            // קריאה והצפנה שורה-שורה
            while (reader.hasNextLine()) {
                String temp = encryptString(reader.nextLine(), key);
                writer.write(temp + '\n');
            }

            // סגירת הקבצים
            writer.close();
            reader.close();
            return true;
        } catch (Exception e) {
            System.out.println("⚠️ error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Decrypts a file encrypted with XOR (same as encryption due to symmetric nature).
     * @param inputPath path to encrypted input file
     * @param outputPath path to save decrypted output file
     * @param key XOR key
     * @return true if decryption is successful
     */
    public static boolean decryptTxtFile(String inputPath, String outputPath, char key) {
        return encryptTxtFile(inputPath,outputPath,key);
    }
    //------------------------------------------------------------------------------------------//
    //caesar encryption:
    /**
     * Encrypts a string using Caesar cipher with a given shift.
     * @param text the original text
     * @param shift number of positions to shift
     * @return encrypted string
     */
    private static String caesarEncrypt(String text, int shift) {
        //new encrypted string
        String str = "";

        //iterate through the string
        for (int i = 0; i < text.length(); i++) {

            //current char
            char current = text.charAt(i);

            //check if charter is a letter
            if (Character.isLetter(current)) {
                //base
                char base = Character.isUpperCase(current) ? 'A' : 'a';
                //how many times to move charcters
                char shifted = (char) ((current - base + shift) % 26 + base);
                str += (shifted);
            } else {
                str += (current);
            }
        }
        return str;
    }

    /**
     * Decrypts a Caesar cipher string using reverse shift.
     * @param text encrypted string
     * @param shift original encryption shift
     * @return decrypted string
     */
    private static String caesarDecrypt(String text, int shift) {
        return caesarEncrypt(text, 26 - (shift % 26));
    }

    /**
     * Encrypts or decrypts a text file line-by-line using Caesar cipher.
     * @param inputPath input file path
     * @param outputPath output file path
     * @param shift shift used in Caesar cipher
     * @param encrypt true to encrypt, false to decrypt
     * @return true if operation was successful
     */
    public static boolean EncryptOrDecryptTxtFileUsingCaesar(String inputPath, String outputPath, int shift, boolean encrypt) {
        try {
            //create files pointers
            File inputFile = new File(inputPath);
            File outputFile = new File(outputPath);

            //if source file not exists -> false
            if (!inputFile.exists()) {return false;}

            //if output file not exist create one
            if (!outputFile.exists()) {
                String desktopPath = System.getProperty("user.home") + File.separator + "Desktop";
                outputFile = new File(desktopPath, outputPath);
            }

            //check if file is not readble
            if (!inputFile.canRead()) {inputFile.setReadable(true);}

            //reader - read file content
            Scanner reader = new Scanner(inputFile);

            //check if file is not empty
            if (!reader.hasNext()){reader.close(); return false;}

            //writer - write to output-file
            FileWriter writer = new FileWriter(outputFile, true);

            //scan file's lines
            while (reader.hasNextLine()) {
                //catch current line
                String line = reader.nextLine();
                //if user want to encrypt - (bool encrypt = true)
                String processedLine = encrypt
                        ? caesarEncrypt(line, shift)
                        : caesarDecrypt(line, shift);
                //add the current encrypted line to the output file
                writer.write(processedLine + "\n");
            }

            //close reader && writer
            reader.close();
            writer.close();

            //proccess completed -> true
            return true;

        } catch (Exception e) {
            //errors catchers
            System.out.println("⚠️ error: " + e.getMessage());
            return false;
        }
    }
    //------------------------------------------------------------------------------------------//
    //cipher encryption
    /**
     * Encrypts or decrypts any file using AES algorithm and a string key.
     * @param mode Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
     * @param key encryption key
     * @param inFile source file
     * @param outFile destination file
     * @throws Exception if encryption/decryption fails
     */
    public static void EncryptOrDecryptAnyFileUsingCipher(int mode, String key, File inFile, File outFile) throws Exception {
        // יצירת מפתח סודי לפי מחרוזת מפתח
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");

        // יצירת אובייקט צופן לפי אלגוריתם AES
        Cipher cipher = Cipher.getInstance("AES");

        // אתחול הצופן למצב הצפנה או פענוח
        cipher.init(mode, secretKey);

        // שימוש ב-try-with-resources לסגירה אוטומטית של הקבצים
        try (
                FileInputStream fis = new FileInputStream(inFile); // זרם לקריאת קובץ מקור
                FileOutputStream fos = new FileOutputStream(outFile); // זרם לכתיבה לקובץ יעד

                // יצירת זרמים מתאימים לפי מצב ההצפנה
                CipherInputStream cis = (mode == Cipher.DECRYPT_MODE) ? new CipherInputStream(fis, cipher) : null;
                CipherOutputStream cos = (mode == Cipher.ENCRYPT_MODE) ? new CipherOutputStream(fos, cipher) : null
        ) {
            // מאגר זמני לקריאת חלקים מהקובץ
            byte[] buffer = new byte[8192];
            int bytesRead;

            if (mode == Cipher.ENCRYPT_MODE) {
                // קריאה רגילה מהקובץ המקורי וכתיבה לזרם מוצפן
                while ((bytesRead = fis.read(buffer)) != -1) {
                    cos.write(buffer, 0, bytesRead);
                }
            } else {
                // קריאה מהזרם המפוענח וכתיבה לקובץ רגיל
                while ((bytesRead = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    //------------------------------------------------------------------------------------------//
    //base64 encryption:
    /**
     * Encodes or decodes a file using Base64.
     * @param inputPath path to original or encoded file
     * @param outputPath path to output file
     * @param encode true to encode, false to decode
     * @return true if operation was successful
     */
    public static boolean processBase64File(String inputPath, String outputPath, boolean encode) {
        try {
            if (encode) {
                // 🔐 קידוד לקובץ Base64
                // קריאת כל תוכן הקובץ לבייטים
                byte[] fileBytes = Files.readAllBytes(Paths.get(inputPath));
                // המרת הבייטים למחרוזת Base64
                String encoded = Base64.getEncoder().encodeToString(fileBytes);
                // כתיבת מחרוזת Base64 לקובץ טקסט
                Files.write(Paths.get(outputPath), encoded.getBytes());
                System.out.println("✅ File encoded to Base64 and saved to: " + outputPath);
            } else {
                // 🔓 פענוח מקובץ Base64
                // קריאת תוכן הקובץ המכיל את מחרוזת Base64
                byte[] encodedBytes = Files.readAllBytes(Paths.get(inputPath));
                // המרת המחרוזת חזרה לבייטים מקוריים
                byte[] decodedBytes = Base64.getDecoder().decode(new String(encodedBytes));
                // כתיבת הבייטים לקובץ חדש (יכול להיות תמונה, טקסט, וכו')
                Files.write(Paths.get(outputPath), decodedBytes);
                System.out.println("✅ File decoded from Base64 and saved to: " + outputPath);
            }
            return true;
        } catch (IOException e) {
            // טיפול בשגיאה כללית של קלט/פלט
            System.out.println("❌ Error: " + e.getMessage());
            return false;
        }
    }
}
