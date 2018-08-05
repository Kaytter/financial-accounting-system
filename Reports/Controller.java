package Reports;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    private JFXButton btnExpenses;

    @FXML
    private JFXButton btnInventory;

    @FXML
    private JFXButton btnHR;

    @FXML
    private JFXButton btnReports;

    @FXML
    private ImageView btnSettings;

    @FXML
    private ImageView btnExit;

    @FXML
    private LineChart<String, Number> linechart;
    @FXML
    private AreaChart<?, ?> areaChart;

    @FXML
    private PieChart pieChart;
    private ObservableList data;

    private void loadLinechart() throws SQLException, ClassNotFoundException {
        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();
        String  qr= "  select item, amount FROM  sales;";
        ResultSet rs = stmt.executeQuery(qr);

        XYChart.Series<String,Number> series =new XYChart.Series<String, Number>();
        linechart.getData().clear();
        try
        {
            while (rs.next())
            {
                series.getData().add(new XYChart.Data<>(rs.getString(1),rs.getDouble(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        finally {
            if (rs != null)
            {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null)
            {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null)
            {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        linechart.getData().addAll(series);
    }

    private void loadAreachart() throws SQLException, ClassNotFoundException {
//        String  qr= "  select item, amount FROM  sales DESC LIMIT 10; ";
        String  qr= "  SELECT item, MAX(amount) FROM sales GROUP BY amount  LIMIT 5; ";
        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(qr);

        XYChart.Series series =new XYChart.Series();
        areaChart.getData().clear();
        areaChart.layout();
        try
        {
            while (rs.next())
            {
                series.getData().add(new XYChart.Data(rs.getString(1),rs.getDouble(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if (rs != null)
            {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null)
            {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null)
            {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        areaChart.getData().add(series);
    }


    public void loadPiechart() throws SQLException, ClassNotFoundException {

//        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(new PieChart.Data("Executed",60),new PieChart.Data("Passed",25),new PieChart.Data("Fails",15));
//        pieChart.setData(pieChartData);
        data = FXCollections.observableArrayList();
        String  qr= "  select item, quantity FROM  inventory;";
        Connection con = dbCon.getConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(qr);

        try
        {
            while (rs.next())
            {
                data.add(new PieChart.Data(rs.getString(1),rs.getDouble(2)));
            }

        }catch (Exception e)
        {

        }
        finally {
            if (rs != null)
            {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null)
            {
                try {
                    stmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null)
            {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        pieChart.getData().addAll(data);
    }

    @FXML
    void btnLoadClicked(ActionEvent event)
    {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadAreachart();
            loadPiechart();
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

}
