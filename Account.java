import java.text.DecimalFormat;
import java.util.ArrayList;

public class Account {
    private String name;
    private String uuid;
    private User holder;
    private double balance;
    private ArrayList<Transaction> transactions;

    public Account(String name, User holder, Bank bank){
        this.name = name;
        this.holder = holder;
        this.balance = 0;

        this.uuid = bank.getNewAccountUUID();
        bank.addAccount(this);
        this.transactions = new ArrayList<Transaction>();
        System.out.printf("New account created with name: %s and UUID: %s\n", this.name, uuid);
    }

    /**
     * Returns the UUID of the account.
     * @return account uuid
     */
    public String getUUID(){
        return uuid;
    }

    /**
     * Adds a transaction to this account.
     * @param amount
     */
    public void addTransaction(double amount){
        transactions.add(new Transaction(amount, this));
        balance += amount;
    }

    /**
     * Adds a transaction to this account with a memo.
     * @param amount
     * @param memo
     */
    public void addTransaction(double amount, String memo){
        transactions.add(new Transaction(amount, this, memo));
        balance += amount;
    }

    /**
     * Returns the current balance of the account.
     * @return
     */
    public double getBalance(){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(balance));
    }

    /**
     * Returns a string containing a summary of transactions.
     * @return summary
     */
    public String getSummaryLine(){
        return String.format("%s : $%.2f : %s", uuid, this.getBalance(), name);
    }

    /**
     * Prints the transaction history of this account.
     */
    public void printTransactionHistory(){
        if(transactions.size() == 0){
            System.out.println("No transactions in Account " + uuid);
        } else {
            String output = "Transaction History for account " + uuid + "\n";
            for (Transaction t : transactions) {
                output += t.getSummaryLine();
            }
            System.out.print(output);
        }
    }
}
