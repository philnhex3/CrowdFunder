package com.training.crowdfunder

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DataRepository {

    fun getRefFromUrl(url : String) : StorageReference{
        val storage = FirebaseStorage.getInstance()
        return storage.getReferenceFromUrl(url)
    }
}