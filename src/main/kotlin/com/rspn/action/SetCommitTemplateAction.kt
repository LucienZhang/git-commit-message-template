package com.rspn.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.VisualPosition
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
                val message = GitUtils.parseAndBuildMessage(
                        prefix = persistentSettings.prefix.withNewLineCharacter(),
                        suffix = persistentSettings.suffix.withNewLineCharacter(),
                        regexPattern = getRegexFromRadioButton(persistentSettings),
                        branchName = branchName,
                        customMessageComponents = getCustomMessageComponents(persistentSettings)
                )
                data.setCommitMessage(message)
                e.getData(CommonDataKeys.EDITOR)?.caretModel?.currentCaret?.let { caret->
                    caret.run {
                        setSelection(0, 0)
                        moveToVisualPosition(VisualPosition(1, message.length))
                    }
                }
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
