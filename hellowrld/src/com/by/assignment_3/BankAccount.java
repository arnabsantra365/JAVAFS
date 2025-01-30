import java.sql.SQLException;
import java.sql.*;
public class BankAccount {
    private  String accNum;
    private String accName;
    private double balance;
    private String accountType;
//    private static List <Transaction> transactionHist;

    public BankAccount(String num,String name,double bal,String type) {
        accNum = num;
        accName = name;
        balance = bal;
        accountType = type;
//        this.transactionHist=new ArrayList<>();
    }

    public String getaccNum() {
        return accNum;
    }
    public String getaccName() {
        return accName;
    }
    public synchronized double getBalance() {
        return balance;
    }
    public String getAccType(){return accountType;}

    String updateBalance = "UPDATE accounts SET balance = ? WHERE account_number = ?";

//    public static void setTransaction(double amount,String fromacc,String toacc){
//        transactionHist.add(new Transaction(transactionId(),"Transfer Out",amount,fromacc));
//        transactionHist.add(new Transaction(transactionId(),"Transfer In",amount,toacc));
//    }

    public synchronized void deposit(double amount) throws TransactionException,SQLException{
        if(amount<=0) {
            throw new TransactionException("Enter a valid deposit");
        }
        else {
            this.balance+=amount;
            this.updateBalanceDB();
//            transactionHist.add(new Transaction(transactionId(),"Deposit",amount,accNum));
            System.out.println("Amount deposited="+amount);
        }
    }

    public synchronized void withdraw(double amount) throws TransactionException,SQLException{
        if(amount<=0 || amount>balance) {
            throw new TransactionException("Enter a valid withdrawal");
        }
        else {
            this.balance-=amount;
            this.updateBalanceDB();
//            transactionHist.add(new Transaction(transactionId(),"Withdraw",amount,accNum));
            System.out.println("Amount withdrawn="+amount);
        }
    }

    public synchronized void transfer(BankAccount toAccount,double amount) throws TransactionException,SQLException{
        if(amount>balance){
            throw new TransactionException("Invalid amount");
        }
        else{
            this.balance -= amount;
            this.updateBalanceDB();
            toAccount.balance += amount;
            toAccount.updateBalanceDB();

            System.out.println("Transfer Successful");
        }
    }

    private void updateBalanceDB() throws SQLException{
        try(Connection con = DatabaseConnection.getConnection();
            PreparedStatement psBalance= con.prepareStatement(updateBalance)){

            psBalance.setDouble(1,this.balance);
            psBalance.setString(2,this.accNum);
            psBalance.executeUpdate();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static BankAccount loadFromDB(String accountNumber) throws SQLException,InvalidAccountException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM accounts WHERE account_number = ?")) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new BankAccount(
                        rs.getString("account_number"),
                        rs.getString("account_holder_name"),
                        rs.getDouble("balance"),
                        rs.getString("account_type")
                );
            } else {
                throw new InvalidAccountException("Account not found.");
            }
        }
    }

//    public void displayTransactionHistory(){
//        System.out.println("Transaction for account number:"+accNum);
//        if(transactionHist.isEmpty()){
//            System.out.println("No transactions to be found");
//        }
//        else
//        {
//            for(Transaction t:transactionHist){
////                System.out.println(t.process());
//                t.process();
//            }
//        }
//    }

    public void displayBalance() {
        System.out.println("Current balance="+balance);
    }

//    public static String transactionId(){
//        return "TXN" + System.currentTimeMillis() + accNum;
//    }


}
