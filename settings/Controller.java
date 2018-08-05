package settings;

import com.jfoenix.controls.*;
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
    private JFXButton btnSales;

    @FXML
    private JFXButton btnExpenses;

    @FXML
    private JFXButton btnInventory;

    @FXML
    private JFXButton btnHR;

    @FXML
    private JFXButton btnReport;

    @FXML
    private JFXButton btnSetting;

    @FXML
    private JFXButton btnExits;

    @FXML
    private TableView<User> userTableView;

    @FXML
    private TableColumn<?, ?> userIDCol;

    @FXML
    private TableColumn<?, ?> usernameCol;

    @FXML
    private TableColumn<?, ?> usertypeCol;

    @FXML
    private TableColumn<?, ?> doaCol;

    @FXML
    private JFXTextField tfUsername;

    @FXML
    private JFXDatePicker dpDate;

    @FXML
    private JFXPasswordField pfPassword;

    @FXML
    private JFXButton btnSave;

    @FXML
    private JFXButton btnEdit;

    @FXML
    private JFXButton btnAddNew;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTextField tfSearchSaleID;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXCheckBox ckbAdmin;

    @FXML
    private JFXTextField tfSearchUsername;

    private boolean isaddNewButtonClick = false;
    private boolean isSetAdminEditButtonClick = false;
    int temp;



    private void adminSetAllEnable() {

        tfUsername.setDisable(false);
        pfPassword.setDisable(false);
        ckbAdmin.setDisable(false);
        btnSave.setDisable(false);
        btnEdit.setDisable(false);
    }

    private void adminSetAllDisable() {
        tfUsername.setDisable(true);
        pfPassword.setDisable(true);
        ckbAdmin.setDisable(true);
        btnSave.setDisable(true);
        btnEdit.setDisable(true);
    }

    private void adminSetAllClear() {

        tfUsername.clear();
        pfPassword.clear();
        ckbAdmin.setSelected(false);
        tfSearchUsername.clear();
        tfSearchSaleID.clear();

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
            User getSelectedRow = userTableView.getSelectionModel().getSelectedItem();
            String sqlQuery = "delete from login where userID = '" + getSelectedRow.getUserID() + "';";


            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sqlQuery);

            try {
                userTableView.setItems(getDataFromSqlAndAddToObservableList("SELECT * FROM login;"));

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
            User getSelectedRow = userTableView.getSelectionModel().getSelectedItem();
            String sqlQuery = "select * from login where (userID= '" + getSelectedRow.getUserID() + "');";
            temp = getSelectedRow.getUserID();

            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            adminSetAllEnable();

            try {
                while (rs.next()) {

                    tfUsername.setText(rs.getString("userName"));
                    pfPassword.setText(rs.getString("password"));

                    if (rs.getString("userType").equals("admin"))
                        ckbAdmin.setSelected(true);


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

    private boolean isAllFieldFillup() {
        boolean fillup;

        if (tfUsername.getText().isEmpty() || pfPassword.getText().isEmpty()) {

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
    void btnRefreshedClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        adminSetAllDisable();
        adminSetAllClear();
        btnEdit.setDisable(false);
        tfSearchSaleID.clear();
        tfSearchUsername.clear();
        tableViewAutoScroll(userTableView);
        userTableView.setItems(getDataFromSqlAndAddToObservableList("SELECT * FROM login;"));//sql Query
    }

    @FXML
    void btnSaveClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        btnEdit.setDisable(false);

        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();

        if (isAllFieldFillup()) {
            String userType = null;
            try {
                if (isaddNewButtonClick) {

                        try {

                            if (ckbAdmin.isSelected())
                                userType="admin";
                            else userType="user";


                                stmt.executeUpdate("INSERT INTO login (userName,password,userType) VALUES ('" + tfUsername.getText()+ "','" + pfPassword.getText() + "','" + userType +"');");

                            NotificationType notificationType = NotificationType.SUCCESS;
                            TrayNotification tray = new TrayNotification();
                            tray.setTitle("SAVED!");
                            tray.setMessage("The user(s) Has Been Successfully Saved.");
                            tray.setNotificationType(notificationType);
                            tray.showAndDismiss(Duration.millis(3000));

                        } catch (Exception e) {
                            System.out.println(e);

                        }


                } else if (isSetAdminEditButtonClick) {
                    try {
                        if (ckbAdmin.isSelected())
                            userType="admin";
                        else userType="user";
                              stmt.executeUpdate("update login set " + "userName = '" + tfUsername.getText() + "'," +"password = '" + pfPassword.getText() + "'," +"userType = '" + userType +
                                        "' where userID = " + temp + ";");

                    } catch (Exception e) {
//                        NotificationType notificationType = NotificationType.ERROR;
//                        TrayNotification tray = new TrayNotification();
//                        tray.setTitle("WARNING!");
//                        tray.setMessage(String.valueOf(e));
//                        tray.setNotificationType(notificationType);
//                        tray.showAndDismiss(Duration.millis(3000));
                        System.out.print(e);
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
            userTableView.setItems(getDataFromSqlAndAddToObservableList("SELECT * FROM login    ;"));


            tableViewAutoScroll(userTableView);
        }

    }

    @FXML
    void btnSearchClicked(ActionEvent event) {
        try
        {
            String sqlQuery = "select * FROM login WHERE userID = " + Integer.parseInt(tfSearchSaleID.getText()) + ";";
            userTableView.setItems(getDataFromSqlAndAddToObservableList(sqlQuery));
        }
        catch (Exception e)
        {

        }
    }

    @FXML
    void tfSearchUsername(KeyEvent event) throws SQLException, ClassNotFoundException {
        String sqlQuery = "select * FROM login WHERE userName like '%" + tfSearchUsername.getText() + "%';";
        userTableView.setItems(getDataFromSqlAndAddToObservableList(sqlQuery));
    }

    private ObservableList<settings.User> getDataFromSqlAndAddToObservableList(String query) throws ClassNotFoundException, SQLException {

        ObservableList<settings.User> settingsTableData = FXCollections.observableArrayList();
        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        try {
            while (rs.next()) {
                settingsTableData.add(new settings.User(
                        rs.getInt("userID"),
                        rs.getString("userName"),
                        rs.getString("userType")
                ));
                userTableView.setItems(settingsTableData);

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


        return settingsTableData;

    }

    private void tableViewAutoScroll(TableView<User> tbv) {
        final int size = tbv.getItems().size();
        if (size > 0) {
            tbv.scrollTo(size - 1);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        usertypeCol.setCellValueFactory(new PropertyValueFactory<>("userType"));

        try {
            getDataFromSqlAndAddToObservableList("SELECT * FROM login;");
            tableViewAutoScroll(userTableView);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void btnExitClicked(ActionEvent event) throws IOException {
        Alert t = new Alert(Alert.AlertType.CONFIRMATION);
        t.initStyle(StageStyle.UTILITY);
        t.setHeaderText("Are You Sure You Want to EXIT?");

        Optional<ButtonType> result = t.showAndWait();
        if (result.get()== ButtonType.OK)
        {
            System.exit(1);
        }

    }

    @FXML
    void btnExpensesClicked(ActionEvent event) throws IOException {
        Parent recParent = FXMLLoader.load(getClass().getResource("/receivables/receivables.fxml"));
        Scene recScene = new Scene(recParent);
        Stage recStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        recStage.setScene(recScene);
        recStage.show();
    }

    @FXML
    void btnHRClicked(ActionEvent event) throws IOException {
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
    void btnReportClicked(ActionEvent event) throws IOException {
        Parent reportParent = FXMLLoader.load(getClass().getResource("/Reports/reports.fxml"));
        Scene reportScene = new Scene(reportParent);
        Stage reportStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        reportStage.setScene(reportScene);
        reportStage.show();
    }

    @FXML
    void btnSalesClicked(ActionEvent event) throws IOException {
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
