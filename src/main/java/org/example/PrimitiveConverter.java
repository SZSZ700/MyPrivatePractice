package org.example;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class PrimitiveConverter {

    // 🔢 int
    public static byte[] intToBytes(int value) {return ByteBuffer.allocate(4).putInt(value).array();}

    public static int bytesToInt(byte[] bytes) {return ByteBuffer.wrap(bytes).getInt();}


    // 🧮 short
    public static byte[] shortToBytes(short value) {return ByteBuffer.allocate(2).putShort(value).array();}

    public static short bytesToShort(byte[] bytes) {return ByteBuffer.wrap(bytes).getShort();}


    // 🧮 long
    public static byte[] longToBytes(long value) {return ByteBuffer.allocate(8).putLong(value).array();}

    public static long bytesToLong(byte[] bytes) {return ByteBuffer.wrap(bytes).getLong();}


    // 🔢 float
    public static byte[] floatToBytes(float value) {return ByteBuffer.allocate(4).putFloat(value).array();}

    public static float bytesToFloat(byte[] bytes) {return ByteBuffer.wrap(bytes).getFloat();}


    // 🔢 double
    public static byte[] doubleToBytes(double value) {return ByteBuffer.allocate(8).putDouble(value).array();}

    public static double bytesToDouble(byte[] bytes) {return ByteBuffer.wrap(bytes).getDouble();}


    // 🅰️ char
    public static byte[] charToBytes(char value) {return ByteBuffer.allocate(2).putChar(value).array();}

    public static char bytesToChar(byte[] bytes) {return ByteBuffer.wrap(bytes).getChar();}


    // ✅ boolean
    public static byte[] booleanToBytes(boolean value) {return new byte[]{(byte) (value ? 1 : 0)};}

    public static boolean bytesToBoolean(byte[] bytes) {return bytes[0] != 0;}


    // 📝 String (UTF-8) – כולל אורך מקדים
    public static byte[] stringToBytes(String str) {
        byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);//ממיר מחרוזת למערך בתים
        byte[] lengthBytes = intToBytes(strBytes.length);//אורך מערך הבתים

        //4 בתים לאורך ושאר הבתים למחרוזת
        ByteBuffer buffer = ByteBuffer.allocate(4 + strBytes.length);

        //   s        h         a       r        b        e        l     (charcters)
        //  115      104       97      114      98       101       108   (ASCII-dECIMAL)
        //  0x73     0x68     0x61    0x72     0x62     0x65      0x6c   (Hex)
        //01110011 01101000 01100001 01110010 01100010 01100101 01101100 (Binary)
        //00000000 00000000 00000000 00111000|(56 bit - size of the bytes array of the string)

        //00000000 00000000 00000000 00111000 01110011 01101000 01100001 01110010 01100010 01100101 01101100
        //            56                     |    s        h        a        r        b        e        l

        buffer.put(lengthBytes);
        buffer.put(strBytes);

        return buffer.array();
    }

    public static String bytesToString(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int length = buffer.getInt();
        byte[] strBytes = new byte[length];
        buffer.get(strBytes);
        return new String(strBytes, StandardCharsets.UTF_8);
    }
}

