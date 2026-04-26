package scot.oskar.hytaledevelopment.project.step

import com.intellij.ide.wizard.NewProjectWizardBaseData
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.ui.dsl.builder.Panel
import org.jetbrains.plugins.gradle.service.project.wizard.GradleNewProjectWizardStep

abstract class HytaleGradleStep<P>(parent: P) :
    GradleNewProjectWizardStep<P>(parent),
    NewProjectWizardBaseData by parent
        where P : NewProjectWizardStep, P : NewProjectWizardBaseData {

    override fun setupSettingsUI(builder: Panel) {
        setupJavaSdkUI(builder)
        setupGradleDslUI(builder)
        setupParentsUI(builder)
    }

    override fun setupAdvancedSettingsUI(builder: Panel) {
        setupGradleDistributionUI(builder)
        setupGroupIdUI(builder)
        setupArtifactIdUI(builder)
        setupVersionUI(builder)
    }
}
