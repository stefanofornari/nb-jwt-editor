# <img src="images/logo.svg" width="64" height="64" alt="JWT Editor Logo" style="vertical-align: middle;"> NetBeans JWT Editor

A NetBeans IDE plugin for viewing and verifying JWT (JSON Web Token) tokens with real-time decoding and signature verification.

Built with JavaFX to provide a modern and responsive user interface within the NetBeans ecosystem.

## Features

- **Real-time JWT Decoding**: Automatically decodes JWT tokens as you paste or type them.
- **Header and Payload Viewer**: Displays decoded headers and payload claims in an organized tree-table.
- **Signature Verification**: Verify JWT signatures using a provided secret key (HMAC256).
- **Visual Feedback**: Immediate v  isual cues for valid tokens and signature status.

## Installation

### Requirements

- **NetBeans IDE**: Version 18 or later is recommended.
- **Java**: JDK 11 or later.
- **JavaFX Toolkit for NetBeans**: The plugin requires the library module [JavaFX
  Toolkit for NetBeans](https://stefanofornari.github.io/nb-javafx-toolkit/)
   available on [Plugin Portal](https://plugins.netbeans.apache.org/catalogue/?id=131)

### NetBans Portal Installation

1. Go to  **Tools** &rarr; **Plugins**.
2. **Available Plugins**:
   - type ```jwt``` in the Search box
   - Select **JWT Editor**
   - Click **Install** and follow the wizard.

Note that NetBeans does not allow to intall the plgin if JavaFX Toolkit for NetBeans
is not available. See secion **JavaFX Toolkit for NetBeans Installation**.

### Step-by-Step Installation

1. **Download the NBM**: Obtain the latest `.nbm` file from the [releases](https://github.com/stefanofornari/nb-jwt-editor/releases) page.
2. **Open NetBeans**: Launch your NetBeans IDE.
3. **Plugins Manager**: Go to **Tools** &rarr; **Plugins**.
4. **Manual Install**:
   - Click the **Downloaded** tab.
   - Click **Add Plugins...**.
   - Select the downloaded `.nbm` file.
   - Click **Install** and follow the wizard.

### Building from Source

If you wish to build the plugin yourself:

```bash
git clone https://github.com/stefanofornari/nb-jwt-editor.git
cd nb-jwt-editor
mvn clean package
```

The resulting `.nbm` file will be in the `target` directory.

## Usage

Once installed, you can access the JWT Editor:

1. Go to **Windows** &rarr; **IDE Tools** &rarr; **JWT Editor**.
2. The JWT Editor panel will open (typically at the bottom).
3. Paste your encoded JWT into the **Encoded Token** area.
4. The **Header** and **Payload** sections will update instantly.
5. To verify a signature, enter the secret in the **JWT Signature Verification** field.

## JavaFX Installation
The plugin requires the library module [JavaFX Toolkit for NetBeans](https://stefanofornari.github.io/nb-javafx-toolkit/)
available on [Plugin Portal](https://plugins.netbeans.apache.org/catalogue/?id=131).

To install the JavaFX Toolkit go to **Tools** &rarr; **Plugins**, click on
Available Plugins and search for module JavaFX Toolkit for Netbeans; select and
install it.

---
**License**: Apache 2.0
**Author**: Stefano Fornari
