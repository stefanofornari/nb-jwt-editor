# NetBeans JWT Editor Plugin

A NetBeans IDE plugin for viewing and verifying JWT (JSON Web Token) tokens with real-time decoding and signature verification.

## Features

- **Real-time JWT Decoding**: Automatically decodes JWT tokens as you paste them
- **Header and Payload Viewer**: Display decoded headers and payload claims in a tree-table
- **Signature Verification**: Verify JWT signatures using a provided secret key

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
- NetBeans IDE 29+

## Main Dependencies

- **java-jwt** (4.4.0): JWT encoding/decoding library
- **javafx**: JavaFX controls and FXML support
- **json**: JSON parsing and formatting
- **testfx**: JavaFX UI testing
- **assertj**: BDD-style assertions

## How to Use

1. Open NetBeans IDE
2. Go to **Tools → JWT Editor**
3. Paste your JWT token in the "Encoded Token" field
4. The plugin automatically:
   - Displays the decoded header and payload claims in tree-table
   - Indicates if the JWT is valid or malformed
5. To verify the signature:
   - Enter your secret key in the "JWT Signature Verification" field
   - Visual indicator shows if the secret meets requirements
   - Signature status updates in real-time

## License

Apache License 2.0 - See LICENSE file for details
