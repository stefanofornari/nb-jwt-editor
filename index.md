# <img src="logo.svg" width="64" height="64" alt="JWT Editor Logo" style="vertical-align: middle;"> NetBeans JWT Editor

A NetBeans IDE plugin for viewing and verifying JWT (JSON Web Token) tokens with real-time decoding and signature verification.

Built with JavaFX to provide a modern and responsive user interface within the NetBeans ecosystem.

[Download .nbm](https://github.com/stefanofornari/nb-jwt-editor/releases/latest) {: .btn}

## Features

- **Real-time JWT Decoding**: Automatically decodes JWT tokens as you paste or type them.
- **Header and Payload Viewer**: Displays decoded headers and payload claims in an organized tree-table.
- **Signature Verification**: Verify JWT signatures using a provided secret key (HMAC256).
- **Syntax Highlighting**: Header JSON is displayed with full syntax highlighting using the NetBeans editor.
- **Visual Feedback**: Immediate visual cues for valid tokens and signature status.

## Installation

### Requirements

- **NetBeans IDE**: Version 18 or later is recommended.
- **Java**: JDK 11 or later.
- **JavaFX**: The plugin bundles the required JavaFX libraries, but ensure your JDK/JRE supports JavaFX or that NetBeans is configured to run on a JDK that includes it.

### Step-by-Step Installation

1. **Download the NBM**: Obtain the latest `.nbm` file from the [releases](https://github.com/stefanofornari/nb-jwt-editor/releases) page.
2. **Open NetBeans**: Launch your NetBeans IDE.
3. **Plugins Manager**: Go to **Tools** &rarr; **Plugins**.
4. **Manual Install**:
   - Click the **Downloaded** tab.
   - Click **Add Plugins...**.
   - Select the downloaded `.nbm` file.
   - Click **Install** and follow the wizard.
5. **Restart**: Restart NetBeans to complete the installation.

## Usage

Once installed, you can access the JWT Editor:

1. Go to **Windows** &rarr; **IDE Tools** &rarr; **JWT Editor**.
2. The JWT Editor panel will open (typically at the bottom).
3. Paste your encoded JWT into the **Encoded Token** area.
4. The **Header** and **Payload** sections will update instantly.
5. To verify a signature, enter the secret in the **JWT Signature Verification** field.

## Building from Source

If you wish to build the plugin yourself:

```bash
git clone https://github.com/stefanofornari/nb-jwt-editor.git
cd nb-jwt-editor
mvn clean package
```

The resulting `.nbm` file will be in the `target` directory.

---
**License**: Apache 2.0  
**Author**: Stefano Fornari
