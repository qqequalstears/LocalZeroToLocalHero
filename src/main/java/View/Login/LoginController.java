package View.Login;

import View.Home.HomeController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

        this.stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setLoginMenu() {
        cityTextfield.setVisible(false);
        nameTextfield.setVisible(false);
    }

    public void setRegisterMenu() {
        cityTextfield.setVisible(true);
        nameTextfield.setVisible(true);
    }

    public void loginOrRegister() {
        System.out.println("Fest");
        HomeController homeController = new HomeController();
        homeController.creatHomeStage();
        stage.close();
    }
}
