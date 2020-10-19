package com.training.crowdfunder

import android.os.Parcelable
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize
import java.lang.Exception

@Parcelize
data class Campaign (
    var id: String,
    val title: String,
    val location : String,
    val body : String,
    val target : Long,
    var raised : Long = 0L,
    var ownerId: String = "0"
) : Parcelable {

    companion object {
        fun DocumentSnapshot.toCampaign() : Campaign? {
            try {
                val title = getString("title")!!
                val location = getString("location")!!
                val body = getString("body")!!
                val target = getLong("target")!!
                val raised = getLong("raised")!!
                val ownerId = getString("owner_id")!!
                return Campaign(id, title, location, body, target, raised, ownerId)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting campaign", e)
                FirebaseCrashlytics.getInstance().log("Error converting campaign data")
                FirebaseCrashlytics.getInstance().setCustomKey("campaign_id", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                return null
            }
        }
        private const val TAG = "Campaign"
    }
}