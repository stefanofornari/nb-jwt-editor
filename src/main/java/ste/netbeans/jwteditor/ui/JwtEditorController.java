package ste.netbeans.jwteditor.ui;

import ste.netbeans.jwteditor.service.JwtDecoderService;
import ste.netbeans.jwteditor.service.JwtVerificationService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.util.Duration;
import java.util.logging.Logger;

public class JwtEditorController {

    final private Logger LOG = Logger.getLogger(getClass().getName());
    private static final int DEBOUNCE_DELAY_MS = 500;

    @FXML private TextArea encodedTokenArea;
    @FXML TreeTableView<PayloadRow> payloadTable;
    @FXML private TreeTableColumn<PayloadRow, String> propertyColumn;
    @FXML private TreeTableColumn<PayloadRow, String> valueColumn;
    @FXML private PasswordField secretField;
    @FXML private Label secretValidationLabel;
    @FXML Label jwtStatusLabel;
    @FXML Label signatureStatusLabel;
    @FXML private Label errorLabel;

    private final JwtDecoderService decoderService = new JwtDecoderService();
    private final JwtVerificationService verificationService = new JwtVerificationService();
    private Timeline debounceTimeline;

    @FXML
    public void initialize() {

        // Setup table columns
        propertyColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("property"));
        valueColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("value"));
        valueColumn.setStyle("-fx-wrap-text: true;");

        // Use a hidden root
        payloadTable.setShowRoot(false);

        // Setup debouncing timeline
        debounceTimeline = new Timeline(new KeyFrame(Duration.millis(DEBOUNCE_DELAY_MS), e -> updateDisplay()));
        debounceTimeline.setCycleCount(1);

        // Setup reactive listeners
        encodedTokenArea.textProperty().addListener((obs, oldVal, newVal) -> triggerUpdate());
        secretField.textProperty().addListener((obs, oldVal, newVal) -> triggerUpdate());

        updateDisplay();
    }

    private void triggerUpdate() {
        debounceTimeline.stop();
        debounceTimeline.playFromStart();
    }

    private void updateDisplay() {
        String token = encodedTokenArea.getText().trim();
        String secret = secretField.getText();

        if (token.isEmpty()) {
            clearDisplay();
            return;
        }

        // Decode JWT
        JwtDecoderService.DecodeResult decodeResult = decoderService.decode(token);

        if (!decodeResult.valid) {
            clearDisplay();
            errorLabel.setText("JWT Error: " + decodeResult.error);
            jwtStatusLabel.setText("Status: Invalid JWT");
            jwtStatusLabel.setStyle("-fx-text-fill: #d32f2f;");
            return;
        }

        errorLabel.setText("");
        jwtStatusLabel.setText("Status: Valid JWT");
        jwtStatusLabel.setStyle("-fx-text-fill: #388e3c;");

        // Display combined header and payload in TreeTableView
        TreeItem<PayloadRow> root = new TreeItem<>(new PayloadRow("Root", ""));

        // Header Branch
        TreeItem<PayloadRow> headerBranch = new TreeItem<>(new PayloadRow("Header", ""));
        headerBranch.setExpanded(true);
        decodeResult.parts.headerMap.forEach((key, value) ->
            headerBranch.getChildren().add(new TreeItem<>(new PayloadRow(key, String.valueOf(value))))
        );
        root.getChildren().add(headerBranch);

        // Payload Branch
        TreeItem<PayloadRow> payloadBranch = new TreeItem<>(new PayloadRow("Payload", ""));
        payloadBranch.setExpanded(true);
        decodeResult.parts.payloadMap.forEach((key, value) ->
            payloadBranch.getChildren().add(new TreeItem<>(new PayloadRow(key, decoderService.formatPayloadValue(key, value))))
        );
        root.getChildren().add(payloadBranch);

        payloadTable.setRoot(root);

        // Update secret validation indicator
        boolean secretValid = verificationService.isSecretValid(secret);
        if (!secret.isEmpty()) {
            if (secretValid) {
                secretValidationLabel.setText("✓ Secret is provided");
                secretValidationLabel.setStyle("-fx-text-fill: #388e3c;");
            }
        } else {
            secretValidationLabel.setText("Secret is required for signature verification");
            secretValidationLabel.setStyle("-fx-text-fill: #888888;");
        }

        // Verify signature if secret is provided
        if (!secret.isEmpty()) {
            JwtVerificationService.VerificationResult verifyResult = verificationService.verify(token, secret);
            if (verifyResult.valid) {
                signatureStatusLabel.setText("Signature: Valid ✓");
                signatureStatusLabel.setStyle("-fx-text-fill: #388e3c;");
            } else {
                signatureStatusLabel.setText("Signature: Invalid ✗");
                signatureStatusLabel.setStyle("-fx-text-fill: #d32f2f;");
                errorLabel.setText(verifyResult.message);
            }
        } else {
            signatureStatusLabel.setText("Signature: -");
            signatureStatusLabel.setStyle("-fx-text-fill: #888888;");
        }
    }

    private void clearDisplay() {
        payloadTable.setRoot(null);
        secretValidationLabel.setText("Secret is required for signature verification");
        secretValidationLabel.setStyle("-fx-text-fill: #888888;");
        jwtStatusLabel.setText("Status: Ready");
        jwtStatusLabel.setStyle("-fx-text-fill: #666666;");
        signatureStatusLabel.setText("Signature: -");
        signatureStatusLabel.setStyle("-fx-text-fill: #888888;");
        errorLabel.setText("");
    }

    // Inner class for table rows
    public static class PayloadRow {
        private final SimpleStringProperty property;
        private final SimpleStringProperty value;

        public PayloadRow(String property, String value) {
            this.property = new SimpleStringProperty(property);
            this.value = new SimpleStringProperty(value);
        }

        public String getProperty() {
            return property.get();
        }

        public String getValue() {
            return value.get();
        }

        public StringProperty propertyProperty() {
            return property;
        }

        public StringProperty valueProperty() {
            return value;
        }
    }
}
