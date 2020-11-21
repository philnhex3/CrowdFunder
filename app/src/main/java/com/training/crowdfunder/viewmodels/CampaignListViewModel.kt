package com.training.crowdfunder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.crowdfunder.models.Campaign
import com.training.crowdfunder.data.FirebaseService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CampaignListViewModel : ViewModel() {
    private val _campaignList = MutableLiveData<List<Campaign>>()
    val campaigns: LiveData<List<Campaign>> = _campaignList

    init {
        viewModelScope.launch {
            FirebaseService.getCampaigns().collect { _campaignList.value = it }
        }
    }
}