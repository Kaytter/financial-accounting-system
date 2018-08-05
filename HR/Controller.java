package HR;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import dbCon.dbCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sales.User;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private JFXButton btnSale;

    @FXML
    private JFXButton btnReceivable;

    @FXML
    private JFXButton btnInventory;

    @FXML
    private JFXButton btnHr;

    @FXML
    private JFXButton btnReports;

    @FXML
    private JFXButton btnSettings;

    @FXML
    private JFXButton btnExit;

    @FXML
    private TableView<user> hrTableView;

    @FXML
    private TableColumn<?, ?> empIdCol;

    @FXML
    private TableColumn<?, ?> fNameCol;

    @FXML
    private TableColumn<?, ?> lNameCol;

    @FXML
    private TableColumn<?, ?> sNameCol;

    @FXML
    private TableColumn<?, ?> IdNoCol;

    @FXML
    private TableColumn<?, ?> emailCol;

    @FXML
    private TableColumn<?, ?> deptCol;

    @FXML
    private TableColumn<?, ?> doeCol;

    @FXML
    private TableColumn<?, ?> phoneNoCol;

    @FXML
    private TableColumn<?, ?> dobCol;

    @FXML
    private JFXTextField tfFname;

    @FXML
    private JFXDatePicker dpDob;

    @FXML
    private JFXTextField tfSurname;

    @FXML
    private JFXTextField tfIdNo;

    @FXML
    private JFXTextField tfEmail;

    @FXML
    private JFXTextField tfPhoneNo;

    @FXML
    private JFXTextField tfDepartment;

    @FXML
    private JFXTextField tfLname;

    @FXML
    private JFXDatePicker dpDoe;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnAddNew;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTextField tfSearchFirstName;

    @FXML
    private JFXButton btnRefresh;

    private boolean isaddNewButtonClick = false;
    private boolean isSetAdminEditButtonClick = false;
    int temp;

    private void adminSetAllEnable() {
        tfFname.setDisable(false);
        tfLname.setDisable(false);
        tfSurname.setDisable(false);
        tfIdNo.setDisable(false);
        tfEmail.setDisable(false);
        tfDepartment.setDisable(false);
        dpDoe.setDisable(false);
        tfPhoneNo.setDisable(false);
        dpDob.setDisable(false);
        btnSave.setDisable(false);
        btnEdit.setDisable(false);
    }

    private void adminSetAllDisable() {
        tfFname.setDisable(true);
        tfLname.setDisable(true);
        tfSurname.setDisable(true);
        tfIdNo.setDisable(true);
        tfEmail.setDisable(true);
        tfDepartment.setDisable(true);
        dpDoe.setDisable(true);
        tfPhoneNo.setDisable(true);
        dpDob.setDisable(true);
        btnSave.setDisable(true);
        btnEdit.setDisable(true);
    }

    private void adminSetAllClear() {
        tfSearchFirstName.clear();
        tfFname.clear();
        tfLname.clear();
        tfSurname.clear();
        tfIdNo.clear();
        tfEmail.clear();
        tfDepartment.clear();
        dpDoe.setValue(null);
        tfPhoneNo.clear();
        dpDob.setValue(null);
      }

    @FXML
    void btnAddNewClicked(ActionEvent event) {
        adminSetAllEnable();
        btnEdit.setDisable(true);
        isaddNewButtonClick = true;
        isSetAdminEditButtonClick = false;
    }

    @FXML
    void btnDeleteClicked(ActionEvent event) {
        try {
            user getSelectedRow = hrTableView.getSelectionModel().getSelectedItem();
            String sqlQuery = "delete from employee where empID = " + getSelectedRow.getEmpID() + ";";

            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();

            stmt.executeUpdate(sqlQuery);

            try {
                hrTableView.setItems(getDataFromSqlAndAddToObservableList("SELECT * FROM employee;"));

                NotificationType notificationType = NotificationType.SUCCESS;
                TrayNotification tray = new TrayNotification();
                tray.setTitle("CONFIRMED!");
                tray.setMessage("The Selected Row Has Been Successfully Deleted.");
                tray.setNotificationType(notificationType);

                tray.showAndDismiss(Duration.millis(3000));
            } catch (SQLException e) {
                System.out.println("Here");
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (con != null) {
                    try {
                        con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    @FXML
    void btnEditClicked(ActionEvent event) {
        try {
            user getSelectedRow = hrTableView.getSelectionModel().getSelectedItem();
            String sqlQuery = "select * from employee where (empID= " + getSelectedRow.getEmpID() + ");";
            temp = getSelectedRow.getEmpID();

            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            adminSetAllEnable();

            try {
                while (rs.next()) {
                    tfFname.setText(rs.getString("fName"));
                    tfLname.setText(rs.getString("lName"));
                    tfSurname.setText(rs.getString("sName"));
                    tfIdNo.setText(rs.getString("idNo"));
                    tfEmail.setText(rs.getString("email"));
                    tfDepartment.setText(rs.getString("department"));
                    dpDoe.setValue(LocalDate.parse(rs.getString("doe")));
                    tfPhoneNo.setText(rs.getString("pNumber"));
                    dpDob.setValue(LocalDate.parse(rs.getString("dob")));
                }
                isSetAdminEditButtonClick = true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (con != null) {
                    try {
                        con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
            isaddNewButtonClick = false;
        } catch (Exception e) {

        }

    }

    @FXML
    void btnRefreshedClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        adminSetAllDisable();
        adminSetAllClear();
        btnEdit.setDisable(false);
        tfSearchFirstName.clear();
        tableViewAutoScroll(hrTableView);
        hrTableView.setItems(getDataFromSqlAndAddToObservableList("SELECT * FROM employee;"));//sql Query

    }

    @FXML
    void btnSaveClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        btnEdit.setDisable(false);

        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();

        if (isAllFieldFillup()) {
            try {
                if (isaddNewButtonClick) {
                        try {

                            stmt.executeUpdate("INSERT INTO employee (fName,lName,sName,idNo,email,department,doe,pNumber,dob) VALUES ('" + tfFname.getText() + "','" + tfLname.getText() + "','" + tfSurname.getText() + "','" + tfIdNo.getText() + "','" + tfEmail.getText() +"','" + tfDepartment.getText()+"','" + dpDoe.getValue()+"','" + tfPhoneNo.getText()+"','" + dpDob.getValue() +"');");

                            NotificationType notificationType = NotificationType.SUCCESS;
                            TrayNotification tray = new TrayNotification();
                            tray.setTitle("SAVED!");
                            tray.setMessage("The employee Has Been Successfully added!  .");
                            tray.setNotificationType(notificationType);
                            tray.showAndDismiss(Duration.millis(3000));

                        } catch (Exception e) {
                            System.out.println(e);

                        }
                } else if (isSetAdminEditButtonClick) {
                    try {
                           stmt.executeUpdate("update employee set " +
                                    "fName = '" + tfFname.getText() + "'," +
                                    "lName = '" + tfLname.getText() + "'," +
                                    "sName = '" + tfSurname.getText()+ "'," +
                                    "idNo = '" + tfIdNo.getText() + "'," +
                                    "email = '" + tfEmail.getText() + "'," +
                                    "department = '" + tfDepartment.getText() + "'," +
                                    "doe = '" + dpDoe.getValue() + "'," +
                                    "pNumber = '" + tfPhoneNo.getText() + "'," +
                                    "dob = '" + dpDob.getValue() +
                                    "' where empID = " + temp + ";");

                        } catch (Exception e) {
                        NotificationType notificationType = NotificationType.ERROR;
                        TrayNotification tray = new TrayNotification();
                        tray.setTitle("WARNING!");
                        tray.setMessage(String.valueOf(e));
                        tray.setNotificationType(notificationType);
                        tray.showAndDismiss(Duration.millis(3000));
                    }
                }
            } finally {

                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            adminSetAllClear();
            adminSetAllDisable();
            btnEdit.setDisable(false);
            hrTableView.setItems(getDataFromSqlAndAddToObservableList("SELECT * FROM employee;"));


            tableViewAutoScroll(hrTableView);
        }


    }

    private boolean isAllFieldFillup() {
        boolean fillup;
        boolean date = false;
        boolean date1 = false;

        if (dpDob.getValue()==null)
            date=true;

        if (dpDoe.getValue()==null)
            date1=true;

        if (tfFname.getText().isEmpty() ||tfLname.getText().isEmpty() ||tfSurname.getText().isEmpty() ||tfIdNo.getText().isEmpty() ||tfEmail.getText().isEmpty() || tfPhoneNo.getText().isEmpty() || date || date1) {

            NotificationType notificationType = NotificationType.INFORMATION;
            TrayNotification tray = new TrayNotification();
            tray.setTitle("Attention!!!");
            tray.setMessage("Fill In Information At All Fields.");
            tray.setNotificationType(notificationType);
            tray.showAndDismiss(Duration.millis(3000));

            fillup = false;
        } else fillup = true;
        return fillup;
    }

    @FXML
    void tfSearchFirstName(KeyEvent event) throws SQLException, ClassNotFoundException {
        String sqlQuery = "select * FROM employee WHERE fName like '%" + tfSearchFirstName.getText() + "%';";
        hrTableView.setItems(getDataFromSqlAndAddToObservableList(sqlQuery));
    }

    private ObservableList<user> getDataFromSqlAndAddToObservableList(String query) throws ClassNotFoundException, SQLException {
        ObservableList<user> hrTableData = FXCollections.observableArrayList();
        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        try {
            while (rs.next()) {
                hrTableData.add(new user(
                        rs.getInt("empID"),
                        rs.getString("fName"),
                        rs.getString("lName"),
                        rs.getString("sName"),
                        rs.getString("idNo"),
                        rs.getString("email"),
                        rs.getString("department"),
                        rs.getString("doe"),
                        rs.getString("pNumber"),
                        rs.getString("dob")
                ));
                hrTableView.setItems(hrTableData);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }


        return hrTableData;

    }

    private void tableViewAutoScroll(TableView<user> tbv) {
        final int size = tbv.getItems().size();
        if (size > 0) {
            tbv.scrollTo(size - 1);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        empIdCol.setCellValueFactory(new PropertyValueFactory<>("empID"));
        fNameCol.setCellValueFactory(new PropertyValueFactory<>("fName"));
        lNameCol.setCellValueFactory(new PropertyValueFactory<>("lName"));
        sNameCol.setCellValueFactory(new PropertyValueFactory<>("sName"));
        IdNoCol.setCellValueFactory(new PropertyValueFactory<>("idNo"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        deptCol.setCellValueFactory(new PropertyValueFactory<>("department"));
        doeCol.setCellValueFactory(new PropertyValueFactory<>("doe"));
        phoneNoCol.setCellValueFactory(new PropertyValueFactory<>("pNumber"));
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dob"));

        try {
            getDataFromSqlAndAddToObservableList("SELECT * FROM employee");
            tableViewAutoScroll(hrTableView);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnExitClicked(ActionEvent event) throws IOException {
        Alert t = new Alert(Alert.AlertType.CONFIRMATION);
        t.initStyle(StageStyle.UTILITY);
        t.setHeaderText("Are You Sure You Want to EXIT?");

        Optional <ButtonType> result = t.showAndWait();
        if (result.get()== ButtonType.OK)
        {
            System.exit(1);
        }

    }

    @FXML
    void btnReceivableClicked(ActionEvent event) throws IOException {
        Parent recParent = FXMLLoader.load(getClass().getResource("/receivables/receivables.fxml"));
        Scene recScene = new Scene(recParent);
        Stage recStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        recStage.setScene(recScene);
        recStage.show();
    }

    @FXML
    void btnHrClicked(ActionEvent event) throws IOException {
        Parent HRParent = FXMLLoader.load(getClass().getResource("/HR/hr.fxml"));
        Scene HRScene = new Scene(HRParent);
        Stage HRStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        HRStage.setScene(HRScene);
        HRStage.show();
    }

    @FXML
    void btnInventoryClicked(ActionEvent event) throws IOException {
        Parent invParent = FXMLLoader.load(getClass().getResource("/inventory/inventory.fxml"));
        Scene invScene = new Scene(invParent);
        Stage invStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        invStage.setScene(invScene);
        invStage.show();
    }


    @FXML
    void btnReportsClicked(ActionEvent event) throws IOException {
        Parent reportParent = FXMLLoader.load(getClass().getResource("/Reports/reports.fxml"));
        Scene reportScene = new Scene(reportParent);
        Stage reportStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        reportStage.setScene(reportScene);
        reportStage.show();
    }

    @FXML
    void btnSaleClicked(ActionEvent event) throws IOException {
        Parent salesParent = FXMLLoader.load(getClass().getResource("/sales/sales.fxml"));
        Scene salesScene = new Scene(salesParent);
        Stage salesStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        salesStage.setScene(salesScene);
        salesStage.show();
    }

    @FXML
    void btnSettingsClicked(ActionEvent event) throws IOException {
        Parent settingsParent = FXMLLoader.load(getClass().getResource("/settings/settings.fxml"));
        Scene settingsScene = new Scene(settingsParent);
        Stage settingsStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        settingsStage.setScene(settingsScene);
        settingsStage.show();
    }


}
