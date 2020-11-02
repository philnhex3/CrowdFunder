package com.training.crowdfunder.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.crowdfunder.FirebaseService
import kotlinx.coroutines.launch

class AddProjectViewModel : ViewModel() {

    fun addProject(
        context: Context,
        ownerId : String,
        title: String,
        location: String,
        target: Long,
        description: String,
        img: String = "") {

        FirebaseService.addProject(
            context,
            ownerId,
            title,
            location,
            target,
            description,
            img)
    }
}