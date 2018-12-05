package Control;

import Database.DBConnect;
import Database.DBControl;
import Model.Student;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.Connection;
import java.util.Optional;

public class RegisterController {
    @FXML
    private TextField idfield;

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private TextField yearfield;

    @FXML
    private Button clearbt;

    @FXML
    private Button submitbt;

    protected Student nowLogin;

    protected Stage stage;

    public void handleRegisterbutton(ActionEvent event){
        Button register = (Button) event.getSource();
        String id;
        String firstName;
        String lastName;
        String year;
        if (register.equals(submitbt)){
            id = idfield.getText();
            firstName = fname.getText();
            lastName = lname.getText();
            year = yearfield.getText();

            Alert ConfirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to submit this information?",
                    ButtonType.YES, ButtonType.NO);
            ConfirmationAlert.setHeaderText("");
            Optional optional = ConfirmationAlert.showAndWait();
            if (optional.get().equals(ButtonType.YES)) {
                boolean canRegister = true;
                DBControl dbControl1 = DBConnect.openDB();
                for (Student student:dbControl1.readStudent()) {
                    if (student.getStudentID().equals(id)){
                        if (nowLogin == null){
                            canRegister = false;
                        }
                    }
                }
                Alert warning = new Alert(Alert.AlertType.WARNING);
                warning.setTitle("Warning");
                warning.setHeaderText("");
                try {
                    long testId = Long.parseLong(id);
                    try {
                        if (id.length() == 10 && Double.parseDouble(year)>=1.1 && Double.parseDouble(year) <=4.2 && canRegister
                                && !idfield.getText().equals("") && !fname.getText().equals("")
                                && !lname.getText().equals("") && !yearfield.getText().equals("")){

                            if (nowLogin == null){
                                DBControl dbControl = DBConnect.openDB();
                                Student newStudent = new Student(id,firstName,lastName,year);
                                newStudent.setRegistersubject("");
                                System.out.println(dbControl.addStudent(newStudent));
                            }
                            if (nowLogin != null){
                                DBControl dbControl2 = DBConnect.openDB();
                                nowLogin.setFirstName(firstName);
                                nowLogin.setLastName(lastName);
                                nowLogin.setYear(year);
                                System.out.println(dbControl2.updateStudent(nowLogin));
                                nowLogin = null;
                                stage.close();
                            }
                            idfield.setStyle("-fx-border-color: green");
                            System.out.println("Complete");
                            warning.setAlertType(Alert.AlertType.INFORMATION);
                            warning.setContentText("Update complete, please re-login before use the system.");
                            warning.show();
                            stage.close();
                        }else{
                            warning.setTitle("Warning");
                            warning.setHeaderText("");
                            if (idfield.getText().equals("") || fname.getText().equals("")
                                    || lname.getText().equals("") || yearfield.getText().equals("")){
                                warning.setContentText("Please complete the form.");
                                warning.showAndWait();

                            }else if (Double.parseDouble(year)<1.1 || Double.parseDouble(year) >4.2){
                                warning.setContentText("Year must in range (1.1 - 4.1)");
                                yearfield.setStyle("-fx-border-color: red");
                                warning.showAndWait();
                            }else if (id.length() != 10){
                                warning.setContentText("The ID must contains 10 digits");
                                idfield.setStyle("-fx-border-color: red");
                                warning.showAndWait();
                            }else if (!canRegister){
                                warning.setContentText("The ID must Not Duplicate in Database");
                                idfield.setStyle("-fx-border-color: red");
                                warning.showAndWait();
                            }else{
                                warning.setContentText("Please check the information field.");
                                idfield.setStyle("-fx-border-color: red");
                                warning.showAndWait();
                            }
                        }
                    }catch (NumberFormatException e){
                        warning.setContentText("Year field must be only Integer. (Ex.\"2.1\" stand for year 2 semester 1.)");
                        yearfield.setStyle("-fx-border-color: red");
                        warning.show();
                    }
                }catch (NumberFormatException e){
                    warning.setContentText("ID must contain be only 10 digits Integer.");
                    idfield.setStyle("-fx-border-color: red");
                    warning.show();
                }

            }else if (optional.get().equals(ButtonType.NO)){
                ConfirmationAlert.close();
            }
        }if (register.equals(clearbt)){
            if (nowLogin == null){
                idfield.clear();
            }
            fname.clear();
            lname.clear();
            yearfield.clear();
        }
    }

    @FXML
    public void setUpstudent(){
        idfield.clear();
        idfield.setText(nowLogin.getStudentID());
        idfield.setEditable(false);
        fname.setText(nowLogin.getFirstName());
        lname.setText(nowLogin.getLastName());
        yearfield.setText(nowLogin.getYear());
    }

    public void setNowLogin(Student nowLogin) {
        this.nowLogin = nowLogin;
        setUpstudent();
    }
}
