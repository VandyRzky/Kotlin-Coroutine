package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.jupiter.api.Test

// ============== Flow ============
// Collection dalam coroutine, digunakan untuk menyimpan data yang dibuat di dalam coroutine

// flow bersifat lazy


class FlowTest {

    fun numberFlow(): Flow<Int> = flow {
        repeat(50){
            emit(it)
        }
    }

    suspend fun toString(number: Int):String{
        delay(100)
        return "number: $number"
    }

    @Test
    fun flowTest(){
        val list = listOf("a", "b", "c")
        val flow = flow<Pair<Int, String>> { //membuat flow
            repeat(3){
                delay(1000)
                val pair = it to list[it]
                emit(pair) //digunakan untuk menyimpan data
            }
        }

        runBlocking {
            flow.collect{value -> //mengambil data dari dalam flow
                println("${value.first} : ${value.second}")
            }

        }
    }

    @Test
    fun flowOperatorTest(){
        val flow = numberFlow()
        runBlocking {
            flow.filter { it % 2 == 0 }
                .map { toString(it) }
                .collect{
                    println(it)
                }
        }
    }

    @Test
    fun flowExceptionTest(){
        val flow = numberFlow()
        runBlocking {
            flow.map { check(it < 10); it}
                .onEach { println(it) }
                // untuk menggantikan catch-finally
                .catch { println("${it.message}") } //operator untuk menangkap exception (catch)
                .onCompletion { println("done") } // untuk finally
                .collect()
            //mengcancel flow
//            flow.onEach {
//                if (it > 10) cancel() //untuk membatalkan flow
//                else println(it)
//            }.collect()
        }
    }

    @Test
    fun sharedFlowTest(){
        // membuat flow dengan lebih dari 1 receiver
        val scope = CoroutineScope(Dispatchers.IO)
        val sharedFlow = MutableSharedFlow<Int>()
        runBlocking {
            scope.launch {
                repeat(10){
                    delay(1000)
                    sharedFlow.emit(it)
                }
            }
            scope.launch {
                sharedFlow.asSharedFlow().collect{
                    delay(2000)
                    // akan ketinggalan data karene berjalan lebih lambat dari pada sender
                    println("Shared flow 1 receive $it")
                }
            }
            scope.launch {
                sharedFlow.asSharedFlow().collect{
                    delay(200)
                    println("Shared flow 2 receive $it")
                }
            }
            delay(21_000)
            scope.cancel()
        }
    }

    @Test
    fun stateFlowTest(){
        //seperti shared flow, akan tetapi jika sender mengirim data terlalu cepat
        //maka receiver hanya akan menerima data yang paling akhir dikirim
        val scope = CoroutineScope(Dispatchers.IO)
        val stateFlow = MutableStateFlow<Int>(value = 0)
        runBlocking {
            scope.launch {
                repeat(10){
                    delay(100)
                    stateFlow.emit(it)
                }
            }
            scope.launch {
                stateFlow.asSharedFlow().collect{
                    delay(500)
                    // akan ketinggalan data karene berjalan lebih lambat dari pada sender
                    println("Shared flow 1 receive $it")
                }
            }
            scope.launch {
                stateFlow.asSharedFlow().collect{
                    delay(250)
                    println("Shared flow 2 receive $it")
                }
            }
            delay(5000)
            scope.cancel()
        }
    }

}