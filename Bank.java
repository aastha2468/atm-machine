import java.util.ArrayList;

public class Bank {
    private String name;
    private ArrayList<Account> accounts;
    private ArrayList<User> users;

    /**
     * Creates a bank with the provided name.
     * @param name
     */
    public Bank(String name){
        this.name = name;
        this.accounts = new ArrayList<Account>();
        this.users = new ArrayList<User>();
    }

    /**
     * Returns the bank name.
     * @return name
     */
    public String getName(){
        return name;
    }

    /**
     * Generates a 6-digit user UUID that is unique in this bank.
     * @return user uuid
     */
    public String getNewUserUUID(){
        boolean isNotUnique = true;
        String uuid = "";
        while(isNotUnique) {
            for(int i = 0; i < 6; i++) { // Generates a random uuid
                uuid += (int) (Math.random() * 10);
            }
            for (User user : users) { // Checks if the uuid is unique
                if (user.getUUID().equals(uuid))
                    continue;
            }
            isNotUnique = false;
        }
        return uuid;
    }

    /**
     * Generates a 10-digit account UUID that is unique in this bank.
     * @return account uuid
     */
    public String getNewAccountUUID(){
        boolean isNotUnique = true;
        String uuid = "";
        while(isNotUnique) {
            for(int i = 0; i < 10; i++) { // Generates a random uuid
                uuid += (int) (Math.random() * 10);
            }
            for (Account account : accounts) { // Checks if the uuid is unique
                if (account.getUUID().equals(uuid))
                    continue;
            }
            isNotUnique = false;
        }
        return uuid;
    }

    /**
     * Adds a user to this bank.
     * @param user
     */
    public void addUser(User user){
        users.add(user);
    }

    /**
     * Add an account to this bank.
     * @param account
     */
    public void addAccount(Account account){
        accounts.add(account);
    }

    /**
     * Checks if the user login credentials are valid.
     * @param userID
     * @param userPin
     * @return the user associated with the credentials; null otherwise.
     */
    public User checkUserLogin(String userID, String userPin){
        for(User user: users){
            if(user.getUUID().equals(userID) && user.getPin().equals(userPin))
                return user;
        }
        return null;
    }
}
