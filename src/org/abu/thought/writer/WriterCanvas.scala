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

import WriterTools.boldVBox
import WriterTools.italicVBox
import WriterTools.thoughtArea
import WriterUtils.APPHEADER
import WriterUtils.CLOSE
import scalafx.scene.control.Label
import scalafx.scene.image.Image
import scalafx.scene.image.Image.sfxImage2jfx
import scalafx.scene.image.ImageView
import scalafx.scene.layout.AnchorPane
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.HBox
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color.sfxColor2jfx
import scalafx.scene.shape.Rectangle
import scalafx.scene.shape.Rectangle.sfxRectangle2jfx

object WriterCanvas {
  //POSSIBLE colors to choose
  /* MAROON
     FIREBRICK
     NAVY
     PURPLE
  */
  val thoughtBoard = Rectangle(300, 300, Color.PURPLE)
  thoughtBoard.setOpacity(0.22d)
  thoughtBoard.setStroke(Color.NAVY)
  thoughtBoard.setStrokeWidth(1d)

  val clsImage = new ImageView {
    image = new Image(CLOSE)
  }
  AnchorPane.setTopAnchor(clsImage, 2d)
  AnchorPane.setLeftAnchor(clsImage, 2d)

  val header = new Label {
    id = "header"
    text = APPHEADER
    textFill = Color.BLACK
  }
  AnchorPane.setTopAnchor(header, 2d)
  AnchorPane.setRightAnchor(header, 2d)

  val dottedLine = new Label {
    id = "dottedLine"
    text = "------------------@@@@@-----------------------"
    textFill = Color.BLACK
  }
  AnchorPane.setTopAnchor(dottedLine, 12d)
  AnchorPane.setLeftAnchor(dottedLine, 42d)

  //Pane where thoughts are pinned
  val thoughtPane = new BorderPane {
    center = new VBox {
      content = List(thoughtArea)
    }
    bottom = new HBox {
      spacing = 2
      content = List(boldVBox, italicVBox)
    }
  }
  AnchorPane.setTopAnchor(thoughtPane, 35d)
  AnchorPane.setLeftAnchor(thoughtPane, 25d)
  AnchorPane.setBottomAnchor(thoughtPane, 2d)
}