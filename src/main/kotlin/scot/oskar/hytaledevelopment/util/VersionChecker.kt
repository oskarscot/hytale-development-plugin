package scot.oskar.hytaledevelopment.util

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.java.Java
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XML
import nl.adaptivity.xmlutil.serialization.XmlElement

enum class HytalePatchline(val displayName: String, val url: String) {

    RELEASE("Release", "https://maven.hytale.com/release/com/hypixel/hytale/Server/maven-metadata.xml"),
    PRE_RELEASE("Pre-Release", "https://maven.hytale.com/pre-release/com/hypixel/hytale/Server/maven-metadata.xml");

    override fun toString(): String = displayName
}

@Serializable
@SerialName("metadata")
data class HytaleVersion(@XmlElement(true) val versioning: Versioning) {
    val latest: String get() = versioning.latest
    val versions: List<String> get() = versioning.versions.list
}

@Serializable
@SerialName("versioning")
data class Versioning(
    @XmlElement(true) val latest: String,
    @XmlElement(true) val release: String? = null,
    @XmlElement(true) val versions: Versions,
)

@Serializable
@SerialName("versions")
data class Versions(
    @SerialName("version") val list: List<String>,
)

class VersionChecker(
    private val httpClient: HttpClient = HttpClient(Java),
) {
    private val xmlFormat = XML.recommended_1_0 {
        policy { ignoreUnknownChildren() }
    }

    suspend fun fetchVersions(patchline: HytalePatchline): Result<HytaleVersion> = runCatching {
        val response = httpClient.get(patchline.url)
        xmlFormat.decodeFromString(HytaleVersion.serializer(), response.body())
    }
}
