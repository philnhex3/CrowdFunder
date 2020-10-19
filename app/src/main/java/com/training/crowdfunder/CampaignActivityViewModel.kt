package com.training.crowdfunder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CampaignActivityViewModel : ViewModel() {
    private val _campaign = MutableLiveData<Campaign>()
    val campaign : LiveData<Campaign> = _campaign
    var campaignId : String = ""

//    init {
//        viewModelScope.launch {
//            _campaign.value = FirebaseCampaignService.getCampaignData(campaignId)
//        }
//    }

    fun setCampaign(id : String){
        viewModelScope.launch {
            _campaign.value = FirebaseCampaignService.getCampaignData(id)
        }
    }
}