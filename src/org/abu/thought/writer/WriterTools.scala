/*
* Copyright 2013-2014 abuabdul.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*/
package org.abu.thought.writer

import WriterUtils.BOLD_ACTION
import WriterUtils.ITALIC_ACTION
import WriterUtils.applyStyle
import javafx.event.EventHandler
import javafx.scene.input.MouseEvent
import scalafx.geometry.Insets
import scalafx.scene.Cursor
import scalafx.scene.Cursor.sfxCursor2jfx
import scalafx.scene.control.Label
import scalafx.scene.control.Label.sfxLabel2jfx
import scalafx.scene.control.TextArea
import scalafx.scene.layout.VBox
import scalafx.scene.text.Font

object WriterTools {
  // Area where writers can pin their thoughts
  val thoughtArea = new TextArea {
    id = "thoughtarea"
    //Explicit style is required -- NO CSS
    style = "-fx-font-weight: normal; -fx-font-style: normal;"
    minWidth = 275
    wrapText = true
    prefColumnCount = 20
    prefRowCount = 15
  }
  // Style for making Bold letter
  val boldStyle = new Label("B") {
    font = new Font("Verdana", 13)
    id = "boldStyle"
  }
  // To make hand cursor to the thoughts
  boldStyle.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler[MouseEvent]() {
    override def handle(me: MouseEvent) {
      boldStyle.setCursor(Cursor.HAND)
    }
  })
  // To make bold to the thoughts
  boldStyle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler[MouseEvent]() {
    override def handle(me: MouseEvent) {
      applyStyle(thoughtArea, boldVBox, BOLD_ACTION)
    }
  })
  // VBox contains boldstyle label
  val boldVBox = new VBox {
    padding = Insets(0, 6, 0, 6)
    content = List(boldStyle)
  }

  // Style for making Italic letter
  val italicStyle = new Label("I") {
    font = new Font("Verdana", 13)
    id = "italicStyle"
  }
  // To make hand cursor to the thoughts
  italicStyle.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler[MouseEvent]() {
    override def handle(me: MouseEvent) {
      italicStyle.setCursor(Cursor.HAND)
    }
  })
  // To make italics to the thoughts
  italicStyle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler[MouseEvent]() {
    override def handle(me: MouseEvent) {
      applyStyle(thoughtArea, italicVBox, ITALIC_ACTION)
    }
  })
  //VBox contains italic style label
  val italicVBox = new VBox {
    padding = Insets(0, 6, 0, 6)
    content = List(italicStyle)
  }
}