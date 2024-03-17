import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class RSADecryption {

    private static final BigInteger n = new BigInteger("33"); // публічний ключ n
    private static final BigInteger d = new BigInteger("3");  // приватний ключ d

    private static final BigInteger e = new BigInteger("7");

    // Метод для розшифрування повідомлення
    public static String decrypt(BigInteger encryptedMessage) {
        BigInteger decryptedMessageBigInt = encryptedMessage.modPow(d, n);
        return new String(String.valueOf(decryptedMessageBigInt));
    }

    // Метод для перевірки підпису
    public static boolean verifySignature(BigInteger receivedSignature, BigInteger decryptedMessage) {
        BigInteger expectedHash = hashFunction(decryptedMessage);
        BigInteger decryptedSignature = receivedSignature.modPow(e, n);
        System.out.println(expectedHash + " " + decryptedSignature);
        return decryptedSignature.equals(expectedHash);
    }

    // Хеш-функція, яка обчислює хеш повідомлення
    public static BigInteger hashFunction(BigInteger message) {
//        BigInteger sum = BigInteger.ZERO;
//        for (char c : message.toCharArray()) {
//            sum = sum.add(BigInteger.valueOf((int) c));
//        }
        BigInteger sum = message;
        return sum.mod(n);
    }

    public static void main(String[] args) {
        try {
            // Читання зашифрованого повідомлення з файлу
            BufferedReader br = new BufferedReader(new FileReader("encrypted_message.txt"));
            BigInteger encryptedMessage = new BigInteger(br.readLine());
            br.close();

            // Читання цифрового підпису з файлу
            br = new BufferedReader(new FileReader("digital_signature.txt"));
            BigInteger receivedSignature = new BigInteger(br.readLine());
            br.close();

            // Розшифрування повідомлення
            String decryptedMessage = decrypt(encryptedMessage);
            System.out.println(decryptedMessage);

            // Перевірка підпису
            boolean signatureVerified = verifySignature(receivedSignature, BigInteger.valueOf(Integer.valueOf(decryptedMessage)));

            if (signatureVerified) {
                System.out.println("Digital signature verified. The message is authentic.");
            } else {
                System.out.println("Digital signature verification failed. The message may have been tampered with.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
