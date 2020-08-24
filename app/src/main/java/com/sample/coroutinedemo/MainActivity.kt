package com.sample.coroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main


class MainActivity : AppCompatActivity() {
    private var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCount.setOnClickListener {
            tvCount.text = count++.toString()
        }
        btnDownloadUserData.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch { downloadUserData() }

        }

        //Sequential Coroutines Execution...
        btnSequencialExec.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                Log.i("MyTag", "======Sequencial execution started...========")
                var stock1 = getStock1()
                var stock2 = getStock2()
                var total = stock1 + stock2
                Log.i("MyTag", "Total is $total")

                //Background thread to main thread
                withContext(Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Sequencial Execution Total : $total",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }

        //Parallel Coroutines Execution...
        btnParallelExec.setOnClickListener {
            CoroutineScope(Main).launch {
                Log.i("MyTag", "=====Parallel Execution Started...==========")

                var stock1 = async(IO) { getStock1() }
                var stock2 = async(IO) { getStock2() }

                var total = stock1.await() + stock2.await()
                Log.i("MyTag", "Total is $total")
                Toast.makeText(this@MainActivity,"Parallel Execution Total : $total",Toast.LENGTH_SHORT).show()

            }
        }

        //Unstructured Concurrency
        btnUnstrucredConcurrency.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                tvConcurrencyCount.text = UserDataManager().getTotalUserCount().toString()
            }
        }
        //Structured Concurrency
        btnStructuredConcurrncy.setOnClickListener {
            CoroutineScope(Main).launch {
                tvConcurrencyCount.text = UserDataManager1().getTotalUserCount().toString()
            }
        }


    }

    private suspend fun downloadUserData() {
        withContext(Dispatchers.Main) {
            for (i in 1..200) {
                textView3.text = "Downloading user  $i in ${Thread.currentThread().name}"
                delay(500)
            }

        }
    }
    private suspend fun getStock1():Int{
        delay(10000)
        Log.i("MyTag","Stock 1 Returned")
        return 50000
    }

    private suspend fun getStock2():Int{
        delay(5000)
        Log.i("MyTag","Stock 2 Returned")
        return 25000
    }



}