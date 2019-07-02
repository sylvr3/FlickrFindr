package com.bleacherreport.flickrfindr.utils

import android.os.Handler
import android.os.Looper

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 *
 * This class provides a threading tool for the app to handle operations and functionality that require separate thread
 * This was inspired by Google Samples project for executors that are specific to Android and not just for Java: https://github.com/googlesamples/android-architecture-components/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/AppExecutors.kt
 * @property diskIO handles the disk I/O
 * @property networkIO handles the network I/O
 * @property onMainThread handles the executions on the worker thread
 * @property offMainThread handles the executions off the worker thread
 * @constructor creates an instance of the AppExecutors class
 *
 *
 * */


class AppExecutors private constructor(private val diskIO: Executor, private val networkIO: Executor, private val onMainThread: Executor, private val offMainThread: Executor) {

    fun diskIO(): Executor {
        return diskIO
    }

    private fun onMainThread(): Executor {
        return onMainThread
    }

    private fun networkIO(): Executor {
        return networkIO
    }

    private fun offMainThread(): Executor {
        return offMainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }

    companion object {

        // For Singleton instantiation
        private val LOCK = Any()
        private var sInstance: AppExecutors? = null

        val instance: AppExecutors?
            get() {
                if (sInstance == null) {
                    synchronized(LOCK) {
                        sInstance = AppExecutors(Executors.newSingleThreadExecutor(),
                                Executors.newFixedThreadPool(3),
                                MainThreadExecutor(),
                                Executors.newSingleThreadExecutor())
                    }
                }
                return sInstance
            }
    }
}