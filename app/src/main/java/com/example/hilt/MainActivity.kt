package com.example.hilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.hilt.databinding.ActivityMainBinding
import com.example.hilt.utils.UserResource
import com.example.hilt.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            userViewModel.getGithubUsers()
                .collect {
                    when(it) {
                        is UserResource.Loading -> {

                        }
                        is UserResource.Error -> {
                            Log.d(TAG, "onCreate: error")
                        }
                        is UserResource.Success -> {
                            Log.d(TAG, "onCreate: ${it.list}")
                        }
                    }
                }
        }
    }
}