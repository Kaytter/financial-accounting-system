package inventory;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import dbCon.dbCon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private JFXButton btnSales;

    @FXML
    private JFXButton btnReceivables;

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
    private TableView<userDel> treeView;

    @FXML
    private TableColumn<?, ?> deliveryIDCol;

    @FXML
    private TableColumn<?, ?> ItemColItems;

    @FXML
    private TableColumn<?, ?> QuantityColItems;

    @FXML
    private TableColumn<?, ?> dateItems;

    @FXML
    private TableColumn<?, ?> supplier;

    @FXML
    private JFXComboBox <String> cbItem;

    @FXML
    private JFXTextField tfQuantityItems;

    @FXML
    private JFXDatePicker dpDateItems;

    @FXML
    private JFXTextField tfSupplier;

    @FXML
    private JFXTextField tfSearchDelItem;

    @FXML
    private JFXButton btnSaveItems;

    @FXML
    private JFXButton btnAddNewItems;

    @FXML
    private JFXButton btnSearchItems;

    @FXML
    private JFXButton btnDeleteItems;

    @FXML
    private JFXTextField tfSearchDeliveryID;

    @FXML
    private JFXButton btnRefreshItems;

    @FXML
    private TableView<user> treeView1;

    @FXML
    private TableColumn<?, ?> invIDCol;

    @FXML
    private TableColumn<?, ?> ItemColInv;

    @FXML
    private TableColumn<?, ?> descriptionCol;

    @FXML
    private TableColumn<?, ?> quantityInvCol;

    @FXML
    private TableColumn<?, ?> sellingPriceCol;

    @FXML
    private JFXTextField tfItem;

    @FXML
    private JFXTextField tfQuantityInv;

    @FXML
    private JFXTextField tfDescription;

    @FXML
    private JFXTextField tfAmountInv;

    @FXML
    private JFXButton btnSaveInv;

    @FXML
    private JFXButton btnEditInv;

    @FXML
    private JFXButton btnAddInv;

    @FXML
    private JFXButton btnSearchInv;

    @FXML
    private JFXButton btnDeleteInv;


    @FXML
    private JFXButton btnSearchDelItem;

    @FXML
    private JFXTextField tfSearchInvID;

    @FXML
    private JFXTextField tfSearchInvItem;


    @FXML
    private JFXButton btnRefreshInv;

    //Inventory variables
    private boolean isaddNewButtonClickInv = false;
    private boolean isSetAdminEditButtonClickInv = false;
    int tempInv;

    //Delivery variables
    private boolean isaddNewButtonClickDel = false;
    private boolean isSetAdminEditButtonClickDel = false;
    int tempDel;

    private void adminSetAllEnableDel() {
        cbItem.setDisable(false);
        tfQuantityItems.setDisable(false);
        dpDateItems.setDisable(false);
        tfSupplier.setDisable(false);
        btnSaveItems.setDisable(false);
    }

    private void adminSetAllDisableDel() {
        cbItem.setDisable(true);
        tfQuantityItems.setDisable(true);
        dpDateItems.setDisable(true);
        tfSupplier.setDisable(true);
        btnSaveItems.setDisable(true);
    }

    private void adminSetAllClearDel() {
        cbItem.setItems(null);
        tfQuantityItems.clear();
        dpDateItems.setValue(null);
        tfSupplier.clear();
        tfSearchDeliveryID.clear();

    }

    public void btnAddNewItemsClicked(ActionEvent actionEvent) {
        try {
            setComboBoxValues();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        adminSetAllEnableDel();

        isaddNewButtonClickDel = true;
        isSetAdminEditButtonClickDel = false;
    }

    public void btnDeleteDelClicked(ActionEvent actionEvent) {
        try {
            userDel getSelectedRow = (userDel) treeView.getSelectionModel().getSelectedItem();
            String sqlQuery = "delete from delivery where deliveryID = '" + getSelectedRow.getDeliveryID() + "';";
            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();

            stmt.executeUpdate(sqlQuery);
            stmt.executeUpdate("update inventory set quantity = " + delInvSub(getSelectedRow.getQuantity(),getSelectedRow.getItem()) +" where item = '"+getSelectedRow.getItem()+"';");

            treeView1.setItems(getDataFromSqlAndAddToObservableListInv("SELECT * FROM inventory;"));
            try {

                treeView.setItems(getDataFromSqlAndAddToObservableListDel("SELECT * FROM delivery;"));
                
                
                NotificationType notificationType = NotificationType.SUCCESS;
                TrayNotification tray = new TrayNotification();
                tray.setTitle("CONFIRMED!");
                tray.setMessage("The Selected Row Has Been Successfully Deleted.");
                tray.setNotificationType(notificationType);

                tray.showAndDismiss(Duration.millis(3000));
            } catch (SQLException e) {
                e.printStackTrace();
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

    private int delInvSub(int quantity, String i) throws SQLException, ClassNotFoundException {
        int invNow = quantity, delBefore = 0;

        String id = i;

        Connection con = dbCon.getConnection();
        Statement stmt;
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from inventory where item ='" + id + "';");
        try {
            while (rs.next()) {
                if (rs.getString("quantity") != null)
                    delBefore = Integer.parseInt(rs.getString("quantity"));
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

            return delBefore - invNow;

        }
    }
    @FXML
    void btnEditClicked(ActionEvent event) {
        try {
            setComboBoxValues();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            userDel getSelectedRow = (userDel) treeView.getSelectionModel().getSelectedItem();
            String sqlQuery = "select * from delivery where (deliveryID= '" + getSelectedRow.getDeliveryID() + "');";
            tempDel = getSelectedRow.getDeliveryID();

            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            adminSetAllEnableDel();

            try {
                while (rs.next()) {

                    cbItem.setValue(rs.getString("item"));
                    tfQuantityItems.setText(rs.getString("quantity"));
                    dpDateItems.setValue(LocalDate.parse(rs.getString("date")));
                    tfSupplier.setText(rs.getString("supplier"));

                }
                isSetAdminEditButtonClickDel = true;
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
            isaddNewButtonClickDel = false;
        } catch (Exception e) {

        }

    }

    @FXML
    void btnSaveClicked(ActionEvent event) throws SQLException, ClassNotFoundException {


        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();

        if (isAllFieldFillupDel()) {
            try {
                if (isaddNewButtonClickDel) {

                    try {
                        stmt.executeUpdate("INSERT INTO delivery (item,quantity,date,supplier) VALUES ('" + cbItem.getValue().toString().trim() + "'," + Integer.parseInt(tfQuantityItems.getText()) + ",'" + dpDateItems.getValue() + "','" + tfSupplier.getText() + "');");
                        stmt.executeUpdate("update inventory set quantity = " + delInvAdd(cbItem.getValue().toString().trim(),Integer.parseInt(tfQuantityItems.getText())) +" where item = '"+cbItem.getValue().toString().trim() +"';");

                        treeView1.setItems(getDataFromSqlAndAddToObservableListInv("SELECT * FROM inventory;"));

                        NotificationType notificationType = NotificationType.SUCCESS;
                        TrayNotification tray = new TrayNotification();
                        tray.setTitle("SAVED!");
                        tray.setMessage("The item(s) Has Been Successfully Saved.");
                        tray.setNotificationType(notificationType);
                        tray.showAndDismiss(Duration.millis(3000));

                    } catch (Exception e) {
                       System.out.println(e);

                    }
                } else if (isSetAdminEditButtonClickDel) {
                    try {
                        stmt.executeUpdate("update delivery set " +
                                "item = '" + cbItem.getValue().toString().trim() + "'," +
                                "quantity = " + Integer.parseInt(tfQuantityItems.getText()) + "," +
                                "date = '" + dpDateItems.getValue() + "'," +
                                "supplier = '" + tfSupplier.getText() +
                                "' where deliveryID = " + tempDel + ";");
                        stmt.executeUpdate("update inventory set quantity = " + delInvAdd(cbItem.getValue().toString().trim(),Integer.parseInt(tfQuantityItems.getText())) +" where item = '"+cbItem.getValue().toString().trim() +"';");

                        treeView1.setItems(getDataFromSqlAndAddToObservableListInv("SELECT * FROM inventory;"));

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

            adminSetAllClearDel();
            adminSetAllDisableDel();

            treeView.setItems(getDataFromSqlAndAddToObservableListDel("SELECT * FROM delivery;"));


            tableViewAutoScroll(treeView);
        }

    }

    private int delInvAdd(String s, int trim) throws SQLException, ClassNotFoundException {
        int invNow = 0, delBefore;

        delBefore = trim;

        Connection con = dbCon.getConnection();
        Statement stmt;
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from inventory where item ='" + s + "';");
        try {
            while (rs.next()) {
                if (rs.getString("quantity") != null)
                    invNow = Integer.parseInt(rs.getString("quantity"));
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

            return invNow + delBefore;

        }
    }

    private boolean isAllFieldFillupDel() {
        boolean fillup;
        if (cbItem.getSelectionModel().isEmpty() || tfQuantityItems.getText().isEmpty() || dpDateItems.getValue().equals(null) || tfSupplier.getText().isEmpty()) {

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


    public void btnSearchClicked(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String sqlQuery = "select * FROM delivery WHERE deliveryID = " + Integer.parseInt(tfSearchDeliveryID.getText()) + ";";
        treeView.setItems(getDataFromSqlAndAddToObservableListDel(sqlQuery));
    }

    public void btnSearchDelItemClicked(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        String sqlQuery = "select * FROM delivery WHERE item like '%" + tfSearchDelItem.getText() + "%';";

        treeView.setItems(getDataFromSqlAndAddToObservableListDel(sqlQuery));
    }

    public void btnRefreshedDelClicked(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        adminSetAllDisableDel();
        adminSetAllClearDel();

        tfSearchDeliveryID.clear();
        tfSearchDelItem.clear();
        tableViewAutoScroll(treeView);
        treeView.setItems(getDataFromSqlAndAddToObservableListDel("SELECT * FROM delivery;"));//sql Query

    }

    private ObservableList getDataFromSqlAndAddToObservableListDel(String query) throws ClassNotFoundException, SQLException {
        ObservableList<userDel> delTableData = FXCollections.observableArrayList();
        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        try {
            while (rs.next()) {
                delTableData.add(new userDel(
                        rs.getInt("deliveryID"),
                        rs.getString("item"),
                        rs.getInt("quantity"),
                        rs.getString("date"),
                        rs.getString("supplier")
                ));
                treeView.setItems(delTableData);

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


        return delTableData;

    }


    private void adminSetAllEnableInv() {
        btnSaveInv.setDisable(false);
        tfItem.setDisable(false);
        tfDescription.setDisable(false);
        tfQuantityInv.setDisable(false);
        tfAmountInv.setDisable(false);

    }

    private void adminSetAllDisableInv() {
        btnSaveInv.setDisable(true);
        tfItem.setDisable(true);
        tfDescription.setDisable(true);
        tfQuantityInv.setDisable(true);
        tfAmountInv.setDisable(true);
    }

    private void adminSetAllClearInv() {
        tfItem.clear();
        tfDescription.clear();
        tfQuantityInv.clear();
        tfAmountInv.clear();

    }

    @FXML
    void btnAddNewInvClicked(ActionEvent event) {
        adminSetAllEnableInv();
        btnEditInv.setDisable(true);
        isaddNewButtonClickInv = true;
        isSetAdminEditButtonClickInv = false;
    }

    @FXML
    void btnDeleteInvClicked(ActionEvent event) {
        try {
            user getSelectedRow = treeView1.getSelectionModel().getSelectedItem();
            String sqlQuery = "delete from inventory where invID = '" + getSelectedRow.getInvID() + "';";
            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();

            stmt.executeUpdate(sqlQuery);
            try {
                treeView1.setItems(getDataFromSqlAndAddToObservableListInv("SELECT * FROM inventory;"));

                NotificationType notificationType = NotificationType.SUCCESS;
                TrayNotification tray = new TrayNotification();
                tray.setTitle("CONFIRMED!");
                tray.setMessage("The Selected Row Has Been Successfully Deleted.");
                tray.setNotificationType(notificationType);

                tray.showAndDismiss(Duration.millis(3000));
            } catch (SQLException e) {
                e.printStackTrace();
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



    private boolean isAllFieldFillupInv() {
        boolean fillup;
        if (tfItem.getText().trim().isEmpty() || tfDescription.getText().isEmpty() || tfQuantityInv.getText().trim().isEmpty() || tfAmountInv.getText().isEmpty()) {

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
    void btnSaveInvClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        btnEditInv.setDisable(false);

        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();

        if (isAllFieldFillupInv()) {
            try {
                if (isaddNewButtonClickInv) {

                    try {
                        stmt.executeUpdate("INSERT INTO inventory (item,description,quantity,sellingPrice) VALUES ('" + tfItem.getText() + "','" + tfDescription.getText() + "'," + Integer.parseInt(tfQuantityInv.getText()) + "," + Integer.parseInt(tfAmountInv.getText()) + ");");

                        NotificationType notificationType = NotificationType.SUCCESS;
                        TrayNotification tray = new TrayNotification();
                        tray.setTitle("SAVED!");
                        tray.setMessage("The item(s) Has Been Successfully Saved.");
                        tray.setNotificationType(notificationType);
                        tray.showAndDismiss(Duration.millis(3000));

                    } catch (Exception e) {
                        NotificationType notificationType = NotificationType.ERROR;
                        TrayNotification tray = new TrayNotification();
                        tray.setTitle("ERROR!");
                        tray.setMessage("That Item Already Exists.");
                        tray.setNotificationType(notificationType);
                        tray.showAndDismiss(Duration.millis(3000));
                    }
                } else if (isSetAdminEditButtonClickInv) {
                    try {
                        stmt.executeUpdate("update inventory set " +
                                "item = '" + tfItem.getText() + "'," +
                                "description = '" + tfDescription.getText() + "'," +
                                "quantity = " + Integer.parseInt(tfQuantityInv.getText()) + "," +
                                "sellingPrice = " + Integer.parseInt(tfAmountInv.getText()) +
                                " where invID = " + tempInv + ";");
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

            adminSetAllClearInv();
            adminSetAllDisableInv();
            treeView1.setItems(getDataFromSqlAndAddToObservableListInv("SELECT * FROM inventory;"));


            tableViewAutoScroll(treeView1);
        }

    }

    private void tableViewAutoScroll(TableView tbv) {
        final int size = tbv.getItems().size();
        if (size > 0) {
            tbv.scrollTo(size - 1);
        }
    }


    private ObservableList getDataFromSqlAndAddToObservableListInv(String query) throws ClassNotFoundException, SQLException {
        ObservableList<user> invTableData = FXCollections.observableArrayList();
        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        try {
            while (rs.next()) {
                invTableData.add(new user(
                        rs.getInt("invID"),
                        rs.getString("item"),
                        rs.getString("description"),
                        rs.getInt("quantity"),
                        rs.getInt("sellingPrice")
                ));
                treeView1.setItems(invTableData);

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


        return invTableData;

    }

    @FXML
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
    void btnEditInvClicked(ActionEvent event) {
        try {
            user getSelectedRow = treeView1.getSelectionModel().getSelectedItem();
            String sqlQuery = "select * from inventory where (invID= '" + getSelectedRow.getInvID() + "');";
            tempInv = getSelectedRow.getInvID();

            Connection con = dbCon.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            adminSetAllEnableInv();

            try {
                while (rs.next()) {

                    tfItem.setText(rs.getString("item"));
                    tfDescription.setText(rs.getString("description"));
                    tfQuantityInv.setText(rs.getString("quantity"));
                    tfAmountInv.setText(rs.getString("sellingPrice"));

                }
                isSetAdminEditButtonClickInv = true;
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
            isaddNewButtonClickInv = false;
        } catch (Exception e) {

        }

    }

    @FXML
    void btnRefreshedInvClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        adminSetAllDisableInv();
        adminSetAllClearInv();
        btnEditInv.setDisable(false);

        tfSearchInvItem.clear();
        tableViewAutoScroll(treeView1);
        treeView1.setItems(getDataFromSqlAndAddToObservableListInv("SELECT * FROM inventory;"));//sql Query

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Set Column Values of Inventory TableView
        invIDCol.setCellValueFactory(new PropertyValueFactory<>("invID"));
        ItemColInv.setCellValueFactory(new PropertyValueFactory<>("item"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantityInvCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        sellingPriceCol.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));

        try {
            getDataFromSqlAndAddToObservableListInv("SELECT * FROM inventory");
            tableViewAutoScroll(treeView1);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        //        cbItem.bindAutoCompletion  - Research on this for comboBox autocompletion

        //Set ComboBox Values for Items In ComboBox
        try {
            setComboBoxValues();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        //Set Column Values of Delivery TableView
        deliveryIDCol.setCellValueFactory(new PropertyValueFactory<>("deliveryID"));
        ItemColItems.setCellValueFactory(new PropertyValueFactory<>("item"));
        QuantityColItems.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        dateItems.setCellValueFactory(new PropertyValueFactory<>("date"));
        supplier.setCellValueFactory(new PropertyValueFactory<>("supplier"));

        try {
            getDataFromSqlAndAddToObservableListDel("SELECT * FROM delivery");
            tableViewAutoScroll(treeView);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

    private void btnSearchInvItemClicked() throws SQLException, ClassNotFoundException {
//        String sqlQuery = "select * FROM inventory WHERE item like '%" + tfSearchInvItem.getText() + "%';";
//        treeView1.setItems(getDataFromSqlAndAddToObservableListInv(sqlQuery));

    }

    @FXML
    private void handleKeyPressedInvItemName(KeyEvent ke) throws SQLException, ClassNotFoundException {
        String sqlQuery = "select * FROM inventory WHERE item like '%" + tfSearchInvItem.getText() + "%';";
        treeView1.setItems(getDataFromSqlAndAddToObservableListInv(sqlQuery));
    }
    @FXML
    private void handleKeyPressedDelItem(KeyEvent ke) throws SQLException, ClassNotFoundException {
        String sqlQuery = "select * FROM delivery WHERE item like '%" + tfSearchDelItem.getText() + "%';";

        treeView.setItems(getDataFromSqlAndAddToObservableListDel(sqlQuery));

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
