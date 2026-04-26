package scot.oskar.hytaledevelopment.project.step

import com.intellij.ide.wizard.AbstractNewProjectWizardMultiStep
import com.intellij.ide.wizard.NewProjectWizardBaseData
import com.intellij.ide.wizard.NewProjectWizardBaseData.Companion.baseData
import com.intellij.ide.wizard.NewProjectWizardStep
import scot.oskar.hytaledevelopment.HytaleDevelopmentBundle
import scot.oskar.hytaledevelopment.project.HytaleLanguage

class HytaleLanguageStep(parent: NewProjectWizardStep) :
    AbstractNewProjectWizardMultiStep<HytaleLanguageStep, HytaleLanguage>(parent, HytaleLanguage.EP_NAME),
    NewProjectWizardBaseData by checkNotNull(parent.baseData) {

    override val self get() = this
    override val label = HytaleDevelopmentBundle.message("hytale.wizard.field.language")
}
