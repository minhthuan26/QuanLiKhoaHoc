package QuanLiKhoaHoc.GUI.TeacherManage;

import QuanLiKhoaHoc.BUS.TeacherManage.TeacherBus;
import QuanLiKhoaHoc.DTO.Person;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class EditTeacherController implements Initializable {

    private final TeacherBus teacherBus = new TeacherBus();

    @FXML
    private AnchorPane windowPane;
    @FXML
    private AnchorPane wrapPane;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField idTxtField;
    @FXML
    private TextField firstNameTxtField;
    @FXML
    private TextField lastNameTxtField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private TextField phoneTxtField;

    private static double xOffset = 0;
    private static double yOffset = 0;
    private Stage primaryStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Handle();
        setDefault();
    }

    public void Handle(){
        windowPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage = (Stage) wrapPane.getScene().getWindow();
                xOffset = primaryStage.getX() - event.getScreenX();
                yOffset = primaryStage.getY() - event.getScreenY();
            }
        });

        windowPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage = (Stage) wrapPane.getScene().getWindow();
                primaryStage.setX(event.getScreenX() + xOffset);
                primaryStage.setY(event.getScreenY() + yOffset);
            }
        });

        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                primaryStage = (Stage) cancelBtn.getScene().getWindow();
                primaryStage.close();
            }
        });

        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String firstName, lastName, phoneNumber;
                LocalDate dateOfBirth;
                firstName = firstNameTxtField.getText().trim();
                lastName = lastNameTxtField.getText().trim();
                dateOfBirth = birthDatePicker.getValue();
                phoneNumber = phoneTxtField.getText().trim();
                if(firstName.equals("") || lastName.equals("") || phoneNumber.equals("")){
                    errorAlert("L???i", "T???t c??? c??c tr?????ng kh??ng ???????c ????? tr???ng");
                }
                else{
                    Person teacher = new Person(
                            Integer.parseInt(idTxtField.getText()),
                            firstName,
                            lastName,
                            "",
                            phoneNumber,
                            dateOfBirth,
                            ""
                            );
                    teacher = teacherBus.updateTeacher(teacher);
                    if(teacher!=null){
                        alert("Th??ng b??o", "C???p nh???t th??nh c??ng");
                        primaryStage = (Stage) saveBtn.getScene().getWindow();
                        primaryStage.close();
                    }
                    else{
                        errorAlert("L???i", "C?? l???i x???y ra");
                    }
                }
            }
        });
    }

    private void errorAlert(String title, String Message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        DialogPane root = alert.getDialogPane();
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../Main/main.css")).toExternalForm());
        root.getScene().setFill(Color.TRANSPARENT);
        Stage dialogStage = (Stage) root.getScene().getWindow();
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        alert.setContentText(Message);
        alert.setHeaderText(null);
        ButtonType okBtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okBtn);
        alert.show();
    }

    private void alert(String title, String Message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        DialogPane root = alert.getDialogPane();
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("../Main/main.css")).toExternalForm());
        root.getScene().setFill(Color.TRANSPARENT);
        Stage dialogStage = (Stage) root.getScene().getWindow();
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        alert.setContentText(Message);
        alert.setHeaderText(null);
        ButtonType okBtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okBtn);
        alert.show();
    }

    public void setDefault(){
        phoneTxtField.setText(Controller.selectedRow.getPhoneNumber());
        birthDatePicker.setValue(Controller.selectedRow.getDateOfBirth());
        lastNameTxtField.setText(Controller.selectedRow.getLastName());
        firstNameTxtField.setText(Controller.selectedRow.getFirstName());
        idTxtField.setText(String.valueOf(Controller.selectedRow.getPersonId()));
    }
}
