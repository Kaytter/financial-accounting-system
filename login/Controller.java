package login;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import dbCon.dbCon;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private JFXTextField tfuserName;

    @FXML
    private JFXPasswordField pfPassword;

    @FXML
    private JFXButton btnExit;

    @FXML
    private JFXCheckBox ckbAdmin;

    @FXML
    private AnchorPane apMain;
    @FXML
    private JFXButton btnYes;

    @FXML
    private JFXButton btnNo;

    @FXML
    private Label lbLabel;

    @FXML
    void btnExitClicked(ActionEvent event) throws IOException {
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
    public void btnLoginClicked(Event event) throws ClassNotFoundException {
        if (isAllFieldFillup())
    {
        String userName=tfuserName.getText();
        String password = pfPassword.getText();
        boolean ckbAdminSelected = ckbAdmin.isSelected();

        String userType;

        if (ckbAdminSelected==true)
        {
            userType="admin";
        }
        else
        {
            userType="user";
        }


        try {
            if(isValidCredentials(userName,password,userType)) {
                NotificationType notificationType = NotificationType.SUCCESS;
                TrayNotification tray = new TrayNotification();
                tray.setTitle("WELCOME");
                tray.setMessage("Login is Successful!");
                tray.setNotificationType(notificationType);
                tray.showAndDismiss(Duration.millis(3000));

                lbLabel.setVisible(true);
                lbLabel.setText("Login Confirmed.");

                try {

                    try {
                        String content = tfuserName.getText() + "  :: Logged in on :   " + Calendar.getInstance().getTime() + "\n\n";

                        File file = new File("LOG.txt");

                        if (!file.exists())
                        {
                            file.createNewFile();
                        }

                        FileWriter fw = new FileWriter(file,true);

                        BufferedWriter bw = new BufferedWriter(fw);
                        bw.write(content);
                        bw.write("\r\n");
                        bw.close();
                    }catch (Exception e)
                    {
                        System.out.println("Exception Occurred");
                    }
                    Parent salesParent = FXMLLoader.load(getClass().getResource("/sales/sales.fxml"));
                    Scene salesScene = new Scene(salesParent);
                    Stage salesStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    FadeTransition ft=new FadeTransition(Duration.millis(2000), salesParent);
                    salesScene.setFill(Color.valueOf("#1f8ca3"));
                    ft.setFromValue(0.0);
                    ft.setToValue(1.0);
                    ft.play();
                    salesStage.setScene(salesScene);
                    salesStage.centerOnScreen();
                    salesStage.show();


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}

    private boolean isValidCredentials(String userName, String password, String userType) throws SQLException, ClassNotFoundException {
        boolean userPassOk = false;
        Connection con= dbCon.getConnection();
        Statement stmt;
        stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery( "select * from login where userName ='"+userName+"' and  password='"+password+"' and userType = '"+userType+"';" );
        try{
            while (rs.next()) {
                if (rs.getString("userName") != null && rs.getString("password") != null&& rs.getString("userType") != null) {
                    userPassOk = true;

                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if (rs!=null)
            {
                try{
                    rs.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (stmt!=null)
            {
                try{
                    stmt.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            if (con!=null)
            {
                try{
                    con.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        if(!userPassOk) {
            lbLabel.setVisible(true);
            lbLabel.setText("Credentials Not Valid.");
            userPassOk = false;
        }



        return userPassOk;
    }

    private boolean isAllFieldFillup(){
        boolean fillup;
        if(tfuserName.getText().trim().isEmpty()){

            NotificationType notificationType = NotificationType.INFORMATION;
            TrayNotification tray = new TrayNotification();
            tray.setTitle("Attention!!!");
            tray.setMessage("userName or Password should not Empty.");
            tray.setNotificationType(notificationType);
            tray.showAndDismiss(Duration.millis(3000));

            fillup = false;
        }
        else fillup = true;
        return fillup;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
