package com.example.habitstracker

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.entity.User
import com.example.habitstracker.ui.fragment.HabitFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    HabitFragment()
                )
                .commit()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }

        insertTestUserIfNeeded()
    }

    private fun insertTestUserIfNeeded() {

        lifecycleScope.launch {

            val db = HabitsTrackerDatabase.getInstance(this@MainActivity)

            val existingUser = db.userDao()
                .getCurrentUser()
                .first()   // LẤY VALUE THẬT

            if (existingUser == null) {

                val userId = db.userDao().insertUser(
                    User(
                        name = "Hoang",
                        gender = "Male",
                        birthDate = "2003-01-01",
                        height = 170f,
                        avatar = null,
                        createdAt = "2026-08-07"
                    )
                )

                Log.d("ROOM_TEST", "Inserted User ID = $userId")

            } else {

                Log.d("ROOM_TEST", "User already exists. Skip insert.")
            }
        }
    }
}