package demoPosMarket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.control.TextInputDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class AddProductController {
    @FXML
    private TextField idTextField;
    @FXML
    private TextField descriptionTextField;
    @FXML
    private TextField priceTextField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private Button addButton;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button browseButton;
    @FXML
    private ImageView imageView;
    private ManageProduct manageProduct;
    private Connection connection;
    private File selectedImageFile;

    public void setManageProduct(ManageProduct manageProduct) {
        this.manageProduct = manageProduct;
        connection = manageProduct.getConnection();
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        // Get data from the fields
        String id = idTextField.getText();
        String description = descriptionTextField.getText();
        String price = priceTextField.getText();
        String category = categoryComboBox.getValue();
        String status = statusComboBox.getValue();

        // Check if any field is empty
        if (id.isEmpty() || description.isEmpty() || price.isEmpty() || categoryComboBox.getSelectionModel().isEmpty() || statusComboBox.getSelectionModel().isEmpty()) {
            // Show a warning if any field is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields.");
            alert.showAndWait();
        } else {
            try {
                // Create a PreparedStatement for the SQL query
                String query = "INSERT INTO products (id, description, price, category, status, image) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                // Set the values of the parameters
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, description);
                preparedStatement.setString(3, price);
                preparedStatement.setString(4, category);
                preparedStatement.setString(5, status);

                // Set the image parameter
                if (selectedImageFile != null) {
                    int len = (int) selectedImageFile.length();
                    //Log.i(len);
                    InputStream imageInputStream = new FileInputStream(selectedImageFile);
                    preparedStatement.setBinaryStream(6, imageInputStream , len);
                    //preparedStatement.setBlob(6, imageInputStream);
                    //preparedStatement.setNull(6, java.sql.Types.BLOB);
                    //preparedStatement.setBinaryStream(6, selectedImageFile , len);
                } else {
                    preparedStatement.setNull(6, java.sql.Types.BLOB);
                }

                // Execute the SQL statement
                preparedStatement.executeUpdate();

                // Show a success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Product saved successfully.");
                alert.showAndWait();

                // Clear the fields
                clearFields();

            } catch (SQLException | FileNotFoundException e) {
           // } catch (SQLException e) {
                // Handle exceptions
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to save product.");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddButton(ActionEvent event) {
        // Show a dialog to enter a new category
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Category");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter a new category:");

        // Get the value of the new category from the dialog
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String newCategory = result.get();

            if (newCategory.isEmpty()) {
                // Show a warning if the category is empty
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please enter a category.");
                alert.showAndWait();
            } else if (categoryComboBox.getItems().contains(newCategory)) {
                // Show a warning if the category already exists
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Category already exists.");
                alert.showAndWait();
            } else {
                // Add the new category to the ComboBox
                categoryComboBox.getItems().add(newCategory);

                // Clear the text field
                categoryComboBox.getSelectionModel().clearSelection();
            }
        }
    }

    private void clearFields() {
        idTextField.clear();
        descriptionTextField.clear();
        priceTextField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        statusComboBox.getSelectionModel().clearSelection();
        imageView.setImage(null);
        selectedImageFile = null;
    }

    @FXML
    private void handleBrowseButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Create an Image object from the selected file
                Image image = new Image(selectedFile.toURI().toString());
                // Display the image on the ImageView
                imageView.setImage(image);

                // Set the selected image file
                selectedImageFile = selectedFile;

            } catch (IllegalArgumentException e) {
                // Handle the exception if the selected image file is not found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Image file not found.");
                alert.showAndWait();
            } catch (Exception e) {
                // Handle other exceptions if any
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to load selected image.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void initialize() {
        // Initialize the initial values for the statusComboBox ComboBox
        if (statusComboBox.getItems().isEmpty()) {
            statusComboBox.getItems().addAll("True", "False");
        }
        addButton.setOnAction(this::handleAddButton);
    }
}
