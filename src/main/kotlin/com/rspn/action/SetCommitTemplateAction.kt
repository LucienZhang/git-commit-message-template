package com.rspn.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.vcs.CommitMessageI
import com.intellij.openapi.vcs.ui.Refreshable
import com.rspn.Bundle
import com.rspn.services.PersistentSettings
import com.rspn.settings.withNewLineCharacter
import com.rspn.util.GitUtils

class SetCommitTemplateAction : DumbAwareAction() {
    init {
        isEnabledInModalContext = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val data = Refreshable.PANEL_KEY.getData(e.dataContext)
        if (data is CommitMessageI) {
            val branchName = GitUtils.extractBranchName(project)
            val persistentSettings = PersistentSettings.getInstance()
            val regexCapturedValue = GitUtils.parseBranchNameByRegex(branchName = branchName, regexString = getRegexFromRadioButton(persistentSettings))
            data.setCommitMessage("${persistentSettings.prefix.withNewLineCharacter()}$regexCapturedValue${persistentSettings.suffix.withNewLineCharacter()}")
        }
    }

    private fun getRegexFromRadioButton(persistentSettings: PersistentSettings): String {
        return when (persistentSettings.selectedBranchRegexRadioButtonIndex) {
            0 -> Bundle.getMessage("defaultRegex")
            1 -> Bundle.getMessage("issueTicketRegex")
            2 -> persistentSettings.customRegex
            else -> throw IllegalStateException("Should have a selected radio button")
        }
    }
}
