import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public  class Encrypt {


    private final static String ALPHABET = "АБВГДЕЁЖЗИКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзиклмнопрстуфхцчшщъыьэя.,”':-!? abc";

    public static void encode() {  // Шифрование

        int key_length = ALPHABET.length()-1;
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter path to source file: ");
        // Path path = Paths.get(scanner.nextLine());
        Path path = Paths.get("C:\\caesar_cipher\\src\\words.txt");
        System.out.print("\nEnter digit key (1 - " + key_length + "): ");

        int shift = 0;

        while (scanner.hasNextInt()) {
            try {
                shift = scanner.nextInt();

                if (shift<=key_length) {
                  break;}
                else {
                    System.out.println("\nKey must be less than "+ key_length); }

            } catch (Exception e) {
                System.out.println("\nThe selection was invalid!");
            }
        }



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


}

