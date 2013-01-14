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