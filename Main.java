package banking;


public class Main {

    public static void main(String[] args) {
        Bank bank = new Bank("jdbc:sqlite:" + args[1]);
        bank.run();
    }
}
