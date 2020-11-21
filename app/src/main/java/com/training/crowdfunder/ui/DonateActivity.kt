package com.training.crowdfunder.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.training.crowdfunder.models.Campaign
import com.training.crowdfunder.R
import com.training.crowdfunder.viewmodels.CampaignViewModel
import com.training.crowdfunder.viewmodels.MyViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi

class DonateActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_CAMPAIGN_ID = "EXTRA_SELECTED_CAMPAIGN_ID"
    }

    private lateinit var campaignTitle : TextView
    private lateinit var campaignImage : ImageView
    private lateinit var campaignTarget : TextView
    private lateinit var campaignRaised : TextView
    private lateinit var donationAmountEt : EditText

    private lateinit var makeDonationBtn : Button

    @ExperimentalCoroutinesApi
    private lateinit var campaignViewModel: CampaignViewModel

    private lateinit var storage : FirebaseStorage

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate)

        storage = FirebaseStorage.getInstance()

        val campaignId = intent.getStringExtra(SELECTED_CAMPAIGN_ID)!!

        campaignTitle = findViewById(R.id.donate_campaign_title)
        campaignImage = findViewById(R.id.donate_campaign_img)
        campaignTarget = findViewById(R.id.campaign_target_amount)
        campaignRaised = findViewById(R.id.campaign_raised_amount)
        donationAmountEt = findViewById(R.id.donate_amount_entry)
        makeDonationBtn = findViewById(R.id.make_donation_button)

        campaignViewModel = ViewModelProvider(this, MyViewModelFactory(campaignId)).get(
            CampaignViewModel::class.java)

        campaignViewModel.campaign.observe(this, Observer<Campaign>{
            campaignTitle.text = it.title
            campaignTarget.text = it.target.toString()
            campaignRaised.text = it.raised.toString()
            
            it.raised

            val _img = it.img

            val ref = storage.reference.child("campaignImgs").child(_img)

            Glide.with(this)
                .load(ref)
                .into(campaignImage)
        })

        makeDonationBtn.setOnClickListener {
            try {
                val amount = donationAmountEt.text.toString().toLong()
                campaignViewModel.donate(campaignId, amount, applicationContext)
                onBackPressed()
            } catch (e: NumberFormatException){
                Toast.makeText(this, "Please enter an amount to donate", Toast.LENGTH_LONG).show()
            }

        }

    }


}