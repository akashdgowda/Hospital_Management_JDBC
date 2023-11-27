import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem{
    private static final String url = "jdbc:mysql://localhost:3306/hospital_management_system";
    private static final String username = "root";
    private static final String password = "MySQLPassword";

    public static void main(String[] args) {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        try{
            Connection connection = DriverManager.getConnection(url,username,password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1. ADD PATIENT");
                System.out.println("2. View PATIENTS");
                System.out.println("3. VIEW DOCTORS");
                System.out.println("4. BOOK APPOINTMENT");
                System.out.println("5. EXIT");
                System.out.println("Enter your Choice");
                int choice = scanner.nextInt();
                switch(choice){
                    case 1:
                        //ADD patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        // View Patient
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        //View Doctor
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        // Book Appointment
                        bookAppointment(patient, doctor, connection, scanner);
                        System.out.println();
                        break;

                    case 5:
                        return;
                    default:
                        System.out.println("Enter valid choice!!");
                        break;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor,Connection connection, Scanner scanner){
        System.out.println("Enter Patient ID: ");
        int Pid = scanner.nextInt();
        System.out.println("Enter Doctor ID");
        int Did = scanner.nextInt();;
        System.out.println("Enter Appointment Date(YYYY-MM-DD):");
        String appointmentDate = scanner.next();
        if(patient.getPtientById(Pid) && doctor.getDoctorById(Did)){
            if(checkDoctorAvailibility(connection, Did, appointmentDate)){
                String query = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?,?,?)";
                try{
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1,Pid);
                    preparedStatement.setInt(2,Did);
                    preparedStatement.setString(3,appointmentDate);
                    int rowsAffected = preparedStatement.executeUpdate();
                    if(rowsAffected > 0){
                        System.out.println("Appointment booked");
                    }else{
                        System.out.println("Failed to book appointment");
                    }
                }catch(SQLException e){
                    System.out.println(e.getMessage());
                }
            }else{
                System.out.println("Doctor not available on this date");
            }
        }else{
            System.out.println("Either Doctor or patient doesn't exist");
        }
    }

    private static boolean checkDoctorAvailibility(Connection connection, int did, String appointmentDate) {
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,did);
            preparedStatement.setString(2,appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int count = resultSet.getInt(1);
                if(count==0){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}