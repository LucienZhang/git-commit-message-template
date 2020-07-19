package com.rspn.settings

import com.intellij.openapi.options.SearchableConfigurable
import com.rspn.TemplateConfiguration
import javax.swing.JComponent

class VCSCommitMessageTemplateSettings : SearchableConfigurable {
    private val vcsCommitMessageTemplateSettingsForm = TemplateConfiguration()

    override fun isModified() = true //TODO("Not yet implemented")

    override fun getId()= "vcs-commit-message-template"

    override fun getDisplayName() = "VCS Commit Message Template"

    override fun apply() {
        // TODO("Not yet implemented")
    }

    override fun createComponent(): JComponent? {
       return vcsCommitMessageTemplateSettingsForm.getRootPanel
    }
}
