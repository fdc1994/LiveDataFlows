package com.example.livedataflows

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.livedataflows.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnLiveData.setOnClickListener {
                viewModel.triggerLiveData()
            }

            btnStateFlow.setOnClickListener{
                viewModel.triggerStateFlow()
            }

            btnFlow.setOnClickListener{
                lifecycleScope.launch {
                    viewModel.triggerFlow().collectLatest {
                        binding.textViewFlow.text = it
                    }

                }
            }

            btnSharedFlow.setOnClickListener {
                viewModel.triggerSharedFlow()
            }
        }

        subscribeToObservables()
    }


    private fun subscribeToObservables() {
        viewModel.liveData.observe(this) {
            binding.textViewLiveData.text = it
        }

        lifecycleScope.launchWhenStarted {
            viewModel.sharedFlow.collectLatest {
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}