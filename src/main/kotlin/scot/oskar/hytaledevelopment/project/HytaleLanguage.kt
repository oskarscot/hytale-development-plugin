package scot.oskar.hytaledevelopment.project

import com.intellij.ide.wizard.NewProjectWizardMultiStepFactory
import com.intellij.openapi.extensions.ExtensionPointName
import scot.oskar.hytaledevelopment.project.step.HytaleLanguageStep

interface HytaleLanguage : NewProjectWizardMultiStepFactory<HytaleLanguageStep> {
    companion object {
        val EP_NAME = ExtensionPointName<HytaleLanguage>("scot.oskar.hytaledevelopment.language")
    }
}
