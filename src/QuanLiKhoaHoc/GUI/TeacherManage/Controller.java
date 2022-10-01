package QuanLiKhoaHoc.GUI.TeacherManage;
import QuanLiKhoaHoc.BUS.TeacherManage.TeacherBus;
import QuanLiKhoaHoc.DTO.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public TeacherBus teacherBus = new TeacherBus();
    ObservableList<Person> teacherList;
    @FXML
    private TableView<Person> teacherTableView;
    @FXML
    private TableColumn<Person, Integer> teacherID;
    @FXML
    private TableColumn<Person, String> teacherHo;
    @FXML
    private TableColumn<Person, String> teacherTen;
    @FXML
    private TableColumn<Person, Integer> teacherSDT;
    @FXML
    private TableColumn<Person, Date> teacherDate;
    @FXML
    private Button btnAddTeacher;
    @FXML
    private Button btnDeleteTeacher;
    @FXML
    private Button btnRefresh;
    private static Person selectedRow = null;
    public void handle() {
        btnAddTeacher.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage newstage = new Stage();
                teacherMain screen = new teacherMain();
                // ngăn tương tác với dashboard
                Stage oldStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                newstage.initModality(Modality.WINDOW_MODAL);
                newstage.initOwner(oldStage);
                // chạy newStage
                try {
                    screen.start(newstage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        });
        btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showTeacherList();
            }
        });

        btnDeleteTeacher.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedRow = selectRow();
                if (selectedRow != null) {
                    Person person = new Person(selectedRow.getPersonId(), selectedRow.getFirstName(), selectedRow.getLastName(),
                            selectedRow.getEmail(), selectedRow.getPhoneNumber(), selectedRow.getDateOfBirth(), selectedRow.getPersonImage());
                    teacherBus.deletePersonRole(selectedRow.getPersonId());
                    teacherBus.deletePerson(person);
                    System.out.println("Xóa thành công");
                    Alert("Thành công", "Xóa giáo viên thành công");
                } else {
                    Alert("Lỗi", "Vui lòng chọn 1 dòng trong bảng trước khi thực hiện huỷ");
                }
            }
        });
    }

    private void Alert(String title, String Message) {
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

    public Person selectRow() {
        if (teacherTableView.getSelectionModel().getSelectedIndex() < 0)
            return null;
        selectedRow = teacherTableView.getSelectionModel().getSelectedItem();
        return selectedRow;
    }

    public ObservableList<Person> getTeacherList() {
        teacherList = FXCollections.observableArrayList();
        return teacherList = teacherBus.getTeacherList();
    }

    public void showTeacherList() {
        teacherList = getTeacherList();
        teacherID.setCellValueFactory(new PropertyValueFactory<Person, Integer>("PersonId"));
        teacherHo.setCellValueFactory(new PropertyValueFactory<Person, String>("FirstName"));
        teacherTen.setCellValueFactory(new PropertyValueFactory<Person, String>("LastName"));
        teacherSDT.setCellValueFactory(new PropertyValueFactory<Person, Integer>("PhoneNumber"));
        teacherDate.setCellValueFactory(new PropertyValueFactory<Person, Date>("DateOfBirth"));
        teacherTableView.setItems(teacherList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getTeacherList();
        showTeacherList();
        handle();
    }
}
