package com.rspn.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.rspn.SettingsForm
import com.rspn.services.PersistentSettings
import java.util.regex.PatternSyntaxException
import javax.swing.JComponent

class GitCommitMessageTemplateSettings : SearchableConfigurable {
    private val settingsForm = SettingsForm()
    private val persistentSettings = PersistentSettings.getInstance()
    private val radioButtonMapping = mapOf(
        0 to settingsForm.ticketAndDescriptionRadioButton,
        1 to settingsForm.prefixTicketAndDescriptionRadioButton,
        2 to settingsForm.customRadioButton
    )

    companion object {
        private const val sampleCommitMessage = "Sample commit message description"
        private const val CUSTOM = "Custom"
    }

    override fun createComponent(): JComponent? {
        addDefaultRadioButtonsActions()
        addCustomRadioButtonAction()
        addRunPreviewButtonAction()
        applySavedState()

        return settingsForm.getRootPanel
    }

    private fun applySavedState() {
        settingsForm.apply {
            radioButtonMapping.getValue(persistentSettings.selectedRadioButtonIndex).isSelected = true
            customRegexTextField.text = persistentSettings.customRegex
            customRegexTextField.isEnabled = persistentSettings.selectedRadioButtonIndex == 2
            issuePrefixTextField.text = persistentSettings.prefix
            issueSuffixTextField.text = persistentSettings.suffix
            branchNameTextFieldPreview.text = persistentSettings.branchName
        }
    }

    private fun addRunPreviewButtonAction() {
        settingsForm.runPreviewButton.addActionListener {
            val selectedRegexButton =
                setOf(
                    settingsForm.customRadioButton,
                    settingsForm.ticketAndDescriptionRadioButton,
                    settingsForm.prefixTicketAndDescriptionRadioButton
                )
                    .first { it.isSelected }
            var customRegex: String? = null
            if (selectedRegexButton.actionCommand == CUSTOM ) {
                customRegex = settingsForm.customRegexTextField.text
            }
            val prefix = settingsForm.issuePrefixTextField.text
            val suffix = settingsForm.issueSuffixTextField.text
            val sampleBranchName = settingsForm.branchNameTextFieldPreview.text
            try {
                val matchResult = Regex(customRegex ?: selectedRegexButton.actionCommand)
                    .find(sampleBranchName)
                val matchedRegexValue = matchResult?.value
                settingsForm.resultingCommitMessageTemplatePreview.text =
                    "$prefix$matchedRegexValue$suffix$sampleCommitMessage"
            } catch (e: PatternSyntaxException) {
                settingsForm.errorLabel.text = e.message
            }
        }
    }

    private fun addCustomRadioButtonAction() {
        settingsForm.customRadioButton.addActionListener {
            settingsForm.customRegexTextField.isEnabled = true
        }
    }

    private fun addDefaultRadioButtonsActions() {
        setOf(settingsForm.prefixTicketAndDescriptionRadioButton, settingsForm.ticketAndDescriptionRadioButton)
            .forEach {
                it.addActionListener {
                    settingsForm.customRegexTextField.isEnabled = false
                }
            }
    }

    override fun isModified(): Boolean {
        return persistentSettings.customRegex != settingsForm.customRegexTextField.text ||
                persistentSettings.branchName != settingsForm.branchNameTextFieldPreview.text ||
                persistentSettings.suffix != settingsForm.issueSuffixTextField.text ||
                persistentSettings.prefix != settingsForm.issuePrefixTextField.text ||
                persistentSettings.selectedRadioButtonIndex != getSelectedRadioButtonIndex()
    }

    override fun getId() = "git-commit-message-template"

    override fun getDisplayName() = "Git Commit Message Template"

    override fun apply() {
        persistentSettings.customRegex = settingsForm.customRegexTextField.text
        persistentSettings.branchName = settingsForm.branchNameTextFieldPreview.text
        persistentSettings.prefix = settingsForm.issuePrefixTextField.text
        persistentSettings.suffix = settingsForm.issueSuffixTextField.text
        persistentSettings.selectedRadioButtonIndex = getSelectedRadioButtonIndex()
    }

    private fun getSelectedRadioButtonIndex(): Int {
        for (index in 0..radioButtonMapping.entries.size) {
            if (radioButtonMapping.getValue(index).isSelected) {
                return index
            }
        }
        throw IllegalStateException("Should have found an selected radio button")
    }
}

fun String.withNewLineCharacter(): String {
    return this.replace("\\n", System.getProperty("line.separator"))
}
