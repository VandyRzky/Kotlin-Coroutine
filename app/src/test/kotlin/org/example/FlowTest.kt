package org.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

// ============== Flow ============
// Collection dalam coroutine, digunakan untuk menyimpan data yang dibuat di dalam coroutine

// flow bersifat lazy


class FlowTest {

    fun numberFlow(): Flow<Int> = flow {
        repeat(50){
            emit(it)
        }
    }

    suspend fun toString(number: Int):String{
        delay(100)
        return "number: $number"
    }

    @Test
    fun flowTest(){
        val list = listOf("a", "b", "c")
        val flow = flow<Pair<Int, String>> { //membuat flow
            repeat(3){
                delay(1000)
                val pair = it to list[it]
                emit(pair) //digunakan untuk menyimpan data
            }
        }

        runBlocking {
            flow.collect{value -> //mengambil data dari dalam flow
                println("${value.first} : ${value.second}")
            }

        }
    }

    @Test
    fun flowOperatorTest(){
        val flow = numberFlow()
        runBlocking {
            flow.filter { it % 2 == 0 }
                .map { toString(it) }
                .collect{
                    println(it)
                }
        }
    }

    @Test
    fun flowExceptionTest(){
        val flow = numberFlow()
        runBlocking {
            flow.map { check(it < 10); it}
                .onEach { println(it) }
                // untuk menggantikan catch-finally
                .catch { println("${it.message}") } //operator untuk menangkap exception (catch)
                .onCompletion { println("done") } // untuk finally
                .collect()
            //mengcancel flow
//            flow.onEach {
//                if (it > 10) cancel() //untuk membatalkan flow
//                else println(it)
//            }.collect()
        }
    }

}