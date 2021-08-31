package com.tma.romanova.presentation

import com.tma.romanova.presentation.feature.main.entity.durationStr
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(
            0L.durationStr, "00:00"
        )
        assertEquals(
            2000L.durationStr, "00:02"
        )
        assertEquals(
            21000L.durationStr, "00:21"
        )
        assertEquals(
            60000L.durationStr, "1:00"
        )
        assertEquals(
            61000L.durationStr, "1:01"
        )
        assertEquals(
            601000L.durationStr, "10:01"
        )
        assertEquals(
            3600000L.durationStr, "1:00:00"
        )
        assertEquals(
            3601000L.durationStr, "1:00:01"
        )
        assertEquals(
            3661000L.durationStr, "1:01:01"
        )
        assertEquals(
            36061000L.durationStr, "10:01:01"
        )
    }
}