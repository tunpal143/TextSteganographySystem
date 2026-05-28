package textsteganography;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class TextSteganography {

    // Invisible Unicode Characters
    static final char ZERO_WIDTH_SPACE = '\u200B';
    static final char ZERO_WIDTH_NON_JOINER = '\u200C';

    // Convert message to hidden binary format
    public static String encodeMessage(String message) {
        StringBuilder binary = new StringBuilder();

        for (char ch : message.toCharArray()) {
            String bin = String.format("%8s",
                    Integer.toBinaryString(ch)).replace(' ', '0');

            binary.append(bin);
        }

        StringBuilder hidden = new StringBuilder();

        for (char bit : binary.toString().toCharArray()) {
            if (bit == '0') {
                hidden.append(ZERO_WIDTH_SPACE);
            } else {
                hidden.append(ZERO_WIDTH_NON_JOINER);
            }
        }

        return hidden.toString();
    }

    // Decode hidden message
    public static String decodeMessage(String hiddenText) {

        StringBuilder binary = new StringBuilder();

        for (char ch : hiddenText.toCharArray()) {

            if (ch == ZERO_WIDTH_SPACE) {
                binary.append("0");
            } else if (ch == ZERO_WIDTH_NON_JOINER) {
                binary.append("1");
            }
        }

        StringBuilder message = new StringBuilder();

        for (int i = 0; i < binary.length(); i += 8) {

            if (i + 8 <= binary.length()) {

                String byteString =
                        binary.substring(i, i + 8);

                int charCode =
                        Integer.parseInt(byteString, 2);

                message.append((char) charCode);
            }
        }

        return message.toString();
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {

            // Read Cover File
            System.out.print("Enter Cover File Name: ");
            String fileName = sc.nextLine();

            String coverText =
                    new String(Files.readAllBytes(
                            Paths.get(fileName)));

            // Secret Message
            System.out.print("Enter Secret Message: ");
            String secretMessage = sc.nextLine();

            // Encode Message
            String hiddenData =
                    encodeMessage(secretMessage);

            // Combine Cover + Hidden Data
            String encodedText =
                    coverText + hiddenData;

            // Save Encoded File
            String outputFile = "encoded_output.txt";

            Files.write(Paths.get(outputFile),
                    encodedText.getBytes());

            System.out.println(
                    "Message Hidden Successfully!");

            System.out.println(
                    "Encoded File Saved As: "
                            + outputFile);

            // Decode Process
            String encodedContent =
                    new String(Files.readAllBytes(
                            Paths.get(outputFile)));

            String hiddenPart =
                    encodedContent.substring(
                            coverText.length());

            String decodedMessage =
                    decodeMessage(hiddenPart);

            System.out.println(
                    "Retrieved Hidden Message: "
                            + decodedMessage);

        } catch (Exception e) {
            System.out.println(
                    "Error: " + e.getMessage());
        }

        sc.close();
    }
}
