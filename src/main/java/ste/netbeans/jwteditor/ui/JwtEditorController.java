package ste.netbeans.jwteditor.ui;

import ste.netbeans.jwteditor.service.JwtDecoderService;
import ste.netbeans.jwteditor.service.JwtVerificationService;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import org.json.JSONObject;
import org.openide.util.Lookup;

import javax.swing.JEditorPane;
import javax.swing.text.EditorKit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class JwtEditorController {
    
    final private Logger LOG = Logger.getLogger(getClass().getName());

    @FXML private TextArea encodedTokenArea;
    @FXML private StackPane headerContainer;
    @FXML TableView<PayloadRow> payloadTable;
    @FXML private TableColumn<PayloadRow, String> propertyColumn;
    @FXML private TableColumn<PayloadRow, String> valueColumn;
    @FXML private PasswordField secretField;
    @FXML private Label secretValidationLabel;
    @FXML Label jwtStatusLabel;
    @FXML Label signatureStatusLabel;
    @FXML private Label errorLabel;

    private final JwtDecoderService decoderService = new JwtDecoderService();
    private final JwtVerificationService verificationService = new JwtVerificationService();
    private JEditorPane jsonHeaderEditor;

    @FXML
    public void initialize() {
        // Setup NetBeans editor for JSON header
        LOG.info("INITIALIZE!!!");
        initializeJsonEditor();

        // Setup table columns
        propertyColumn.setCellValueFactory(new PropertyValueFactory<>("property"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        valueColumn.setStyle("-fx-wrap-text: true;");

        // Setup reactive listeners
        encodedTokenArea.textProperty().addListener((obs, oldVal, newVal) -> updateDisplay());
        secretField.textProperty().addListener((obs, oldVal, newVal) -> updateDisplay());

        updateDisplay();
    }

    private void initializeJsonEditor() {
        // Create Swing JSON editor
        jsonHeaderEditor = new JEditorPane();
        jsonHeaderEditor.setContentType("application/json");
        
        try {
            // Try to load JSON editor kit from NetBeans
            Lookup lookup = org.netbeans.api.editor.mimelookup.MimeLookup.getLookup("application/json");
            EditorKit kit = lookup.lookup(EditorKit.class);
            if (kit != null) {
                jsonHeaderEditor.setEditorKit(kit);
            }
        } catch (Exception e) {
            // Fallback if JSON kit not available
            System.err.println("JSON editor kit not available: " + e.getMessage());
        }
        
        jsonHeaderEditor.setEditable(false);
        
        // Embed Swing component in JavaFX
        SwingNode swingNode = new SwingNode();
        swingNode.setContent(jsonHeaderEditor);
        headerContainer.getChildren().add(swingNode);
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

        // Display header with formatting in JSON editor
        jsonHeaderEditor.setText(formatJson(decodeResult.parts.header));

        // Display payload
        List<PayloadRow> rows = new ArrayList<>();
        decodeResult.parts.payloadMap.forEach((key, value) ->
            rows.add(new PayloadRow(key, decoderService.formatPayloadValue(key, value)))
        );
        payloadTable.setItems(FXCollections.observableArrayList(rows));

        // Update secret validation indicator
        boolean secretValid = verificationService.isSecretValid(secret);
        if (!secret.isEmpty()) {
            if (secretValid) {
                secretValidationLabel.setText("✓ Secret is valid (≥32 bytes)");
                secretValidationLabel.setStyle("-fx-text-fill: #388e3c;");
            } else {
                secretValidationLabel.setText("✗ Secret too short (min 32 bytes)");
                secretValidationLabel.setStyle("-fx-text-fill: #d32f2f;");
            }
        } else {
            secretValidationLabel.setText("Secret requirement: At least 32 bytes");
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
        jsonHeaderEditor.setText("");
        payloadTable.setItems(FXCollections.observableArrayList());
        secretValidationLabel.setText("Secret requirement: At least 32 bytes");
        secretValidationLabel.setStyle("-fx-text-fill: #888888;");
        jwtStatusLabel.setText("Status: Ready");
        jwtStatusLabel.setStyle("-fx-text-fill: #666666;");
        signatureStatusLabel.setText("Signature: -");
        signatureStatusLabel.setStyle("-fx-text-fill: #888888;");
        errorLabel.setText("");
    }

    private String formatJson(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return obj.toString(2);
        } catch (Exception e) {
            return json;
        }
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
