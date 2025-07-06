package org.example

import org.junit.jupiter.api.Test
import java.util.*
import kotlin.concurrent.thread

class ThreadTest {

    @Test
    fun threadTest(){
        val runable = {
            println(Date())
            Thread.sleep(2000)
            println("Finish: ${Date()}")
        }

        //thread akan memakan memori
        val threadNew = Thread(runable) //membuat thread baru

        thread(true){ //membuat thread baru dengan helper function
            println("Mulai thread baru: ${Date()}")
            Thread.sleep(2000)
            println("Selesai thread baru: ${Date()}")
        }

        threadNew.start()
//        println("Tunggu")
        Thread.sleep(3_000)
        //menghentikan thread utama untuk beberapa saat
        //agar thread baru dapat dijalankan bersamaan dengan thread utama
        println("Finish: Proses pada seluruh thread selesai")
    }

}