import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Encrypt {

    private final static String ALPHABET = "1234567890АБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя.,”':-!? ";

    public static void file_encode() {  // Шифрование

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nВведите путь к файлу, который требуется зашифровать: ");

        // Path path = Paths.get(scanner.nextLine());
        Path path = Paths.get("C:\\caesar_cipher\\src\\words.txt");
        System.out.print("\nВведите ключ шифрования: ");
        int shift = scanner.nextInt();
        Path encodedFile = path.getParent().resolve("encode.txt");

        if (!Files.exists(encodedFile)) {
            try {
                Files.createFile(path.getParent().resolve("encode.txt"));
            } catch (IOException e) {
                System.out.println("\nНевозможно создать файл!");
            }
        }

        try (
                BufferedReader input = Files.newBufferedReader(path);
                BufferedWriter output = Files.newBufferedWriter(encodedFile)) {

            while (input.ready()) {

                String line = input.readLine();
                output.write(caesar_encode(line, shift) + '\n');
            }
            System.out.println("\nПуть к зашифрованному файлу: " + encodedFile);

        } catch (FileNotFoundException e) {
            System.out.println("\nФайл не найден!");
        } catch (IOException e) {
            System.out.println("\nОшибка I/O Error");
        }
    }

    public static void file_decode() { // Дешифровка

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nВведите путь к зашифрованному файлу: ");

        // Path path = Paths.get(scanner.nextLine());
        Path path = Paths.get("C:\\caesar_cipher\\src\\encode.txt");
        System.out.print("\nВведите ключ дешифровки: ");
        int shift = scanner.nextInt();
        Path decodedFile = path.getParent().resolve("decode.txt");

        if (!Files.exists(decodedFile)) {
            try {
                Files.createFile(path.getParent().resolve("decode.txt"));
            } catch (IOException e) {
                System.out.println("\nНевозможно создать файл!");
            }
        }

        try (
                BufferedReader input = Files.newBufferedReader(path);
                BufferedWriter output = Files.newBufferedWriter(decodedFile)) {

            while (input.ready()) {

                String line = input.readLine();
                output.write(caesar_decode(line, shift) + '\n');
            }
            System.out.println("\nOutput file path: " + decodedFile);

        } catch (FileNotFoundException e) {
            System.out.println("\nФайл не найден!");
        } catch (IOException e) {
            System.out.println("\nОшибка I/O Error");
        }
    }

    private static String caesar_encode(String text, int shift) {

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

    private static String caesar_decode(String text, int shift) { // Алгоритм

        StringBuilder result = new StringBuilder(text.length());
        int replace;

        for (int i = 0; i < text.length(); i++) {
            int index = ALPHABET.indexOf(text.charAt(i));
            if (index >= 0) { //проверить есть ли символ в алфавите
                replace = (shift * -1 + index) % ALPHABET.length(); //положение шифрованного символа относительно индекса вхождения в алфавит со смещением
                if (replace < 0) { //расчет для отрицательного индекса
                    replace = replace + ALPHABET.length();
                }
                result.append(ALPHABET.charAt(replace));
            } else result.append(text.charAt(i));
        }
        return result.toString();
   }

    public static void analysis() { // Статистический анализ

        Path path_Crypted = Paths.get("C:\\caesar_cipher\\src\\encode.txt");
        Path path_Original = Paths.get("C:\\caesar_cipher\\src\\words.txt");
        Path path_Decrypted = Paths.get("C:\\caesar_cipher\\src\\decode.txt");


        Map<Character, Integer> map_crypted = new TreeMap<>();
        Map<Character, Integer> map_original = new TreeMap<>();
        Map<Character, Character> merged_Map = new TreeMap<>();

        try (
                BufferedReader input_crypto = Files.newBufferedReader(path_Crypted)) {

            while (input_crypto.ready()) {

                String crypted_String = input_crypto.readLine();

                for (int i = 0; i < crypted_String.length(); i++) {
                    char c = crypted_String.charAt(i);
                    for (Character character : ALPHABET.toCharArray()) {
                        if (character == c) {
                            map_crypted.merge(character, 1, Integer::sum);}
                    }
                }
            }

            try (BufferedReader input_original = Files.newBufferedReader(path_Original)) {

                while (input_original.ready()) {

                    String original_String = input_original.readLine();

                    for (int i = 0; i < original_String.length(); i++) {
                        char c = original_String.charAt(i);
                        for (Character character : ALPHABET.toCharArray()) {
                            if (character == c) {
                                map_original.merge(character, 1, Integer::sum);}
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }

        List<Map.Entry<Character, Integer>> list_original = map_original.entrySet().stream()
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());

        List<Map.Entry<Character, Integer>> list_crypted = map_crypted.entrySet().stream()
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());

        for (int i = 0; i < list_original.size(); i++) {
            merged_Map.put(list_original.get(i).getKey(), list_crypted.get(i).getKey());
        }

        try (
                BufferedReader input_crypto = Files.newBufferedReader(path_Crypted);
                BufferedWriter output_crypto = Files.newBufferedWriter(path_Decrypted)) {

            while (input_crypto.ready()) {

                String crypted_String = input_crypto.readLine();
                StringBuilder sbdecrypt = new StringBuilder();

                for (int i = 0; i < crypted_String.length(); i++) {
                    char c = crypted_String.charAt(i);
                      for (Character character : ALPHABET.toCharArray()) {
                        if (character == c)   {
                            for (Map.Entry<Character, Character> characterCharacterEntry : merged_Map.entrySet()) {
                                if (characterCharacterEntry.getValue() == c) {
                                    sbdecrypt.append(characterCharacterEntry.getKey());
                                 }
                            }
                        }
                      }
                }
                output_crypto.write(sbdecrypt.toString() + '\n');
            }

            System.out.println("\nOutput file path: " + path_Decrypted);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void bruteforce() {

        Path path_Crypted = Paths.get("C:\\caesar_cipher\\src\\encode.txt");

        StringBuilder sbdecrypt;

        int key;
        int i;
        int index;
        char currentchar;
        char newchar;


        try (
                BufferedReader input_crypto = Files.newBufferedReader(path_Crypted)) {

            while (input_crypto.ready()) {

                String encryptmessage = input_crypto.readLine();

                //Loop through the keys in the alphabet.
                for (key = 1; key < ALPHABET.length(); key++) {

                    sbdecrypt = new StringBuilder(encryptmessage);
                    for (i = 0; i < sbdecrypt.length(); i++) {

                        currentchar = sbdecrypt.charAt(i);
                        index = ALPHABET.indexOf(currentchar);

                        //If the currentchar is in the alphabet
                        if (index != -1) {
                            //Reduce the character by the key in the alphabet
                            index = index - key;
                            //If the character goes below 0, go back to the end of the alphabet
                            if (index < 0) {
                                index = index + ALPHABET.length();
                                //Get the new character in the alphabet
                                newchar = ALPHABET.charAt(index);
                                //Set the character in the stringbuilder
                                sbdecrypt.setCharAt(i, newchar);
                            } else {
                                //Get the new character in the alphabet
                                newchar = ALPHABET.charAt(index);
                                //Set the character in the stringbuilder
                                sbdecrypt.setCharAt(i, newchar);
                            }
                        }
                    }

                    if (sbdecrypt.toString().contains(". ") & sbdecrypt.toString().contains(", ")) {
                        System.out.println("Key: " + key + " Decrypted String: " + sbdecrypt); //побел + запятая + пробел + точка,

                    }

                }
                //Print the key and the resulting string
            }

        } catch (IOException e) {
            System.out.println("\nI/O Error");
        }
    }
}
