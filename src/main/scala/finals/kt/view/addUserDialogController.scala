package finals.kt.view

import finals.kt.MyApp
import finals.kt.model.User
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, TextField}
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class addUserDialogController( // (Teck Min, 2024)
                              private val  userNameField : TextField
                            ) {

  var         dialogStage : Stage  = null
  private var _user     : User = null
  var         okClicked            = false

  def user = _user

  def user_=(x : User) {
    _user = x

    userNameField.text = _user.userName.value
  }

  def handleEnter(action :ActionEvent){ // Bring user to enter username
    if (isInputValid()) {
      _user.userName <== userNameField.text
      okClicked = true;
      MyApp.username = _user.userName.value
      MyApp.showchooseSpaceship()
      dialogStage.close()
    }
  }

  def nullChecking (x : String) = x == null || x.length == 0

  def isInputValid() : Boolean = { // Check validity of username
    var errorMessage = ""

    if (nullChecking(userNameField.text.value))
      errorMessage += "No valid username!\n"

    if (errorMessage.length() == 0) {
      return true;
    } else {
      // Show the error message.
      val alert = new Alert(Alert.AlertType.Error){
        initOwner(dialogStage)
        title = "Invalid Fields"
        headerText = "Please correct invalid fields"
        contentText = errorMessage
      }.showAndWait()

      return false;
    }
  }

  def handleBack(): Unit = { // Bring user back to Ranking Page
    dialogStage.close()
  }
}
