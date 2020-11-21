package com.training.crowdfunder.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.training.crowdfunder.models.Campaign
import com.training.crowdfunder.data.FirebaseService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CampaignViewModel(private val campaignId: String) : ViewModel() {
    val TAG = "com.training.crowdfunder.CampaignActivityViewModel"
    private val _campaign = MutableLiveData<Campaign>()
    var campaign : LiveData<Campaign> = _campaign
    var progress : Double = 0.0

    init {
        viewModelScope.launch {
            FirebaseService.getCampaignData(campaignId).collect { _campaign.value = it }
            Log.d(TAG, "VM data: ${_campaign.value}")
        }


    }

    fun donate(campaignId: String, amount: Long, context: Context){
        FirebaseService.addDonation(campaignId, amount, context)
    }

    fun calculateProgress(target: Long, raised: Long){
        val result = (raised.toDouble() / target) * 100
        progress = result
    }

//    fun setCampaign(id : String){
//        viewModelScope.launch {
//            val data = FirebaseCampaignService.getCampaignData(id)
//            _campaign.value = data
//            campaign = _campaign
//        }
//    }
}

class MyViewModelFactory(private val campaignId : String): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CampaignViewModel(campaignId) as T
    }
}