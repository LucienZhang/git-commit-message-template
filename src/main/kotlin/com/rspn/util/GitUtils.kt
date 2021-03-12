package com.rspn.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.impl.ProjectLevelVcsManagerImpl
import git4idea.branch.GitBranchUtil
import java.util.regex.Pattern

object GitUtils {
    fun extractBranchName(project: Project): String {
        var branch: String? = null
        val instance = ProjectLevelVcsManagerImpl.getInstance(project)
        if (instance.checkVcsIsActive("Git")) {
            val currentBranch = GitBranchUtil.getCurrentRepository(project)?.currentBranch
            if (currentBranch != null) {
                branch = currentBranch.name.trim()
            }
        }
        return branch ?: ""
    }

    fun parseAndBuildMessage(
        prefix: String,
        suffix: String,
        regexPattern: String,
        branchName: String,
        sampleCommitMessage: String = "",
        customMessageComponents: String?
    ): String {
        try {
            val matchResult = Regex(regexPattern)
                .find(branchName)
            val matchedRegexValue = matchResult?.value
            return when (customMessageComponents) {
                null -> {
                    "$prefix$matchedRegexValue$suffix${sampleCommitMessage}"
                }
                else -> Pattern.compile(regexPattern)
                    .matcher(branchName)
                    .replaceAll("$customMessageComponents${sampleCommitMessage}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}