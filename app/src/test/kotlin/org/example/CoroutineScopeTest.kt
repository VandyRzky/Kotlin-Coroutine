package org.example

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import java.util.Date
import java.util.concurrent.Executors

// ========= Coroutine Scope ==========
// tempat menjalankan coroutine

class CoroutineScopeTest {

    suspend fun getFoo() : Int{
        delay(1000)
        return 10
    }

    suspend fun getBar() : Int{
        delay(1000)
        return 10
    }

    suspend fun getSum(): Int = coroutineScope {
        val foo = async { getFoo() }
        delay(1000)
        val bar = async { getBar() }
        foo.await() + bar.await()
    }

    @Test
    fun newScopeTest(){
        val newScope = CoroutineScope(Executors.newSingleThreadExecutor().asCoroutineDispatcher())
        newScope.launch {
            delay(1000)
            println("Hello from : ${Thread.currentThread().name}, ${Date()}")
        }
        newScope.launch {
            delay(2000)
            println("World from : ${Thread.currentThread().name}, ${Date()}")
        }
        runBlocking {
            delay(1500)
            newScope.cancel()
        }
    }

    @Test
    fun coroutineScopeFuncTest(){
        val scope = CoroutineScope(Dispatchers.IO)
        val hasil = scope.async { getSum() }
        scope.launch {
            println(hasil.await())
        }
        runBlocking {
            delay(3000)
        }
    }

    @Test
    fun coroutineScopeParentChildTest(){
        val parentScope = CoroutineScope(Dispatchers.IO)
        val job = parentScope.launch { //parent scope
            println("Hello from parent scope ${Thread.currentThread().name}")
            delay(1000)
            coroutineScope { //child scope
                launch {
                    println("Hello from child scope ${Thread.currentThread().name}")
                }
            }
        }

        runBlocking {
            job.join()
//            job.cancelAndJoin()
            delay(1000)
        }
    }

    @Test
    fun coroutineParentChildTest(){
        val scope = CoroutineScope(Dispatchers.IO)
        val job = scope.launch {
            launch { // child coroutine
//                delay(1000)
                println("Child 1, ${Thread.currentThread().name}, ${Date()}")
            }
            launch {
//                delay(1000)
                println("Child 2, ${Thread.currentThread().name}, ${Date()}")
            }
            println("Parent, ${Thread.currentThread().name}, ${Date()}")
        }

        runBlocking {
            job.join()
        }
    }

}