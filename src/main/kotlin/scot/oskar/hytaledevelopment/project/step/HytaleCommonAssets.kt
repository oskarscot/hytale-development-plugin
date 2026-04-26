package scot.oskar.hytaledevelopment.project.step

import org.gradle.util.GradleVersion
import org.jetbrains.plugins.gradle.service.project.wizard.GradleAssetsNewProjectWizardStep
import org.jetbrains.plugins.gradle.service.project.wizard.addGradleGitIgnoreAsset
import org.jetbrains.plugins.gradle.service.project.wizard.addGradleWrapperAsset
import scot.oskar.hytaledevelopment.project.HytaleNewProjectWizardData.Companion.hytaleData
import scot.oskar.hytaledevelopment.project.HytaleNewProjectWizardData.Companion.mainClassName

internal fun GradleAssetsNewProjectWizardStep<*>.addHytaleCommonAssets(gradleVersion: GradleVersion) {
    if (context.isCreatingNewProject) {
        addGradleGitIgnoreAsset()
        addGradleWrapperAsset(gradleVersion)
    }
    addOrConfigureSettingsScript {}
    addFileAsset(
        "gradle.properties",
        """
        org.gradle.parallel=true
        org.gradle.caching=true
        """.trimIndent() + "\n",
    )

    val cfg = hytaleData ?: return
    addTemplateAsset(
        "src/main/resources/manifest.json",
        "HytaleManifestJson",
        mapOf(
            "GROUP" to cfg.packageName,
            "NAME" to cfg.pluginName,
            "MAIN" to "${cfg.packageName}.${cfg.mainClassName}",
            "SERVER_VERSION" to cfg.hytaleVersion,
        ),
    )
    addEmptyDirectoryAsset("src/test/resources")
}
