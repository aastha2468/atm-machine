import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private double amount;
    private String memo;
    private Date timestamp;
    private Account account;

    public Transaction(double amount, Account account){
        this.amount = amount;
        this.account = account;
        this.timestamp = new Date();
        this.memo = null;
    }
    public Transaction(double amount, Account account, String memo){
        this.amount = amount;
        this.account = account;
        this.timestamp = new Date();
        this.memo = memo;
    }

    /**
     * Returns a correctly formatted transaction amount.
     * @return amount
     */
    public double getAmount(){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.valueOf(df.format(amount));
    }

    /**
     * Returns a string containing a summary of the transaction.
     * @return summary line
     */
    public String getSummaryLine(){
        String sign = "";
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd, yyyy HH:mm:ss z");
        if(amount < 0)
            sign = "-";
        else if(amount > 0)
            sign = "+";
        else
            sign = "";
        if(memo != null)
            return String.format("%s, %s$%.2f : %s\n", formatter.format(timestamp), sign, Math.abs(getAmount()), memo);
        else
            return String.format("%s, %s$%.2f\n", formatter.format(timestamp), sign, Math.abs(getAmount()));
    }
}
