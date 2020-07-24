package com.rspn.vcs.services

import com.intellij.openapi.project.Project
import com.rspn.vcs.Bundle

class ProjectService(project: Project) {

    init {
        println(Bundle.message("projectService", project.name))
    }
}
