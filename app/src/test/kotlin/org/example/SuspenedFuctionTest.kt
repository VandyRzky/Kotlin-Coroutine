package org.example

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import java.util.Date

// =========== parallel vs concurrency ===============
// parallel = menjalankan beberapa proses secara berbarengan menggunakan thread yang berbeda
// concurrency = menjalankan beberapa proses secara bergantian

// ========== coroutine vs Thread =============
// coroutine lebih ringan dari pada thread
// coroutine dijalankan di Thread

// ========= Job ==========
// return value dari coroutine ketika dijalankan dengan fungsi launch

class SuspenedFuctionTest {

    suspend fun helloWorld(){
        println("Hello: ${Date()}")

        delay(2000)

        println("Hello: ${Date()}")
    }

    @Test
    fun helloWorldTest(){
        runBlocking { // cara sementara untuk memanggil suspened function
            // !!note: bukan untuk digunakan secara umum, biasanya pake launch
            helloWorld()
        }
    }

    @Test
    fun coroutineTest(){
        GlobalScope.launch {
            helloWorld()
        }

        runBlocking {
            delay(3000)
        }
    }


}