# NetBeans JWT Editor - Installation & Usage Guide

## Installation

### Step-by-Step Installation

1. **Build the Plugin** (if not already built):
   ```bash
   cd /Users/ste/Projects/nb-jwt-editor
   mvnd clean package
   ```

2. **Locate the NBM file**:
   ```
   /Users/ste/Projects/nb-jwt-editor/target/nb-jwt-editor-1.0.0-SNAPSHOT.nbm
   ```

3. **Install in NetBeans**:
   - Open NetBeans IDE
   - Go to **Tools** → **Plugins**
   - Click the **Downloaded** tab
   - Click **Add Plugins...**
   - Navigate to the target folder above
   - Select **nb-jwt-editor-1.0.0-SNAPSHOT.nbm**
   - Click **Install**
   - Follow the installation wizard
   - **Restart NetBeans**

4. **Access the Tool**:
   - After restart, go to **Windows** → **IDE Tools** → **JWT Editor**
   - The JWT Editor window will open as a dockable panel at the bottom of the IDE

## Using the JWT Editor

### Basic Workflow

1. **Paste JWT Token**:
   - In the "Encoded Token" text area (left column), paste your JWT token
   - The tool will automatically decode it in real-time

2. **View Decoded Data**:
   - **Header**: Right side, top section
     - Shows JSON with syntax highlighting via NetBeans editor
     - Typically contains algorithm (alg) and token type (typ)
   - **Payload**: Right side, middle section
     - Table showing all claims (property names and values)
     - Special formatting for `exp` field (shows both Unix timestamp and datetime)

3. **Verify Signature** (Optional):
   - In the "JWT Signature Verification" field, enter your secret key
   - Minimum 32 bytes required
   - Green checkmark (✓) appears when secret is valid length
   - Signature status updates in real-time at the bottom

4. **Monitor Status**:
   - Status bar shows:
     - JWT validity (Valid ✓ or Invalid ✗)
     - Signature verification result
     - Error messages in red if there are issues

### Example

**Valid JWT Token**:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

**Secret for verification**:
```
your_32_byte_or_longer_secret_key
```

The tool will display:
- ✓ Status: Valid JWT
- Header showing: `{"alg":"HS256","typ":"JWT"}`
- Payload showing claims in table format
- ✓ Signature: Valid (if correct secret provided)

## Features

### Real-Time Processing
- Changes are reflected instantly as you type
- No need to click any buttons
- Reactive UI updates using JavaFX Properties

### JSON Syntax Highlighting
- Header JSON is displayed with syntax highlighting
- Uses NetBeans built-in JSON editor component
- Proper formatting and indentation

### Error Handling
- Clear error messages for malformed tokens
- Visual feedback on secret key validity
- Color-coded status indicators

### Visual Feedback
- ✓ Green for valid/acceptable values
- ✗ Red for invalid/problematic values
- Color-coded status labels for quick reference

## Troubleshooting

### "Token is empty" error
- Make sure you've pasted a complete JWT token
- Token should have 3 parts separated by dots (.)

### "Expected 3 parts separated by dots" error
- Your token is malformed
- Check that it has header.payload.signature format
- Ensure you copied the entire token

### "Secret too short (min 32 bytes)" message
- Your secret key must be at least 32 bytes
- This equals 32 characters minimum
- Can be longer, but must be at least 32

### "Signature verification failed" message
- Your secret key doesn't match the one used to sign the token
- Double-check the secret
- Some systems use different encoding for secrets

## Uninstallation

To remove the plugin:
1. Go to **Tools** → **Plugins**
2. Find **JWT Editor** in the list
3. Click **Uninstall**
4. Restart NetBeans

## Build & Development

### Building from Source
```bash
# Build without tests
mvnd clean package -DskipTests

# Build with all tests
mvnd clean test package

# Run only tests
mvnd clean test
```

### Project Structure
```
nb-jwt-editor/
├── pom.xml                  # Maven configuration
├── src/
│   ├── main/java/           # Source code
│   ├── main/resources/       # FXML, manifests, properties
│   ├── assembly/            # NBM assembly descriptor
│   └── test/java/           # Unit and UI tests
└── target/
    ├── nb-jwt-editor-1.0.0-SNAPSHOT.jar
    └── nb-jwt-editor-1.0.0-SNAPSHOT.nbm
```

## Technical Details

### Dependencies
- **java-jwt** 4.4.0: JWT encoding/decoding
- **JavaFX** 21.0.2: UI framework
- **NetBeans API**: IDE integration
- **JSON**: JSON parsing

### Supported Algorithms
- HMAC256 (currently supported)
- Future: RS256, RS512, etc.

### Minimum Requirements
- Java 11+
- NetBeans 18+
- 256 bits (32 bytes) for secret keys

## Support

For issues or feature requests, refer to the README.md in the project root.

---

**Version**: 1.0.0-SNAPSHOT  
**License**: Apache 2.0  
**Author**: Stefano Fornari
