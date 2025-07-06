package org.example

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test


// ========= Job ==========
// return value dari coroutine ketika dijalankan dengan fungsi launch

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

            delay(1_500) //agar coroutine yang sudah dibuat dapat dijalankan
        }
    }
}