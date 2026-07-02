package com.example.habitstracker

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.habitstracker.data.database.HabitsTrackerDatabase
import com.example.habitstracker.data.entity.WorkoutType
import com.example.habitstracker.data.repository.UserRepository
import com.example.habitstracker.databinding.ActivityMainBinding
import com.example.habitstracker.ui.fragment.CreateProfileFragment
import com.example.habitstracker.ui.fragment.DashboardFragment
import com.example.habitstracker.ui.fragment.HabitFragment
import com.example.habitstracker.ui.fragment.HealthFragment
import com.example.habitstracker.ui.fragment.ProfileFragment
import com.example.habitstracker.ui.fragment.WorkoutFragment
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }

        insertWorkoutTypesIfNeeded()

        if (savedInstanceState == null) {
            lifecycleScope.launch {
                val repository = UserRepository(
                    HabitsTrackerDatabase
                        .getInstance(this@MainActivity)
                        .userDao()
                )
                val fragment =
                    if (repository.getUserCount() == 0) {
                        CreateProfileFragment()
                    } else {
                        DashboardFragment()
                    }

                supportFragmentManager
                    .beginTransaction()
                    .replace(
                        R.id.fragmentContainer,
                        fragment
                    )
                    .commit()

            }
        }
        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.nav_dashboard -> {

                    openFragment(DashboardFragment())

                    true
                }

                R.id.nav_habit -> {

                    openFragment(HabitFragment())

                    true
                }

                R.id.nav_workout -> {

                    openFragment(WorkoutFragment())

                    true
                }

                R.id.nav_health -> {

                    openFragment(HealthFragment())

                    true
                }

                R.id.nav_profile -> {

                    openFragment(ProfileFragment())

                    true
                }

                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.fragmentContainer,
                fragment
            )
            .commit()
    }

    private fun insertWorkoutTypesIfNeeded() {

        lifecycleScope.launch {

            val dao = HabitsTrackerDatabase
                .getInstance(this@MainActivity)
                .workoutDao()

            if (dao.getAllWorkoutTypes().first().isEmpty()) {

                dao.insertWorkoutType(
                    WorkoutType(
                        name = "Running",
                        category = "Cardio"
                    )
                )

                dao.insertWorkoutType(
                    WorkoutType(
                        name = "Walking",
                        category = "Cardio"
                    )
                )

                dao.insertWorkoutType(
                    WorkoutType(
                        name = "Cycling",
                        category = "Cardio"
                    )
                )

                dao.insertWorkoutType(
                    WorkoutType(
                        name = "Push Up",
                        category = "Strength"
                    )
                )

                dao.insertWorkoutType(
                    WorkoutType(
                        name = "Pull Up",
                        category = "Strength"
                    )
                )

                dao.insertWorkoutType(
                    WorkoutType(
                        name = "Squat",
                        category = "Strength"
                    )
                )

                dao.insertWorkoutType(
                    WorkoutType(
                        name = "Plank",
                        category = "Core"
                    )
                )

                dao.insertWorkoutType(
                    WorkoutType(
                        name = "Yoga",
                        category = "Flexibility"
                    )
                )

                Log.d("ROOM_TEST", "Workout types inserted.")

            } else {

                Log.d("ROOM_TEST", "Workout types already exist. Skip insert.")
            }
        }
    }
}