package scot.oskar.hytaledevelopment.project.step

import com.intellij.ide.wizard.AbstractNewProjectWizardStep
import com.intellij.ide.wizard.NewProjectWizardBaseData.Companion.baseData
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.asContextElement
import com.intellij.openapi.components.service
import com.intellij.ui.dsl.builder.COLUMNS_LARGE
import com.intellij.ui.dsl.builder.COLUMNS_MEDIUM
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.bindItem
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import scot.oskar.hytaledevelopment.HytaleCoroutineService
import scot.oskar.hytaledevelopment.HytaleDevelopmentBundle
import scot.oskar.hytaledevelopment.project.HytaleNewProjectWizardData
import scot.oskar.hytaledevelopment.util.HytalePatchline
import scot.oskar.hytaledevelopment.util.VersionChecker

class HytaleConfigStep(parent: NewProjectWizardStep) : AbstractNewProjectWizardStep(parent), HytaleNewProjectWizardData {

    private val pluginNameProperty = propertyGraph.lazyProperty {
        baseData?.name?.toClassName().orEmpty().ifBlank { "MyHytalePlugin" }
    }
    private val packageNameProperty = propertyGraph.lazyProperty {
        "org.example.${pluginNameProperty.get().lowercase().filter { it.isLetterOrDigit() }}"
    }
    private val patchlineProperty = propertyGraph.property(HytalePatchline.RELEASE)
    private val hytaleVersionProperty = propertyGraph.property("")

    private val availableVersionsProperty = propertyGraph.property<List<String>>(emptyList())
    private val loadErrorProperty = propertyGraph.property("")

    override val pluginName by pluginNameProperty
    override val packageName by packageNameProperty
    override val patchline by patchlineProperty
    override val hytaleVersion by hytaleVersionProperty

    private val checker = VersionChecker()
    private var fetchJob: Job = launchFetch(patchlineProperty.get())

    init {
        data.putUserData(HytaleNewProjectWizardData.KEY, this)
        patchlineProperty.afterChange {
            fetchJob.cancel()
            fetchJob = launchFetch(it)
        }
    }

    private fun launchFetch(target: HytalePatchline): Job {
        loadErrorProperty.set("")

        return service<HytaleCoroutineService>().scope.launch {
            val result = checker.fetchVersions(target)
            withContext(Dispatchers.EDT + ModalityState.any().asContextElement()) {
                if (target != patchlineProperty.get()) return@withContext
                result
                    .onSuccess {
                        availableVersionsProperty.set(it.versions)
                        hytaleVersionProperty.set(it.latest)
                    }
                    .onFailure { e ->
                        availableVersionsProperty.set(emptyList())
                        loadErrorProperty.set(e.message ?: "Failed to fetch versions")
                    }
            }
        }
    }

    override fun setupUI(builder: Panel) {
        with(builder) {
            group(HytaleDevelopmentBundle.message("hytale.wizard.group.title")) {
                row(HytaleDevelopmentBundle.message("hytale.wizard.field.plugin.name")) {
                    textField()
                        .bindText(pluginNameProperty)
                        .columns(COLUMNS_MEDIUM)
                        .validationOnApply {
                            if (it.text.isBlank()) error("Plugin name is required")
                            else if (!it.text.matches(Regex("[A-Za-z][A-Za-z0-9_]*")))
                                error("Plugin name must start with a letter and only use letters, digits, or underscore")
                            else null
                        }
                }
                row(HytaleDevelopmentBundle.message("hytale.wizard.field.package")) {
                    textField()
                        .bindText(packageNameProperty)
                        .columns(COLUMNS_LARGE)
                        .validationOnApply {
                            if (!it.text.matches(Regex("[a-z][a-z0-9_]*(\\.[a-z][a-z0-9_]*)*")))
                                error("Package must be lowercase, dot-separated, e.g. org.example.myplugin")
                            else null
                        }
                }
                row(HytaleDevelopmentBundle.message("hytale.wizard.field.patchline")) {
                    comboBox(HytalePatchline.entries.toList())
                        .bindItem(patchlineProperty)
                }
                row(HytaleDevelopmentBundle.message("hytale.wizard.field.hytale.version")) {
                    comboBox(availableVersionsProperty.get())
                        .bindItem(hytaleVersionProperty)
                        .applyToComponent {
                            availableVersionsProperty.afterChange { newVersions ->
                                val previous = selectedItem as? String
                                removeAllItems()
                                newVersions.forEach { addItem(it) }
                                selectedItem = previous?.takeIf { it in newVersions } ?: newVersions.lastOrNull()
                            }
                        }
                        .validationOnApply {
                            val v = it.selectedItem as? String
                            when {
                                !v.isNullOrBlank() -> null
                                loadErrorProperty.get().isNotBlank() ->
                                    error("Couldn't load Hytale versions: ${loadErrorProperty.get()}")
                                else -> error("Pick a Hytale version")
                            }
                        }
                }
            }
        }
    }

    private fun String.toClassName(): String =
        split(Regex("[^A-Za-z0-9]"))
            .filter { it.isNotEmpty() }
            .joinToString("") { it.replaceFirstChar(Char::uppercase) }
}
