package scot.oskar.hytaledevelopment.project.step

import com.intellij.ide.wizard.NewProjectWizardChainStep.Companion.nextStep
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.openapi.project.Project
import org.jetbrains.plugins.gradle.service.project.wizard.GradleAssetsNewProjectWizardStep
import scot.oskar.hytaledevelopment.project.HytaleLanguage
import scot.oskar.hytaledevelopment.project.HytaleNewProjectWizardData.Companion.hytaleData
import scot.oskar.hytaledevelopment.project.HytaleNewProjectWizardData.Companion.mainClassName
import scot.oskar.hytaledevelopment.project.HytaleNewProjectWizardData.Companion.packagePath

class JavaHytaleLanguage : HytaleLanguage {

    override val name = "Java"

    override fun createStep(parent: HytaleLanguageStep): NewProjectWizardStep =
        Step(parent).nextStep(::AssetsStep)

    private class Step(parent: HytaleLanguageStep) : HytaleGradleStep<HytaleLanguageStep>(parent)

    private class AssetsStep(parent: Step) : GradleAssetsNewProjectWizardStep<Step>(parent) {

        override fun setupAssets(project: Project) {
            val cfg = hytaleData ?: return
            val mainClass = cfg.mainClassName
            val mainPath = "src/main/java/${cfg.packagePath}/$mainClass.java"
            val patchline = cfg.patchline.name.lowercase().replace('_', '-')

            addHytaleCommonAssets(parent.gradleVersionToUse)

            addBuildScript {
                withJavaPlugin()
                withJavaToolchain(25)
                withMavenCentral()
                withRepository {
                    call("maven", string("https://maven.hytale.com/$patchline"))
                }
                withDependency {
                    call("compileOnly", string("com.hypixel.hytale:Server:${cfg.hytaleVersion}"))
                }
            }

            addTemplateAsset(mainPath, "HytaleJavaMainClass", mapOf(
                "PACKAGE" to cfg.packageName,
                "CLASS_NAME" to mainClass,
            ))
            addEmptyDirectoryAsset("src/test/java")
        }
    }
}
