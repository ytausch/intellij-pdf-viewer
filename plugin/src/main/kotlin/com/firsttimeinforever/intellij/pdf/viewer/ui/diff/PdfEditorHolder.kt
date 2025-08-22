package com.firsttimeinforever.intellij.pdf.viewer.ui.diff

import com.firsttimeinforever.intellij.pdf.viewer.ui.editor.PdfFileEditorProvider
import com.firsttimeinforever.intellij.pdf.viewer.utility.getVirtualFile
import com.firsttimeinforever.intellij.pdf.viewer.utility.isPdfBinary
import com.intellij.diff.DiffContext
import com.intellij.diff.contents.DiffContent
import com.intellij.diff.tools.holders.EditorHolder
import com.intellij.diff.tools.holders.EditorHolderFactory
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.project.ProjectManager
import com.intellij.openapi.util.Disposer
import java.awt.event.FocusListener
import javax.swing.JComponent

class PdfEditorHolder(
  private val myEditor: FileEditor,
  private val myEditorProvider: FileEditorProvider?
) : EditorHolder() {

  override fun dispose() {
    if (myEditorProvider != null) {
      myEditorProvider.disposeEditor(myEditor)
    } else {
      Disposer.dispose(myEditor)
    }
  }

  override fun getComponent(): JComponent = myEditor.component

  override fun installFocusListener(listener: FocusListener) {
    myEditor.component.addFocusListener(listener)
  }

  override fun getPreferredFocusedComponent(): JComponent? = myEditor.preferredFocusedComponent

  class Factory() : EditorHolderFactory<PdfEditorHolder>() {
    companion object {
      val INSTANCE = Factory()
    }

    override fun create(content: DiffContent, context: DiffContext): PdfEditorHolder {
      val project = context.project ?: ProjectManager.getInstance().defaultProject
      if (!content.isPdfBinary(project)) {
        error("Content must be a PDF binary")
      }
      val provider = PdfFileEditorProvider()
      val editor = provider.createEditor(
        project,
        content.getVirtualFile(project) ?: error("Content must have a virtual file")
      )

      Disposer.register(editor) {
        provider.disposeEditor(editor)
      }

      return PdfEditorHolder(editor, provider)
    }

    override fun canShowContent(content: DiffContent, context: DiffContext): Boolean = content.isPdfBinary(context.project ?: ProjectManager.getInstance().defaultProject)

    override fun wantShowContent(content: DiffContent, context: DiffContext): Boolean = true
  }
}
