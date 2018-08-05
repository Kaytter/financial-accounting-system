package sales;

import com.jfoenix.controls.*;
import dbCon.dbCon;
import javafx.animation.FadeTransition;
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
import javafx.scene.paint.Color;
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
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
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
    private ImageView Reports;


    @FXML
    private TableView<User> salesTableView;

    @FXML
    private TableColumn<?, ?> SaleIDCol;

    @FXML
    private TableColumn<?, ?> ItemCol;

    @FXML
    private TableColumn<?, ?> QuantityCol;

    @FXML
    private TableColumn<?, ?> DateCol;

    @FXML
    private TableColumn<?, ?> AmountCol;

    @FXML
    private TableColumn<?, ?> customerCol;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private JFXComboBox cbItem;

    @FXML
    private JFXTextField tfQuantity;

    @FXML
    private JFXDatePicker dpDate;

    @FXML
    private JFXTextField tfAmount;

    @FXML
    private JFXTextField tfCustomer;

    @FXML
    private JFXTextArea taDescription;

    @FXML
    private JFXButton btnsave;

    @FXML
    private ImageView Save;

    @FXML
    private JFXButton btnedit;

    @FXML
    private ImageView Edit;

    @FXML
    private JFXButton btnAddNew;

    @FXML
    private JFXButton btnSearchSaleID;

    @FXML
    private JFXButton btnDelete;

    @FXML
    private JFXTextField tfSearchSaleID;

    @FXML
    private JFXButton btnRefresh;

    @FXML
    private JFXCheckBox ckbCredit;

    @FXML
    private JFXButton btnSearchCustomer;

    @FXML
    private JFXTextField tfSearchCustomer;

    private boolean isaddNewButtonClick = false;
    private boolean isSetAdminEditButtonClick = false;
    int temp;
    private int unitsRemaining;
        private int delID;


    private void adminSetAllEnable() {
        cbItem.setDisable(false);
        taDescription.setDisable(false);
        tfQuantity.setDisable(false);
        dpDate.setDisable(false);
        tfAmount.setDisable(false);
        tfCustomer.setDisable(false);
        ckbCredit.setDisable(false);
        btnedit.setDisable(false);
        btnsave.setDisable(false);
    }

    private void adminSetAllDisable() {
        cbItem.setDisable(true);
        taDescription.setDisable(true);
        tfQuantity.setDisable(true);
        dpDate.setDisable(true);
        tfAmount.setDisable(true);
        tfCustomer.setDisable(true);
        ckbCredit.setDisable(true);
        btnedit.setDisable(true);
        btnsave.setDisable(true);

    }

    private void adminSetAllClear() {
        cbItem.setValue(null);
        taDescription.clear();
        tfQuantity.clear();
        dpDate.setValue(null);
        tfAmount.clear();
        tfCustomer.clear();
        ckbCredit.setSelected(false);

    }

    @FXML
    void btnAddNewClicked(ActionEvent event) {
        try {
            setComboBoxValues();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        adminSetAllEnable();
        btnedit.setDisable(true);
        isaddNewButtonClick = true;
        isSetAdminEditButtonClick = false;
    }

    @FXML
    void btnDeleteClicked(ActionEvent event) {
        try {
            User getSelectedRow = salesTableView.getSelectionModel().getSelectedItem();
            String sqlQuery = "delete from sales where saleID = '" + getSelectedRow.getSaleID() + "';";


            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();

            String sqlQuery1 = "delete from receivables where saleID = " + delID + ";";

            stmt.executeUpdate(sqlQuery);
            stmt.executeUpdate(sqlQuery1);
            stmt.executeUpdate("update inventory set quantity = " + delInvAdd(getSelectedRow.getItem(),getSelectedRow.getQuantity()) +" where item = '"+getSelectedRow.getItem()+"';");

            try {
                salesTableView.setItems(getDataFromSqlAndAddToObservableList("SELECT * FROM sales;"));

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
            User getSelectedRow = salesTableView.getSelectionModel().getSelectedItem();
            String sqlQuery = "select * from sales where (saleID= '" + getSelectedRow.getSaleID() + "');";
            temp = getSelectedRow.getSaleID();

            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            adminSetAllEnable();

            try {
                while (rs.next()) {


                          cbItem.setValue(rs.getString("item"));
                           tfQuantity.setText(String.valueOf(rs.getInt("quantity")));
                           dpDate.setValue(LocalDate.parse(rs.getString("date")));
                           tfAmount.setText(String.valueOf(rs.getInt("amount")));
                           tfCustomer.setText(rs.getString("customer"));
                           String status =(rs.getString("status"));

                           if (status=="credit")
                           {
                               ckbCredit.setSelected(true);
                           }else {
                               ckbCredit.setSelected(false);
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


        try {
            setComboBoxValues();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
    void btnExpensesClicked(ActionEvent event) throws IOException {
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

    @FXML
    void btnRefreshedClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        adminSetAllDisable();
        adminSetAllClear();
        btnedit.setDisable(false);
        tfSearchSaleID.clear();
        tfSearchCustomer.clear();
        tableViewAutoScroll(salesTableView);
        salesTableView.setItems(getDataFromSqlAndAddToObservableList("SELECT * FROM sales;"));//sql Query

    }

    @FXML
    void btnSaveClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        boolean ckbAdminSelected = ckbCredit.isSelected();

        String status;


        if (ckbAdminSelected==true)
        {
            status="credit";
        }
        else
        {
            status="cash";
        }


        btnedit.setDisable(false);

        Connection con = dbCon.getConnection();

        Statement stmt = con.createStatement();

        if (isAllFieldFillup()) {
            try {
                if (isaddNewButtonClick) {
                    if ( checkStock(cbItem.getValue().toString().trim(),Integer.parseInt(tfQuantity.getText())))
                    {
                        try {

                            stmt.executeUpdate("INSERT INTO sales (item,quantity,date,amount,customer,status) VALUES ('" + cbItem.getValue().toString() + "'," + Integer.parseInt(tfQuantity.getText()) + ",'" + dpDate.getValue() + "','" + tfAmount.getText() + "','" + tfCustomer.getText() +"','" + status +"');");

                            int id=0;
                            ResultSet rs = stmt.getGeneratedKeys();
                            if (rs.next())
                            {
                                id = rs.getInt( 1);
                            }

                            sendID(id);

                            if (status=="credit")
                                stmt.executeUpdate("INSERT INTO receivables (saleID,item,quantity,amount,purchaseDate,paymentDate,customer,status,balance) VALUES ("+ id +",'" + cbItem.getValue().toString().trim() + "'," + Integer.parseInt(tfQuantity.getText()) + "," + Integer.parseInt(tfAmount.getText()) + ",'"+ dpDate.getValue() + "','"+ " " + "','"+ tfCustomer.getText() + "','PENDING',"+ Integer.parseInt(tfAmount.getText()) + ");");

                            stmt.executeUpdate("update inventory set quantity = " + delInvSub( cbItem.getValue().toString().trim(),Integer.parseInt(tfQuantity.getText())) +" where item = '"+ cbItem.getValue().toString() +"';");

                            NotificationType notificationType = NotificationType.SUCCESS;
                            TrayNotification tray = new TrayNotification();
                            tray.setTitle("SAVED!");
                            tray.setMessage("The item(s) Has Been Successfully Saved.");
                            tray.setNotificationType(notificationType);
                            tray.showAndDismiss(Duration.millis(3000));

                        } catch (Exception e) {
                            System.out.println(e);

                        }
                    }else {
                        NotificationType notificationType = NotificationType.NOTICE;
                        TrayNotification tray = new TrayNotification();
                        tray.setTitle("STOCK LEVELS LOW!");
                        tray.setMessage("Only "+ unitsRemaining +" units of the item(s) is/are remaining.");
                        tray.setNotificationType(notificationType);
                        tray.showAndDismiss(Duration.millis(3000));
                    }

                } else if (isSetAdminEditButtonClick) {
                    try {
                        if (checkStock(cbItem.getValue().toString().trim(),Integer.parseInt(tfQuantity.getText())))
                        {
                            stmt.executeUpdate("update sales set " +
                                    "item = '" + cbItem.getValue().toString() + "'," +
                                    "quantity = " + Integer.parseInt(tfQuantity.getText()) + "," +
                                    "date = '" + dpDate.getValue() + "'," +
                                    "amount = '" + tfAmount.getText() + "'," +
                                    "customer = '" + tfCustomer.getText() + "'," +
                                    "status = '" + status +
                                    "' where saleID = " + temp + ";");
                            stmt.executeUpdate("update inventory set quantity = " + delInvSub(cbItem.getValue().toString().trim(),Integer.parseInt(tfQuantity.getText())) +" where item = '"+cbItem.getValue().toString().trim() +"';");
                            if (status=="credit")
                                stmt.executeUpdate("INSERT INTO receivables (saleID,item,quantity,amount,purchaseDate,paymentDate,customer,status,balance) VALUES ("+ delID +",'" + cbItem.getValue().toString().trim() + "'," + Integer.parseInt(tfQuantity.getText()) + "," + Integer.parseInt(tfAmount.getText()) + ",'"+ dpDate.getValue() + "','"+ " " + "','"+ tfCustomer.getText() + "','PENDING',"+ 0 + ");");
                                else
                            {
//                                stmt.execute("delete from sales where saleID = '" + temp + "';");
                            }
                        }else {
                            NotificationType notificationType = NotificationType.NOTICE;
                            TrayNotification tray = new TrayNotification();
                            tray.setTitle("STOCK LEVELS LOW!");
                            tray.setMessage("Only "+ unitsRemaining +" units of the item(s) is/are remaining.");
                            tray.setNotificationType(notificationType);
                            tray.showAndDismiss(Duration.millis(3000));
                        }


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
                try {
                    setComboBoxValues();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }

            adminSetAllClear();
            adminSetAllDisable();
            btnedit.setDisable(false);
            salesTableView.setItems(getDataFromSqlAndAddToObservableList("SELECT * FROM sales;"));


            tableViewAutoScroll(salesTableView);
        }


        try {
            setComboBoxValues();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendID(int id) {
        this.delID=id;
    }

    private boolean checkStock(String trim, int i) throws SQLException, ClassNotFoundException {
        boolean stock=false;

        int stockAv = 0, request = i;

        Connection con = dbCon.getConnection();
        Statement stmt;
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from inventory where item ='" + trim + "';");
        try {
            while (rs.next()) {
                if (rs.getString("quantity") != null)
                    stockAv = Integer.parseInt(rs.getString("quantity"));
                unitsRemaining=stockAv;
            }
        } catch (Exception e) {
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

            if ((stockAv-request) >= 0)
            {
                stock=true;
            }

            return stock;

        }
    }

    private void tableViewAutoScroll(TableView<User> tbv) {
        final int size = tbv.getItems().size();
        if (size > 0) {
            tbv.scrollTo(size - 1);
        }
    }

    private ObservableList<User> getDataFromSqlAndAddToObservableList(String query) throws ClassNotFoundException, SQLException {
        ObservableList<User> salesTableData = FXCollections.observableArrayList();
        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        try {
            while (rs.next()) {
                salesTableData.add(new User(
                        rs.getInt("saleID"),
                        rs.getString("item"),
                        rs.getInt("quantity"),
                        rs.getString("date"),
                        rs.getInt("amount"),
                        rs.getString("customer"),
                        rs.getString("status")
                ));
                salesTableView.setItems(salesTableData);

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


        return salesTableData;

    }


    private int delInvSub(String trim, int i) throws SQLException, ClassNotFoundException {

            int invQnty = 0, qntSub = i;

            Connection con = dbCon.getConnection();
            Statement stmt;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from inventory where item ='" + trim + "';");
        //noinspection finally
        try {
                while (rs.next()) {
                    if (rs.getString("quantity") != null)
                        invQnty = Integer.parseInt(rs.getString("quantity"));
                }
            } catch (Exception e) {
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

                return invQnty - qntSub;

            }
        }
    @SuppressWarnings("finally")
    private int delInvAdd(String trim, int i) throws SQLException, ClassNotFoundException {
        int toBeAdd = i, invQnty = 0;

        String s = trim;

        Connection con = dbCon.getConnection();
        Statement stmt;
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from inventory where item ='" + s + "';");
        //noinspection finally
        try {
            while (rs.next()) {
                if (rs.getString("quantity") != null)
                    invQnty = Integer.parseInt(rs.getString("quantity"));
            }
        } catch (Exception e) {
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

            return invQnty + toBeAdd;

        }
    }

    private boolean isAllFieldFillup() {
        boolean fillup;
        boolean date = false;

        if (dpDate.getValue()==null)
            date=true;

        if (cbItem.getSelectionModel().isEmpty() || tfQuantity.getText().isEmpty() || date) {

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
    void btnSearchSaleIDClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        try
        {
            String sqlQuery = "select * FROM sales WHERE saleID = " + Integer.parseInt(tfSearchSaleID.getText()) + ";";
            salesTableView.setItems(getDataFromSqlAndAddToObservableList(sqlQuery));
        }
       catch (Exception e)
       {

       }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setComboBoxValues();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SaleIDCol.setCellValueFactory(new PropertyValueFactory<>("saleID"));
        ItemCol.setCellValueFactory(new PropertyValueFactory<>("item"));
        QuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        DateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        AmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customer"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        try {
            getDataFromSqlAndAddToObservableList("SELECT * FROM sales");
            tableViewAutoScroll(salesTableView);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    private void setComboBoxValues() throws SQLException, ClassNotFoundException {
        ObservableList comboBoxData = FXCollections.observableArrayList();
        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select item FROM inventory");
        try {
            while (rs.next()) {
                comboBoxData.add(new userCombo(rs.getString("item")).getItem());

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
        Collections.sort(comboBoxData);  //Sort the Data in the ComboBox in Alphabetic order
        cbItem.setItems(comboBoxData);   // Inputs the data into the ComboBox
    }

    @FXML
    private void setTextAreaValues() throws SQLException, ClassNotFoundException {
        String item = (String) cbItem.getValue();
        String description = null;

        Connection con = dbCon.getConnection();
        Statement stmt;
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select description from inventory where item ='" + item + "';");

        try {
            while (rs.next()) {
                if (rs.getString("description") != null)
                    description = rs.getString("description");
            }
        } catch (Exception e) {
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

            taDescription.setText(description);

        }
    }

    @FXML
    private void getAmount() throws SQLException, ClassNotFoundException {
        try {
            int quantity = Integer.parseInt(tfQuantity.getText());
            int sellingPrice = 0;
            int amount = 0;
            String item = cbItem.getValue().toString().trim();

            Connection con = dbCon.getConnection();
            Statement stmt;
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select sellingPrice from inventory where item ='" + item + "';");

            try {
                while (rs.next()) {
                    if (rs.getString("sellingPrice") != null)
                        sellingPrice = Integer.parseInt(rs.getString("sellingPrice"));
                }
            } catch (Exception e) {
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

                amount=quantity*sellingPrice;
                tfAmount.setText(String.valueOf(amount));
            }

        } catch (Exception e) {

        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent ke) throws SQLException, ClassNotFoundException {
        String sqlQuery = "select * FROM sales WHERE customer like '%" + tfSearchCustomer.getText() + "%';";
        salesTableView.setItems(getDataFromSqlAndAddToObservableList(sqlQuery));
    }


}
