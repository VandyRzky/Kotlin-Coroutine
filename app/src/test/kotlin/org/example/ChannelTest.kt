package org.example

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

// ============ Channel ==============
// digunakan untuk mentransfer aliran data dari satu tempat ke tempat lain

class ChannelTest {

    @Test
    fun makeChannelTest(){
        val channel = Channel<Int>()
        runBlocking {
            val job = launch {
                println("Send: 2")
                channel.send(2) // mengirim
                println("Send: 1")
                channel.send(1)
            }

            val job2 = launch {
                println("Receive channel: ${channel.receive()}") //menerima
                println("Receive channel: ${channel.receive()}")
            }
            joinAll(job, job2)
            channel.close() //menutuo channel
        }
    }

    @Test
    fun channelUnlimitedTest(){
//      channel dapat menampung semua data yang dikirim tanpa harus menunggu ada penerima terlebih dahulu.
        // umumnya channel selalu menunggu ada penerima (receive)
        val channel = Channel<Int>(capacity = Channel.UNLIMITED)
        runBlocking {
            val job = launch {
                println("Send: 2")
                channel.send(2) // mengirim
                println("Send: 1")
                channel.send(1)
            }

            val job2 = launch {
                println("Receive channel: ${channel.receive()}") //menerima
                println("Receive channel: ${channel.receive()}")
            }
            joinAll(job, job2)
            channel.close() //menutuo channel
        }
    }

    @Test
    fun channelConflatedTest(){
        //hanya menyimpan nilai terbaru yang dikirim.
        val channel = Channel<Int>(capacity = Channel.CONFLATED)
        runBlocking {
            val job = launch {
                println("Send: 2")
                channel.send(2) // mengirim
                println("Send: 1")
                channel.send(1) // yang disimpan
            }
            joinAll(job)
            val job2 = launch {
                println("Receive channel: ${channel.receive()}") //menerima
            }
            joinAll(job2)
            channel.close() //menutuo channel
        }
    }

    @Test
    fun channelBufferOverflow(){
        //membuat kapasitas channel:5, jika kapasistas penuh data paling lama akan dibuang
        val channel = Channel<Int>(capacity = 5, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        runBlocking {
            val job = launch {
                repeat(10){
                    channel.send(it)
                }
            }
            joinAll(job)
            val job2 = launch {
                repeat(5){
                    println("Receive: ${channel.receive()}")
                }
            }
            joinAll(job2)
            channel.close() //menutuo channel
        }
    }

    @Test
    fun testChannelUndelivered(){
        val channel = Channel<Int>(Channel.UNLIMITED){ value ->
            println("Undelivered value $value")
        }
        runBlocking {
            val job = launch {
                repeat(10){
                    channel.send(it)
                }
            }
            channel.close()
            joinAll(job)
        }
    }

    @Test
    fun produceFunctionOnChannelTest(){
        val scope = CoroutineScope( Executors.newSingleThreadExecutor().asCoroutineDispatcher() )
        val channel: ReceiveChannel<Int> = scope.produce (capacity = 10){
            repeat(10){
                println("Send: $it")
//                delay(500)
                send(it)}
        }
        runBlocking {
            val job = launch {
                repeat(10){
                    println("Receive: ${channel.receive()}")
//                delay(500)
                }
            }
            joinAll(job)
        }
    }
}