import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Encrypt {

    private final static String ALPHABET = "1234567890АБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя.,”':-!? ";

    public static void file_encode() {  // Шифрование

        System.out.print("\nВведите путь к файлу, который требуется зашифровать: ");
        Path path = Paths.get(new Scanner(System.in).nextLine());
        System.out.print("\nВведите ключ шифрования: ");
        int shift = new Scanner(System.in).nextInt();
        Path pathEncrypted = Paths.get(String.valueOf(path.resolve(path.getParent() + "\\caesarEncoded.txt")));

        try (
                BufferedReader input = Files.newBufferedReader(path);
                BufferedWriter output = Files.newBufferedWriter(pathEncrypted)) {

            while (input.ready()) {

                String line = input.readLine();
                output.write(caesarEncode(line, shift) + '\n');
            }
            System.out.println("\nШифрование завершено: " + pathEncrypted);

        } catch (IOException e) {
            e.printStackTrace();
            }
    }

    public static void file_decode() { // Дешифровка


        System.out.print("\nВведите путь к зашифрованному файлу: ");
        Path path = Paths.get(new Scanner(System.in).nextLine());
        System.out.print("\nВведите ключ для дешифровки: ");
        int shift = new Scanner(System.in).nextInt();
        Path pathDecrypted = Paths.get(String.valueOf(path.resolve(path.getParent() + "\\caesarDecoded.txt")));

        try (
                BufferedReader input = Files.newBufferedReader(path);
                BufferedWriter output = Files.newBufferedWriter(pathDecrypted)) {

            while (input.ready()) {

                String line = input.readLine();
                output.write(caesarEncode(line, shift * -1) + '\n');
            }
            System.out.println("\nРасшифрованный файл успешно создан: " + pathDecrypted);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void analysis() { // Метод статистического анализа

        System.out.print("\nВведите путь к зашифрованному файлу: ");
        Path path_Crypt = Paths.get(new Scanner(System.in).nextLine());
        System.out.print("\nВведите путь к словарю с данными для анализа: ");
        Path path_Original = Paths.get(new Scanner(System.in).nextLine());
        Path path_Decrypted = Paths.get(String.valueOf(path_Crypt.resolve(path_Crypt.getParent() + "\\statisticDecoded.txt")));

        Map<Character, Integer> mapCrypted = new TreeMap<>();
        Map<Character, Integer> mapOriginal = new TreeMap<>();
        Map<Character, Character> mapMerged = new TreeMap<>();

        try (
                BufferedReader inputCrypto = Files.newBufferedReader(path_Crypt)) {

            while (inputCrypto.ready()) {

                String cryptedString = inputCrypto.readLine();

                for (int i = 0; i < cryptedString.length(); i++) {
                    char c = cryptedString.charAt(i);
                    for (Character character : ALPHABET.toCharArray()) {
                        if (character == c) {
                            mapCrypted.merge(character, 1, Integer::sum);   // Мар с количеством вхождений символов зашифрованного файла
                        }
                    }
                }
            }

            try (BufferedReader inputOriginal = Files.newBufferedReader(path_Original)) {

                while (inputOriginal.ready()) {

                    String originalString = inputOriginal.readLine();

                    for (int i = 0; i < originalString.length(); i++) {
                        char c = originalString.charAt(i);
                        for (Character character : ALPHABET.toCharArray()) {
                            if (character == c) {
                                mapOriginal.merge(character, 1, Integer::sum); // Мар с количеством вхождений символов словаря
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }

        List<Map.Entry<Character, Integer>> listOriginal = mapOriginal.entrySet().stream()  // Сортировка данных в Map по убыванию
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());

        List<Map.Entry<Character, Integer>> listCrypted = mapCrypted.entrySet().stream()
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());

        for (int i = 0; i < listOriginal.size(); i++) {                                        // Сборка новой Map по символам с совпадающим количеством вхождений (по-порядку из отсортированных MAP)
            if (i > listCrypted.size() - 1) {                                                  // Условие, если в словаре будет большее количество символов алвавита чем в зашифрованном тексте
                mapMerged.put(listOriginal.get(i).getKey(), listOriginal.get(i).getKey());
            } else {
                mapMerged.put(listOriginal.get(i).getKey(), listCrypted.get(i).getKey());
            }
        }

        try (
                BufferedReader inputCrypto = Files.newBufferedReader(path_Crypt);
                BufferedWriter outputCrypto = Files.newBufferedWriter(path_Decrypted)) {

            while (inputCrypto.ready()) {

                String crypted_String = inputCrypto.readLine();
                StringBuilder sbdecrypt = new StringBuilder();

                for (int i = 0; i < crypted_String.length(); i++) {
                    char c = crypted_String.charAt(i);
                    for (Map.Entry<Character, Character> characterCharacterEntry : mapMerged.entrySet()) {
                        if (characterCharacterEntry.getValue() == c) {
                            sbdecrypt.append(characterCharacterEntry.getKey());   // собираем новую строку из зашифрованной подменяя совпадающие символы из ключей символами из значений (key-value)
                        }
                    }
                    if (ALPHABET.indexOf(c) < 0) {
                        sbdecrypt.append(c); // если символа нет в алфавите, он добавляется из строки с зашифрованным текстом  без изменений
                    }
                }
                outputCrypto.write(sbdecrypt.toString() + '\n');
            }
            System.out.println("\nФайл успешно создан: " + path_Decrypted);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void bruteforce() { //Метод брутфорса

        System.out.print("\nВведите путь к зашифрованному файлу: ");
        Path pathCrypted = Paths.get(new Scanner(System.in).nextLine());
        Path pathDecrypted = Paths.get(String.valueOf(pathCrypted.resolve(pathCrypted.getParent() + "\\bruteforceDecoded.txt")));

        int key;
        ArrayList<String> arraylist = new ArrayList<>();
        Pattern p = Pattern.compile("[^аеёиоуыэюя\\s.,!*&?]{7,}");

        try (Scanner scanner = new Scanner(pathCrypted);
             BufferedWriter output_crypto = Files.newBufferedWriter(pathDecrypted)
        ) {
            String cryptoMessage = scanner.useDelimiter("\\A").next();

            if (cryptoMessage.length() < 100) { // если сообщение меньше 100 символов выводим список ключ/значение для самостоятельного выбора
                System.out.println("\nКорректный ключ в списке:");
                for (key = 1; key < ALPHABET.length(); key++) {
                    System.out.println("Ключ: " + key + " Шифр: " + caesarEncode(cryptoMessage, key * -1).substring(0, 50));
                }

            }else {

                for (key = 1; key < ALPHABET.length(); key++) {
                    arraylist.add(caesarEncode(cryptoMessage, key * -1).substring(cryptoMessage.length()/2, cryptoMessage.length()/2+200)); //200 символов из середины текста
                }
                {
                    for (String s : arraylist) {

                        Matcher m = p.matcher(s);
                        {
                            if (!m.find()) {

                                System.out.println("\nКлюч найден: " + (arraylist.indexOf(s) + 1));
                                output_crypto.write(caesarEncode(cryptoMessage, (arraylist.indexOf(s) + 1) * -1));
                                System.out.println("\nРасшифровка завершена: " + pathDecrypted);
                                break;

                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String caesarEncode(String text, int shift) {

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
