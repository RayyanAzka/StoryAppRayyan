package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.viewmodel.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.data.domain.Result
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail Story"

        detailViewModel = ViewModelProvider(this, ViewModelFactory(this))[DetailViewModel::class.java]

        val storyId = intent.getStringExtra(EXTRA_ID)
        storyId?.run(detailViewModel::setStoryId)

        detailViewModel.detailStory.observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> { showLoading(true) }
                is Result.Success -> {
                    showLoading(false)
                    val item = result.data
                    binding.apply {
                        Glide.with(this@DetailActivity)
                            .load(item.photoUrl)
                            .into(binding.imgItemPhoto)
                        tvObjectReceived.text = "Name : ${item.name}\nDescription : ${item.description}\n"
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu_detail, menu)

        return true
    }

    companion object {
        const val EXTRA_ID = "id"
    }
}