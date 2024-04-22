package com.example.mykotlincorutine

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mykotlincorutine.ui.theme.MyKotlinCorutineTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    var scope = CoroutineScope(Job())
    val thread_context = newSingleThreadContext("Custom Thread")

    companion object{
        const val THIS_FILE = "MainActivity"
        var MMMM:String = "MMMM"
    }

    suspend fun mydelay(delay: Int): Int {
        Log.e(THIS_FILE,"inside 0 mydelay:${delay} ${Thread.currentThread().name}")
        delay(delay.toLong())
        Log.e(THIS_FILE,"inside 1 mydelay:${delay} ${Thread.currentThread().name}")
        return delay
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_activity)
        val btn_num1 = findViewById<Button>(R.id.btn_num1)
        btn_num1.setOnClickListener { v ->
            val txt = (v as Button).text.toString()
            when (txt) {
                "btn_num1" -> (v as Button).text = "btn_num1_clicked"
                else -> (v as Button).text = "btn_num1"
            }
            //runBlocking {
            Log.e(THIS_FILE, (v as Button).text.toString())
            scope = CoroutineScope(Job())
            Log.e(THIS_FILE, "before scope.isActive:" + scope.isActive.toString())
            var rr = "RRRRRRR"
            runBlocking {
                Log.e(THIS_FILE, "runBlocking : ${Thread.currentThread().name}")
                val job1 = scope.launch(thread_context) {
                    //scope.launch(thread_context) {
                    withContext(Dispatchers.IO){
                        rr = "qqqqqqqqqqqqqq"
                        Log.e(THIS_FILE, "launch rr: ${rr} ${Thread.currentThread().name}")
                        val mydel1 = mydelay(3000)
                        Log.e(THIS_FILE, "launch mydel1: ${mydel1} ${Thread.currentThread().name}")
                        val mydel2 = mydelay(4000)
                        Log.e(THIS_FILE, "launch mydel2: ${mydel2} ${Thread.currentThread().name}")
                        //Thread.sleep(10000)
                        Log.e(THIS_FILE, "launch after Корутина выполняется на потоке: mydel1: ${mydel1} mydel2: ${mydel2} ${Thread.currentThread().name}"
                        )
                    }
                    //ob1.join()
                    Log.e(THIS_FILE, "in last runBlocking:  ${Thread.currentThread().name}")
                }
                Log.e(THIS_FILE, "out runBlocking:  ${Thread.currentThread().name}")
                Log.e(THIS_FILE, "  Функция main rr ${rr} выполняется на потоке: ${Thread.currentThread().name} "
                )
                Log.e(THIS_FILE, "scope.isActive:" + scope.isActive.toString())
                Log.e(THIS_FILE, "end run Blocking ${Thread.currentThread().name}")
            }
            Log.e(THIS_FILE, "out setOnClickListener ${Thread.currentThread().name}")
        }

        findViewById<Button>(R.id.btn_cancel).setOnClickListener{ v ->
            val txt = (v as Button).text.toString()
            when (txt){
                "btn_cancel" ->(v as Button).text = "btn_cancel_clicked"
                else -> (v as Button).text = "btn_cancel"
            }
            //scope.cancel()
            //Log.e(THIS_FILE,"scope.cancel()")



            runBlocking {
                Log.e(THIS_FILE,"START runBlocking ${Thread.currentThread().name}")
                val deferred = scope.async {
                    Log.e(THIS_FILE,"child coroutine, start ${Thread.currentThread().name}")
                    TimeUnit.MILLISECONDS.sleep(3000)
                    Log.e(THIS_FILE,"child coroutine, end ${Thread.currentThread().name}")
                    "async result"
                }

                Log.e(THIS_FILE,"parent coroutine, wait until child returns result ${Thread.currentThread().name}")
                val result = deferred.await()
                Log.e(THIS_FILE,"parent coroutine, child returns: $result ${Thread.currentThread().name}")

                Log.e(THIS_FILE,"end RunBlocking ${Thread.currentThread().name}")
            }
            Log.e(THIS_FILE,"End OnClick")
        }



//        setContent {
//            MyKotlinCorutineTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    Greeting("Android")
//                }
//            }
//        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyKotlinCorutineTheme {
        Greeting("Android")
    }
}