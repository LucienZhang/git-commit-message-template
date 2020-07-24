package com.rspn.vcs.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.impl.ProjectLevelVcsManagerImpl
import git4idea.branch.GitBranchUtil
import java.util.regex.Pattern
import org.zmlx.hg4idea.util.HgUtil

object VCSUtils {
    fun extractBranchName(project: Project): String {
        var branch: String? = null
        val instance = ProjectLevelVcsManagerImpl.getInstance(project)
        if (instance.checkVcsIsActive("Git")) {
            val currentBranch = GitBranchUtil.getCurrentRepository(project)?.currentBranch
            if (currentBranch != null) {
                branch = currentBranch.name.trim()
            }
        } else if (instance.checkVcsIsActive("Mercurial")) {
            branch = HgUtil.getCurrentRepository(project)?.currentBranch?.trim()
        }
        return branch ?: ""
    }

    fun parseBranchNameByRegex(branchName: String, regexString: String): String {
        val pattern = Pattern.compile(regexString)
        val matcher = pattern.matcher(branchName)
        val sb = StringBuilder()
        while (matcher.find()) {
            sb.append(matcher.group() + " ")
        }
        if (sb.isNotEmpty()) {
            return sb.toString().trim { it <= ' ' }
        }
        return branchName
    }
}
