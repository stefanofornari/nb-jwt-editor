package ste.netbeans.jwteditor.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.BDDAssertions.*;

@DisplayName("JWT Editor Top Component")
public class JwtEditorTopComponentTest {

    @Test
    @DisplayName("formatTitle_returns_indexed_name")
    public void formatTitle_returns_indexed_name() {
        String baseName = "JWT Editor";
        
        then(JwtEditorTopComponent.formatTitle(baseName, 1)).isEqualTo("1. JWT Editor");
        then(JwtEditorTopComponent.formatTitle(baseName, 2)).isEqualTo("2. JWT Editor");
        then(JwtEditorTopComponent.formatTitle(baseName, 10)).isEqualTo("10. JWT Editor");
    }

    @Test
    @DisplayName("icon_is_set_in_constructor")
    public void icon_is_set_in_constructor() {
        JwtEditorTopComponent tc = new JwtEditorTopComponent();
        then(tc.getIcon()).isNotNull();
    }

    @Test
    @DisplayName("icon_is_defined_in_annotation")
    public void icon_is_defined_in_annotation() {
        org.openide.windows.TopComponent.Description annotation = 
                JwtEditorTopComponent.class.getAnnotation(org.openide.windows.TopComponent.Description.class);
        
        then(annotation).isNotNull();
        then(annotation.iconBase()).isEqualTo("ste/netbeans/jwteditor/logo-16x16.png");
    }
}
