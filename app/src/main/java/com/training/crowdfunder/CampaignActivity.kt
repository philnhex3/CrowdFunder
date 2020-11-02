package com.training.crowdfunder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.training.crowdfunder.User.Companion.toUser
import com.training.crowdfunder.viewmodels.CampaignViewModel
import com.training.crowdfunder.viewmodels.MyViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CampaignActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_CAMPAIGN_ID = "EXTRA_SELECTED_CAMPAIGN_ID"
        const val SELECTED_CAMPAIGN_OWNER_ID = "EXTRA_SELECTED_CAMPAIGN_OWNER_ID"
    }

    private lateinit var titleTV : TextView
    private lateinit var ownerNameTV : TextView
    private lateinit var locationTV : TextView
    private lateinit var descriptionTV : TextView
    private lateinit var profileImg : ImageView
    private lateinit var targetTV : TextView
    private lateinit var raisedTV : TextView
    private lateinit var uid : String
    private lateinit var donateFAB : ExtendedFloatingActionButton
    private lateinit var headerImg : ImageView
    private lateinit var progressBar : ProgressBar
    private var progress : Double = 0.0


    @ExperimentalCoroutinesApi
    private lateinit var campaignViewModel: CampaignViewModel
    private lateinit var storage : FirebaseStorage
    private lateinit var db : FirebaseFirestore

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign)

        val campaignId = intent.getStringExtra(SELECTED_CAMPAIGN_ID)!!

        storage = FirebaseStorage.getInstance()
        db = FirebaseFirestore.getInstance()


        campaignViewModel =
            ViewModelProvider(this, MyViewModelFactory(campaignId)).get(CampaignViewModel::class.java)

        titleTV = findViewById(R.id.campaign_title)
        ownerNameTV = findViewById(R.id.campaignOwnerName)
        locationTV = findViewById(R.id.locationTV)
        descriptionTV = findViewById(R.id.campaignDescription)
        profileImg = findViewById<ImageView>(R.id.campaignProfileImg)
        donateFAB = findViewById(R.id.donate_fab)
        headerImg = findViewById(R.id.campaignHeaderImg)
        progressBar = findViewById(R.id.progressBarCampaignView)
        targetTV = findViewById(R.id.campaign_target_amount)
        raisedTV = findViewById(R.id.campaign_raised_amount)

        campaignViewModel.campaign.observe(this, Observer<Campaign> { campaign ->
            titleTV.text = campaign.title
//            ownerNameTV.text = campaign.ownerId
            locationTV.text = campaign.location
            descriptionTV.text = campaign.body
            targetTV.text = "$${campaign.target}"
            raisedTV.text = "$${campaign.raised}"

            uid = campaign.ownerId

            campaignViewModel.calculateProgress(campaign.target, campaign.raised)

            progress = campaignViewModel.progress

            progressBar.visibility = View.VISIBLE
            progressBar.setProgress(progress.toInt(), true)

            var ref = storage.reference.child("campaignImgs")
            val img = campaign.img

            Glide.with(this)
                .load(ref.child(img))
                .into(headerImg)

            val uid = campaign.ownerId
            val user = db.collection("users")

            var imgLink = ""

            val userItem = user.document(uid).get().addOnCompleteListener {
                val userInfo = it.result!!.toUser()

                ownerNameTV.text = userInfo?.fullName

                if (userInfo != null) {
                    imgLink = userInfo.profile_image

                    ref = storage.reference.child("user_imgs")
                    Glide.with(this)
                        .load(ref.child(imgLink))
                        .into(profileImg)
                }
            }
        })

        profileImg.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            intent.putExtra(SELECTED_CAMPAIGN_OWNER_ID, uid)
            startActivity(intent)
        }

        donateFAB.setOnClickListener {
            val intent = Intent(this, DonateActivity::class.java)
            intent.putExtra(SELECTED_CAMPAIGN_ID, campaignId)
            startActivity(intent)
        }


//        campaignActivityViewModel._campaign.observe(this, Observer<Campaign> { campaign ->
//            if (campaign != null) {
//                titleTV.text = campaign.title
//                ownerNameTV.text = campaign.ownerId
//                locationTV.text = campaign.location
//                descriptionTV.text = campaign.body
//            }
//        })

    }

}

