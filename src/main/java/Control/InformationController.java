package Control;

import Database.DBConnect;
import Database.DBControl;
import Model.Student;
import Model.Subject;
import animatefx.animation.Flash;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class InformationController {
    @FXML
    protected TableView<Subject> passView;

    @FXML
    protected TableColumn<Subject, String> PassSubID;

    @FXML
    protected TableColumn<Subject, String> PassSubname;

    @FXML
    protected TableColumn<Subject, String> PassSubCredit;

    @FXML
    protected TableColumn<Subject, String> PassSubyear;

    @FXML
    protected TableColumn<Subject, String> PassSubStatus;

    @FXML
    protected TableView<Subject> notView;

    @FXML
    protected TableColumn<Subject, String> notSubID;

    @FXML
    protected TableColumn<Subject, String> notSubname;

    @FXML
    protected TableColumn<Subject, String> notSubCredit;

    @FXML
    protected TableColumn<Subject, String> notSubyear;

    @FXML
    protected TableColumn<Subject, String> notHard;

    @FXML
    protected TableView<Subject> ChooseTable;

    @FXML
    protected TableColumn<Subject, String> ChooseID;

    @FXML
    protected TableColumn<Subject, String> ChooseName;

    @FXML
    protected TableColumn<Subject, String> ChooseCredit;

    @FXML
    protected TableColumn<Subject, String> ChooseYear;

    @FXML
    public Button addbt,viewAllCourse,dropbt,exitbtninfo;
    @FXML
    protected Button cancleBtn,signout,editInfo;

    @FXML
    protected Label fname,lname,Year,tcredit,preTcredit,showID,clockText;

    @FXML
    protected ImageView image;

    @FXML
    protected AnchorPane informationPane;

    protected Stage courseController;
    protected Stage baseAndConController;

    protected Student presentStudent;
    private  ArrayList<Subject> allSubjects = new ArrayList<>();
    private ObservableList<Subject> dataSubject = FXCollections.observableArrayList();
    private ObservableList<Subject> dataNotPassSubject = FXCollections.observableArrayList();
    private ObservableList<Subject> dataChooseSubject = FXCollections.observableArrayList();


    @FXML
    public void initialize(){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String time = now.format(formatter);
            clockText.setText(time);
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        updateColor(notHard);
        exitbtninfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });
    }

    public void handleSignout(ActionEvent event){
        presentStudent = null;
        Stage stage = (Stage) informationPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/MainPage.fxml")) ;
        try {
            stage.setScene(new Scene(loader.load(),666,475));
            stage.setResizable(false);
            MainController controller = (MainController) loader.getController();
            stage.centerOnScreen();
            stage.show();
            new Flash(stage.getScene().getRoot()).play();
            courseController.close();
            baseAndConController.close();
            CourseController.notPassAndCanRegister.clear();
        } catch (IOException e2){
            e2.printStackTrace();
        }
    }

    @FXML
    public void setPresentStudent(Student presentStudent) {
        this.presentStudent = presentStudent;
        readStudent();
        showImage();
        fname.setText(presentStudent.getFirstName());
        lname.setText(presentStudent.getLastName());
        Year.setText(presentStudent.getYear());
        showID.setText(presentStudent.getStudentID());
    }


    @FXML
    public void showImage() {
        try {
            if (presentStudent.getPath() == null){
                image.setImage(new Image("Person/default2.png"));
            }else {
                image.setImage(new Image("Person/"+presentStudent.getPath()));
            }
        }catch (IllegalArgumentException exception){
            System.out.println(presentStudent.getPath());
            System.out.println("Not found picture.");
            image.setImage(new Image("Person/default2.png"));
        }
    }

    @FXML
    public void updateColor(TableColumn nowColumn){
        nowColumn.setCellFactory(column -> {
            return new TableCell<Subject, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty); //This is mandatory
                    if (item == null || empty) { //If the cell is empty
                        setText(null);
                        setStyle("");
                    } else { //If the cell is not empty\
                        setText(item); //Put the String data in the cell
                        Subject auxPerson = getTableView().getItems().get(getIndex());
                        if (auxPerson.getHardness().equals("3")) {
                            setText("Hard");
                            setStyle("-fx-font-weight: bold;" + "-fx-background-color: rgba(223,0,11,0.45)");
                        }else if (auxPerson.getHardness().equals("2")) {
                            setText("Medium");
                            setStyle("-fx-font-weight: bold;" + "-fx-background-color: rgba(15,86,223,0.45)");
                        }else if (auxPerson.getHardness().equals("1")) {
                            setText("Easy");
                            setStyle("-fx-font-weight: bold;" + "-fx-background-color: rgba(0,202,24,0.45)");
                        }else{
                            setStyle("-fx-background-color: white");
                            if (getTableView().getSelectionModel().getSelectedItems().contains(auxPerson)) {
                                setTextFill(Color.WHITE);
                            } else
                                setTextFill(Color.BLACK);
                        } } }};
        });
        PassSubStatus.setCellFactory(column -> {
            return new TableCell<Subject, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        setStyle("-fx-background-color: rgba(0,202,24,0.53)");
                    } }}; });
    }


    public void readStudent(){
        DBControl connect = DBConnect.openDB();
        this.allSubjects = connect.readSubject();
        int totalCredit = 0;
        try {
            String[] subjectforStudent = presentStudent.getRegistersubject().split("#");
            ArrayList<Subject> checkNotpass = allSubjects; // to check duplicate subject in passed subject.
            dataNotPassSubject.addAll(checkNotpass); // to show in not pass table.
            if (subjectforStudent[0].equals("")&& subjectforStudent.length == 1){ // not wasting time to for loop if array is not contain any data.
                //System will throw new NullPointerException() Or IndexOutOfBoundsException;
            }else {
                for (Subject subjectcheck : checkNotpass) {
                    for (String subject : subjectforStudent) {
                        if (subjectcheck.getSubCode().equals(subject)) { // if duplicated
                            //System.out.println(shownotPass.getSubCode()); for debug
                            subjectcheck.setIsPass("Passed");
                            dataSubject.add(subjectcheck); // add to passed table
                            totalCredit += Integer.parseInt(subjectcheck.getCreDit()); // calculate total credit
                            dataNotPassSubject.remove(subjectcheck); // delete duplicate subject
                        }
                    }
                }
            }
            showStudentPassSubject();
            tcredit.setText(String.valueOf(totalCredit));
        }catch (NullPointerException | IndexOutOfBoundsException e){
            dataNotPassSubject.addAll(allSubjects);// handle exception when system throws
            showStudentPassSubject();
        }
    }


    public void showStudentPassSubject(){
        PassSubID.setCellValueFactory(cellData->cellData.getValue().subCodeProperty());
        PassSubname.setCellValueFactory(cellData->cellData.getValue().subNameProperty());
        PassSubCredit.setCellValueFactory(cellData->cellData.getValue().creDitProperty());
        PassSubyear.setCellValueFactory(cellData->cellData.getValue().yearProperty());
        PassSubStatus.setCellValueFactory(cellData-> new SimpleStringProperty("Pass"));
        passView.setItems(dataSubject);
        passView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                dropbt.setDisable(false);
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////
        notSubID.setCellValueFactory(cellData->cellData.getValue().subCodeProperty());
        notSubname.setCellValueFactory(cellData->cellData.getValue().subNameProperty());
        notSubCredit.setCellValueFactory(cellData->cellData.getValue().creDitProperty());
        notSubyear.setCellValueFactory(cellData->cellData.getValue().yearProperty());
        notHard.setCellValueFactory(cellData->cellData.getValue().hardnessProperty());
        notView.setItems(dataNotPassSubject);
        notView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        notView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Subject clicked1 = notView.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 1 ){
                    setBaseAndContSubject(clicked1);
                }else if (event.getClickCount() == 2){
                    if (Double.parseDouble(presentStudent.getYear()) < (Double.parseDouble(clicked1.getYear()))){
                        Alert alert = new Alert(Alert.AlertType.WARNING,"This subject is not available because your year is not reached.",ButtonType.OK);
                        alert.setHeaderText("");
                        alert.show();

                    }else{
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to add this subject to Pre-Register table?",ButtonType.YES,ButtonType.NO);
                        alert.setHeaderText("");
                        Optional optional = alert.showAndWait();

                        Boolean canChoose = checkcanRegis(clicked1);

                        if (optional.get().equals(ButtonType.YES) && canChoose == true){
                            dataChooseSubject.add(clicked1);
                            dataNotPassSubject.remove(clicked1);
                            updateTotolPreCredit();
                            addbt.setDisable(false);
                        }else if (optional.get().equals(ButtonType.NO)){
                            return;
                        }
                        else {
                            String nameSubject = "\""+ clicked1.getBaseSubject() +" "+findBaseName(clicked1)+"\"";
                            alert = new Alert(Alert.AlertType.WARNING,"You have to Pass "+nameSubject+" Before register "+clicked1.getSubName()+".",ButtonType.OK);
                            alert.setHeaderText("");
                            alert.show();
                        }
                    }
                }
            }
        });
        notView.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to add all selected to Pre-Register table?",ButtonType.YES,ButtonType.NO);
                    alert.setHeaderText("");
                    Optional optional = alert.showAndWait();
                    if (optional.get().equals(ButtonType.YES)) {
                        ObservableList<Subject> subjectObservableList = notView.getSelectionModel().getSelectedItems();
                        boolean canAdd = false;
                        for (Subject subject: subjectObservableList) {
                            canAdd = checkcanRegis(subject);
                        }
                        if (canAdd){
                            dataChooseSubject.addAll(subjectObservableList);
                            dataNotPassSubject.removeAll(subjectObservableList);
                            updateTotolPreCredit();
                            addbt.setDisable(false);
                        }
                        else {
                            Alert alert1 = new Alert(Alert.AlertType.WARNING,"Cannot add these subject, please check that you already passed base Subject of these subject.");
                            alert1.setHeaderText("");
                            alert1.show();
                        }
                    }else {
                        return;
                    }
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////
        ChooseID.setCellValueFactory(cellData->cellData.getValue().subCodeProperty());
        ChooseName.setCellValueFactory(cellData->cellData.getValue().subNameProperty());
        ChooseCredit.setCellValueFactory(cellData->cellData.getValue().creDitProperty());
        ChooseYear.setCellValueFactory(cellData->cellData.getValue().yearProperty());
        ChooseTable.setItems(dataChooseSubject);
        ChooseTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (dataChooseSubject.size() >=1){
                    cancleBtn.setDisable(false);
                }
            }
        });
    }

    @FXML
    public void viewAllBThandle(ActionEvent event){
        if (event.getSource().equals(viewAllCourse)){
            MainController.openAllCourse(presentStudent,getDataSubject());
        }
    }

    @FXML
    public void handleEdit(ActionEvent event){
        if (event.getSource().equals(editInfo)){
            MainController.openRegister(presentStudent);
        }
    }



    @FXML
    public void handleCanclebt(ActionEvent event){
        Subject subject = ChooseTable.getSelectionModel().getSelectedItem();
        if (event.getSource().equals(cancleBtn)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to remove this subject from Pre-Register table?.",ButtonType.YES,ButtonType.NO);
            alert.setHeaderText("");
            Optional optional = alert.showAndWait();
            if (optional.get().equals(ButtonType.YES)){
                dataChooseSubject.remove(subject);
                dataNotPassSubject.add(subject);
                cancleBtn.setDisable(true);
                if (dataChooseSubject.size()==0){
                    addbt.setDisable(true);
                }
            }
        }
    }

    @FXML
    public void handleDropbtn(ActionEvent event){ // มีปุ่ม ลบวิชาออกจาก Pass list ด้วย
        if (event.getSource().equals(dropbt)){
            Subject subject = passView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to drop this subject from Passed table?.",ButtonType.YES,ButtonType.NO);
            alert.setHeaderText("");
            Optional optional = alert.showAndWait();
            if (optional.get().equals(ButtonType.YES)){
                dataSubject.remove(subject);
                dataNotPassSubject.add(subject);
                CourseController.updateList(subject,0);
                updateRegister();
                dropbt.setDisable(true);
            }
        }
    }

    public void updateRegister(){
        DBControl open = DBConnect.openDB();
        String newSubject = "";
        int toSetCredit = 0;
        for (Subject subjectnew : dataSubject) {
            newSubject += subjectnew.getSubCode()+"#";
            toSetCredit += Integer.parseInt(subjectnew.getCreDit());
            subjectnew.setIsPass("Passed");
        }
        presentStudent.setRegistersubject(newSubject);
        presentStudent.setCredit(String.valueOf(toSetCredit));
        open.updateStudentSubject(presentStudent);
        tcredit.setText(String.valueOf(toSetCredit));
        System.out.println("Update Complete");
    }

    @FXML
    public void handleAddPass(ActionEvent event){ // เหลือแก้ให้ น้องลงวิชาพี่ไม่ได้
        if (event.getSource().equals(addbt)){
            Alert newAlert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to add all the subject to your passed subject table?",
                    ButtonType.YES,ButtonType.NO);
            newAlert.setHeaderText("");
            Optional optional = newAlert.showAndWait();
            if (optional.get().equals(ButtonType.NO)){
                return;
            }else if (optional.get().equals(ButtonType.YES)) {
                for (Subject subject : dataChooseSubject) {
                    if (!dataSubject.contains(subject) && !dataChooseSubject.isEmpty()) {
                        subject.setIsPass("Passed");
                        dataSubject.add(subject);
                        CourseController.updateList(subject, 1);
                        updateRegister();
                    }
                }
                preTcredit.setText("");
                dataChooseSubject.clear();
                addbt.setDisable(true);
            }
        }else{
            return;
        }
    }

    public void updateTotolPreCredit(){
        int pre = 0;
        for (Subject subject : dataChooseSubject) {
            pre += Integer.parseInt(subject.getCreDit());
        }
        preTcredit.setText(String.valueOf(pre));
    }

    public boolean checkcanRegis(Subject subject){
        if (subject.getBaseSubject() == null){
            return true;
        }else if (subject.getBaseSubject().length() == 0){
            return true;
        }
        try {
            if (subject.getBaseSubject().length() > 0){
                String[] checkBase = subject.getBaseSubject().split("#");
                for (Subject pre : dataChooseSubject) {
                    for (String check : checkBase) {
                        if (pre.getSubCode().equals(check)){
                            return false;
                        }
                    }
                }
                if (dataSubject.size()==0 && checkBase.length == 0){
                    return true;
                }
                int countBase = checkBase.length;
                int inPrechoose = 0;
                for (Subject passed : dataSubject) {
                    for (String checksub : checkBase) {
                        if (passed.getSubCode().equals(checksub)){
                            inPrechoose++;
                        }
                    }
                }
                if (inPrechoose == countBase){
                    return true;
                }
            }
                return false;

        }catch (NullPointerException e){
            e.getStackTrace();
        }
        return false;
    }

    public ObservableList<Subject> getDataSubject() {
        return dataSubject;
    }

    public String findBaseName(Subject subject){
        String name = "";
        DBControl dbControl = DBConnect.openDB();
        ArrayList<Subject> subjectList = dbControl.readSubject();
        for (Subject read : subjectList) {
            if (read.getSubCode().equals(subject.getBaseSubject())){
                name = read.getSubName();
                break;
            }
        }
        return name;
    }


    @FXML
    public void setBaseAndContSubject(Subject subject){
        BaseAndConController.baseSub.clear();// BaseSubject
        try {
            String[] base = subject.getBaseSubject().split("#");
            DBControl control = DBConnect.openDB();
            for (Subject subjectdb:control.readSubject()) {
                for (int i = 0; i < base.length; i++) {
                    if (subjectdb.getSubCode().equals(base[i])){
                        BaseAndConController.baseSub.add(subjectdb);
                    }
                }
            }
        }
        catch (NullPointerException e){
            e.getStackTrace();
        }
        BaseAndConController.contSub.clear();// ContinueSubject
        try {
            String[] cont = subject.getContSubject().split("#");
            DBControl control2 = DBConnect.openDB();
            for (Subject subjectdb:control2.readSubject()) {
                for (int i = 0; i < cont.length; i++) {
                    if (subjectdb.getSubCode().equals(cont[i])){
                        BaseAndConController.contSub.add(subjectdb);
                    }
                }
            }
        }
        catch (NullPointerException e){
            e.getStackTrace();
        }
    }


}
