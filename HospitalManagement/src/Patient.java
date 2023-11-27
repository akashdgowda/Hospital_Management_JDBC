import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    public void addPatient(){
        System.out.println("Enter Patient name");
        String name = scanner.next();
        System.out.println("Enter Patient Age");
        int age = scanner.nextInt();
        System.out.println("Enter Patient Gender");
        String gender = scanner.next();
        try{
            String query = "INSERT INTO patients(name, age, gender) VALUES(?, ?, ? )";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0){
                System.out.println("Patient details added");
            }else{
                System.out.println("Failed to add Patient details");
            }

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public void viewPatient(){
        String query = "SELECT * FROM patients";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+------------+--------------------+--------------+-----------+");
            System.out.println("| Patient ID | Name               | Age          | Gender    |");
            System.out.println("+------------+--------------------+--------------+-----------+");
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-21s|%-15s|%-12s|\n",id,name,age,gender);
                System.out.println("+------------+--------------------+--------------+-----------+");
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public boolean getPtientById(int id){
        String query = "SELECT * FROM patients WHERE id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }return false;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }
}
