import java.util.ArrayList;
import java.util.Scanner;

public class ATM {
    public static void main(String[] args){
        boolean quit;
        ArrayList<Bank> banks = createBanks();
        Bank currentBank;
        Scanner scan = new Scanner(System.in);
        User currentUser;

        while(true){ //Infinite Loop
            quit = false;
            System.out.println("Welcome to the ATM");

            // Retrieve the bank object that corresponds to the user's bank
            currentBank = getCurrentBank(banks, scan);
            if(currentBank == null) { // Check if the user wanted to shut down the ATM
                break;
            }
            System.out.println("Welcome to " + currentBank.getName());

            // Have the user log in or create a new account
            currentUser = getCurrentUser(currentBank, scan);
            if(currentUser == null)
                continue;

            // Main Loop
            while(!quit){
                System.out.print("\nWhat would you like to do?\n" +
                        "1. Show account transaction history\n" +
                        "2. Show accounts summary\n" +
                        "3. Add account\n" +
                        "4. Withdraw\n" +
                        "5. Deposit\n" +
                        "6. Transfer\n" +
                        "7. Quit\n" +
                        "Enter your choice: ");
                int input = getValidInt(0, 8, scan);
                switch (input){
                    case -1: // Invalid input
                        System.out.println("Please enter a number between 1 and 7.");
                        break;
                    case 1: // Show account transaction history
                        showTransactionHistory(currentUser, scan);
                        break;
                    case 2: // Show accounts summary
                        currentUser.printAccountsSummary();
                        break;
                    case 3: // Add account
                        System.out.println("What would you like to name your account?");
                        String name = scan.nextLine();
                        name = removeSurroundingSpaces(name);
                        if(name.equals("x"))
                            break;
                        currentUser.addAccount(new Account(name, currentUser, currentBank));
                        break;
                    case 4: // Withdraw
                        withdraw(currentUser, scan);
                        break;
                    case 5: // Deposit
                        deposit(currentUser, scan);
                        break;
                    case 6: // Transfer
                        transfer(currentUser, scan);
                        break;
                    case 7: // Quit
                        quit = true;
                        break;
                }
            }
        }
        scan.close();
    }

    /**
     * Loops until the user provides a valid input. Returns the corresponding bank
     * or null if the user wants to shut down the ATM.
     * @param banks a list of banks that are in the system
     * @param scan
     * @return currentBank or null
     */
    public static Bank getCurrentBank(ArrayList<Bank> banks, Scanner scan) {
        while (true) {
            System.out.println("Please select your bank:");
            int totalBanks = 1;
            for (Bank bank : banks) { // Print out all the banks
                System.out.printf("%d. %s\n", totalBanks, bank.getName());
                totalBanks++;
            }
            System.out.println("Enter the corresponding number or enter 0 " +
                    "to power off the ATM");
            int index = getValidInt(-1, totalBanks, scan);
            switch (index) {
                case 0: // Shut down the ATM
                    return null;
                case -1: // Invalid input
                    System.out.println("Please enter a number between 0 and " + (totalBanks - 1) + ".");
                    break;
                default: // Select the correct bank
                    return banks.get(index - 1);
            }
        }
    }

    /**
     * Has the user login or create a new account. If the user wants to quit
     * the screen the method returns null.
     * @param currentBank
     * @param scan
     * @return currentUser or null if the user wants to quit
     */
    public static User getCurrentUser(Bank currentBank, Scanner scan){
        String userID, pin;
        User currentUser;
        while(true){
            pin = null;
            userID = null;
            System.out.print("\nWhat would you like to do?\n" +
                    "1. Log in\n2. Create a new user account\n3. Quit\n");
            int input = getValidInt(1, 4, scan);
            switch(input){
                case -1: // Invalid input
                    System.out.println("Please enter a number between 1 and 3");
                    break;
                case 1: // Log in
                    while(userID == null) { // get the user's ID
                        System.out.print("Enter your user ID: ");
                        userID = getValidString(6, scan);
                        if(userID == null)
                            System.out.println("Please enter a valid 6-digit user ID.");
                    }
                    if(userID.equals("x")) // check if the user wants to exit
                        break;
                    while(pin == null){ // get the user's pin
                        System.out.print("Enter your pin: ");
                        pin = getValidString(4, scan);
                        if(pin == null)
                            System.out.println("Please enter a valid 4-digit pin.");
                    }
                    if(pin.equals("x")) // check if the user wants to exit
                        break;
                    currentUser = currentBank.checkUserLogin(userID, pin);
                    if(currentUser == null){
                        System.out.println("Invalid Credentials.");
                    } else {
                        System.out.printf("Welcome %s.\n", currentUser.getName());
                        return currentUser;
                    }
                    break;
                case 2: // Create a new account
                    // get the user's first and last name
                    System.out.print("Enter your first name: ");
                    String firstName = null, lastName = null;
                    if(scan.hasNext())
                        firstName = scan.nextLine();
                    firstName = removeSurroundingSpaces(firstName);
                    if(firstName.equals("x")) // check if the user wants to exit
                        break;
                    System.out.print("Enter your last name: ");
                    if(scan.hasNext())
                        lastName = scan.nextLine();
                    lastName = removeSurroundingSpaces(lastName);
                    if(lastName.equals("x")) // check if the user wants to exit
                        break;
                    while(pin == null) { // get the user's pin
                        System.out.print("Enter your preferred 4-digit pin: ");
                        pin = getValidString(4, scan);
                        if(pin == null)
                            System.out.println("Please enter a valid 4-digit pin.");
                    }
                    if(pin.equals("x")) // check if the user wants to exit
                        break;
                    currentUser = new User(firstName, lastName, pin, currentBank);
                    currentUser.printAccountsSummary();
                    return currentUser;
                case 3: // Quit
                    return null;
            }
        }
    }

    /**
     * Prints the transaction history for an account. If there are no accounts, then prints
     * "No Accounts. You need at least 1 account to show the transaction history."
     * @param currentUser
     * @param scan
     */
    public static void showTransactionHistory(User currentUser, Scanner scan){
        if(currentUser.numAccounts() < 1){
            System.out.println("No Accounts. You need at least 1 account to show the transaction history.");
        } else {
            int account = -1;
            while (account == -1) { // get a valid account number
                currentUser.printAccountsSummary();
                System.out.print("Enter which account you'd like to the transaction history of: ");
                account = getValidInt(0, currentUser.numAccounts() + 1, scan);
                if (account == -1) {
                    if (currentUser.numAccounts() == 1)
                        System.out.println("Please enter the number 1.");
                    else
                        System.out.println("Please enter a number between 1 and " +
                                (currentUser.numAccounts()) + ".");
                } else if(account == -2)
                    return;
            }
            currentUser.printAccountTransactionHistory(account - 1);
        }
    }

    /**
     * Allows the user to withdraw money from their account. If there are no accounts, then prints
     * "No Accounts. You need at least 1 account to withdraw from." This method will not allow a
     * user to withdraw from empty accounts, $0, or more than the money present in the account.
     * @param currentUser
     * @param scan
     */
    public static void withdraw(User currentUser, Scanner scan){
        if(currentUser.numAccounts() < 1){
            System.out.println("No Accounts. You need at least 1 account to withdraw from.");
        } else {
            int account = -1;
            double amount = -1;
            String memo;
            currentUser.printAccountsSummary();
            while (account == -1) { // get the account to withdraw from
                System.out.print("Enter which account you'd like to withdraw from: ");
                account = getValidInt(0, currentUser.numAccounts() + 1, scan);
                if (account == -1)
                    System.out.println("Please enter a number between 1 and " +
                            (currentUser.numAccounts() + 1) + ".");
            }
            if(account == -2) // check if the user wants to exit
                return;
            if(currentUser.getAccountBalance(account - 1) == 0){
                System.out.printf("No funds in Account %s to withdraw.\n",
                        currentUser.getAccountUUID(account - 1));
                return;
            }
            while (amount == -1) { // get a valid money amount to withdraw
                System.out.printf("Enter the amount to withdraw ($%.2f limit): $",
                        currentUser.getAccountBalance(account - 1));
                amount = getValidDouble(0, currentUser.getAccountBalance(account - 1), scan);
                if (amount == -1)
                    System.out.printf("Please enter an amount between 0 and %.2f.\n",
                            currentUser.getAccountBalance(account - 1));
                else if(amount == -2) // check if the user wants to exit
                    return;
            }
            System.out.print("Enter a memo: ");
            memo = scan.nextLine();
            memo = removeSurroundingSpaces(memo);
            if (memo.equals("")) {
                currentUser.addAccountTransaction(account - 1, -1 * amount, null);
            } else {
                currentUser.addAccountTransaction(account - 1, -1 * amount, memo);
            }
        }
    }

    /**
     * Allows the user to deposit no more than $1000. If there are no accounts, then prints
     * "No Accounts. You need at least 1 account to withdraw from."
     * @param currentUser
     * @param scan
     */
    public static void deposit(User currentUser, Scanner scan){
        if(currentUser.numAccounts() < 1){
            System.out.println("No Accounts. You need at least 1 account to deposit to.");
        } else {
            int account = -1;
            double amount = -1;
            String memo;
            currentUser.printAccountsSummary();
            while (account == -1) { // get the account to deposit to
                System.out.print("Enter which account you'd like to deposit to: ");
                account = getValidInt(0, currentUser.numAccounts() + 1, scan);
                if (account == -1)
                    System.out.println("Please enter a number between 1 and " +
                            (currentUser.numAccounts() + 1) + ".");
                else if(account == -2) // check if the user wants to exit
                    return;
            }
            while (amount == -1) { // get a valid deposit amount
                System.out.print("Enter the amount to deposit: $");
                amount = getValidDouble(0, 1000, scan);
                if (amount == -1)
                    System.out.println("Please enter an amount between 0 and 1,000.");
                else if(amount == -2) // check if the user wants to exit
                    return;
            }
            System.out.print("Enter a memo: ");
            memo = scan.nextLine();
            memo = removeSurroundingSpaces(memo);
            if (memo.equals("")) {
                currentUser.addAccountTransaction(account - 1, amount, null);
            } else {
                currentUser.addAccountTransaction(account - 1, amount, memo);
            }
            currentUser.printAccountsSummary();
        }
    }

    /**
     * Allows the user to transfer funds from one account to another. If there are fewer than 2 accounts,
     * then prints "Not enough accounts. You need at least 2 accounts in order to transfer." This method
     * doesn't allow the user to transfer more funds than the available funds in the account.
     * @param currentUser
     * @param scan
     */
    public static void transfer(User currentUser, Scanner scan){
        if(currentUser.numAccounts() < 2){
            System.out.println("Not enough accounts. You need at least 2 accounts in order to transfer.");
        } else{
            int accountFrom = -1, accountTo = -1;
            double amount = -1;
            currentUser.printAccountsSummary();
            while(accountFrom == -1) { // get the account to transfer from
                System.out.print("Enter which account you'd like to transfer from: ");
                accountFrom = getValidInt(0, currentUser.numAccounts() + 1, scan);
                if(accountFrom == -1)
                    System.out.println("Please enter a number between 1 and " +
                            (currentUser.numAccounts()+1) + ".");
                else if(accountFrom == -2) // check if the user wants to exit
                    return;
            }
            if(currentUser.getAccountBalance(accountFrom-1) == 0){ // Stop if there are no funds to transfer
                System.out.printf("No funds in Account %s to transfer.\n",
                        currentUser.getAccountUUID(accountFrom-1));
                return;
            }
            while(accountTo == -1) { // get the account to transfer to
                System.out.print("Enter which account you'd like to transfer to: ");
                accountTo = getValidInt(0, currentUser.numAccounts() + 1, scan);
                if(accountTo == -1)
                    System.out.println("Please enter a number between 1 and " +
                            (currentUser.numAccounts()+1) + ".");
                else if(accountTo == accountFrom){
                    System.out.println("Please enter a different account.");
                    accountTo = -1;
                }
                else if(accountTo == -2) // check if the user wants to exit
                    return;
            }
            while(amount == -1) { // get a valid amount to transfer
                System.out.printf("Enter the amount to transfer ($%.2f limit): $",
                        currentUser.getAccountBalance(accountFrom-1));
                amount = getValidDouble(0, currentUser.getAccountBalance(accountFrom-1), scan);
                if(amount == -1)
                    System.out.printf("Please enter an amount between 0 and %f.\n",
                            currentUser.getAccountBalance(accountFrom-1));
                if(amount == -2) // check if the user wants to exit
                    return;
            }
            currentUser.addAccountTransaction(accountFrom - 1, -1 * amount,
                    "Transfer to account " + currentUser.getAccountUUID(accountTo - 1));
            currentUser.addAccountTransaction(accountTo - 1, amount,
                    "Transfer from account " + currentUser.getAccountUUID(accountFrom - 1));
        }
    }
    /**
     * Creates an ArrayList of banks.
     * @return banks
     */
    private static ArrayList<Bank> createBanks(){
        String[] names = {"Bank of 'Murica", "Finance Incorporated", "Capital Two",
                "Banking & Co.", "Investment 4 U"};
        ArrayList<Bank> banks = new ArrayList<>();
        for(String name: names){
            banks.add(new Bank(name));
        }
        return banks;
    }

    /**
     * Returns a user inputted int that is greater than or equal to lower and less than upper.
     * @param lower
     * @param upper
     * @param scan
     * @return integer if it is in the range; -1 otherwise. Returns -2 if the user wants to exit.
     */
    private static int getValidInt(int lower, int upper, Scanner scan){
        if (scan.hasNextInt()) { // check if the input is an integer
            int input = scan.nextInt();
            scan.nextLine(); // Garbage collect
            if (input >= lower && input < upper) {
                return input;
            } else {
                return -1;
            }
        } else { // check if the user wants to exit
            String exit = scan.nextLine();
            if(exit.equals("x"))
                return -2;
            return -1;
        }
    }

    /**
     * Returns a string number with a certain amount of digits.
     * @param digits
     * @param scan
     * @return string if it has the correct number of digits; null otherwise.
     * Returns "x" if the user wants to exit.
     */
    private static String getValidString(int digits, Scanner scan){
        if(scan.hasNext()){
            String input = scan.nextLine();
            input = removeSpaces(input);
            if(input.equals("x")) // check if the user wants to quit
                return input;
            else if(input.length() != digits)
                return null;
            else
                return input;
        } else{
            return null;
        }
    }

    /**
     * Returns a double between the provided range.
     * @param lower
     * @param upper
     * @param scan
     * @return money-formatted double which is in the specified range; -1 otherwise.
     * Returns -2 if the user wants to exit.
     */
    private static double getValidDouble(double lower, double upper, Scanner scan){
        if (scan.hasNextDouble()) { // check if the input is a double
            double input = scan.nextDouble();
            scan.nextLine();
            if (input > lower && input <= upper) {
                return input;
            } else {
                return -1;
            }
        } else { // check if the user wants to quit
            String exit = scan.nextLine();
            if(exit.equals("x"))
                return -2;
            return -1;
        }
    }
    /**
     * Removes all the spaces from a given string.
     * @param input
     * @return string without spaces
     */
    private static String removeSpaces(String input){
        String output = "";
        for(int i = 0; i < input.length(); i++){
            if(!input.substring(i, i+1).equals(" ")) {
                output += input.substring(i, i + 1);
            }
        }
        return output;
    }

    /**
     * Removes only the leading and ending spaces from a string.
     * @param input
     * @return string without surrounding spaces
     */
    private static String removeSurroundingSpaces(String input){
        int start, stop;
        if(input == null)
            return "";
        for(start = 0; start < input.length(); start++){
            if(input.charAt(start) != ' ')
                break;
        }
        for(stop = input.length(); stop > 0; stop--){
            if(input.charAt(stop - 1) != ' ')
                break;
        }
        return input.substring(start, stop);
    }
}
