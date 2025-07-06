package org.example

// ============ submit vs execute ===============
//submit digunakan untuk menjalankan Callable
//execute digunakan untuk menjalankan Runnable

// ============= Runnable vs Callable =============
// Runnable baris kode yang tidak akan mengembalikan return value ketika dijalankan
// Callable baris kode yang akan mengembalikan return value berupa Future


import org.junit.jupiter.api.Test
import java.util.Date
import java.util.concurrent.Executors

class ExecutorServiceTest {

    //membuat thread dengan menggunakan executor service
    //executor service berfungsi untuk memanajemen thread yang digunakan

    @Test
    fun singleThreadPoolTest(){
        val singleExecutor = Executors.newSingleThreadExecutor() //membuat 1 thread
        (1..10).forEach {
            singleExecutor.execute( Runnable { //execute menggunakan Runnable
                Thread.sleep(1000)
                println("iterasi ke $it, name: ${Thread.currentThread().name}, date ${Date()}")
            })
        }

        Thread.sleep(11000)
    }

    @Test
    fun fixThreadPoolTest(){
        val singleExecutor = Executors.newFixedThreadPool(3) //membuat 3 thread
        (1..10).forEach {
            singleExecutor.execute {
                Thread.sleep(1000)
                println("iterasi ke $it, name: ${Thread.currentThread().name}, date ${Date()}")
            }
        }

        Thread.sleep(11000)
    }

    @Test
    fun cacheThreadPoolTest(){
        //tidak disarankan untuk digunakan
        //boros memori !!!!
        val singleExecutor = Executors.newCachedThreadPool() //jumlah thread ditentukan oleh jvm
        (1..10).forEach {
            singleExecutor.execute (Runnable{
                Thread.sleep(1000)
                println("iterasi ke $it, name: ${Thread.currentThread().name}, date ${Date()}")
            })
        }

        Thread.sleep(11000)
    }
}