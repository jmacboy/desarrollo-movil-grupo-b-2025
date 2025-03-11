package com.example.practicaintents

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {
    private lateinit var btnOpenThirdActivity: Button
    private lateinit var txtName: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnOpenThirdActivity = findViewById(R.id.btnOpenThirdActivity)
        txtName = findViewById(R.id.txtName)
        setupEventListeners()
    }

    private fun setupEventListeners() {
        btnOpenThirdActivity.setOnClickListener {
            val name = txtName.text.toString()
            val intent = Intent(this, ThirdActivity::class.java)
            intent.putExtra("username", name);
            startActivity(intent);
        }
    }
}