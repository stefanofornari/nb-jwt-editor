package ste.netbeans.jwteditor.ui;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

@ActionID(category = "Tools", id = "ste.netbeans.jwteditor.ShowJwtEditorAction")
@ActionReferences({
    @ActionReference(path = "Menu/Tools", position = 1450)
})


public final class ShowJwtEditorAction {

    public static void performAction() {
        TopComponent tc = WindowManager.getDefault().findTopComponent(JwtEditorTopComponent.PREFERRED_ID);
        if (tc == null) {
            tc = new JwtEditorTopComponent();
        }
        tc.open();
        tc.requestActive();
    }
}
