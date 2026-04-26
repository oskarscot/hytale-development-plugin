package scot.oskar.hytaledevelopment.project

import com.intellij.ide.util.projectWizard.WizardContext
import com.intellij.ide.wizard.GeneratorNewProjectWizard
import com.intellij.ide.wizard.GitNewProjectWizardStep
import com.intellij.ide.wizard.NewProjectWizardBaseStep
import com.intellij.ide.wizard.NewProjectWizardChainStep.Companion.nextStep
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.ide.wizard.RootNewProjectWizardStep
import org.jetbrains.annotations.Nls
import scot.oskar.hytaledevelopment.HytaleDevelopmentBundle
import scot.oskar.hytaledevelopment.HytaleIcons.Hytale
import scot.oskar.hytaledevelopment.project.step.HytaleConfigStep
import scot.oskar.hytaledevelopment.project.step.HytaleLanguageStep
import javax.swing.Icon

class HytaleProjectGenerator : GeneratorNewProjectWizard {

    override val id: String = "hytale"
    override val name: @Nls(capitalization = Nls.Capitalization.Title) String = HytaleDevelopmentBundle.message("hytale.wizard.project.name")
    override val icon: Icon = Hytale

    override fun createStep(context: WizardContext): NewProjectWizardStep =
        RootNewProjectWizardStep(context)
            .nextStep(::NewProjectWizardBaseStep)
            .nextStep(::GitNewProjectWizardStep)
            .nextStep(::HytaleConfigStep)
            .nextStep(::HytaleLanguageStep)
}
