package com.firsttimeinforever.intellij.pdf.viewer.utility

import com.firsttimeinforever.intellij.pdf.viewer.lang.PdfFileType
import com.intellij.diff.contents.DiffContent
import com.intellij.diff.contents.DocumentContent
import com.intellij.diff.contents.FileContent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager

fun DiffContent.getVirtualFile(project: Project): VirtualFile? {
  return when (this) {
    is FileContent -> this.file
    is DocumentContent -> PsiDocumentManager.getInstance(project).getPsiFile(this.document)?.virtualFile
    else -> null
  }
}

fun DiffContent.isPdfBinary(project: Project): Boolean {
  val virtualFile = getVirtualFile(project) ?: return false
  return virtualFile.fileType == PdfFileType
}
