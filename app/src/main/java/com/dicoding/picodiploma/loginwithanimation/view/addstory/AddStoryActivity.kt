package com.dicoding.picodiploma.loginwithanimation.view.addstory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.loginwithanimation.R

class AddStoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)

        supportActionBar?.title = "Tambah Cerita"
    }
}