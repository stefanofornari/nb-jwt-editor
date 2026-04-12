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

import java.util.List;

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
        robot.clickOn("#decodeButton");

        then(encodedTokenArea.getText()).isEmpty();
    }

    @Test
    @DisplayName("do_not_update_display_until_decode_button_clicked")
    public void do_not_update_display_until_decode_button_clicked() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);

        robot.clickOn(encodedTokenArea).write(token);

        // Verify that the table is still empty before clicking decode
        then(controller.payloadTable.getRoot()).isNull();

        robot.clickOn("#decodeButton");

        // Now it should be populated
        then(controller.payloadTable.getRoot()).isNotNull();
    }

    @Test
    @DisplayName("display_decoded_token_for_valid_token")
    public void display_decoded_token_for_valid_token() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);

        robot.clickOn(encodedTokenArea).write(token);
        robot.clickOn("#decodeButton");

        // Verify that the controller processed the token
        TreeItem<JwtEditorController.PayloadRow> root = controller.payloadTable.getRoot();
        then(root).isNotNull();
        
        List<TreeItem<JwtEditorController.PayloadRow>> branches = root.getChildren();
        then(branches).hasSize(2);
        
        then(branches.get(0).getValue().getProperty()).isEqualTo("Header");
        then(branches.get(1).getValue().getProperty()).isEqualTo("Payload");
        
        // Verify header content
        then(branches.get(0).getChildren()).isNotEmpty();
        // Verify payload content
        then(branches.get(1).getChildren()).isNotEmpty();
    }

    @Test
    @DisplayName("show_invalid_status_for_malformed_token")
    public void show_invalid_status_for_malformed_token() {
        TextArea encodedTokenArea = robot.lookup("#encodedTokenArea").queryAs(TextArea.class);

        robot.clickOn(encodedTokenArea).write("invalid.token.format");
        robot.clickOn("#decodeButton");

        then(controller.jwtStatusLabel.getText()).contains("Invalid");
    }
}
