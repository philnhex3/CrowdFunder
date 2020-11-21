package com.training.crowdfunder.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.training.crowdfunder.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val auth = FirebaseAuth.getInstance()
        auth.addAuthStateListener {
            val user = it.currentUser
            user?.reload()

            if (user != null){
                val intent = Intent(this, CampaignListActivity::class.java)
                startActivity(intent)
            }
        }
    }   
} 