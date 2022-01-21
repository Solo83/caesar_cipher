import java.util.Scanner;

public class MainMenu { // класс для консольного меню

    private static MainMenu instance;

    private MainMenu() {
    }

    public static MainMenu getInstance() {

        if (instance == null) {
            instance = new MainMenu();
        }
        return instance;
    }

    public void mainMenu() {  // главное меню консоли

        System.out.println("\nДобро пожаловать в приложение Шифр Цезаря");

        int selection = 0;

        {
            System.out.println("\n[1] Зашифровать файл");
            System.out.println("[2] Расшифровать файл");
            System.out.println("[3] Взлом методом BruteForce");
            System.out.println("[4] Статистический анализ");
            System.out.println("[5] Выход");
            System.out.print("\nВыберите раздел: ");

            Scanner scanner = new Scanner(System.in);

            while (scanner.hasNextInt()) {
                try {
                    selection = scanner.nextInt();
                    break;
                } catch (Exception e) {
                    System.out.println("\nНекорректный ввод");
                }
            }

            switch (selection) {

                case 1:
                    Encrypt.file_encode();
                    mainMenu();
                    break;
                case 2:
                    Encrypt.file_decode();
                    mainMenu();
                    break;
                case 3:
                    Encrypt.bruteforce();
                    mainMenu();
                    break;
                case 4:
                    Encrypt.analysis();
                    mainMenu();
                    break;
                case 5:
                    break;

                default:
                    System.out.println("\nНекорректный ввод");
                    mainMenu();

            }
        }
    }

}


