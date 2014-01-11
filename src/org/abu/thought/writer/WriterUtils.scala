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

import java.awt.AWTException
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon
import java.awt.event.ActionListener
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Properties

import WriterTools.boldVBox
import WriterTools.italicVBox
import WriterTools.thoughtArea
import javax.imageio.ImageIO
import scalafx.application.Platform
import scalafx.scene.Node
import scalafx.scene.Node.sfxNode2jfx
import scalafx.scene.control.TextArea.sfxTextArea2jfx
import scalafx.stage.Stage
import scalafx.stage.Stage.sfxStage2jfx

object WriterUtils {
  //Application constants
  val APPNAME = "ThoughtWriter"
  val APPHEADER = "Write your thoughts"
  val THOUGHTSTYLE = "/org/abu/thought/writer/css/ThoughtStyle.css"
  val THOUGHTPROPS = "/org/abu/thought/writer/resources/Thoughts.properties"
  val LOGO = "/org/abu/thought/writer/images/blueicon.jpg"
  val TRAYICON = "/org/abu/thought/writer/images/tray_icon.jpg"
  val CLOSE = "/org/abu/thought/writer/images/close.png"
  val BOLD_ACTION = "bold"
  val ITALIC_ACTION = "italic"
  val CONTENT = "content"

  //Style constants
  val styleNormal = "-fx-font-style: normal;"
  val styleItalic = "-fx-font-style: italic;"
  val fontNormal = "-fx-font-weight: normal;"
  val fontBold = "-fx-font-weight: bold;"
  val boldItalicBg = "#b380b3"
  val boldItalicBgStyle = "-fx-background-color: " + boldItalicBg + ";"
  val boldItalicBgOpac = "-fx-background-color: transparent;"

  // function literals for utility methods
  val nodeBG = (node: Node) => node setStyle boldItalicBgStyle
  val nodeOpac = (node: Node) => node setStyle boldItalicBgOpac
  val nodeBold = (node: Node) => node setStyle fontBold
  val nodeItalic = (node: Node) => node setStyle styleItalic
  val nodeBoldItalic = (node: Node) => node setStyle fontBold + styleItalic
  val nodeBoldNormal = (node: Node) => node setStyle fontBold + styleNormal
  val nodeNormalItalic = (node: Node) => node setStyle fontNormal + styleItalic
  val nodeNormals = (node: Node) => node setStyle fontNormal + styleNormal

  // Function that validates whether textarea contains the style or not
  val isNodeStyled = (node: Node, style: String) => node getStyle () contains style
  // Is Bold Sytle applied
  val isBoldStyled = (node: Node) => isNodeStyled(node, fontBold)
  //Is Italic Sytle applied
  val isItalicStyled = (node: Node) => isNodeStyled(node, styleItalic)

  // Method to apply styles when bold and italic are clicked
  def applyStyle = (node: Node, subnode: Node, action: String) => {
    action match {
      case BOLD_ACTION => isBoldStyled(node) match {
        case true => isItalicStyled(node) match {
          case true => nodeNormalItalic(node); nodeOpac(subnode)
          case false => nodeNormals(node); nodeOpac(subnode)
        }
        case false => isItalicStyled(node) match {
          case true => nodeBoldItalic(node); nodeBG(subnode)
          case false => nodeBoldNormal(node); nodeBG(subnode)
        }
      }
      case ITALIC_ACTION => isItalicStyled(node) match {
        case true => isBoldStyled(node) match {
          case true => nodeBoldNormal(node); nodeOpac(subnode)
          case false => nodeNormals(node); nodeOpac(subnode)
        }
        case false => isBoldStyled(node) match {
          case true => nodeBoldItalic(node); nodeBG(subnode)
          case false => nodeNormalItalic(node); nodeBG(subnode)
        }
      }
      case _ => println("Unknown action event")
    }
  }

  // Method to load styles based on properties 
  def loadThoughtAreaStyles = (isBold: Boolean, isItalic: Boolean) => {
    thoughtArea.setText(thoughtProps.getProperty(CONTENT))
    isBold match {
      case true =>
        nodeBG(boldVBox); isItalic match {
          case true => nodeBG(italicVBox); nodeBoldItalic(thoughtArea)
          case false => nodeBoldNormal(thoughtArea)
        }
      case false => isItalic match {
        case true => nodeBG(italicVBox); nodeNormalItalic(thoughtArea)
        case false => nodeNormals(thoughtArea)
      }
    }
  }

  // Method to get PopupMenu
  def getPopupMenu(stage: Stage) = {
    val popup = new PopupMenu()
    val itemOpen = new MenuItem("Open")
    val itemExit = new MenuItem("Exit")
    popup.add(itemOpen)
    popup.add(itemExit)

    val listenerOpen = getListener {
      callJavaFXThreadPlatform {
        {
          if (!stage.isShowing())
            stage.show
        }
      }
    }
    itemOpen.addActionListener(listenerOpen)

    val listenerExit = getListener {
      // Get thoughts from textarea in stringbuffer
      val contentBuffer = processTextArea
      //Set properties of the thoughtArea
      thoughtProperties(contentBuffer)
      //Persist the thoughts
      writeThoughts
      // Exit the application
      System.exit(0)
    }
    itemExit.addActionListener(listenerExit)
    popup
  }

  // Method to get TrayIcon
  def getTrayIcon(stage: Stage, popup: PopupMenu) = {
    val imgFile = new File(this.getClass().getResource(TRAYICON).getFile())
    val buffImg = ImageIO.read(imgFile)
    val trayIcon = new TrayIcon(buffImg, APPNAME, popup)
    // Default double click event handler
    val listenerTray = getListener {
      callJavaFXThreadPlatform {
        if (!stage.isShowing())
          stage.show
      }
    }
    //TODO Control key + Shift key event handler
    trayIcon.addActionListener(listenerTray)
    trayIcon
  }

  // Applying system tray method
  def applySystemTray = (tray: SystemTray, stage: Stage) => {
    if (SystemTray.isSupported())
      try {
        val popup = getPopupMenu(stage)
        val trayIcon = getTrayIcon(stage, popup)
        tray.add(trayIcon)
      } catch {
        case ex: AWTException => println("Can't add to tray")
        case _ => println("Tray unavailable")
      }
  }

  // Process text area which contains thoughts
  def processTextArea = {
    val thoughtContent = thoughtArea.getParagraphs()
    val thoughtContentSplit = thoughtContent.toString.split(",")
    val contentBuffer = new StringBuffer
    for (i <- 0 until thoughtContentSplit.length) {
      contentBuffer append thoughtContent.get(i)
      contentBuffer append "\n"
    }
    contentBuffer.setLength(contentBuffer.length - 1)
    contentBuffer
  }

  // Set Properties from thoughtarea
  def thoughtProperties(contentBuffer: StringBuffer) {
    thoughtProps.setProperty(BOLD_ACTION, isBoldStyled(thoughtArea).toString)
    thoughtProps.setProperty(ITALIC_ACTION, isItalicStyled(thoughtArea).toString)
    thoughtProps.setProperty(CONTENT, contentBuffer.toString())
  }

  // Write thoughts in file
  def writeThoughts = {
    val outputStream = new FileOutputStream(this.getClass().getResource(THOUGHTPROPS).getFile())
    if (outputStream ne null)
      quietlyDispose(thoughtProps store (outputStream, "Thoughts are stored"), outputStream.close)
    true
  }

  // Thought writer properties file
  lazy val thoughtProps: Properties = {
    val props = new Properties
    val inputStream = this.getClass getResourceAsStream THOUGHTPROPS
    if (inputStream ne null)
      quietlyDispose(props load inputStream, inputStream.close)
    props
  }

  // Method to quietly dispose any stream - scala control abstraction
  private def quietlyDispose(action: => Unit, disposal: => Unit) = {
    try { action }
    finally {
      try { disposal }
      catch { case _: IOException => println("IO exception occurred") }
    }
  }

  // Method to call Platform thread - scala control abstraction
  def callJavaFXThreadPlatform(action: => Unit) = {
    Platform.runLater(new Runnable {
      override def run {
        action
      }
    })
  }

  // Method to get ActionListener by passing action - scala control abstraction
  def getListener(action: => Unit) = {
    new ActionListener() {
      override def actionPerformed(ae: java.awt.event.ActionEvent) {
        action
      }
    }
  }
}