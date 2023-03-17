package com.example.workmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.constraintlayout.widget.Constraints
import androidx.work.*
import com.example.workmanager.databinding.ActivityMainBinding
import com.example.workmanager.worker.MyWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnOneTimeWork.setOnClickListener {
                myOneTimeWork()
            }
            btnPeriodicWork.setOnClickListener {
                myPeriodicWork()
            }
        }
    }

    private fun myOneTimeWork() {

        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val myWorkRequest: OneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueue(myWorkRequest)
    }

    private fun myPeriodicWork() {

        val constraints = androidx.work.Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .build()

        val myWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(MyWorker.WORK_NAME)
                .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                MyWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                myWorkRequest
            )
        Log.i(
            "synchronize_items_work",
            "WorkManager: Periodic Work request for synchronization of items is scheduled"
        )
    }
}