package com.rspn.services

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.rspn.Bundle

@State(name = "VCSCommitMessageTemplate", storages = [Storage("vcs-commit-message-template.xml")])
class ApplicationService : PersistentStateComponent<ApplicationService> {

    init {
        println(Bundle.message("applicationService"))
    }

    override fun getState() = this

    override fun loadState(state: ApplicationService) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance() = ServiceManager.getService(ApplicationService::class.java)!!
    }
}
