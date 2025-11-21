package org.example.QrGenerator;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QrGenerator {
    public static void main(String[] args) throws WriterException, IOException {
        // QrCode Writer object
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // create the matrix that represent the QR
        BitMatrix bitMatrix = qrCodeWriter
                .encode(
                        "https://github.com/SZSZ700/MyPrivatePractice.git",
                        BarcodeFormat.QR_CODE,
                        500,
                        500
                );

        // build the full path to Desktop
        String filePath = System.getProperty("user.home") + "/Desktop/MYQR.png";

        // create Path object from the string path
        Path path = FileSystems.getDefault().getPath(filePath);

        // write the Qr into PNG file
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        System.out.println("QR saved to: " + path.toAbsolutePath());
    }
}

