package Banking;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

       private Connection connection;
       private Scanner scanner;

       public AccountManager(Connection connection, Scanner scanner) {
            this.connection = connection;
            this.scanner = scanner;

       }




    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter amount");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("enter security pin");
        String security_pin = scanner.nextLine();
        try {
            connection.setAutoCommit(false);
            if (account_number!=0) {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1,account_number);
                preparedStatement.setString(2,security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                        String credit_query = "update accounts set balance = balance +? where account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                        preparedStatement1.setDouble(1,amount);
                        preparedStatement1.setLong(2,account_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0 ) {
                            System.out.println("Rs." +amount + "credited successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else {
                            System.out.println("transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }


                }
                else {
                    System.out.println("invalid pin");
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }





    public void debit_money(long account_number) throws SQLException {
             System.out.println("Enter amount");
             double amount = scanner.nextDouble();
             scanner.nextLine();

             System.out.println("enter security pin");
             String security_pin = scanner.nextLine();
             try {
                 connection.setAutoCommit(false);
                 if (account_number!=0) {
                     PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_number = ? and security_pin = ? ");
                     preparedStatement.setLong(1,account_number);
                     preparedStatement.setString(2,security_pin);
                     ResultSet resultSet = preparedStatement.executeQuery();

                     if (resultSet.next()) {
                         double current_balance = resultSet.getDouble("balance");
                         if (amount<=current_balance) {
                             String debit_query = "update accounts set balance = balance -? where account_number = ?";
                             PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                             preparedStatement1.setDouble(1,amount);
                             preparedStatement1.setLong(2,account_number);
                             int rowsAffected = preparedStatement1.executeUpdate();
                             if (rowsAffected > 0 ) {
                                 System.out.println("Rs." +amount + "debited successfully");
                                 connection.commit();
                                 connection.setAutoCommit(true);
                                 return;
                             }
                             else {
                                 System.out.println("transaction failed");
                                 connection.rollback();
                                 connection.setAutoCommit(true);
                             }
                         }
                         else {
                             System.out.println("Insufficient balance");
                         }
                     }
                     else {
                         System.out.println("invalid pin");
                     }
                 }
             }
             catch (SQLException e) {
                 e.printStackTrace();
             }
             connection.setAutoCommit(true);
         }


// hkl
            public  void transfer_money(long sender_account_number) throws SQLException {
                scanner.nextLine();
                System.out.println("enter reciever account number");
                long reciever_account_number = scanner.nextLong();
                System.out.println("enter amount: ");
                double amount = scanner.nextDouble();
                scanner.nextLine();
                System.out.println("enter pin: ");
                String security_pin = scanner.nextLine();
                try {
                    connection.setAutoCommit(false);
                    if (sender_account_number !=0 && reciever_account_number !=0) {
                        PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_number = ? and security_pin= ?");
                        preparedStatement.setLong(1,sender_account_number);
                        preparedStatement.setString(2, security_pin);
                        ResultSet resultSet = preparedStatement.executeQuery();
                        if (resultSet.next()) {
                            double current_balance = resultSet.getDouble("balance");
                            if (amount <= current_balance) {
                                String debit_query = "update accounts set balance = balance -? where account_number= ?" ;
                                String credit_query = "update accounts set balance = balance +? where account_number = ?" ;
                                PreparedStatement debitPreparedStatement = connection.prepareStatement(debit_query);
                                PreparedStatement creditPreparedStatement = connection.prepareStatement(credit_query);
                                debitPreparedStatement.setDouble(1,amount);
                                debitPreparedStatement.setLong(2,sender_account_number);
                                creditPreparedStatement.setDouble(1,amount);
                                creditPreparedStatement.setLong(2,reciever_account_number);
                                int rowsAffected1= debitPreparedStatement.executeUpdate();
                                int rowsAffected2 = creditPreparedStatement.executeUpdate();
                                if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                                    System.out.println("transaction successfully");
                                    System.out.println("Rs: " +amount + "transfered succesfully");
                                   connection.commit();
                                   connection.setAutoCommit(true);
                                    return;
                                }
                                else {
                                    System.out.println("transaction failed");
                                    connection.rollback();
                                    connection.setAutoCommit(true);
                                }
                            }
                            else {
                                System.out.println("insufficient balance");
                            }
                        }
                        else {
                            System.out.println("invalid security pin");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                connection.setAutoCommit(true);

            }

          public void getBalance (long account_number) {
           scanner.nextLine();
              System.out.println("enter pin number");
              String security_pin = scanner.nextLine();

              try {
                  PreparedStatement preparedStatement = connection.prepareStatement("select balance from accounts where account_number = ? and security_pin = ?");
                  preparedStatement.setLong(1,account_number);
                  preparedStatement.setString(2,security_pin);
                   ResultSet resultSet = preparedStatement.executeQuery();
                   if (resultSet.next()) {
                       double balance = resultSet.getDouble("balance");
                       System.out.println("balance" +balance);
                   }
                   else {
                       System.out.println("invalid pin");
                   }
              }
              catch (SQLException e) {
                  e.printStackTrace();
              }
          }

    public static void main(String[] args) {

    }

}
