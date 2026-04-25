package scot.oskar.hytaledevelopment

import com.intellij.openapi.components.Service
import kotlinx.coroutines.CoroutineScope

@Service(Service.Level.APP)
class HytaleCoroutineService(val scope: CoroutineScope)
