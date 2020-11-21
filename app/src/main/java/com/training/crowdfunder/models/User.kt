package com.training.crowdfunder.models

import android.os.Parcelable
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var uid : String,
    val fullName : String,
    val email : String,
    var donated : Long,
    var profile_image : String = ""
) : Parcelable {

    companion object{
        fun DocumentSnapshot.toUser() : User? {
            try {
                val fullName = getString("full_name")!!
                val email = getString("email")!!
                val donated = getLong("donated")!!
                val profile_image = getString("profile_img")!!
                return User(id, fullName, email, donated, profile_image)
            } catch (e: Exception){
                Log.e(TAG, "Error converting User")
                FirebaseCrashlytics.getInstance().log("Error converting User")
                FirebaseCrashlytics.getInstance().setCustomKey("user_id", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }
        private const val TAG = "User"
    }
}

