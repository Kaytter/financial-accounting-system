package receivables;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
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
import javafx.scene.image.ImageView;
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
    private JFXButton btnInventory;

    @FXML
    private JFXButton btnHR;

    @FXML
    private JFXButton btnReports;

    @FXML
    private JFXButton btnSettings;

    @FXML
    private JFXButton btnExit;

    @FXML
    private TableView<User> rcTableView;

    @FXML
    private TableColumn<?, ?> rcIDCol;

    @FXML
    private TableColumn<?, ?> saleCol;

    @FXML
    private TableColumn<?, ?> itemCol;

    @FXML
    private TableColumn<?, ?> quantityCol;

    @FXML
    private TableColumn<?, ?> amountCol;

    @FXML
    private TableColumn<?, ?> purchaseDateCol;

    @FXML
    private TableColumn<?, ?> paymentDateCol;

    @FXML
    private TableColumn<?, ?> customerCol;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private TableColumn<?, ?> balanceCol;

    @FXML
    private TableColumn<?, ?> amountPaidCol;

    @FXML
    private JFXTextField tfRcID;

    @FXML
    private JFXTextField tfSaleID;

    @FXML
    private JFXTextField tfAmount;

    @FXML
    private JFXDatePicker dpPayment;

    @FXML
    private JFXTextField tfAmountPaid;

    @FXML
    private JFXTextField tfBalance;

    @FXML
    private JFXButton btnConfPayment;

    @FXML
    private ImageView btnSave;

    @FXML
    private JFXButton btnLoad;

    @FXML
    private ImageView btnEdit;

    @FXML
    private JFXButton btnSearchRcID;

    @FXML
    private JFXTextField tfSearchRcID;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXCheckBox ckbPaid;

    @FXML
    private JFXButton btnSearchSaleID;

    @FXML
    private JFXTextField tfSearchSaleID;

     @FXML
    private JFXTextField tfCustomer;


    @FXML
    private JFXTextField tfSearchCustomerName;

    private boolean isaddNewButtonClick = false;
    private boolean isSetAdminEditButtonClick = false;
    int temp;

    private void adminSetAllEnable() {

        dpPayment.setDisable(false);
        tfAmountPaid.setDisable(false);

        ckbPaid.setDisable(false);
        btnConfPayment.setDisable(false);
    }

    private void adminSetAllDisable() {
        tfRcID.setDisable(true);
        tfSaleID.setDisable(true);
        tfAmount.setDisable(true);
        tfCustomer.setDisable(true);
        tfAmount.setDisable(true);
        dpPayment.setDisable(true);
        tfAmountPaid.setDisable(true);
        tfBalance.setDisable(true);
        ckbPaid.setDisable(true);
        btnConfPayment.setDisable(true);
    }

    private void adminSetAllClear() {
        tfRcID.clear();
        tfSaleID.clear();
        tfAmount.clear();
        tfCustomer.clear();
        tfAmount.clear();
        dpPayment.setValue(null);
        tfAmountPaid.clear();
        tfBalance.clear();
        ckbPaid.setSelected(false);

    }

    @FXML
    private void showBalance()
    {

        try {
            int amountPaid = 0;
            int amountOwed = 0;
            int balance = 0;

            amountPaid= Integer.parseInt(tfAmountPaid.getText());
            amountOwed= Integer.parseInt(tfAmount.getText());
            balance = amountOwed - amountPaid;

            tfBalance.setText(String.valueOf(balance));
        }catch (Exception e)
        {

        }


    }

    @FXML
    void btnConfPaymentClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        boolean ckbAdminSelected = ckbPaid.isSelected();
        String status;
        if (ckbAdminSelected == true) {
            status = "PAID";
        } else {
            status = "PENDING";
        }

        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();

        if (isAllFieldFillup()) {
                    try {
                            stmt.executeUpdate("update receivables set " +
                                    "rcID = " + Integer.parseInt(tfRcID.getText())+ "," +
                                    "saleID = " + Integer.parseInt(tfSaleID.getText()) + "," +
                                    "amount = " + Integer.parseInt(tfAmount.getText()) + "," +
                                    "customer = '" + tfCustomer.getText() + "'," +
                                    "paymentDate = '" + dpPayment.getValue() + "'," +
                                    "amountPaid = " + Integer.parseInt(tfAmountPaid.getText()) + "," +
                                    "balance = " + Integer.parseInt(tfBalance.getText()) + "," +
                                    "status = '" + status +
                                    "' where rcID = " + temp + ";");

                        NotificationType notificationType = NotificationType.SUCCESS;
                        TrayNotification tray = new TrayNotification();
                        tray.setTitle("SAVED!");
                        tray.setMessage("Changes Successfully Saved.");
                        tray.setNotificationType(notificationType);
                        tray.showAndDismiss(Duration.millis(3000));

                    } catch (Exception e) {
                        NotificationType notificationType = NotificationType.ERROR;
                        TrayNotification tray = new TrayNotification();
                        tray.setTitle("WARNING!");
                        tray.setMessage(String.valueOf(e));
                        System.out.println(e);
                        tray.setNotificationType(notificationType);
                        tray.showAndDismiss(Duration.millis(3000));
                    }

            finally {

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
            rcTableView.setItems(getDataFromSqlAndAddToObservableList("SELECT * FROM receivables;"));

            tableViewAutoScroll(rcTableView);
        }
    }

    @FXML
    void btnLoadClicked(ActionEvent event) {
        try {
            adminSetAllClear();
            User getSelectedRow = rcTableView.getSelectionModel().getSelectedItem();
            String sqlQuery = "select * from receivables where (rcID= '" + getSelectedRow.getRcID() + "');";
            temp = getSelectedRow.getRcID();

            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            adminSetAllEnable();

            try {
                while (rs.next()) {


                    tfRcID.setText(String.valueOf(rs.getInt("rcID")));
                    tfSaleID.setText(String.valueOf(rs.getInt("saleID")));
                    tfAmount.setText(String.valueOf(rs.getInt("amount")));
                    tfCustomer.setText(rs.getString("customer"));
                    dpPayment.setValue(LocalDate.parse(rs.getString("paymentDate")));
                    tfAmountPaid.setText(String.valueOf(rs.getInt("amountPaid")));
                    tfCustomer.setText(rs.getString("customer"));
                    tfBalance.setText(rs.getString("balance"));
                    String status =(rs.getString("status"));

                    if (status=="credit")
                    {
                        ckbPaid.setSelected(false);
                    }else {
                        ckbPaid.setSelected(true);
                    }

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
        btnSave.setDisable(false);
        tfSearchRcID.clear();
        tfSearchCustomerName.clear();
        tfSearchSaleID.clear();
        tableViewAutoScroll(rcTableView);
        rcTableView.setItems(getDataFromSqlAndAddToObservableList("SELECT * FROM receivables;"));//sql Query

    }

    @FXML
    void btnSearchRcIDClicked(ActionEvent event) {
        try
        {
            String sqlQuery = "select * FROM receivables WHERE rcID = " + Integer.parseInt(tfSearchRcID.getText()) + ";";
            rcTableView.setItems(getDataFromSqlAndAddToObservableList(sqlQuery));
        }
        catch (Exception e)
        {

        }
    }

    @FXML
    void btnSearchSaleIDClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        String sqlQuery = "select * FROM receivables WHERE saleID = " + Integer.parseInt(tfSearchSaleID.getText()) + ";";
        rcTableView.setItems(getDataFromSqlAndAddToObservableList(sqlQuery));
    }

    private void tableViewAutoScroll(TableView<User> tbv) {
        final int size = tbv.getItems().size();
        if (size > 0) {
            tbv.scrollTo(size - 1);
        }
    }

    private ObservableList<User> getDataFromSqlAndAddToObservableList(String query) throws ClassNotFoundException, SQLException {
        ObservableList<User> rcTableData = FXCollections.observableArrayList();
        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        try {
            while (rs.next()) {
                rcTableData.add(new User(
                        rs.getInt("rcID"),
                        rs.getInt("saleID"),
                        rs.getString("item"),
                        rs.getInt("quantity"),
                        rs.getInt("amount"),
                        rs.getString("purchaseDate"),
                        rs.getString("paymentDate"),
                        rs.getString("customer"),
                        rs.getInt("amountPaid"),
                        rs.getString("status"),
                        rs.getInt("balance")
                ));
                rcTableView.setItems(rcTableData);

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
        return rcTableData;
    }

    @FXML
    private void tfSearchCustomerNameTyped(KeyEvent ke) throws SQLException, ClassNotFoundException {
        String sqlQuery = "select * FROM receivables WHERE customer like '%" + tfSearchCustomerName.getText() + "%';";
        rcTableView.setItems(getDataFromSqlAndAddToObservableList(sqlQuery));
    }

    private boolean isAllFieldFillup() {
        boolean fillup;
        boolean date = false;

        if (dpPayment.getValue()==null)
            date=true;

        if(tfAmountPaid.getText().trim().isEmpty()||date){

            NotificationType notificationType = NotificationType.INFORMATION;
            TrayNotification tray = new TrayNotification();
            tray.setTitle("Attention!!!");
            tray.setMessage("Date of Payment or Amount Paid should not Empty.");
            tray.setNotificationType(notificationType);
            tray.showAndDismiss(Duration.millis(3000));

            fillup = false;
        }
        else fillup = true;
        return fillup;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rcIDCol.setCellValueFactory(new PropertyValueFactory<>("rcID"));
        saleCol.setCellValueFactory(new PropertyValueFactory<>("saleID"));
        itemCol.setCellValueFactory(new PropertyValueFactory<>("item"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        purchaseDateCol.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        paymentDateCol.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        amountPaidCol.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        balanceCol.setCellValueFactory(new PropertyValueFactory<>("balance"));

        try {
            getDataFromSqlAndAddToObservableList("SELECT * FROM receivables;");
            tableViewAutoScroll(rcTableView);
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
    void btnReceivablesClicked(ActionEvent event) throws IOException {
        Parent recParent = FXMLLoader.load(getClass().getResource("/receivables/receivables.fxml"));
        Scene recScene = new Scene(recParent);
        Stage recStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        recStage.setScene(recScene);
        recStage.show();
    }

    @FXML
    void btnHRclicked(ActionEvent event) throws IOException {
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
