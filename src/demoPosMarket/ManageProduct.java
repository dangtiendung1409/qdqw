package demoPosMarket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ManageProduct {
    private Connection connection;

    public ManageProduct() {
        // Thay đổi thông tin kết nối cho phù hợp với cấu hình cơ sở dữ liệu của bạn
        String url = "jdbc:mysql://localhost:3306/posjavafx";
        String username = "root";
        String password = "";

        try {
            // Khởi tạo kết nối
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @FXML
    private void xuLyNutTaoMoi(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProduct.fxml"));
            Parent root = loader.load();

            // Get the controller instance
            AddProductController addProductController = loader.getController();

            // Set the connection instance
            addProductController.setManageProduct(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Root cause: " + e.getCause());
        }
    }

    public void addProduct(String id, String description, String price, String category, String status, String image) {
        // Ví dụ sử dụng JDBC để lưu dữ liệu vào bảng Quản lý Sản phẩm
        try {
            String query = "INSERT INTO products (id, description, price, category, status, image) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, price);
            preparedStatement.setString(4, category);
            preparedStatement.setString(5, status);
            preparedStatement.setString(6, image);
            preparedStatement.executeUpdate();

            // Đóng kết nối
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
