package scot.oskar.hytaledevelopment

import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import org.junit.Test
import scot.oskar.hytaledevelopment.util.HytalePatchline
import scot.oskar.hytaledevelopment.util.VersionChecker

class VersionCheckerTests {

    @Test
    fun `should return pre-release versions`() = runBlocking {
        val versions = VersionChecker.fetchVersions(HytalePatchline.PRE_RELEASE)
        println(versions)
        assertNotNull(versions)
    }
}