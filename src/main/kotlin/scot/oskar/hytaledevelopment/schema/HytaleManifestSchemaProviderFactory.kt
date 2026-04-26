package scot.oskar.hytaledevelopment.schema

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.search.GlobalSearchScope
import com.jetbrains.jsonSchema.extension.JsonSchemaFileProvider
import com.jetbrains.jsonSchema.extension.JsonSchemaProviderFactory
import com.jetbrains.jsonSchema.extension.SchemaType

class HytaleManifestSchemaProviderFactory : JsonSchemaProviderFactory {
    override fun getProviders(project: Project): List<JsonSchemaFileProvider> =
        listOf(HytaleManifestSchemaProvider(project))
}

private class HytaleManifestSchemaProvider(private val project: Project) : JsonSchemaFileProvider {

    override fun getName(): String = "Hytale Plugin Manifest"

    override fun isAvailable(file: VirtualFile): Boolean {
        if (file.name != "manifest.json") return false
        if (DumbService.isDumb(project)) return false
        return runReadActionBlocking {
            JavaPsiFacade.getInstance(project)
                .findClass(HYTALE_PLUGIN_FQN, GlobalSearchScope.allScope(project)) != null
        }
    }

    override fun getSchemaFile(): VirtualFile? = SCHEMA_FILE
    override fun getSchemaType(): SchemaType = SchemaType.embeddedSchema

    private companion object {
        const val HYTALE_PLUGIN_FQN = "com.hypixel.hytale.server.core.plugin.JavaPlugin"

        val SCHEMA_FILE: VirtualFile? = JsonSchemaProviderFactory.getResourceFile(
            HytaleManifestSchemaProvider::class.java,
            "/schemas/hytale-manifest.schema.json",
        )
    }
}
