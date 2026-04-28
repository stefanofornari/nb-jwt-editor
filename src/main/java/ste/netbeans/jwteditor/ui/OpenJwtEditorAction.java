/*
 * Copyright 2026 ste.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ste.netbeans.jwteditor.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;

/**
 *
 */
@ActionID(category = "Tools", id = "ste.netbeans.jwteditor.ui.OpenJwtEditor")
@ActionReference(path = "Menu/Tools", position = 900)
@ActionRegistration(
    displayName = "#CTL_JwtEditorAction",
    iconBase = "ste/netbeans/jwteditor/logo-16x16.png"
)

public class OpenJwtEditorAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        JwtEditorTopComponent tc = new JwtEditorTopComponent();
        tc.open();
        tc.requestActive();
    }    
}
