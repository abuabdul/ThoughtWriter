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