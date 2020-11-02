package com.training.crowdfunder

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.training.crowdfunder.viewmodels.UserProfileViewModel
import com.training.crowdfunder.viewmodels.UserViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

class UserProfileActivity : AppCompatActivity() {

    lateinit var fullNameTV: TextView
    lateinit var emailTV: TextView
    lateinit var profileImg: ImageView

    @ExperimentalCoroutinesApi
    private lateinit var userViewModel: UserProfileViewModel
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    companion object {
        const val SELECTED_CAMPAIGN_OWNER_ID = "EXTRA_SELECTED_CAMPAIGN_OWNER_ID"
    }

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(findViewById(R.id.toolbar))

        val userId = intent.getStringExtra(SELECTED_CAMPAIGN_OWNER_ID)!!

        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        userViewModel = ViewModelProvider(this, UserViewModelFactory(userId))
            .get(UserProfileViewModel::class.java)

        fullNameTV = findViewById<TextView>(R.id.user_profile_name)
        emailTV = findViewById<TextView>(R.id.user_profile_emailTV)
        profileImg = findViewById<ImageView>(R.id.user_profile_img)

        userViewModel.user.observe(this, Observer<User> { user ->
            fullNameTV.text = user.fullName
            emailTV.text = user.email

            val ref = storage.reference
                .child("user_imgs")

            Glide.with(this)
                .load(ref.child(user.profile_image))
                .into(profileImg)
            
        })

//        val user = FirebaseAuth.getInstance().currentUser
//        user?.let {
//            fullNameTV.text = user.displayName
//            emailTV.text = user.email
//        }
//

    }
}