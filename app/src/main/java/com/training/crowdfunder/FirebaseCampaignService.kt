package com.training.crowdfunder

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.training.crowdfunder.Campaign.Companion.toCampaign
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

object FirebaseCampaignService {
    private const val TAG = "FirebaseCampaignService"
    
    suspend fun getCampaignData(campaign_id: String): Campaign? {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("campaigns")
                            .document(campaign_id)
                            .get()
                            .result!!
                            .toCampaign()
        } catch (e: Exception) {
            Log.e(TAG, "Error getting campaign details", e)
            FirebaseCrashlytics.getInstance().log("Error getting campaign details")
            FirebaseCrashlytics.getInstance().setCustomKey("campaign id", campaign_id)
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun getCampaigns () : Flow<List<Campaign>> {
        val db = FirebaseFirestore.getInstance()
        return callbackFlow { 
            val listenerRegistration = db.collection("campaigns")
                .addSnapshotListener { querySnapshot : QuerySnapshot?, firebaseFirestoreException : FirebaseFirestoreException? ->
                   if (firebaseFirestoreException != null){
                       cancel(message = "Error fetching campaigns",
                           cause = firebaseFirestoreException)
                       return@addSnapshotListener
                   }
                    val map = querySnapshot!!.documents
                        .mapNotNull { it.toCampaign() }
                    offer(map)
                }
            awaitClose {
                Log.d(TAG, "Cancelling campaigns listener")
                listenerRegistration.remove()
            }
        }
    }
}