package ru.test.testapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.test.testapplication.R

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_TestApplication)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)
    }
}