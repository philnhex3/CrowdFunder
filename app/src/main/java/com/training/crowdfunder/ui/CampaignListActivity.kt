package com.training.crowdfunder.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.training.crowdfunder.utils.CampaignAdapter
import com.training.crowdfunder.R
import com.training.crowdfunder.viewmodels.CampaignListViewModel
import kotlinx.android.synthetic.main.activity_campaign_list.*

class CampaignListActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_CAMPAIGN_OWNER_ID = "EXTRA_SELECTED_CAMPAIGN_OWNER_ID"
    }

    private lateinit var campaignListViewModel: CampaignListViewModel
    private lateinit var auth : FirebaseAuth

    private lateinit var addPostFab : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_campaign_list)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()

        checkAuthStatus()

        addPostFab = findViewById(R.id.fab)

        val recyclerView : RecyclerView =findViewById(R.id.rv_campaigns)
        val adapter = CampaignAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        campaignListViewModel = ViewModelProvider(this).get(CampaignListViewModel::class.java)

        campaignListViewModel.campaigns.observe(this, Observer { campaigns ->
            campaigns?.let { adapter.setCampaigns(it) }
        })

        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
//                    setContent("Home")
                    true
                }
                R.id.menu_profile -> {
                    val intent = Intent(this, UserProfileActivity::class.java)
                    val user = auth.currentUser!!.uid
                    intent.putExtra(SELECTED_CAMPAIGN_OWNER_ID, user)
                    startActivity(intent)
//                    setContent("Profile")
                    true
                }
            }
            false
        }

        addPostFab.setOnClickListener {
            val intent = Intent(this, AddProjectActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {

    }

    private fun checkAuthStatus() {
        if (auth.currentUser == null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout -> signOut()
        }
        return true
    }

    private fun signOut() {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun setContent(content: String) {
        title = content
    }
}