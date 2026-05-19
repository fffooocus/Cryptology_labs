import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.Scanner;

public class DSAExample {

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] createSignature(String message, PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));

        return signature.sign();
    }

    public static boolean verifySignature(String message, byte[] digitalSignature, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

        Signature signature = Signature.getInstance("SHA256withDSA");
        signature.initVerify(publicKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));

        return signature.verify(digitalSignature);
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Програма для створення та перевірки цифрового підпису DSA");
            System.out.print("Введіть повідомлення для підпису: ");
            String message = scanner.nextLine();

            if (message.isEmpty()) {
                System.out.println("Помилка: повідомлення не може бути порожнім.");
                return;
            }

            KeyPair keyPair = generateKeyPair();

            byte[] digitalSignature = createSignature(message, keyPair.getPrivate());

            boolean isValid = verifySignature(
                    message,
                    digitalSignature,
                    keyPair.getPublic()
            );

            System.out.println("\n-Результати");
            System.out.println("Відкрите повідомлення: " + message);
            System.out.println("Алгоритм:              SHA256withDSA");

            System.out.println("\nПублічний ключ Base64:");
            System.out.println(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));

            System.out.println("\nПриватний ключ Base64:");
            System.out.println(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));

            System.out.println("\nЦифровий підпис Base64:");
            System.out.println(Base64.getEncoder().encodeToString(digitalSignature));

            System.out.println("\nПеревірка підпису:");
            if (isValid) {
                System.out.println("Підпис дійсний. Повідомлення не було змінено.");
            } else {
                System.out.println("Підпис недійсний.");
            }

            String changedMessage = message + " змінено";

            boolean isChangedValid = verifySignature(
                    changedMessage,
                    digitalSignature,
                    keyPair.getPublic()
            );

            System.out.println("\nПеревірка після зміни повідомлення:");
            if (isChangedValid) {
                System.out.println("Підпис дійсний.");
            } else {
                System.out.println("Підпис недійсний, оскільки повідомлення було змінено.");
            }

        } catch (Exception e) {
            System.out.println("Сталася помилка під час виконання програми:");
            e.printStackTrace();
        }
    }
}