package finals.kt

import finals.kt.model.User
import finals.kt.util.Database
import finals.kt.view.{addUserDialogController, endGameController, startGameController}
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.scene.image.Image
import scalafx.scene.media.AudioClip
import scalafx.stage.{Modality, Stage}

import java.net.URL

object MyApp extends JFXApp{
  Database.setupDB()
  val userData = new ObservableBuffer[User]()

  /**Constructor**/
  userData ++= User.getAllPersons // (Teck Min, 2024)

  var username: String = _
  private val rootResource: URL = getClass.getResource("view/RootLayout.fxml")
  private val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load
  private val roots = loader.getRoot[jfxs.layout.BorderPane]
  private val backgroundMusic: AudioClip = new AudioClip(getClass.getClassLoader.getResource("sounds/backgroundMusic.mp3").toURI.toString)

  stage = new PrimaryStage{
    icons += new Image(getClass.getResourceAsStream("/images/logo.png"))
    title = "Starfarer"
    resizable = false
    scene = new Scene{
      root = roots
    }
    onShown = _ => backgroundMusic.play() // Play background music when the stage is shown
    onHidden = _ => backgroundMusic.stop() // Stop background music when the stage is hidden
  }

  // To show the Menu page
  def showMenu(): Unit = {
    val resource = getClass.getResource("view/Menu.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // To show the Ranking page after clicking Start in Menu page
  def showRanking(): Unit = {
    val resource = getClass.getResource("view/Ranking.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // To add username
  def showAddUserDialog(user: User): Boolean = {
    val resource = getClass.getResourceAsStream("view/AddUserDialog.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[addUserDialogController#Controller]

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    control.user = user
    dialog.showAndWait()
    control.okClicked
  }

  // To choose the spaceship
  def showchooseSpaceship(): Unit = {
    val resource = getClass.getResource("view/chooseSpaceship.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // To show the main game page
  def showGameScreen(selectedSpaceship: String): Unit = {
    val resource = getClass.getResource("view/startGame.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load()
    val newRoot = loader.getRoot[jfxs.layout.AnchorPane]
    val controller = loader.getController[startGameController#Controller]
    controller.initData(selectedSpaceship)
    this.roots.setCenter(newRoot)
  }

  // To show the final results
  def showEndGame(finalScore: Int, remainingTime: Int, endedDueToTime: Boolean): Unit = {
    val resource = getClass.getResourceAsStream("view/endGame.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2  = loader.getRoot[jfxs.Parent]
    val control = loader.getController[endGameController#Controller]

    control.showFinalResults(finalScore, remainingTime, endedDueToTime, username)

    val dialog = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene {
        root = roots2
      }
    }
    control.dialogStage = dialog
    dialog.show()
  }

  // To show the Tutorial page
  def showTutorial(): Unit = {
    val resource = getClass.getResource("view/Tutorial.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // To show the Credit page
  def showCredits(): Unit = {
    val resource = getClass.getResource("view/Credits.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // To show Menu page as main page
  showMenu()
}