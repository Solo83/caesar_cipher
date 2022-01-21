import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Encrypt {

    private final static String ALPHABET = "1234567890АБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя.,”':-!? ";

    public static void file_encode() {  // Шифрование

        System.out.print("\nВведите путь к файлу, который требуется зашифровать: ");
        Path path = Paths.get(new Scanner(System.in).nextLine());
        System.out.print("\nВведите ключ шифрования: ");
        int shift = new Scanner(System.in).nextInt();
        System.out.print("\nВведите путь к зашифрованному файлу: ");
        Path encodedFile = Paths.get(new Scanner(System.in).nextLine());

        try (
             BufferedReader input = Files.newBufferedReader(path);
             BufferedWriter output = Files.newBufferedWriter(encodedFile)) {

            while (input.ready()) {

                String line = input.readLine();
                output.write(caesar_encode(line, shift) + '\n');
            }
            System.out.println("\nФайл успешно создан" + encodedFile);

        } catch (FileNotFoundException e) {
            System.out.println("\nФайл не найден!");
        } catch (IOException e) {
            System.out.println("\nОшибка I/O Error");
        }
    }

    public static void file_decode() { // Дешифровка


        System.out.print("\nВведите путь к зашифрованному файлу: ");
        Path path = Paths.get(new Scanner(System.in).nextLine());
        System.out.print("\nВведите ключ дешифровки: ");
        int shift = new Scanner(System.in).nextInt();
        System.out.print("\nВведите путь к расшифрованному файлу: ");
        Path decodedFile = Paths.get(new Scanner(System.in).nextLine());

        try (
                BufferedReader input = Files.newBufferedReader(path);
                BufferedWriter output = Files.newBufferedWriter(decodedFile)) {

            while (input.ready()) {

                String line = input.readLine();
                output.write(caesar_encode(line, shift*-1) + '\n');
            }
            System.out.println("\nФайл успешно создан: " + decodedFile);

        } catch (FileNotFoundException e) {
            System.out.println("\nФайл не найден!");
        } catch (IOException e) {
            System.out.println("\nОшибка I/O Error");
        }
    }


    public static void analysis() { // Метод статистического анализа

        System.out.print("\nВведите путь к зашифрованному файлу: ");
        Path path_Crypt = Paths.get(new Scanner(System.in).nextLine());
        System.out.print("\nВведите путь к словарю с данными для анализа: ");
        Path path_Original = Paths.get(new Scanner(System.in).nextLine());
        System.out.print("\nВведите путь к расшифрованному файлу: ");
        Path path_Decrypted = Paths.get(new Scanner(System.in).nextLine());


        Map<Character, Integer> map_crypted = new TreeMap<>();
        Map<Character, Integer> map_original = new TreeMap<>();
        Map<Character, Character> merged_Map = new TreeMap<>();

        try (
                BufferedReader input_crypto = Files.newBufferedReader(path_Crypt)) {

            while (input_crypto.ready()) {

                String crypted_String = input_crypto.readLine();

                for (int i = 0; i < crypted_String.length(); i++) {
                    char c = crypted_String.charAt(i);
                    for (Character character : ALPHABET.toCharArray()) {
                        if (character == c) {
                            map_crypted.merge(character, 1, Integer::sum);   // Мар с количеством вхождений символов зашифрованного файла
                        }
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
                                map_original.merge(character, 1, Integer::sum); // Мар с количеством вхождений символов словаря
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }

        List<Map.Entry<Character, Integer>> list_original = map_original.entrySet().stream()  // Сортировка данных в Map по убыванию
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());

        List<Map.Entry<Character, Integer>> list_crypted = map_crypted.entrySet().stream()
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());

        for (int i = 0; i < list_original.size(); i++) {                                        // Сборка новой Map по символам с совпадающим количеством вхождений (по-порядку из отсортированных MAP)
            if (i > list_crypted.size() - 1) {                                                  // Условие, если в словаре будет большее количество символов алвавита чем в зашиврованном тексте
                merged_Map.put(list_original.get(i).getKey(), list_original.get(i).getKey());
            } else {
                merged_Map.put(list_original.get(i).getKey(), list_crypted.get(i).getKey());
            }
        }

        try (
                BufferedReader input_crypto = Files.newBufferedReader(path_Crypt);
                BufferedWriter output_crypto = Files.newBufferedWriter(path_Decrypted)) {

            while (input_crypto.ready()) {

                String crypted_String = input_crypto.readLine();
                StringBuilder sbdecrypt = new StringBuilder();

                for (int i = 0; i < crypted_String.length(); i++) {
                    char c = crypted_String.charAt(i);
                    for (Map.Entry<Character, Character> characterCharacterEntry : merged_Map.entrySet()) {
                        if (characterCharacterEntry.getValue() == c) {
                            sbdecrypt.append(characterCharacterEntry.getKey());   // собираем новую строку из зашифрованной подменяя совпадающие символы из ключей символами из значений (key-value)
                        }
                    }
                    if (ALPHABET.indexOf(c) < 0) {
                        sbdecrypt.append(c); // если символа нет в алфавите, он добавляется из строки с зашифрованным текстом  без изменений
                    }
                }
                output_crypto.write(sbdecrypt.toString() + '\n');
            }

            System.out.println("\nФайл успешно создан: " + path_Decrypted);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void bruteforce() { //Метод брутфорса

        Path path_Crypted = Paths.get("C:\\caesar_cipher\\src\\encode.txt");
        int key;

        List <String> decrypted = new ArrayList<>();

        try {
            List<String> lines =  Files.readAllLines(path_Crypted);

            for (String line : lines) {

            }


            System.out.println(lines);

            for (String line : lines) {
                decrypted.add(caesar_encode(line, -5));
            }

            System.out.println(decrypted);
           /* for (key = 1; key < ALPHABET.length(); key++) {

                for (String line : lines) {
                    decrypted.add(caesar_encode(line, key * -1));
                }

                System.out.println(decrypted);

            }*/


        } catch (IOException e) {
            e.printStackTrace();
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


}
