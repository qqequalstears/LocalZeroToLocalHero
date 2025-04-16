package Client.Controller.GUIControllers;

import Client.Controller.GUIMediator;
import Client.Controller.GUIMediatorImpl;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController {
    @FXML
    private ImageView background;
    @FXML
    private Label welcomeLabel;

    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private Button loginOrRegisterButton;

    @FXML
    private TextField mailTextfield;
    @FXML
    private TextField nameTextfield;
    @FXML
    private TextField cityTextfield;
    @FXML
    private PasswordField passwordTextfield;
    private GUIMediator guiMediator;

    public LoginController() {}

    @FXML
    public void initialize() {
        nameTextfield.setVisible(false);
        cityTextfield.setVisible(false);

        Image image = new Image("LogIn/background.png");
        background.setImage(image);

        guiMediator = GUIMediatorImpl.getInstance();
        guiMediator.registerController(this.getClass().getName(), this);
    }

    public void setLoginMenu() {
        cityTextfield.setVisible(false);
        nameTextfield.setVisible(false);
        loginOrRegisterButton.setText("Log in");
    }

    public void setRegisterMenu() {
        cityTextfield.setVisible(true);
        nameTextfield.setVisible(true);
        loginOrRegisterButton.setText("Register");
    }

    public void loginOrRegister() {
        String loginOrRegister = loginOrRegisterButton.getText();
        String mail = mailTextfield.getText();
        String password = passwordTextfield.getText();
        String city = cityTextfield.getText();
        String name = nameTextfield.getText();

        if (loginOrRegister.equals("Log in")) {
            handleLogin(mail, password);
        } else {
           handleRegister(mail,password,name,city);
        }
    }

    public void closeStage() {
        Stage stage = (Stage) loginOrRegisterButton.getScene().getWindow();
        stage.close();
    }

    private void handleLogin(String mail, String password) {
        if (!mail.isEmpty() && !password.isEmpty()) {
            loginOrRegisterButton.setDisable(false);
            guiMediator.notify("LOGIN",mail, password);
            guiMediator.notify("NEWSTAGE","HOMESTAGE");
        } else {
            guiMediator.notify("NOTIFYUSER", "Mail or password has not been entered, please try again");
        }
    }

    private void handleRegister(String mail, String password, String name, String city) {
        if (mail.contains("@")) {
            if (!password.isEmpty() && !name.isEmpty() && !city.isEmpty()) {
                loginOrRegisterButton.setDisable(true);
                guiMediator.notify("REGISTER", mail, password, name, city);
            } else {
                guiMediator.notify("NOTIFYUSER", "Mail, password, name or city has not been entered properly, please try again");
            }
        } else {
            guiMediator.notify("NOTIFYUSER", "The mail needs to contain @");
        }
    }
}