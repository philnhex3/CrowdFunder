package com.training.crowdfunder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CampaignAdapter(
    private val context: Context
) : RecyclerView.Adapter<CampaignAdapter.ViewHolder>() {

    companion object {
        const val SELECTED_CAMPAIGN_ID = "EXTRA_SELECTED_CAMPAIGN_ID"
    }
    private var campaigns = emptyList<Campaign>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val title : TextView = itemView.findViewById(R.id.tvBigTitle)
        private var campaign_id : String = ""

        init {
            itemView.setOnClickListener {
                val intent = Intent(context, CampaignActivity::class.java)
                intent.putExtra(SELECTED_CAMPAIGN_ID, campaign_id)
                context.startActivity(intent)
            }
        }

        fun bind(campaign: Campaign){
            title.text = campaign.title
            campaign_id = campaign.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_campaign, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(campaigns[position])
    }

    override fun getItemCount(): Int {
        return campaigns.size
    }

    internal fun setCampaigns(campaigns: List<Campaign>){
        this.campaigns = campaigns
        notifyDataSetChanged()
    }


}