package com.rspn.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.ui.Refreshable
import com.rspn.util.VCSUtils

class SetCommitTemplateAction : DumbAwareAction() {
    init {
        isEnabledInModalContext = true
    }

    companion object {
        const val issueIdSeparator = ":" // TODO set as configurable
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val data = Refreshable.PANEL_KEY.getData(e.dataContext)
        if (data is CommitMessageI) {
            val branchName = VCSUtils.extractBranchName(project)
            val issueId = VCSUtils.parseBranchNameByRegex(branchName = branchName, regexString = "[A-Z0-9]+-[0-9]+")
            data.setCommitMessage("$issueId$issueIdSeparator")
        }
    }
}
