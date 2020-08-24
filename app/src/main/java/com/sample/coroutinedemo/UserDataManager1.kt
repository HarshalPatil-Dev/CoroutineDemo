package com.sample.coroutinedemo

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class UserDataManager1 {

    var count = 0
    lateinit var deferred: Deferred<Int>
    suspend fun getTotalUserCount(): Int {

        //Structured concurrency gurantees to complete all the works started by coroutines within the child scope before the return of the suspending function
        //This will create child scope coroutine. Start with small c.

        coroutineScope {

            launch(IO) {  //If not provide any distpacher here it will use parent dispatcher
                delay(1000)
                count = 50
            }

            deferred =
                async(Dispatchers.IO) {
                    delay(3000)
                    return@async 70
                }
        }
        return count+deferred.await()
    }

}