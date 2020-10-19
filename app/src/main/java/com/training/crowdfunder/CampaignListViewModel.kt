package com.training.crowdfunder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CampaignListViewModel : ViewModel() {
    private val _campaignList = MutableLiveData<List<Campaign>>()
    val campaigns : LiveData<List<Campaign>> = _campaignList

    init {
        viewModelScope.launch {
            FirebaseCampaignService.getCampaigns().collect { _campaignList.value = it }
        }
    }
}