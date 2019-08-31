package at.xa1.shell

import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import kotlin.system.measureTimeMillis

class ProcessShellTest {
    private val instance = ProcessShell(File("."))

    @Test
    fun `echo returns TEST`() = runBlockingTest {
        val result = instance.run("echo", "TEST")
        assertEquals(0, result.exitCode)
        assertEquals("TEST", result.output.trim())
    }

    @Test
    fun `pwd returns current working path`() = runBlockingTest {
        val result = instance.run("pwd")
        assertEquals(0, result.exitCode)
        assertEquals(File(".").canonicalPath, result.output.trim())
    }

    @Test
    fun `rm NOT_EXISTING_FILE returns non-zero exit code`() = runBlockingTest {
        val result = instance.run("rm", "NOT_EXISTING_FILE")
        kotlin.test.assertNotEquals(0, result.exitCode)
    }

    @Test
    fun `sleep 5 can be cancelled`() = runBlockingTest {
        val durationMillis = measureTimeMillis {
            val job = launch {
                instance.run("sleep", "5")
            }
            job.cancelAndJoin()
        }

        kotlin.test.assertTrue(durationMillis < 1000)
    }
}

