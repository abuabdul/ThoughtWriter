/*
 * Copyright (c) 2013, ScalaFX Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the ScalaFX Project nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE SCALAFX PROJECT OR ITS CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
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