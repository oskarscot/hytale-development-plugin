package scot.oskar.hytaledevelopment.project.step

import com.intellij.ide.projectWizard.generators.AssetsNewProjectWizardStep
import com.intellij.ide.wizard.NewProjectWizardBaseData.Companion.baseData
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.openapi.externalSystem.importing.ImportSpecBuilder
import com.intellij.openapi.externalSystem.service.execution.ProgressExecutionMode
import com.intellij.openapi.externalSystem.util.ExternalSystemUtil
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.gradle.settings.GradleProjectSettings
import org.jetbrains.plugins.gradle.util.GradleConstants
import scot.oskar.hytaledevelopment.project.HytaleNewProjectWizardData.Companion.hytaleData

class HytaleAssetsStep(parent: NewProjectWizardStep) : AssetsNewProjectWizardStep(parent) {

    private var targetPath: String? = null

    override fun setupAssets(project: Project) {
        val base = baseData ?: return
        val cfg = hytaleData ?: return

        val path = "${base.path}/${base.name}"
        targetPath = path
        setOutputDirectory(path)

        val pkgPath = cfg.packageName.replace('.', '/')
        val mainClass = cfg.pluginName.toClassName()

        val props: Map<String, Any> = mapOf(
            "PROJECT_NAME" to base.name,
            "PACKAGE" to cfg.packageName,
            "PLUGIN_NAME" to cfg.pluginName,
            "CLASS_NAME" to mainClass,
            "PATCHLINE" to cfg.patchline.name.lowercase().replace('_', '-'),
            "HYTALE_VERSION" to cfg.hytaleVersion,
        )

        addTemplateAsset("build.gradle.kts", "HytaleBuildGradleKts", props)
        addTemplateAsset("settings.gradle.kts", "HytaleSettingsGradleKts", props)
        addTemplateAsset("gradle.properties", "HytaleGradleProperties", props)
        addTemplateAsset(".gitignore", "HytaleGitignore", props)
        addTemplateAsset("src/main/java/$pkgPath/$mainClass.java", "HytaleMainClass", props)

        addEmptyDirectoryAsset("src/main/resources")
        addEmptyDirectoryAsset("src/test/java")
        addEmptyDirectoryAsset("src/test/resources")

        addFilesToOpen("src/main/java/$pkgPath/$mainClass.java")
    }

    override fun setupProject(project: Project) {
        super.setupProject(project)
        val target = targetPath ?: return

        val settings = GradleProjectSettings().apply {
            externalProjectPath = target
        }
        ExternalSystemUtil.linkExternalProject(
            settings,
            ImportSpecBuilder(project, GradleConstants.SYSTEM_ID)
                .use(ProgressExecutionMode.IN_BACKGROUND_ASYNC),
        )
    }

    private fun String.toClassName(): String =
        split(Regex("[^A-Za-z0-9]"))
            .filter { it.isNotEmpty() }
            .joinToString("") { it.replaceFirstChar(Char::uppercase) }
}
