package com.dicoding.picodiploma.loginwithanimation.view.main

import com.dicoding.picodiploma.loginwithanimation.data.domain.Result
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.viewmodel.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.adapter.StoryAdapter
import com.dicoding.picodiploma.loginwithanimation.view.addstory.AddStoryActivity
import com.dicoding.picodiploma.loginwithanimation.view.detail.DetailActivity
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        storyAdapter = StoryAdapter()

        mainViewModel = ViewModelProvider(this, ViewModelFactory(this))[MainViewModel::class.java]

        getRecycle()
//        setupView()
        setupViewModel()
        onClickCallback()
    }

    private fun getRecycle() {
        storyAdapter = StoryAdapter()
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
            adapter = storyAdapter
        }
        supportActionBar?.title = "Daftar Cerita"

        mainViewModel.story.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }
    }

//    private fun setupView() {
//        @Suppress("DEPRECATION")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.insetsController?.hide(WindowInsets.Type.statusBars())
//        } else {
//            window.setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            )
//        }
//        supportActionBar?.title = "Daftar Cerita"
//
//        mainViewModel.story.observe(this) {
//            storyAdapter.submitData(lifecycle, it)
//        }
//    }

    private fun onClickCallback() {
        storyAdapter.setOnItemCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListStoryItem) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_ID, data.id)
                startActivity(intent)
            }
        })
    }

    private fun setupViewModel() {
        lifecycleScope.launch {
            mainViewModel.isLogin().observe(this@MainActivity) { isLogin ->
                if (!isLogin) {
                    val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_story -> {
                val intent = Intent(this@MainActivity, AddStoryActivity::class.java)
                launcherCamera.launch(intent)
                return true
            }
            R.id.maps -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.logout -> {
                mainViewModel.logout()
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
            else -> return true
        }
    }

    private val launcherCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            mainViewModel.refreshData()
        }
    }
}