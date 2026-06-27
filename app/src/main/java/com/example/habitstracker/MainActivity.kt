package com.example.habitstracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.habitstracker.ui.fragment.HabitFragment
import android.os.Build

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

                arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS
                ),

                100
            )
        }
    }
}