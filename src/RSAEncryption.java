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


    public static BigInteger hashFunction(int message) {
//        BigInteger sum = BigInteger.ZERO;
//        for (char c : message.toCharArray()) {
//            sum = sum.add(BigInteger.valueOf((int) c));
//        }
        BigInteger sum = BigInteger.valueOf(message);
        return sum.mod(n);
    }

    public static BigInteger generateDigitalSignature(int message) {
        BigInteger hashedMessage = hashFunction(message);
        return hashedMessage.modPow(d, n);
    }

    public static void main(String[] args) {
        try {

            File file = new File("input.txt");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder messageBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                messageBuilder.append(line);
            }
            int message = Integer.parseInt(messageBuilder.toString());
            br.close();


            BigInteger encryptedMessage = encrypt(BigInteger.valueOf(message));


            FileOutputStream fos = new FileOutputStream("encrypted_message.txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            bw.write(encryptedMessage.toString());
            bw.close();


            BigInteger digitalSignature = generateDigitalSignature(message);
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