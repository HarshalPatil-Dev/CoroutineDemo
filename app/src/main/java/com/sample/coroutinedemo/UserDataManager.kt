package com.sample.coroutinedemo

import kotlinx.coroutines.*

class UserDataManager {

    //Unstructured concurrency does not guarantee to complete all the task of the suspending function, before it returns.
    //Here it returns 0 not 70
    suspend fun getTotalUserCount() : Int{
        var count = 0
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000)
            count = 50
        }
        val deferred = CoroutineScope(Dispatchers.IO).async {
            delay(5000)
            return@async 70
        }
        return count + deferred.await()
    }
}