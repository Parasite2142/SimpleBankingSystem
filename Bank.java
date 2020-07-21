package banking;

import java.util.Scanner;

public class Bank {
    private Scanner sc = new Scanner(System.in);
    private DataBase db;

    public Bank(String url) {
        this.db = DataBase.getInstance(url);
    }

    private void createAccount() {
        db.addAccountToBase(generateCardCS(), generatePin());
    }

    private String generateCardCS() {
        StringBuilder cn = new StringBuilder("400000");
        int[] table = new int[15];
        table[0] = 4;
        for (int i = 6; i < 15; i++) {
            table[i] = (int)(Math.random() * 10);
            cn.append(table[i]);
        }
        cn.append(luhnAlgorithmCheckSum(table));

        return cn.toString();
    }

    private String generatePin() {
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            pin.append((int)(Math.random() * 10));
        }
        return pin.toString();
    }

    private static int luhnAlgorithmCheckSum(int[] nums) {
        int sum = 0;
        int k = 0;

        for (int i = 1; i < 16; i++) {
            if (i % 2 == 1) {
                nums[i - 1] *= 2;
            }
            if (nums[i - 1] > 9) {
                nums[i - 1] -= 9;
            }
            sum += nums[i - 1];
        }

        for (int n = 1; n < 10; n++) {
            if ((sum + n) % 10 == 0) {
                k = n;
                break;
            }
        }

        return k;
    }

    private void printTable() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }

    public void run() {
        mainMenu();
        System.out.println("Bye !");
    }

    private void mainMenu() {
        int input;
        Account account;
        do {
            printTable();
            input = Integer.parseInt(sc.nextLine());
            System.out.println();
            if (input == 1) {
                this.createAccount();
            } else if (input == 2) {
                String cn = sc.nextLine();
                if (checkValidity(cn)) {
                    account = db.logIntoAccount(cn,
                            sc.nextLine(), this.sc);
                    if (account == null) {
                        System.out.println("Such card does not exist.");
                    } else {
                        input = account.interfaceOfAccount();
                        account = null;
                    }
                } else {
                    System.out.println("Probably you made mistake in " +
                            "the card number. Please try again!");
                }
            }
        } while (input != 0);

    }

    protected static boolean checkValidity(String cn) {
        if (cn.length() != 16) {
            return false;
        }
        int[] nums = new int[16];
        String[] arrays = cn.split("");
        int sum = 0;
        for (int i = 1; i < cn.length(); i++) {
            nums[i - 1] = Integer.parseInt(arrays[i - 1]);
            if (i % 2 == 1) {
                nums[i - 1] *= 2;
            }
            if (nums[i - 1] > 9) {
                nums[i - 1] -= 9;
            }
            sum += nums[i - 1];
        }
        sum += Integer.parseInt(arrays[15]);
        return sum % 10 == 0;
    }


}
