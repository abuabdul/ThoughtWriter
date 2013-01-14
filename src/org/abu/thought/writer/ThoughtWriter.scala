package org.abu.thought.writer

import java.awt.SystemTray

import WriterCanvas.clsImage
import WriterCanvas.dottedLine
import WriterCanvas.header
import WriterCanvas.thoughtBoard
import WriterCanvas.thoughtPane
import WriterUtils.APPNAME
import WriterUtils.BOLD_ACTION
import WriterUtils.ITALIC_ACTION
import WriterUtils.LOGO
import WriterUtils.THOUGHTSTYLE
import WriterUtils.applySystemTray
import WriterUtils.callJavaFXThreadPlatform
import WriterUtils.loadThoughtAreaStyles
import WriterUtils.thoughtProps
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import javafx.stage.StageStyle
import scalafx.application.JFXApp
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.image.Image
import scalafx.scene.image.Image.sfxImage2jfx
import scalafx.scene.layout.AnchorPane
import scalafx.scene.layout.StackPane
import scalafx.stage.Stage
import scalafx.stage.Stage.sfxStage2jfx

object ThoughtWriter extends JFXApp {
  // Remove default exit behaviour of Stage
  Platform.implicitExit = false
  // System tray
  val tray = SystemTray.getSystemTray()
  // Flag to identify system tray
  def isTrayAdded = tray.getTrayIcons().length > 0
  // Method to apply tray icon
  def applyTrayIcon = if (!isTrayAdded) { applySystemTray(tray, stage) }

  //Stage with only transparent background
  stage = new Stage {
    maxWidth = 300
    maxHeight = 300
    title = APPNAME
    scene = new Scene {
      content = List(new StackPane {
        content = List(thoughtBoard,
          new AnchorPane {
            content = List(clsImage,
              header,
              dottedLine,
              thoughtPane)
          })
      })
    }
    // Add styles to external file and add it to the scene
    val styles = this.getClass().getResource(THOUGHTSTYLE).toExternalForm()
    scene.get.getStylesheets().add(styles)
  }
  // Transparency window
  stage.initStyle(StageStyle.TRANSPARENT)
  // Add icon to the ThoughtWriter
  stage.getIcons().add(new Image(LOGO))
  //To resize the window
  stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler[MouseEvent]() {
    override def handle(me: MouseEvent) {
      stage.x = me.getScreenX()
      stage.y = me.getScreenY()
    }
  })
  // To close the window
  clsImage.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler[MouseEvent]() {
    override def handle(me: MouseEvent) {
      callJavaFXThreadPlatform {
        // Stage is hidden
        stage.hide
        // Apply system tray icon - ONCE
        applyTrayIcon
      }
    }
  })
  //Load thought content and styles
  loadThoughtAreaStyles(thoughtProps.getProperty(BOLD_ACTION).toBoolean, thoughtProps.getProperty(ITALIC_ACTION).toBoolean)
}