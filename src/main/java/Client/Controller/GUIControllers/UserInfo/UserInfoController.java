package Client.Controller.GUIControllers.UserInfo;

import Client.Controller.GUIControllers.FxController;

import Client.Controller.GUIControllers.GUIControllerRegistry;
import Client.Controller.GUIControllers.GUIInController;
import Client.Controller.GUIControllers.GUIOutController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class UserInfoController implements FxController {
    @FXML
    private ComboBox rolesComboBox;

    @FXML
    private TextField nameTextfield;

    @FXML
    private TextField locationTextfield;

    @FXML
    private TextField mailTextfield;

    @FXML
    private TextField currentRolesTextfield;

    @FXML
    private Button confirmButton;
    private GUIOutController guiOutController;
    private GUIInController guiInController;

    @FXML
    public void initialize() {
        GUIControllerRegistry.getInstance().add(this.getClass().getName(), this);
        guiInController = GUIInController.getInstance();
        guiOutController = GUIOutController.getInstance();
        List<String> roles = new ArrayList<>();
        roles.add("Resident");
        roles.add("Taxi");
        roles.add("Handyman");
        roles.add("CommunityManager");
        roles.add("Admin");
        rolesComboBox.getItems().addAll(roles);
    }

    public void confirm() {
        if (currentRolesTextfield.getText().isEmpty()) {
            guiInController.notifyUser("The user needs to have at least one role");
        } else {
            String roles = currentRolesTextfield.getText();
            String mail = mailTextfield.getText();
            guiOutController.updateUsersRoles(roles, mail);
            closeStage();
        }
    }

    public void changeRole() {
        String selectedRole = rolesComboBox.getValue().toString();
        String currentText = currentRolesTextfield.getText();
        String newText;
        if (currentText.contains(selectedRole)) {
            newText = currentText.replace(selectedRole + ", ", "");
        } else {
            newText = currentText + selectedRole + ", ";
        }
        currentRolesTextfield.setText(newText);
    }

    public void disableAdminPrivileges() {
        rolesComboBox.setDisable(true);
        confirmButton.setDisable(true);
    }

    public void setInformation(String name, String location, String mail, String roles) {
        nameTextfield.setText(name);
        locationTextfield.setText(location);
        mailTextfield.setText(mail);
        currentRolesTextfield.setText(roles);
    }

    @Override
    public void closeStage() {
        Stage stage = (Stage) rolesComboBox.getScene().getWindow();
        stage.close();
    }
}