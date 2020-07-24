package com.rspn.vcs.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.rspn.vcs.Bundle

@State(name = "VCSCommitMessageTemplate", storages = [Storage("vcs-commit-message-template.xml")])
class PersistentSettings(
    var selectedRadioButtonIndex: Int = 0,
    var suffix: String = Bundle.getMessage("suffix"),
    var customRegex: String = Bundle.getMessage("customRegex"),
    var branchName: String = Bundle.getMessage("branchName")
) : PersistentStateComponent<PersistentSettings> {

    override fun getState() = this

    override fun loadState(state: PersistentSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance() = ServiceManager.getService(PersistentSettings::class.java)!!
    }
}
