package com.tma.romanova.core

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.absoluteValue

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val list = (-10..10)
        val numberOfItems = 2
        list.forEach { i->
            val i = (if(i<=0) i % (numberOfItems * 2) else i.inv(numberOfItems))
            when {
                i == 0 -> 0
                (i).absoluteValue == (numberOfItems - 2 + 1) -> numberOfItems + 2 - 1
                i == numberOfItems -> numberOfItems
                i == 1 -> 1
                else -> if (i > 0) i % numberOfItems else (i % numberOfItems) + numberOfItems
            }.also {
                println("$i $it")
            }
        }
    }

    private fun Int.inv(numOfItems: Int): Int{
        val i = this%(numOfItems*2)
        return when{
            i == 0 -> 0
            i < 0 -> i
            i > 0 -> i-(numOfItems*2)
            else -> i
        }
    }
}