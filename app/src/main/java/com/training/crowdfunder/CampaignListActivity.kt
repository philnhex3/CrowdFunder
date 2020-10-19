package com.training.crowdfunder

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CampaignListActivity : AppCompatActivity() {

    private lateinit var campaignListViewModel: CampaignListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign_list)

        val recyclerView : RecyclerView =findViewById(R.id.rv_campaigns)
        val adapter = CampaignAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        campaignListViewModel = ViewModelProvider(this).get(CampaignListViewModel::class.java)

        campaignListViewModel.campaigns.observe(this, Observer { campaigns ->
            campaigns?.let { adapter.setCampaigns(it) }
        })
    }
}