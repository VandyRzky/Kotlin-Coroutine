package org.example

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.Date
import java.util.concurrent.Executors

// ======== Coroutine Dispatcher =========
// digunakan untuk menentukan thread mana yang bertanggung jawab untuk mengeksekusi coroutine

class CoroutineDispatcherTest {

    @Test
    fun dispatcherTest(){
        // membuat coroutine dispatcher
        val dispatcherA = Executors.newFixedThreadPool(5).asCoroutineDispatcher()
        val dispatcherB = Executors.newFixedThreadPool(5).asCoroutineDispatcher()
        runBlocking {
            val job1 = GlobalScope.launch(dispatcherA) {
                println("Hello form job 1, ${Thread.currentThread().name}, ${Date()}")
            }
            val job2 = GlobalScope.launch(dispatcherB) {
                println("Hello form job 2, ${Thread.currentThread().name}, ${Date()}")
            }
            val job3 = GlobalScope.launch(dispatcherA) {
                println("Hello form job 3, ${Thread.currentThread().name}, ${Date()}")
            }

            joinAll(job1, job2, job3)
        }
    }

    @Test
    fun dispatcherWithContext(){
        val dispatcherA = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val dispatcherB = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        runBlocking {
            val job = GlobalScope.launch(dispatcherA) {
                println("Hello from thread: ${Thread.currentThread().name}")
                withContext(dispatcherB){ // digunakan untuk menjalankan coroutine lain dalam coroutine
                    println("Hello from thread: ${Thread.currentThread().name}")
                }
                println("Hello from thread: ${Thread.currentThread().name}")
            }

            job.join()
        }
    }
}