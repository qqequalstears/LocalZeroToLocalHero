package Client.Controller.GUIControllers.LoginController;

import Client.Controller.GUIControllers.FxController;
import Client.Controller.GUIControllers.GUIControllerRegistry;
import Client.Controller.GUIControllers.GUIInController;
import Client.Controller.GUIControllers.GUIOutController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController implements FxController {
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
    private GUIOutController guiOutController;
    private GUIInController guiInController;


    public LoginController() {
    }

    @FXML
    public void initialize() {
        nameTextfield.setVisible(false);
        cityTextfield.setVisible(false);

        Image image = new Image("LogIn/background.png");
        background.setImage(image);

        guiOutController = GUIOutController.getInstance();
        guiInController = GUIInController.getInstance();
        GUIControllerRegistry.getInstance().add(this.getClass().getName(), this);
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
            handleRegister(mail, password, name, city);
        }
    }

    public void closeStage() {
        Stage stage = (Stage) loginOrRegisterButton.getScene().getWindow();
        stage.close();
    }

    private void handleLogin(String mail, String password) {
        if (!mail.isEmpty() && !password.isEmpty()) {
            // loginOrRegisterButton.setDisable(false); //todo lägg till detta om vi villa ha bättre inloggningsprocess
            guiOutController.login(mail, password);
        } else {
            guiInController.notifyUser("Mail or password has not been entered, please try again");
        }
    }

    private void handleRegister(String mail, String password, String name, String city) {
        if (mail.contains("@")) {
            if (!password.isEmpty() && !name.isEmpty() && !city.isEmpty()) {
                //loginOrRegisterButton.setDisable(true); //todo lägg till detta om vi villa ha bättre inloggningsprocess
                guiOutController.register(mail, password, name, city);
            } else {
                guiInController.notifyUser("Mail, password, name or city has not been entered properly, please try again");
            }
        } else {
            guiInController.notifyUser("The mail needs to contain @");
        }
    }

}