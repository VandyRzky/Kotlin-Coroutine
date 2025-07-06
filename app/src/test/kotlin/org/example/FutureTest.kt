package org.example

// ============ submit vs execute ===============
//submit digunakan untuk menjalankan Callable
//execute digunakan untuk menjalankan Runnable

// ============= Runnable vs Callable =============
// Runnable baris kode yang tidak akan mengembalikan return value ketika dijalankan
// Callable baris kode yang akan mengembalikan return value berupa Future


import org.junit.jupiter.api.Test
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

class FutureTest {

    val executorService = Executors.newFixedThreadPool(10)

    fun getFoo():Int{
        Thread.sleep(1000)
        return 10
    }


    fun getBar():Int{
        Thread.sleep(1000)
        return 10
    }

    @Test
    fun futureTest(){ //future: return value dari hasil callable
        val foo: Future<Int> = executorService.submit(Callable { getFoo() }) // submit menggunakan callable
        val bar: Future<Int> = executorService.submit(Callable { getBar() })

        val result = foo.get() + bar.get()

        println(foo)
        println(bar)

        println(result)


    }
}