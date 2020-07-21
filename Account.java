package banking;

import java.util.Scanner;

public class Account {

    private Scanner sc;
    private final String card;
    private int balance;

    public Account(String cn, int balance, Scanner sc) {
        this.card = cn;
        this.balance = balance;
        this.sc = sc;
    }

    protected void updateBalance(long amount) {
        this.balance += amount;
    }

    public int getBalance() {
        return this.balance;
    }

    public String getCard() {
        return this.card;
    }

    public int interfaceOfAccount() {
        int input;
        do {
            printLoggedTable();
            input = Integer.parseInt(sc.nextLine());
            System.out.println();
            if (input == 1) {
                System.out.println("Balance: " + getBalance() + "\n");
            } else if (input == 2) {
                addIncomeOp();
            } else if (input == 3) {
                transferOp();
            } else if (input == 4) {
                closeAccountOp();
                break;
            } else if (input == 5) {
                System.out.println("You have successfully logged out!\n");
                break;
            }
        } while (input != 0);

        return input;
    }

    private void transferOp() {

        System.out.println("Transfer\n Enter card number:");
        String cn = sc.nextLine();
        if (Bank.checkValidity(cn) && DataBase.getInstance(null).checkAccount(cn)) {
            int amount = Integer.parseInt(sc.nextLine());
            if (amount > this.getBalance()) {
                System.out.println("Not enough money!");
            } else {
                DataBase.getInstance(null).makeTransfer(this.getCard(), cn, amount);
            }
        }
    }

    private void addIncomeOp() {
        int amount = Integer.parseInt(sc.nextLine());
        if (amount < 0) {
            System.out.println("Can't add negative income");
        } else {
            this.updateBalance(amount);
            DataBase.getInstance(null).addIncome(this.getCard(), amount);
        }
    }

    private int closeAccountOp() {
        DataBase.getInstance(null).closeAccount(this.getCard());
        System.out.println("The account has been closed!");
        return 2;
    }

    private void printLoggedTable(){
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

}
