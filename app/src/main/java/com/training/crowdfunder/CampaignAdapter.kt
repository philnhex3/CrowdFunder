package com.training.crowdfunder

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class   CampaignAdapter(
    private val context: Context
) : RecyclerView.Adapter<CampaignAdapter.ViewHolder>() {

    companion object {
        const val SELECTED_CAMPAIGN_ID = "EXTRA_SELECTED_CAMPAIGN_ID"
    }
    private var campaigns = emptyList<Campaign>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val title : TextView = itemView.findViewById(R.id.tvBigTitle)
        private var campaign_id : String = ""
        private val postImage : ImageView = itemView.findViewById(R.id.cardImage)
        private val supportBtn : Button = itemView.findViewById(R.id.supportThisProjectBtn)
        private val progressBar : ProgressBar = itemView.findViewById(R.id.list_item_progress_bar)

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

            val progress = (campaign.raised.toDouble() / campaign.target) * 100.0

            progressBar.visibility = View.VISIBLE
            progressBar.progress = progress.toInt()

            if (campaign.img.isNotEmpty()){
                val _img = campaign.img
                val storage = FirebaseStorage.getInstance()
//                val ref = storage.getReferenceFromUrl("gs://crowdfunder-1985e.appspot.com/campaignImgs/farmer1.jpg")
                val ref = storage.reference.child("campaignImgs").child(_img)

                Glide.with(context)
                    .load(ref)
                    .fitCenter()
                    .into(postImage)
            }

            supportBtn.setOnClickListener {
                val intent = Intent(context, DonateActivity::class.java)
                intent.putExtra(SELECTED_CAMPAIGN_ID, campaign_id)
                context.startActivity(intent)
            }

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