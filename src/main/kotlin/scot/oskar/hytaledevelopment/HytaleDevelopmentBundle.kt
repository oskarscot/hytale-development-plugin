package scot.oskar.hytaledevelopment

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

object HytaleDevelopmentBundle : DynamicBundle("messages.HytaleDevelopment") {

    @JvmStatic
    fun message(@PropertyKey(resourceBundle = "messages.HytaleDevelopment") key: String, vararg params: Any) =
        getMessage(key, *params)

    @Suppress("unused")
    @JvmStatic
    fun messagePointer(@PropertyKey(resourceBundle = "messages.HytaleDevelopment") key: String, vararg params: Any) =
        getLazyMessage(key, *params)
}
