package ste.netbeans.jwteditor;

import java.util.logging.Logger;
import javafx.application.Platform;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    final private Logger LOG = Logger.getLogger(getClass().getName());

    @Override
    public void restored() {
        LOG.info("restoring JWTEditor plug-in");
        Platform.setImplicitExit(false);
    }

    @Override
    public boolean closing() {
        LOG.info("closing JWTEditor plug-in");
        try {
            Platform.exit();
        } catch (Exception x) {}

        return true;
    }
}
