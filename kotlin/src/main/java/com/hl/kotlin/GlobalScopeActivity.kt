package com.hl.kotlin

import android.os.Bundle
import com.hl.base.BaseActivity
import kotlinx.coroutines.*

/**
 * 协程
 */
class GlobalScopeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kotlin_activity_main)
        initToolbar(true)

        test1()
        test2()
        test3()
        test4()
        test5()
    }

    private fun test1() {
        GlobalScope.launch {
            // 创建并启动一个协程
            delay(1000L) // 延迟（挂起）1000毫秒，注意这不会阻塞线程
            println("World!") //延迟之后执行打印
        }
        println("Hello,") // 协程延迟的时候不会影响主线程的执行
        Thread.sleep(2000L) // 阻塞线程2s，保证JVM存活，协程可正常执行完
    }

    // 延迟执行
    private fun test2() {
        val job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            println("World!")
        }
        println("Hello,")
        job.start()
        Thread.sleep(2000L)
    }

    // cancel 取消
    private fun test3() {
        val job = GlobalScope.launch() {
            println("World!")
            delay(1000L)
        }
        job.cancel()
        println("Hello,")
        Thread.sleep(2000L)
    }

    // join()等待协程执行完毕
    private fun test4() = runBlocking {
        val job = GlobalScope.launch {
            delay(1000L)
            println("World!")
            delay(1000L)
        }
        println("Hello,")
        job.join()
        println("Good！")
    }

    // 子协程
    private fun test5() = runBlocking {
        GlobalScope.launch {
            delay(1000L)
            println("World!")
        }
        println("Hello,")
        runBlocking {
            delay(2000L)
        }
    }
}