package scot.oskar.hytaledevelopment

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import scot.oskar.hytaledevelopment.util.HytalePatchline
import scot.oskar.hytaledevelopment.util.VersionChecker

class VersionCheckerTests {

    @Test
    fun `should return pre-release versions`() = runBlocking {
        val versionChecker = VersionChecker()
        val fetchVersions = versionChecker.fetchVersions(HytalePatchline.RELEASE)
        assertTrue(fetchVersions.isSuccess)
    }
}