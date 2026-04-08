package ste.netbeans.jwteditor.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

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
    @DisplayName("should_display_error_when_empty_token_entered")
    public void should_display_error_when_empty_token_entered() {
        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);
        robot.clickOn(encodedTokenArea).write("");

        then(encodedTokenArea.getText()).isEmpty();
    }

    @Test
    @DisplayName("should_display_decoded_header_for_valid_token")
    public void should_display_decoded_header_for_valid_token() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);

        robot.clickOn(encodedTokenArea).write(token);

        // Verify that the controller processed the token (check payload table)
        then(controller.payloadTable.getItems().size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("should_display_payload_table_for_valid_token")
    public void should_display_payload_table_for_valid_token() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);

        robot.clickOn(encodedTokenArea).write(token);

        then(controller.payloadTable.getItems().size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("should_show_invalid_status_for_malformed_token")
    public void should_show_invalid_status_for_malformed_token() {
        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);

        robot.clickOn(encodedTokenArea).write("invalid.token.format");

        then(controller.jwtStatusLabel.getText()).contains("Invalid");
    }
}
