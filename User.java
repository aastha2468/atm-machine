import java.util.ArrayList;

public class User {
    private String firstName;
    private String lastName;
    private String pin;
    private String uuid;
    private ArrayList<Account> accounts;

    /**
     * Creates a user with a first and last name, a pin number and their associated bank.
     * @param first
     * @param last
     * @param pin
     * @param bank
     */
    public User(String first, String last, String pin, Bank bank){
        this.firstName = first;
        this.lastName = last;
        this.pin = pin;
        this.accounts = new ArrayList<Account>();
        this.uuid = bank.getNewUserUUID();

        bank.addUser(this);
        System.out.printf("User %s %s created with pin number: %s and UUID: %s\n",
                firstName, lastName, this.pin, uuid);
    }

    /**
     * Returns the UUID.
     * @return uuid
     */
    public String getUUID(){
        return uuid;
    }

    /**
     * Returns the pin.
     * @return pin
     */
    public String getPin(){
        return pin;
    }

    /**
     * Returns this user's full name.
     * @return full name
     */
    public String getName(){
        return firstName + " " + lastName;
    }

    /**
     * Adds a new account to this specific user.
     * @param newAccount
     */
    public void addAccount(Account newAccount){
        accounts.add(newAccount);
    }

    /**
     * Returns the total number of accounts held by this user.
     * @return number of accounts
     */
    public int numAccounts(){
        return accounts.size();
    }

    /**
     * Returns the balance of the account at the provided index.
     * @param index
     * @return account balance
     */
    public double getAccountBalance(int index){
        return accounts.get(index).getBalance();
    }

    /**
     * Returns the UUID of the account at the provided index.
     * @param index
     * @return account UUID
     */
    public String getAccountUUID(int index){
        return accounts.get(index).getUUID();
    }

    /**
     * Prints the transaction history of the account the provided index.
     * @param index
     */
    public void printAccountTransactionHistory(int index){
        accounts.get(index).printTransactionHistory();
    }

    /**
     * Creates a transaction within the account at the provided index.
     * If the memo parameter is null, then a transaction without a memo will be created.
     * @param index
     * @param amount
     * @param memo
     */
    public void addAccountTransaction(int index, double amount, String memo){
        if(memo != null)
            accounts.get(index).addTransaction(amount, memo);
        else
            accounts.get(index).addTransaction(amount);
    }

    /**
     * Checks if the pin is valid.
     * @param pin
     * @return true if the provided pin matches; false otherwise
     */
    public boolean validatePin(String pin){
        return pin.equals(this.pin);
    }

    /**
     * Prints a summary of all the user's accounts
     */
    public void printAccountsSummary(){
        if(this.numAccounts() == 0){
            System.out.printf("%s %s has no accounts.\n", firstName, lastName);
        } else {
            String output = String.format("%s %s's Accounts Summary\n", firstName, lastName);
            int i = 1;
            for (Account account : accounts) {
                output += String.format("%d. %s\n", i, account.getSummaryLine());
                i++;
            }
            System.out.print(output);
        }
    }


}