package org.example

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.Date
import kotlin.system.measureTimeMillis

// =========== parallel vs concurrency ===============
// parallel = menjalankan beberapa proses secara berbarengan menggunakan thread yang berbeda
// concurrency = Menjalankan beberapa tugas dalam waktu yang sama secara logis

// ========== coroutine vs Thread =============
// coroutine lebih ringan dari pada thread
// coroutine dijalankan di Thread

// ========= Job ==========
// return value dari coroutine ketika dijalankan dengan fungsi launch


// ========= Deferred ==========
// return value dari coroutine ketika dijalankan dengan fungsi async

class SuspenedFuctionTest {

    suspend fun helloWorld(){
        println("Hello: ${Date()}")

        delay(2000)

        println("Hello: ${Date()}")
    }

    suspend fun getFoo(): Int{
        delay(1_000)
        return 10
    }

    suspend fun getBar(): Int{
        delay(1_000)
        return 10
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


    @Test
    fun sequentialTest(){
        runBlocking {
            val total = measureTimeMillis {
                //memanggil fungsi secara sequential
                getBar()
                getFoo()
            }

            println("Total waktu:  $total")
        }
    }

    @Test
    fun concurrencyLaunchTest(){
        runBlocking {
            val total = measureTimeMillis {
                //menggakses secara concurrency
                val job1: Job = GlobalScope.launch { getBar() }
                val job2: Job = GlobalScope.launch { getFoo() }

                joinAll(job1, job2)

            }

            println("Total waktu:  $total") //waktu lebih cepat dibanding secara sequential
        }
    }

    @Test
    fun asyncTest(){
        runBlocking {
            val foo = GlobalScope.async { getFoo() }
            val bar = GlobalScope.async { getBar() }

            val total = foo.await() + bar.await()
            // await digunakan untuk menunggu coroutine dan mengambil hasilnya

            val total2 = awaitAll(foo, bar).sum()

            println(total)
            println(total2)
        }
    }



}