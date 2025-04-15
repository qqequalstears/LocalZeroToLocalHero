package Client.View.Login;

import Client.Controller.GUIMediator;
import Client.Controller.GUIMediatorImpl;
import Client.View.Home.HomeController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class LoginController extends Application {
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
    private Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login/LoginStage.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, 400);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.setTitle("Welcome to LocalZeroToHero");
        nameTextfield.setVisible(false);
        cityTextfield.setVisible(false);
        Image image = new Image("LogIn/background.png");
        background.setImage(image);
        guiMediator = GUIMediatorImpl.getInstance();
        guiMediator.registerController(this.getClass().getName(), this);

        this.stage.show();
    }

    public static void main(String[] args) {
        launch(args);
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

    private void handleLogin(String mail, String password) {
        if (!mail.isEmpty() && !password.isEmpty()) {
            guiMediator.notify("LOGIN",mail, password);
        } else {
            guiMediator.notify("NOTIFYUSER", "Mail or password has not been entered, please try again");
        }
    }

    private void handleRegister(String mail, String password, String name, String city) {
        if (mail.contains("@") && !password.isEmpty() && !name.isEmpty() && !city.isEmpty()) { // todo Möjligtvivs lägg till bättre felhantering
            guiMediator.notify("REGISTER", mail,password,name,city);
        } else {
            guiMediator.notify("NOTIFYUSER", "Mail, password, name or city has not been entered properly, please try again");
        }
    }
}