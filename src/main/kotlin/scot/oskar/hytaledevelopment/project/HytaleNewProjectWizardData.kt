package scot.oskar.hytaledevelopment.project

import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.openapi.util.Key
import scot.oskar.hytaledevelopment.util.HytalePatchline

interface HytaleNewProjectWizardData {

    val pluginName: String
    val packageName: String
    val patchline: HytalePatchline
    val hytaleVersion: String

    companion object {
        val KEY = Key.create<HytaleNewProjectWizardData>(HytaleNewProjectWizardData::class.java.name)

        val NewProjectWizardStep.hytaleData: HytaleNewProjectWizardData?
            get() = data.getUserData(KEY)

        val HytaleNewProjectWizardData.mainClassName: String
            get() = pluginName.split(Regex("[^A-Za-z0-9]"))
                .filter { it.isNotEmpty() }
                .joinToString("") { it.replaceFirstChar(Char::uppercase) }

        val HytaleNewProjectWizardData.packagePath: String
            get() = packageName.replace('.', '/')
    }

}
