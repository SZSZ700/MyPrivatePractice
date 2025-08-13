package org.example;

import java.util.*;

public class AESManual {

    // גודל בלוק AES בבתים (128 ביט)
    private static final int BLOCK_SIZE = 16;

    // תיבת S - משמשת ל־SubBytes (החלפת בתים) לפי תקן AES
    private final int[] sBox = {
            0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5,
            0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76,
            0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0,
            0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0,
            0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC,
            0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15,
            0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A,
            0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75,
            0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0,
            0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84,
            0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B,
            0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF,
            0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85,
            0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8,
            0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5,
            0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2,
            0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17,
            0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73,
            0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88,
            0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB,
            0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C,
            0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79,
            0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9,
            0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08,
            0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6,
            0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A,
            0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E,
            0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E,
            0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94,
            0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF,
            0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68,
            0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16
    };

    // קבועים לפעולת MixColumns
    private final int[] mixColMatrix = {
            0x02, 0x03, 0x01, 0x01,
            0x01, 0x02, 0x03, 0x01,
            0x01, 0x01, 0x02, 0x03,
            0x03, 0x01, 0x01, 0x02
    };

    // קבועים לפעולת Inverse MixColumns (שלב פענוח)
    private final int[] invMixColMatrix = {
            0x0e, 0x0b, 0x0d, 0x09,
            0x09, 0x0e, 0x0b, 0x0d,
            0x0d, 0x09, 0x0e, 0x0b,
            0x0b, 0x0d, 0x09, 0x0e
    };

    // טבלת Inverse S (לפענוח)
    private final int[] invSBox = new int[256];
    {
        for (int i = 0; i < 256; i++) {
            invSBox[sBox[i]] = i;
        }
    }

    // קבועי Rcon - לצורך הרחבת מפתח בשיטת AES
    private static final int[] Rcon = {
            0x00,
            0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1B, 0x36,
            0x6C, 0xD8, 0xAB, 0x4D, 0x9A, 0x2F, 0x5E, 0xBC, 0x63, 0xC6,
            0x97, 0x35, 0x6A, 0xD4, 0xB3, 0x7D, 0xFA, 0xEF, 0xC5, 0x91
    };

    private byte[] roundKeys;

    // מפתח AES בגודל 16 בתים
    public AESManual(byte[] key) {
        if (key.length != 16) {
            throw new IllegalArgumentException("המפתח חייב להיות באורך 16 בתים (128 ביט)");
        }
        this.roundKeys = keyExpansion(key);
    }


    // 📦 הצפנת בלוק בודד בגודל 16 בתים
    public byte[] encryptBlock(byte[] input) {
        byte[] state = Arrays.copyOf(input, BLOCK_SIZE);

        addRoundKey(state, roundKeys, 0);
        for (int round = 1; round < 10; round++) {
            subBytes(state);
            shiftRows(state);
            mixColumns(state);
            addRoundKey(state, roundKeys, round);
        }
        subBytes(state);
        shiftRows(state);
        addRoundKey(state, roundKeys, 10);
        return state;
    }

    // 🔓 פענוח בלוק בודד
    public byte[] decryptBlock(byte[] input) {
        byte[] state = Arrays.copyOf(input, BLOCK_SIZE);

        addRoundKey(state, roundKeys, 10);
        for (int round = 9; round > 0; round--) {
            invShiftRows(state);
            invSubBytes(state);
            addRoundKey(state, roundKeys, round);
            invMixColumns(state);
        }
        invShiftRows(state);
        invSubBytes(state);
        addRoundKey(state, roundKeys, 0);
        return state;
    }

    // ✂️ ריפוד לפי PKCS7
    private byte[] pad(byte[] data) {
        int pad = BLOCK_SIZE - (data.length % BLOCK_SIZE);
        byte[] padded = Arrays.copyOf(data, data.length + pad);
        Arrays.fill(padded, data.length, padded.length, (byte) pad);
        return padded;
    }

    // 🔁 הסרת ריפוד
    private byte[] unpad(byte[] data) {
        int pad = data[data.length - 1] & 0xFF;
        return Arrays.copyOf(data, data.length - pad);
    }

    // 🔐 הצפנת טקסט מלא
    public byte[] encrypt(byte[] data) {
        byte[] padded = pad(data);
        byte[] output = new byte[padded.length];
        for (int i = 0; i < padded.length; i += BLOCK_SIZE) {
            byte[] enc = encryptBlock(Arrays.copyOfRange(padded, i, i + BLOCK_SIZE));
            System.arraycopy(enc, 0, output, i, BLOCK_SIZE);
        }
        return output;
    }

    // 🔓 פענוח טקסט מלא
    public byte[] decrypt(byte[] data) {
        byte[] output = new byte[data.length];
        for (int i = 0; i < data.length; i += BLOCK_SIZE) {
            byte[] dec = decryptBlock(Arrays.copyOfRange(data, i, i + BLOCK_SIZE));
            System.arraycopy(dec, 0, output, i, BLOCK_SIZE);
        }
        return unpad(output);
    }


    // פעולת AddRoundKey - XOR עם מקש עגול
    private void addRoundKey(byte[] state, byte[] key, int round) {
        for (int i = 0; i < BLOCK_SIZE; i++) {
            state[i] ^= key[round * BLOCK_SIZE + i];
        }
    }

    // הרחבת מפתח ל־AES-128 (176 בתים)
    private byte[] keyExpansion(byte[] key) {
        byte[] expanded = new byte[176];
        System.arraycopy(key, 0, expanded, 0, 16);

        int bytesGenerated = 16;
        int rconIndex = 1;
        byte[] temp = new byte[4];

        while (bytesGenerated < 176) {
            for (int i = 0; i < 4; i++) temp[i] = expanded[bytesGenerated - 4 + i];

            if (bytesGenerated % 16 == 0) {
                temp = rotate(temp);
                for (int i = 0; i < 4; i++) temp[i] = (byte)(sBox[temp[i] & 0xff]);
                temp[0] ^= (byte) Rcon[rconIndex++];
            }

            for (int i = 0; i < 4; i++) {
                expanded[bytesGenerated] = (byte)(expanded[bytesGenerated - 16] ^ temp[i]);
                bytesGenerated++;
            }
        }

        return expanded;
    }

    private byte[] rotate(byte[] word) {
        byte tmp = word[0];
        word[0] = word[1];
        word[1] = word[2];
        word[2] = word[3];
        word[3] = tmp;
        return word;
    }

    // שלב SubBytes
    private void subBytes(byte[] state) {
        for (int i = 0; i < BLOCK_SIZE; i++) {
            state[i] = (byte)(sBox[state[i] & 0xff]);
        }
    }

    // שלב הפוך של SubBytes
    private void invSubBytes(byte[] state) {
        for (int i = 0; i < BLOCK_SIZE; i++) {
            state[i] = (byte)(invSBox[state[i] & 0xff]);
        }
    }

    // שלב ShiftRows
    private void shiftRows(byte[] state) {
        byte[] temp = Arrays.copyOf(state, BLOCK_SIZE);

        state[1] = temp[5];  state[5] = temp[9];  state[9] = temp[13]; state[13] = temp[1];
        state[2] = temp[10]; state[6] = temp[14]; state[10] = temp[2]; state[14] = temp[6];
        state[3] = temp[15]; state[7] = temp[3];  state[11] = temp[7]; state[15] = temp[11];
    }

    // שלב הפוך של ShiftRows
    private void invShiftRows(byte[] state) {
        byte[] temp = Arrays.copyOf(state, BLOCK_SIZE);

        state[1] = temp[13]; state[5] = temp[1];  state[9] = temp[5];  state[13] = temp[9];
        state[2] = temp[10]; state[6] = temp[14]; state[10] = temp[2]; state[14] = temp[6];
        state[3] = temp[7];  state[7] = temp[11]; state[11] = temp[15];state[15] = temp[3];
    }

    // שלב MixColumns
    private void mixColumns(byte[] state) {
        for (int c = 0; c < 4; c++) {
            int i = c * 4;
            byte a0 = state[i], a1 = state[i + 1], a2 = state[i + 2], a3 = state[i + 3];

            state[i]     = (byte)(mul(0x02, a0) ^ mul(0x03, a1) ^ a2 ^ a3);
            state[i + 1] = (byte)(a0 ^ mul(0x02, a1) ^ mul(0x03, a2) ^ a3);
            state[i + 2] = (byte)(a0 ^ a1 ^ mul(0x02, a2) ^ mul(0x03, a3));
            state[i + 3] = (byte)(mul(0x03, a0) ^ a1 ^ a2 ^ mul(0x02, a3));
        }
    }

    // שלב Inverse MixColumns
    private void invMixColumns(byte[] state) {
        for (int c = 0; c < 4; c++) {
            int i = c * 4;
            byte a0 = state[i], a1 = state[i + 1], a2 = state[i + 2], a3 = state[i + 3];

            state[i]     = (byte)(mul(0x0e, a0) ^ mul(0x0b, a1) ^ mul(0x0d, a2) ^ mul(0x09, a3));
            state[i + 1] = (byte)(mul(0x09, a0) ^ mul(0x0e, a1) ^ mul(0x0b, a2) ^ mul(0x0d, a3));
            state[i + 2] = (byte)(mul(0x0d, a0) ^ mul(0x09, a1) ^ mul(0x0e, a2) ^ mul(0x0b, a3));
            state[i + 3] = (byte)(mul(0x0b, a0) ^ mul(0x0d, a1) ^ mul(0x09, a2) ^ mul(0x0e, a3));
        }
    }

    // פעולת הכפלה בשדה GF(2^8)
    private byte mul(int a, byte b) {
        int res = 0;
        int val = b & 0xFF;
        while (a > 0) {
            if ((a & 1) != 0) res ^= val;
            val <<= 1;
            if ((val & 0x100) != 0) val ^= 0x11b;
            a >>= 1;
        }
        return (byte)res;
    }
}
