import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
public class RSAEncryption {

    private static final BigInteger p = new BigInteger("3");
    private static final BigInteger q = new BigInteger("11");
    private static final BigInteger n = p.multiply(q);
    private static final BigInteger e = new BigInteger("7");
    private static final BigInteger d = new BigInteger("3");

    public static BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }

    public static BigInteger hashFunction(String[] numbers) {
        BigInteger sum = BigInteger.ZERO;
        for (String number : numbers) {
            sum = sum.add(new BigInteger(number));
        }
        return sum.mod(n);
    }

    public static BigInteger generateDigitalSignature(String[] numbers) {
        BigInteger hashedMessage = hashFunction(numbers);
        return hashedMessage.modPow(d, n);
    }

    public static void main(String[] args) {
        try {
            File file = new File("input.txt");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);

            String line = br.readLine();
            String[] numbers = line.split("\\s+"); // Split input line by whitespace

            BigInteger[] encryptedNumbers = new BigInteger[numbers.length];
            for (int i = 0; i < numbers.length; i++) {
                encryptedNumbers[i] = encrypt(new BigInteger(numbers[i])); // Encrypt each number
            }

            FileOutputStream fos = new FileOutputStream("encrypted_message.txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            for (BigInteger encryptedNumber : encryptedNumbers) {
                bw.write(encryptedNumber.toString() + " ");
            }
            bw.close();

            BigInteger digitalSignature = generateDigitalSignature(numbers);

            FileOutputStream fos2 = new FileOutputStream("digital_signature.txt");
            BufferedWriter bw2 = new BufferedWriter(new OutputStreamWriter(fos2, StandardCharsets.UTF_8));
            bw2.write(digitalSignature.toString());
            bw2.close();

            System.out.println("Encryption and digital signature generation completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}