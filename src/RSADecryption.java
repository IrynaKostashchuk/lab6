import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class RSADecryption {

    private static final BigInteger n = new BigInteger("33"); // public key n
    private static final BigInteger d = new BigInteger("3");  // private key d
    private static final BigInteger e = new BigInteger("7");

    // Method for decrypting the message
    public static BigInteger[] decrypt(BigInteger[] encryptedNumbers) {
        BigInteger[] decryptedNumbers = new BigInteger[encryptedNumbers.length];
        for (int i = 0; i < encryptedNumbers.length; i++) {
            decryptedNumbers[i] = encryptedNumbers[i].modPow(d, n);
        }
        return decryptedNumbers;
    }

    // Method for verifying the signature
    public static boolean verifySignature(BigInteger receivedSignature, BigInteger[] decryptedNumbers) {
        BigInteger expectedHash = hashFunction(decryptedNumbers);
        BigInteger decryptedSignature = receivedSignature.modPow(e, n);
        return decryptedSignature.equals(expectedHash);
    }

    // Hash function that computes the hash of the sum of decrypted numbers
    public static BigInteger hashFunction(BigInteger[] numbers) {
        BigInteger sum = BigInteger.ZERO;
        for (BigInteger number : numbers) {
            sum = sum.add(number);
        }
        return sum.mod(n);
    }

    public static void main(String[] args) {
        try {
            // Reading the encrypted numbers from the file
            BufferedReader br = new BufferedReader(new FileReader("encrypted_message.txt"));
            String[] encryptedNumbersString = br.readLine().split("\\s+");
            br.close();

            // Converting the encrypted numbers to BigInteger array
            BigInteger[] encryptedNumbers = new BigInteger[encryptedNumbersString.length];
            for (int i = 0; i < encryptedNumbersString.length; i++) {
                encryptedNumbers[i] = new BigInteger(encryptedNumbersString[i]);
            }

            // Reading the digital signature from the file
            br = new BufferedReader(new FileReader("digital_signature.txt"));
            BigInteger receivedSignature = new BigInteger(br.readLine());
            br.close();

            // Decrypting the numbers
            BigInteger[] decryptedNumbers = decrypt(encryptedNumbers);

            // Verifying the signature
            boolean signatureVerified = verifySignature(receivedSignature, decryptedNumbers);

            if (signatureVerified) {
                System.out.println("Digital signature verified. The message is authentic.");
                System.out.println("Decrypted numbers:");
                for (BigInteger num : decryptedNumbers) {
                    System.out.print(num + " ");
                }
            } else {
                System.out.println("Digital signature verification failed. The message may have been tampered with.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}