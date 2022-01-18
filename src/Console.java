import java.util.Scanner;

public class Console {


    public Console mainMenu(Console console) {

        System.out.println("\nWelcome to the Caesar_cipher application");

        int selection;

        do {
            System.out.println("\n[1] Encrypt/Decrypt");
            System.out.println("[2] File Analysis");
            System.out.println("[3] Quit");
            System.out.print("\nInsert selection: ");

            Scanner scanner = new Scanner(System.in);

            if (!scanner.hasNext()) {
                break;
            }
            if (scanner.hasNextInt())
                selection = scanner.nextInt();
            else {
                System.out.println("\nEnter a decimal digit!");

                selection = 0;
                continue;
            }

            switch (selection) {

                case 1: return console.submenu1(console);
                case 2: return console.submenu2(console);
                case 3: break;

                default:
                    System.out.println("\nThe selection was invalid!");
                    continue;
            }

            scanner.close();

        } while (selection != 3);

        return console;
    }

    private Console submenu1(Console console) {

        System.out.println("\nWelcome to the Encrypt/Decrypt");

        int selection=0;

        while (true) {

            System.out.println("\n[1] File encrypt");
            System.out.println("[2] File decrypt");
            System.out.println("[3] Return to main menu");
            System.out.print("\nInsert selection: ");

            Scanner scanner = new Scanner(System.in);

            if (!scanner.hasNext()) {
                break;
            }
            if (scanner.hasNextInt())
                selection = scanner.nextInt();
            else {

                System.out.println("\nEnter a decimal digit!");

                continue;
            }

            scanner.close();

            switch (selection) {
                case 1: Encrypt.encrypt(); break;
                case 2: return console.submenu1(console);
                case 3: return console.mainMenu(console);

                default:
                    System.out.println("\nThe selection was invalid!");
                    return console.submenu1(console);
            }

        }

        return console;

    }

    private Console submenu2(Console console) {

        System.out.println("\nWelcome to the file Analyse");
        int selection;

        do {

            System.out.println("\n[1] Brute Force");
            System.out.println("[2] Statistical analysis");
            System.out.println("[3] Return to main menu");
            System.out.print("\nInsert selection: ");

            Scanner scanner = new Scanner(System.in);

            if (!scanner.hasNext()) {
                break;
            }
            if (scanner.hasNextInt())
                selection = scanner.nextInt();
            else {
                System.out.println("\nEnter a decimal digit!");
                continue;
            }

            scanner.close();

            switch (selection) {
                case 1: return console.submenu2(console);
                case 2: return console.submenu2(console);
                case 3: return console.mainMenu(console);

                default:
                    System.out.println("\nThe selection was invalid!");
                    return console.submenu2(console);
            }
        } while (true);

        return console;
    }

}

