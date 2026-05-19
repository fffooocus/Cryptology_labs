import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Scanner;

public class TripleDESExample {

    public static SecretKey createKey(String keyText) {
        byte[] keyBytes = new byte[24];
        byte[] inputKeyBytes = keyText.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < keyBytes.length; i++) {
            keyBytes[i] = inputKeyBytes[i % inputKeyBytes.length];
        }

        return new SecretKeySpec(keyBytes, "DESede");
    }

    public static String encrypt(String text, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(
                text.getBytes(StandardCharsets.UTF_8)
        );

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Програма для шифрування алгоритмом 3DES");

            System.out.print("Введіть текст для шифрування: ");
            String message = scanner.nextLine();

            System.out.print("Введіть ключ шифрування: ");
            String keyText = scanner.nextLine();

            if (message.isEmpty() || keyText.isEmpty()) {
                System.out.println("Помилка: текст та ключ не можуть бути порожніми.");
                return;
            }

            SecretKey key = createKey(keyText);

            String encryptedText = encrypt(message, key);
            String decryptedText = decrypt(encryptedText, key);

            System.out.println("\n-Результати");
            System.out.println("Відкритий текст:      " + message);
            System.out.println("Ключ:                 " + keyText);
            System.out.println("Алгоритм:             DESede/ECB/PKCS5Padding");
            System.out.println("Шифротекст Base64:    " + encryptedText);
            System.out.println("Розшифрований текст:  " + decryptedText);

        } catch (Exception e) {
            System.out.println("Сталася помилка під час виконання програми:");
            e.printStackTrace();
        }
    }
}