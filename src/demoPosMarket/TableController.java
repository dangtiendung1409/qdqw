package demoPosMarket;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

public class TableController {
    @FXML
    private Label lblTableName;
    @FXML
    private TextField txtTableName;
    @FXML
    private TableView<TableData> tableView;
    @FXML
    private TableColumn<TableData, Integer> colId;
    @FXML
    private TableColumn<TableData, String> colName;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;


    private ObservableList<TableData> tableDataList;

    @FXML
    private void initialize() {
        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(data -> data.getValue().nameProperty());

        tableDataList = FXCollections.observableArrayList();
        tableView.setItems(tableDataList);


        loadData();
    }

    void loadData() {
        // Clear the existing data
        tableDataList.clear();

        // Perform the database operation to load the data
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posjavafx", "root", "");
            String query = "SELECT * FROM Createtable_name";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                TableData tableData = new TableData(id, name);
                tableDataList.add(tableData);
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void saveData(ActionEvent event) {
        String tableName = txtTableName.getText().trim();

        if (tableName.isEmpty()) {
            // Display an error message or handle the empty input case
            return;
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posjavafx", "root", "");
            String query = "INSERT INTO Createtable_name (name) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, tableName);
            statement.executeUpdate();

            statement.close();
            connection.close();

            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtTableName.clear();
    }


    @FXML
    private void updateData(ActionEvent event) {
        TableData selectedTableData = tableView.getSelectionModel().getSelectedItem();
        if (selectedTableData == null) {
            // No item selected, display an error message or handle the case
            return;
        }

        // Create a new TextInputDialog to edit the name
        TextInputDialog dialog = new TextInputDialog(selectedTableData.getName());
        dialog.setTitle("Edit Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter the new name:");

        // Show the dialog and wait for the user response
        dialog.showAndWait().ifPresent(result -> {
            // User clicked OK, update the data
            String editedName = result.trim();

            if (editedName.isEmpty()) {
                // Display an error message or handle the empty input case
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Name cannot be empty!");
                alert.showAndWait();
                return;
            }

            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posjavafx", "root", "");
                String query = "UPDATE Createtable_name SET name = ? WHERE id = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, editedName);
                statement.setInt(2, selectedTableData.getId());
                statement.executeUpdate();

                statement.close();
                connection.close();

                loadData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    private void deleteData(ActionEvent event) {
        TableData selectedTableData = tableView.getSelectionModel().getSelectedItem();
        if (selectedTableData == null) {
            // No item selected, display an error message or handle the case
            return;
        }

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posjavafx", "root", "");
            String query = "DELETE FROM Createtable_name WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, selectedTableData.getId());
            statement.executeUpdate();

            statement.close();
            connection.close();

            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
