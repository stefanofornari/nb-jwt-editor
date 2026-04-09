package ste.netbeans.jwteditor.ui;

import java.awt.BorderLayout;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.openide.windows.TopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;


@TopComponent.Description(
        preferredID = JwtEditorTopComponent.PREFERRED_ID,
        persistenceType = TopComponent.PERSISTENCE_ALWAYS,
        iconBase = "ste/netbeans/jwteditor/logo-16x16.png"
)
@TopComponent.Registration(mode = "output", openAtStartup = false)
@ActionID(category = "Window", id = JwtEditorTopComponent.PREFERRED_ID)
@ActionReference(path = "Menu/Window", position = 5100)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_JwtEditorAction",
        preferredID = JwtEditorTopComponent.PREFERRED_ID
)
@NbBundle.Messages({
    "CTL_JwtEditorAction=JWT Editor",
    "CTL_JwtEditorTopComponent=JWT Editor Window",
    "HINT_JwtEditorTopComponent=JWT Editor Window"
})
public class JwtEditorTopComponent extends TopComponent {
    public static final String PREFERRED_ID = "ste-netbeans-jwteditor-JWTEditorTopComponent";
    
    
    final private Logger LOG = Logger.getLogger(getClass().getName());

    public JwtEditorTopComponent() {
        LOG.info(">>> JwtEditorTopComponent");
        setName(Bundle.CTL_JwtEditorTopComponent());
        setDisplayName(Bundle.CTL_JwtEditorTopComponent());
        setToolTipText(Bundle.HINT_JwtEditorTopComponent());
    }

    @Override
    protected void componentOpened() {
        LOG.info(">>> JwtEditorTopComponent.componentOpened");
        super.componentOpened();
        if (getComponentCount() == 0) {
            initializeUI();
        }
    }

    private void initializeUI() {
        LOG.info(">>> JwtEditorTopComponent.initializeUI");
        setLayout(new BorderLayout());
        try {
            JFXPanel fxPanel = new JFXPanel();
            add(fxPanel, BorderLayout.CENTER);

            // Load FXML on JavaFX thread
            javafx.application.Platform.runLater(() -> {
                try {
                    ResourceBundle bundle = NbBundle.getBundle(JwtEditorTopComponent.class);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("JwtEditor.fxml"), bundle);
                    Parent root = loader.load();
                    JwtEditorController fxController = loader.getController();
                    LOG.info("fxController: " + fxController);
                    Scene scene = new Scene(root);
                    fxPanel.setScene(scene);
                } catch (Exception e) {
                    LOG.severe(String.valueOf(e));
                    Exceptions.printStackTrace(e);
                }
            });
            LOG.info("<<< JwtEditorTopComponent.initializeUI");
        } catch (Exception e) {
            LOG.severe(String.valueOf(e));
            Exceptions.printStackTrace(e);
        }
    }
}
