package com.training.crowdfunder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CampaignActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_CAMPAIGN_ID = "EXTRA_SELECTED_CAMPAIGN_ID"
    }

    private lateinit var titleTV : TextView
    private lateinit var ownerNameTV : TextView
    private lateinit var ownerIcon : ImageView
    private lateinit var locationTV : TextView
    private lateinit var descriptionTV : TextView


    @ExperimentalCoroutinesApi
    private lateinit var campaignActivityViewModel: CampaignActivityViewModel

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign)

        campaignActivityViewModel = ViewModelProvider(this).get(CampaignActivityViewModel::class.java)

        val campaignId = intent.getStringExtra(SELECTED_CAMPAIGN_ID)!!

        campaignActivityViewModel.setCampaign(campaignId)

        titleTV = findViewById(R.id.campaign_title)
        ownerNameTV = findViewById(R.id.campaignOwnerName)
        locationTV = findViewById(R.id.locationTV)
        descriptionTV = findViewById(R.id.campaignDescription)

        campaignActivityViewModel.campaign.observe(this, Observer<Campaign>{campaign ->
            titleTV.text = campaign.title
            ownerNameTV.text = campaign.ownerId
            locationTV.text = campaign.location
            descriptionTV.text = campaign.body
        })

    }



}