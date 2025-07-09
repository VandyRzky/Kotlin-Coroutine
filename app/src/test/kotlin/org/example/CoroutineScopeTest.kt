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
        val job = scope.launch (Dispatchers.IO + CoroutineName("Parent Test")){
        // ARGS: menentukan coroutine dispatcher dan memberi nama coroutine
            launch { // child coroutine
                delay(1000)
                println("Child 1, ${Thread.currentThread().name}, ${Date()}")
            }
            launch {
                delay(3000)
                println("Child 2, ${Thread.currentThread().name}, ${Date()}")
            }
            println("Parent, ${Thread.currentThread().name}, ${Date()}")

        }

        runBlocking {
//            job.cancelChildren() //digunakan untuk mengcancel child coroutine

            job.join()
        }
    }

    suspend fun runJob (number:Int){
        println("Start Job $number, ${Thread.currentThread().name}, ${Date()}")
        yield() // coroutine menyerahkan kontrol sementara ke dispatcher agar coroutine lain bisa dijalankan.
//        delay(1_500)
        println("Finish Job $number, ${Thread.currentThread().name}, ${Date()}")
    }

    @Test
    fun yieldFunctionTest(){
        val dispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        val scope = CoroutineScope(dispatcher)
        val name = CoroutineName("Yield Coroutine")
//        val job = scope.launch( name ) {
//            runJob(1)
//            runJob(2)
//        }

        runBlocking {
            scope.launch (name) { runJob(1) }
            scope.launch (name) { runJob(2) }

            delay(5_000)
        }

    }


}