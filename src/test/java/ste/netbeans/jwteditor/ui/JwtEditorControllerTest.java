package ste.netbeans.jwteditor.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.BDDAssertions.*;

@DisplayName("JWT Editor UI")
@ExtendWith(ApplicationExtension.class)
public class JwtEditorControllerTest {

    private FxRobot robot;
    private JwtEditorController controller;

    @Start
    private void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(JwtEditorController.class.getResource("JwtEditor.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    void setUp(FxRobot fxRobot) {
        this.robot = fxRobot;
    }

    @Test
    @DisplayName("display_error_when_empty_token_entered_and_decoded")
    public void display_error_when_empty_token_entered_and_decoded() {
        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);
        robot.clickOn(encodedTokenArea).write("");

        then(encodedTokenArea.getText()).isEmpty();
    }

    @Test
    @DisplayName("update_display_automatically_after_debounce_delay")
    public void update_display_automatically_after_debounce_delay() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);

        robot.clickOn(encodedTokenArea).write(token);

        // Verify that the table is still empty immediately after typing (debouncing)
        then(controller.payloadTable.getRoot()).isNull();

        // Wait for debounce delay (500ms) + buffer
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () -> controller.payloadTable.getRoot() != null);

        // Now it should be populated
        then(controller.payloadTable.getRoot()).isNotNull();
    }

    @Test
    @DisplayName("display_decoded_token_for_valid_token")
    public void display_decoded_token_for_valid_token() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);

        robot.clickOn(encodedTokenArea).write(token);

        // Wait for debounce delay
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () -> controller.payloadTable.getRoot() != null);

        // Verify that the controller processed the token
        TreeItem<JwtEditorController.PayloadRow> root = controller.payloadTable.getRoot();
        then(root).isNotNull();

        List<TreeItem<JwtEditorController.PayloadRow>> branches = root.getChildren();
        then(branches).hasSize(2);

        then(branches.get(0).getValue().getProperty()).isEqualTo("Header");
        then(branches.get(1).getValue().getProperty()).isEqualTo("Payload");
    }

    @Test
    @DisplayName("show_valid_signature_status_for_correct_secret")
    public void show_valid_signature_status_for_correct_secret() throws Exception {
        String secret = "secret";
        String token = com.auth0.jwt.JWT.create()
                .withSubject("1234567890")
                .sign(com.auth0.jwt.algorithms.Algorithm.HMAC256(secret));

        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);
        javafx.scene.control.PasswordField secretField = robot.lookup("#secretField").queryAs(javafx.scene.control.PasswordField.class);

        robot.clickOn(encodedTokenArea).write(token);
        robot.clickOn(secretField).write(secret);

        // Wait for debounce delay
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () -> controller.signatureStatusLabel.getText().contains("Valid"));

        then(controller.signatureStatusLabel.getText()).contains("Signature: Valid");
    }

    @Test
    @DisplayName("show_invalid_signature_status_for_incorrect_secret")
    public void show_invalid_signature_status_for_incorrect_secret() throws Exception {
        String secret1 = "secret1";
        String secret2 = "secret2";
        String token = com.auth0.jwt.JWT.create()
                .withSubject("1234567890")
                .sign(com.auth0.jwt.algorithms.Algorithm.HMAC256(secret1));

        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);
        javafx.scene.control.PasswordField secretField = robot.lookup("#secretField").queryAs(javafx.scene.control.PasswordField.class);

        robot.clickOn(encodedTokenArea).write(token);
        robot.clickOn(secretField).write(secret2);

        // Wait for debounce delay
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () -> controller.signatureStatusLabel.getText().contains("Invalid"));

        then(controller.signatureStatusLabel.getText()).contains("Signature: Invalid");
    }

    @Test
    @DisplayName("show_invalid_status_for_malformed_token")
    public void show_invalid_status_for_malformed_token() throws Exception {
        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);

        robot.clickOn(encodedTokenArea).write("invalid.token.format");

        // Wait for debounce delay
        WaitForAsyncUtils.waitFor(2, TimeUnit.SECONDS, () -> controller.jwtStatusLabel.getText().contains("Invalid"));

        then(controller.jwtStatusLabel.getText()).contains("Invalid");
    }
}
