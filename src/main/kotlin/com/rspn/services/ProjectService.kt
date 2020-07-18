package com.rspn.services

import com.intellij.openapi.project.Project
import com.rspn.Bundle

class ProjectService(project: Project) {

    init {
        println(Bundle.message("projectService", project.name))
    }
}
