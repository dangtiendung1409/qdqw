package demoPosMarket;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {
    @FXML
    private Label lblUsername;
    @FXML
    private Button btnManageTable;

    private static Stage primaryStage = null;

    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        // TODO: Add initialization logic if needed
    }

    public static void setPrimaryStage(Stage primaryStage) {
        Home.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @FXML
    private void manageTable(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                openModalWindow("AddTable.fxml", "Manage Tables");
            } catch (IOException ex) {
                System.out.println("" + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    private void openModalWindow(String resource, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
        Parent root = loader.load();

        Stage window = new Stage();
        window.setScene(new Scene(root));
        window.initModality(Modality.APPLICATION_MODAL);
        window.setAlwaysOnTop(true);
        window.setTitle(title);
        setPrimaryStage(window);
        window.showAndWait();
    }

    @FXML
    private void actionManageProduct(ActionEvent event) {
        Platform.runLater(() -> {
            try {
                openModalWindow("ManageProduct.fxml", "Manage Products");
            } catch (IOException ex) {
                System.out.println("" + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        System.out.println("Key pressed: " + event.getCode());
        switch (event.getCode()) {
            case F1:
                System.out.println("New Order");
                break;
            case F2:
                System.out.println("Payment Triggered.");
                break;
            case F3:
                System.out.println("Cancel order.");
                break;
            case F4:
                System.out.println("Manage products.");
                Platform.runLater(() -> {
                    try {
                        openModalWindow("Products.fxml", "Manage Products");
                    } catch (IOException ex) {
                        System.out.println("" + ex.getMessage());
                        ex.printStackTrace();
                    }
                });
                break;
            case F5:
                System.out.println("Manage Table");
                Platform.runLater(() -> {
                    try {
                        openModalWindow("Tables.fxml", "Manage Tables");
                    } catch (IOException ex) {
                        System.out.println("" + ex.getMessage());
                        ex.printStackTrace();
                    }
                });
                break;
            case F6:
                System.out.println("Sales Report");
                break;
            case F7:
                Platform.runLater(() -> {
                    try {
                        openModalWindow("Lookup.fxml", "Product Lookup");
                    } catch (IOException ex) {
                        System.out.println("" + ex.getMessage());
                        ex.printStackTrace();
                    }
                });
                break;
            case F8:
                System.out.println("Logout.");
                break;
        }
    }
}
