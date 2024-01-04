package login_ver2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class login extends Application {
    ArrayList<User> userList = new ArrayList<>();
    ArrayList<User> userpasshash = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        userList.add(new User("user01", "123"));
        userList.add(new User("user02", "456"));
        userList.add(new User("user03", "789"));

        String salt = BCrypt.gensalt(10);

        for (User user : userList) {
            String hashedPassword = BCrypt.hashpw(user.getPassword(), salt);
            User passhas = new User(user.getUsername(), hashedPassword);
            userpasshash.add(passhas);
        }

        String url = "jdbc:mysql://localhost:3306/logininfover2";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = "INSERT INTO logininfover2 (username, password) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);

            for (User user : userpasshash) {
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("loginform.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

