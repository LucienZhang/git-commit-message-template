package com.rspn.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.rspn.Bundle

@State(name = "git-commit-message-template", storages = [Storage("git-commit-message-template.xml")])
class PersistentSettings(
    var selectedBranchRegexRadioButtonIndex: Int = 0,
    var selectedMessageComponentsRegexRadioButtonIndex: Int = 0,
    var suffix: String = Bundle.getMessage("suffix"),
    var prefix: String = Bundle.getMessage("prefix"),
    var customRegex: String = Bundle.getMessage("customRegex"),
    var messageComponentsBackreference: String = Bundle.getMessage("messageComponentsBackreference"),
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
