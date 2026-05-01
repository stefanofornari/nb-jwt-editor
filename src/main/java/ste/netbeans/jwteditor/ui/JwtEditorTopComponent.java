package ste.netbeans.jwteditor.ui;

import java.awt.BorderLayout;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.openide.windows.TopComponent;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import static ste.lloop.Loop.on;

@TopComponent.Description(
        preferredID = JwtEditorTopComponent.PREFERRED_ID,
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
        iconBase = "ste/netbeans/jwteditor/logo-16x16.png"
)
@TopComponent.Registration(
    mode = "output", 
    openAtStartup = false
)
@NbBundle.Messages({
    "CTL_JwtEditorTopComponent=JWT Editor",
    "HINT_JwtEditorTopComponent=JWT Editor"
})
public class JwtEditorTopComponent extends TopComponent {
    public static final String PREFERRED_ID = "ste-netbeans-jwteditor-JWTEditorTopComponent";
    
    final private Logger LOG = Logger.getLogger(getClass().getName());
    
    public JwtEditorTopComponent() {
        LOG.info(">>> JwtEditorTopComponent");
    }
    
    @Override
    protected void componentOpened() {
        LOG.info(">>> JwtEditorTopComponent.componentOpened");
        
        super.componentOpened();
        
        // Count how many instances of THIS class are currently open in the Window System
        final int[] count = new int[] { 0 };
        try {
            on(WindowManager.getDefault().getModes()).loop(mode -> {
                on(mode.getTopComponents()).loop(tc -> {
                    if (tc instanceof JwtEditorTopComponent) {
                        ++count[0];
                    }
                });
            });

            // Set names based on that count
            --count[0];
            final String title = Bundle.CTL_JwtEditorTopComponent() + ((count[0] > 1) ? (" " + count[0]) : "");

            this.setName(title);
            this.setDisplayName(title);
        } catch (Throwable t) {
            Exceptions.printStackTrace(t);
        }
        
        initializeUI();
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
