package com.rspn.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.rspn.SettingsForm
import java.util.regex.PatternSyntaxException
import javax.swing.JComponent

class VCSCommitMessageTemplateSettings : SearchableConfigurable {
    private val settingsForm = SettingsForm()

    companion object {
        const val sampleCommitMessage = "Sample commit message description"
    }

    override fun createComponent(): JComponent? {
        settingsForm.prefixTicketAndDescriptionRadioButton.addActionListener {
            settingsForm.customRegexTextField.isEnabled = false
        }
        settingsForm.ticketAndDescriptionRadioButton.addActionListener {
            settingsForm.customRegexTextField.isEnabled = false
        }
        settingsForm.customRadioButton.addActionListener {
            settingsForm.customRegexTextField.isEnabled = true
        }
        settingsForm.runPreviewButton.addActionListener {
            val selectedRegexButton =
                    setOf(settingsForm.customRadioButton, settingsForm.ticketAndDescriptionRadioButton, settingsForm.prefixTicketAndDescriptionRadioButton)
                            .first { it.isSelected }
            var customRegex: String? = null
            if (selectedRegexButton.actionCommand == "Custom") {
                customRegex = settingsForm.customRegexTextField.text
            }
            val suffix = settingsForm.issueSuffixTextField.text
            val sampleBranchName = settingsForm.branchNameTextFieldPreview.text
            try {
                val matchResult = Regex(customRegex ?: selectedRegexButton.actionCommand).find(sampleBranchName)
                val matchedRegexValue = matchResult?.value
                settingsForm.resultingCommitMessageTemplatePreview.text = "$matchedRegexValue$suffix$sampleCommitMessage"
            } catch (e: PatternSyntaxException) {
                settingsForm.errorLabel.text = e.message
            }
        }
        return settingsForm.getRootPanel

    }

    override fun isModified(): Boolean {
        //TODO
        return false
    }

    override fun getId() = "vcs-commit-message-template"

    override fun getDisplayName() = "VCS Commit Message Template"

    override fun apply() {
        // TODO("Not yet implemented")
    }
}
