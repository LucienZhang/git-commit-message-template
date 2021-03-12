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
            try {
                data.setCommitMessage(
                    GitUtils.parseAndBuildMessage(
                        prefix = persistentSettings.prefix.withNewLineCharacter(),
                        suffix = persistentSettings.suffix.withNewLineCharacter(),
                        regexPattern = getRegexFromRadioButton(persistentSettings),
                        branchName = branchName,
                        customMessageComponents = getCustomMessageComponents(persistentSettings)
                    )
                )
            } catch (e: Exception) {
                throw e
            }
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

    private fun getCustomMessageComponents(persistentSettings: PersistentSettings): String? {
        return when (persistentSettings.selectedMessageComponentsRegexRadioButtonIndex) {
            0 -> null
            1 -> persistentSettings.messageComponentsBackreference
            else -> throw IllegalStateException("Unknown selected field, it should either be static or backreference index")
        }
    }
}
