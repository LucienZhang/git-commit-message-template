package com.rspn.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.rspn.SettingsForm
import com.rspn.services.PersistentSettings
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException
import javax.swing.JComponent
import javax.swing.JRadioButton

class GitCommitMessageTemplateSettings : SearchableConfigurable {
    private val settingsForm = SettingsForm()
    private val persistentSettings = PersistentSettings.getInstance()
    private val radioButtonBranchRegexMapping = mapOf(
        0 to settingsForm.ticketAndDescriptionRadioButton,
        1 to settingsForm.prefixTicketAndDescriptionRadioButton,
        2 to settingsForm.branchRegexCustomRadioButton
    )

    private val radioButtonMappingMessageComponents = mapOf(
        0 to settingsForm.staticComponentsRadioButton,
        1 to settingsForm.regexGroupsAndBackreferencesRadioButton
    )
    private val branchRegexButtonSet = setOf(
        settingsForm.branchRegexCustomRadioButton,
        settingsForm.ticketAndDescriptionRadioButton,
        settingsForm.prefixTicketAndDescriptionRadioButton
    )
    private val messageComponentsButtonSet =
        setOf(settingsForm.staticComponentsRadioButton, settingsForm.regexGroupsAndBackreferencesRadioButton)

    companion object {
        private const val sampleCommitMessage = "Sample commit message description"
        private const val CUSTOM = "Custom"
        private const val CUSTOM_COMPONENTS = "Regex groups and backreferences"
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
            radioButtonBranchRegexMapping.getValue(persistentSettings.selectedBranchRegexRadioButtonIndex).isSelected = true
            customRegexTextField.text = persistentSettings.customRegex
            customRegexTextField.isEnabled =
                persistentSettings.selectedBranchRegexRadioButtonIndex == radioButtonBranchRegexMapping.entries.first { it.value == settingsForm.branchRegexCustomRadioButton }.key

            regexGroupBackreferenceTextField.text = persistentSettings.messageComponentsBackreference
            regexGroupBackreferenceTextField.isEnabled =
                persistentSettings.selectedMessageComponentsRegexRadioButtonIndex == radioButtonMappingMessageComponents.entries.first { it.value == settingsForm.regexGroupsAndBackreferencesRadioButton }.key
            radioButtonMappingMessageComponents.getValue(persistentSettings.selectedMessageComponentsRegexRadioButtonIndex).isSelected = true
            issuePrefixTextField.text = persistentSettings.prefix
            issueSuffixTextField.text = persistentSettings.suffix
            issuePrefixTextField.isEnabled = !regexGroupBackreferenceTextField.isEnabled
            issueSuffixTextField.isEnabled = !regexGroupBackreferenceTextField.isEnabled
            branchNameTextFieldPreview.text = persistentSettings.branchName
        }
    }

    private fun addRunPreviewButtonAction() {
        settingsForm.runPreviewButton.addActionListener {
            val selectedRegexButton = branchRegexButtonSet.first { it.isSelected }
            var customRegex: String? = null
            if (selectedRegexButton.actionCommand == CUSTOM) {
                customRegex = settingsForm.customRegexTextField.text
            }
            val selectedComponentsButton = messageComponentsButtonSet.first { it.isSelected }

            var customMessageComponents: String? = null
            if (selectedComponentsButton.actionCommand == CUSTOM_COMPONENTS) {
                customMessageComponents = settingsForm.regexGroupBackreferenceTextField.text
            }

            settingsForm.errorLabel.text = ""
            val prefix = settingsForm.issuePrefixTextField.text
            val suffix = settingsForm.issueSuffixTextField.text
            val sampleBranchName = settingsForm.branchNameTextFieldPreview.text
            try {
                val pattern = customRegex ?: selectedRegexButton.actionCommand
                val matchResult = Regex(pattern)
                    .find(sampleBranchName)
                val matchedRegexValue = matchResult?.value
                when (customMessageComponents) {
                    null -> {
                        settingsForm.resultingCommitMessageTemplatePreview.text =
                            "$prefix$matchedRegexValue$suffix$sampleCommitMessage"
                    }
                    else -> settingsForm.resultingCommitMessageTemplatePreview.text = Pattern.compile(pattern)
                        .matcher(sampleBranchName)
                        .replaceAll("$customMessageComponents$sampleCommitMessage")
                }
            } catch (e: PatternSyntaxException) {
                settingsForm.errorLabel.text = e.message
            } catch (e: IndexOutOfBoundsException) {
                settingsForm.errorLabel.text = e.message
            }
        }
    }

    private fun addCustomRadioButtonAction() {
        settingsForm.branchRegexCustomRadioButton.addActionListener {
            settingsForm.customRegexTextField.isEnabled = true
        }

        settingsForm.regexGroupsAndBackreferencesRadioButton.addActionListener {
            settingsForm.regexGroupBackreferenceTextField.isEnabled = true
            settingsForm.issuePrefixTextField.isEnabled = false
            settingsForm.issueSuffixTextField.isEnabled = false
        }
    }

    private fun addDefaultRadioButtonsActions() {
        setOf(settingsForm.prefixTicketAndDescriptionRadioButton, settingsForm.ticketAndDescriptionRadioButton)
            .forEach {
                it.addActionListener {
                    settingsForm.customRegexTextField.isEnabled = false
                }
            }
        settingsForm.staticComponentsRadioButton.addActionListener {
            settingsForm.regexGroupBackreferenceTextField.isEnabled = false
            settingsForm.issuePrefixTextField.isEnabled = true
            settingsForm.issueSuffixTextField.isEnabled = true
        }
    }

    override fun isModified(): Boolean {
        return persistentSettings.customRegex != settingsForm.customRegexTextField.text ||
                persistentSettings.branchName != settingsForm.branchNameTextFieldPreview.text ||
                persistentSettings.suffix != settingsForm.issueSuffixTextField.text ||
                persistentSettings.prefix != settingsForm.issuePrefixTextField.text ||
                persistentSettings.selectedBranchRegexRadioButtonIndex != getSelectedRadioButtonIndex(radioButtonBranchRegexMapping) ||
                persistentSettings.selectedMessageComponentsRegexRadioButtonIndex != getSelectedRadioButtonIndex(radioButtonMappingMessageComponents) ||
                persistentSettings.messageComponentsBackreference != settingsForm.regexGroupBackreferenceTextField.text
    }

    override fun getId() = "git-commit-message-template"

    override fun getDisplayName() = "Git Commit Message Template"

    override fun apply() {
        persistentSettings.customRegex = settingsForm.customRegexTextField.text
        persistentSettings.branchName = settingsForm.branchNameTextFieldPreview.text
        persistentSettings.prefix = settingsForm.issuePrefixTextField.text
        persistentSettings.suffix = settingsForm.issueSuffixTextField.text
        persistentSettings.selectedBranchRegexRadioButtonIndex = getSelectedRadioButtonIndex(radioButtonBranchRegexMapping)
        persistentSettings.selectedMessageComponentsRegexRadioButtonIndex = getSelectedRadioButtonIndex(radioButtonMappingMessageComponents)
        persistentSettings.messageComponentsBackreference = settingsForm.regexGroupBackreferenceTextField.text
    }

    private fun getSelectedRadioButtonIndex(radioButtonMapping: Map<Int, JRadioButton>): Int {
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
