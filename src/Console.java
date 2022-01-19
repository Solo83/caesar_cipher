import java.util.Scanner;

public class Console { // класс для консольного меню

    private static Console instance;

    private Console() {
    }

    public static Console getInstance() {
        if (instance == null) {
            instance = new Console();
        }
        return instance;
    }

    public void mainMenu() {  // главное меню консоли

        System.out.println("\nWelcome to the Caesar_cipher application");

        int selection = 0;

        {
            System.out.println("\n[1] Encrypt/Decrypt");
            System.out.println("[2] File Analysis");
            System.out.println("[3] Quit");
            System.out.print("\nInsert selection: ");

            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNextInt()) {
                try {
                    selection = scanner.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("\nThe selection was invalid!");
                }
            }

            switch (selection) {

                case 1:
                    submenu1();
                    break;
                case 2:
                    submenu2();
                    break;
                case 3:
                    break;

                default:
                    System.out.println("\nThe selection was invalid!");
                    mainMenu();

            }
        }
    }

    private void submenu1() { //вспомогательное меню 1 Шифрование / Дешифровка


        System.out.println("\nWelcome to the Encrypt/Decrypt");

        int selection = 0;

        System.out.println("\n[1] File encrypt");
        System.out.println("[2] File decrypt");
        System.out.println("[3] Return to main menu");
        System.out.print("\nInsert selection: ");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextInt()) {

            try {
                selection = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("\nThe selection was invalid!");
            }
        }

        switch (selection) {
            case 1:
                Encrypt.encode();
                submenu1();
                break;
            case 2:
                Encrypt.decode();
                submenu1();
                break;
            case 3:
                mainMenu();
                break;

            default:
                System.out.println("\nThe selection was invalid!");
                submenu1();
        }

    }

    private void submenu2() { //вспомогательное меню 2 Брутфорс / Анализ

        int selection = 0;

        System.out.println("\nWelcome to the file Analyse");
        System.out.println("\n[1] Brute Force");
        System.out.println("[2] Statistical analysis");
        System.out.println("[3] Return to main menu");
        System.out.print("\nInsert selection: ");

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextInt()) {
            try {
                selection = scanner.nextInt();
                break;
            } catch (Exception e) {
                System.out.println("\nThe selection was invalid!");
            }
        }

        switch (selection) {
            case 1:
                submenu2();
                break;
            case 2:
                submenu2();
                break;
            case 3:
                mainMenu();
                break;

            default:
                System.out.println("\nThe selection was invalid!");
                submenu2();
        }
    }

}


