package org.example

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.Date
import java.util.concurrent.Executors


// ========= Job ==========
// return value dari coroutine ketika dijalankan dengan fungsi launch

// Job dapat digunakan untuk membatalkan coroutine

class JobTest {

    @Test
    fun jobStartTest(){
        runBlocking {
            val job:Job = GlobalScope.launch( //Job secara default akan lansung dijalankan
                start = CoroutineStart.LAZY // LAZY digunakan agar job tidak langsung dijalankan
            ) {
                delay(1_000)
                println("Hello")
            }

            job.start() //untuk menjalankan Job
//            job.join() //join digunakan  untuk menunggu job hingga selesai dijalankan
//            job.cancel() //untuk menghentikan Job

            delay(1_500) //agar coroutine yang sudah dibuat dapat dijalankan
        }
    }


    @Test
    fun joinAllJobTest(){
        runBlocking {
            val job1: Job = GlobalScope.launch {
                delay(1000)
                println("Hello from job 1: ${Date()}, ${Thread.currentThread().name}")
            }
            val job2: Job = GlobalScope.launch {
                delay(1000)
                println("Hello form job 2: ${Date()}, ${Thread.currentThread().name}")
            }

            //join digunakan  untuk menunggu job hingga selesai dijalankan oleh coroutine
            joinAll(job1, job2) // digunakan untuk menjalankan seluruh job yang sudah dibuat

//            delay(1500)
        }
    }

    @Test
    fun cantCancelJobTest(){
        runBlocking {
            val job: Job = GlobalScope.launch {
                println("Hello ${Date()}")
                Thread.sleep(1_000) //penyebab job tidak dapat dibatalkan
                println("Hello ${Date()}")
            }

            job.cancel()
            job.join()
        }
    }

    @Test
    fun cancelableJobTest(){
        runBlocking {
            val job: Job = GlobalScope.launch {
                if (!isActive) throw CancellationException()
                println("Hello ${Date()}")      // 1️⃣ Dicetak

                ensureActive()                  // 2️⃣ Masih aktif, lanjut

                Thread.sleep(1000)             // 3️⃣ Thread diblok selama 1 detik

                ensureActive()                  // 4️⃣ Baru dicek lagi SETELAH sleep selesai

                println("Hello ${Date()}")      // 5️⃣ Mungkin masih dicetak
            }


            job.cancel()
            job.join()
        }
    }

    @Test
    fun cancelFinallyTest(){
        runBlocking {
            val job: Job = GlobalScope.launch {
                try {
                    println("Hello ${Date()}")
                    delay(1000) //setelah bagian ini coroutin mengecek apakah telah di cancel
                    println("Hello ${Date()}")

                }
//                catch (e: CancellationException){
//                    println("Canceled")
//                }
                finally {
                    println("Hello from finally")

                }
            }

            job.cancelAndJoin()
        }
    }

    @Test
    fun timeoutTest(){
        //timeout digunakna untuk menghentikan coroutine agar tidak berjalan melebihi waktu yg semestinya
        //timeout akan menghasilkan TimeoutCancellationException
        runBlocking {
            val job: Job = GlobalScope.launch {
                withTimeout(5000){
                    repeat(1000){
                        println("Hello from: ${Date()}")
                        delay(1000)
                    }
                }
            }
            job.join()
        }
    }

    @Test
    fun timeoutOrNull(){
        //untuk menghentikan coroutine tanpa menghasilkan TimeoutCancellationException
        runBlocking {
            val job: Job = GlobalScope.launch {
                withTimeoutOrNull(5000){
                    repeat(1000){
                        println("Hello from: ${Date()}")
                        delay(1000)
                    }
                }
            }
            job.join()
        }
    }


    @Test
    fun supervisorJobTest(){
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

        //dengan supervisor job, child coroutine gagal tanpa membatalkan sibling (saudara) coroutine lainnya.
        val scope = CoroutineScope(dispatcher + SupervisorJob())

        //tanpa supervisor job, seluruh child coroutine akan berhenti jika ada error salah satu sibling
//        val scope = CoroutineScope(dispatcher)

        val job = scope.launch {
            delay(2000)
            println("Hello")
        }
        val job2 = scope.launch {
            delay(1000)
            throw IllegalArgumentException()
        }
        runBlocking {
            joinAll(job, job2)
        }
    }

    @Test
    fun supervisorJobScopeFuncTest(){
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)

        runBlocking {
            scope.launch {
                supervisorScope { //digunakan ketika tidak bisa mengubah kondisi coroutine scope
                    // coroutine tetap dijalankan walaupun error
                    launch {
                        delay(2000)
                        println("Hello")
                    }
                    launch {
                        delay(1000)
                        throw IllegalArgumentException()
                    }
                }
                delay(1000)
                println("World")
            }
            delay(3000)
        }
    }

}