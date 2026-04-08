# NetBeans JWT Editor Plugin

A NetBeans IDE plugin for viewing and verifying JWT (JSON Web Token) tokens with real-time decoding and signature verification.

## Features

- **Real-time JWT Decoding**: Automatically decodes JWT tokens as you paste them
- **Header Viewer**: Display and pretty-print the decoded JWT header in JSON format
- **Payload Display**: Show all payload claims in a table format
- **Timestamp Formatting**: The `exp` claim displays both Unix timestamp and human-readable datetime
- **Signature Verification**: Verify JWT signatures using a secret key (minimum 32 bytes)
- **Visual Feedback**: Real-time validation indicators for JWT validity and signature status
- **Error Reporting**: Clear error messages in a status bar for debugging

## Architecture

### Core Components

1. **JwtDecoderService**: Handles JWT token decoding
   - Validates token format (three dot-separated parts)
   - Decodes Base64URL encoded header and payload
   - Parses JSON and provides structured data
   - Special formatting for `exp` field (timestamp + datetime)

2. **JwtVerificationService**: Handles JWT signature verification
   - Validates secret key length (minimum 32 bytes)
   - Verifies HMAC256 signatures
   - Provides detailed error messages

3. **JwtEditorController**: UI logic and reactive bindings
   - Listens to token and secret key changes
   - Updates display in real-time using JavaFX Properties
   - Manages visual feedback (colors, indicators, messages)

4. **JwtEditorTopComponent**: NetBeans integration
   - Provides dockable window in the IDE
   - Registers in Tools menu
   - Manages JavaFX panel embedding

## Build Instructions

```bash
# Build the plugin
mvnd clean package

# Run tests
mvnd clean test

# Build and install
mvnd clean install
```

### Requirements
- Java 11+
- Maven (with mvnd for faster builds)
- NetBeans IDE 18+

## Dependencies

- **java-jwt** (4.4.0): JWT encoding/decoding library
- **javafx**: JavaFX controls and FXML support
- **json**: JSON parsing and formatting
- **testfx**: JavaFX UI testing
- **assertj**: BDD-style assertions

## Project Structure

```
src/
├── main/
│   ├── java/com/github/stefanofornari/nb/jwt/editor/
│   │   ├── Installer.java                 # Module installer
│   │   ├── service/
│   │   │   ├── JwtDecoderService.java    # Token decoding logic
│   │   │   └── JwtVerificationService.java # Signature verification
│   │   └── ui/
│   │       ├── JwtEditorController.java   # FXML controller
│   │       ├── JwtEditorTopComponent.java # NetBeans integration
│   │       ├── ShowJwtEditorAction.java   # Menu action
│   │       └── JwtEditor.fxml             # UI layout
│   └── resources/
│       └── com/github/stefanofornari/nb/jwt/editor/ui/
│           ├── JwtEditor.fxml             # UI layout
│           ├── Bundle.properties          # I18n
│           └── layer.xml                  # Layer registration
└── test/
    └── java/com/github/stefanofornari/nb/jwt/editor/
        ├── service/
        │   ├── JwtDecoderServiceTest.java
        │   └── JwtVerificationServiceTest.java
        └── ui/
            └── JwtEditorControllerTest.java
```

## Test Coverage

- **13 Unit Tests**: JWT decoding and verification logic
- **4 UI Tests**: FXML controller and user interactions
- **BDD Style**: All tests use AssertJ's `then()` pattern
- **TestFX**: JavaFX UI component testing

## How to Use

1. Open NetBeans IDE
2. Go to **Windows → IDE Tools → JWT Editor**
3. Paste your JWT token in the "Encoded Token" field
4. The plugin automatically:
   - Displays the decoded header in JSON format
   - Shows all payload claims in a table
   - Indicates if the JWT is valid or malformed
5. To verify the signature:
   - Enter your secret key in the "JWT Signature Verification" field
   - The field requires at least 32 bytes
   - Visual indicator shows if the secret meets requirements
   - Signature status updates in real-time

## License

Apache License 2.0 - See LICENSE file for details

## Compliance with Requirements

✅ Uses java-jwt library for JWT handling
✅ JavaFX UI with FXML layouts
✅ Proper styling and error messages
✅ Two-column layout (Encoded Token | Decoded Header + Payload + Verification)
✅ Real-time processing with JavaFX Properties observability
✅ Accessible via Tools/IDE Tools menu
✅ Dockable window at bottom (TopComponent)
✅ JSON syntax highlighting (pretty-printed JSON display)
✅ exp field shows timestamp + datetime
✅ Secret validation (minimum 32 bytes)
✅ Visual indicators for Valid/Invalid JWT and signature
✅ Error messages in status bar
✅ Comprehensive test suite with AssertJ BDD pattern
✅ TestFX for UI testing
