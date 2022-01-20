import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Encrypt {


    private final static String ALPHABET = "АБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзиклмнопрстуфхцчшщъыьэя.,”':-!? ";


    public static void encode() {  // Шифрование


        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter path to source file: ");

        // Path path = Paths.get(scanner.nextLine());

        Path path = Paths.get("C:\\caesar_cipher\\src\\words.txt");

        System.out.print("\nEnter key: ");

        int shift = scanner.nextInt();

        Path encodedFile = path.getParent().resolve("encode.txt");

        if (!Files.exists(encodedFile)) {
            try {
                Files.createFile(path.getParent().resolve("encode.txt"));
            } catch (IOException e) {
                System.out.println("\nCan't create file");
            }
        }


        try (

                BufferedReader input = Files.newBufferedReader(path);
                BufferedWriter output = Files.newBufferedWriter(encodedFile)) {

            while (input.ready()) {

                String line = input.readLine();

                output.write(caesar(line, shift) + '\n');
            }

            System.out.println("\nOutput file path: " + encodedFile);


        } catch (FileNotFoundException e) {
            System.out.println("\nFile not found");
        } catch (IOException e) {
            System.out.println("\nI/O Error");
        }


    }

    public static void decode() { // Дешифровка


        Scanner scanner = new Scanner(System.in);

        System.out.print("\nEnter path to encoded file: ");

        // Path path = Paths.get(scanner.nextLine());

        Path path = Paths.get("C:\\caesar_cipher\\src\\encode.txt");

        System.out.print("\nEnter key: ");

        int shift = scanner.nextInt();

        Path decodedFile = path.getParent().resolve("decode.txt");

        if (!Files.exists(decodedFile)) {
            try {
                Files.createFile(path.getParent().resolve("decode.txt"));
            } catch (IOException e) {
                System.out.println("\nCan't create file");
            }
        }


        try (

                BufferedReader input = Files.newBufferedReader(path);
                BufferedWriter output = Files.newBufferedWriter(decodedFile)) {

            while (input.ready()) {

                String line = input.readLine();

                output.write(caesar(line, shift) + '\n');
            }

            System.out.println("\nOutput file path: " + decodedFile);

        } catch (FileNotFoundException e) {
            System.out.println("\nFile not found");
        } catch (IOException e) {
            System.out.println("\nI/O Error");
        }

    }

    private static String caesar(String text, int shift) { // Алгоритм

        StringBuilder result = new StringBuilder(text.length());

        int replace;

        for (int i = 0; i < text.length(); i++) {

            int index = ALPHABET.indexOf(text.charAt(i));
            if (index >= 0) { //проверить есть ли символ в алфавите

                replace = (shift + index) % ALPHABET.length(); //положение шифрованного символа относительно индекса вхождения в алфавит со смещением

                if (replace < 0) { //расчет для отрицательного индекса
                    replace = replace + ALPHABET.length();
                }
                result.append(ALPHABET.charAt(replace));

            } else result.append(text.charAt(i));

        }

        return result.toString();

    }

    public static void analysis() { // Криптоанализ



        Path path_Crypted = Paths.get("C:\\caesar_cipher\\src\\encode.txt");
        Path path_Original = Paths.get("C:\\caesar_cipher\\src\\words.txt");

        Map<Character, Integer> map_crypted = new HashMap<>();
        Map<Character, Integer> map_original = new HashMap<>();

        try (
                BufferedReader input_crypto = Files.newBufferedReader(path_Crypted))
                {

            while (input_crypto.ready()) {

                String crypted_String = input_crypto.readLine();

                for (int i = 0; i < crypted_String.length(); i++) {
                    char c = crypted_String.charAt(i);

                    map_crypted.merge(c, 1, Integer::sum);
                }

                 }
                    System.out.println(map_crypted);

            try ( BufferedReader input_original = Files.newBufferedReader(path_Original)) {

                while (input_original.ready()) {

                String original_String = input_original.readLine();

                for (int i = 0; i < original_String.length(); i++) {
                    char c = original_String.charAt(i);

                        map_original.merge(c, 1, Integer::sum);
                }
            }
                System.out.println(map_original);
            }
                } catch (IOException e) {
            System.out.println("File not found");
        }

        int max_crypted = Collections.max(map_crypted.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        int max_original = Collections.max(map_original.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();

        int prefered_key = (ALPHABET.indexOf(max_original) + ALPHABET.indexOf(max_crypted))  % ALPHABET.length();

        System.out.println("\nThe key is: " + prefered_key);

        System.out.println(ALPHABET.indexOf(max_original));
        System.out.println(ALPHABET.indexOf(max_crypted));



    }
}
