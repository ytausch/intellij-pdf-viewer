package com.firsttimeinforever.intellij.pdf.viewer.ui.diff

import com.firsttimeinforever.intellij.pdf.viewer.utility.isPdfBinary
import com.intellij.diff.DiffContext
import com.intellij.diff.requests.ContentDiffRequest
import com.intellij.diff.requests.DiffRequest
import com.intellij.diff.tools.binary.BinaryDiffTool
import com.intellij.openapi.project.ProjectManager

class PdfTwoSideDiffTool : BinaryDiffTool() {
  override fun canShow(context: DiffContext, request: DiffRequest): Boolean {
    if (request !is ContentDiffRequest) return false
    if (request.contents.size != 2) return false

    return request.contents.all { it.isPdfBinary(context.project ?: ProjectManager.getInstance().defaultProject) }
  }

  override fun getName(): String = "PDF two-side diff"

  override fun createComponent(context: DiffContext, request: DiffRequest): TwosidePdfDiffViewer {
    return TwosidePdfDiffViewer(context, request)
  }
}
