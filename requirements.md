We want to build a NetBeans plug-in to view the content of a JWT (RFC 7519) token. 
Before creating code, provide a plan of what you are going to do. Let's review it together before 
starting execution.
If you need anything or clarifications, ask.

Main Requirements
- use java-jwt as JWT library
- use JavaFX in Netbeans for the UI
- use AtlantaFX as JFX styling framework
- use fxml to describe the UI whenever makes sense 
- the tool should be available in the Windows/IDE Tools menu of the NB menu
- when selected, it's main UI should be attached to the bottom dock (where output windows are attached)
- it should provide the following functionality:
  - two columns for the widgets
    - first column:
      - Encoded Token: text area to insert the text JTW
    - second column
      - Decoded Header: read only text area to show the json header of the token; it should support JSON syntax highlighting
        - use either RichTextFX or the NB editor in readonly mode and proper mimetype as json is supported out of the box
      - Decoded Payload: a table with two columns
        - first column: the property name
        - second column: the property value (note that exp should show both the timestamp and the local datetime
          in the same cell)
      - JWT Signature Verification: entry field to provide the secret to decode the JWT
        - password must be at least 32 bytes (it can be longer, but not shorter)
        - provide visual feedback that the requirement has been met or not
  - each of the section above shall have a title and then the mentioned widget
  - decoding or verification shall be done on the fly at each change of JWT text or password
    - no debounce for now, let's use javafx property observability
  - provide visual indicator of Valid/Invalid JWT and "Valid/Invalid signature
    - errors should display in a status area at the bottom of the widjets
- you can refer to the same functionality on https://www.jwt.io/ , in particular for the look&field

Provide the tests too. Here some requirements for the tests:
- Use assertj as unit test framework
- Use BDDAssertions' then() pattern rather than assertThat()
- Use testfx or assertj-swing to test UI functionalities
- Use snake convention for tests name

Build instructions
- Create a maven project in the current directory (no need to create a root); building system is maven
- group id is com.github.stefanofornari
- artifact id is nb-jwt-editor
- license is Apache 2.0


