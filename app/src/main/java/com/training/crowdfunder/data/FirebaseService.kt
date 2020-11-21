package com.training.crowdfunder.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.*
import com.training.crowdfunder.models.Campaign.Companion.toCampaign
import com.training.crowdfunder.models.User.Companion.toUser
import com.training.crowdfunder.models.Campaign
import com.training.crowdfunder.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*

object FirebaseService {
    private const val TAG = "FirebaseCampaignService"

    @ExperimentalCoroutinesApi
    suspend fun getCampaignData(campaign_id: String): Flow<Campaign?> {
        val db = FirebaseFirestore.getInstance()

        return callbackFlow {
            val listenerRegistration = db.collection("campaigns").document(campaign_id)
                .addSnapshotListener{ querySnapshot: DocumentSnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error Fetching campaign",
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val map = querySnapshot!!.toCampaign()
                    offer(map)
                }
            awaitClose {
                Log.d(TAG, "Cancelling campaign listener")
                listenerRegistration.remove()
            }
        }
    }


    @ExperimentalCoroutinesApi
    suspend fun getUserData(user_id: String): Flow<User?>{
        val db = FirebaseFirestore.getInstance()

        return callbackFlow {
            val listenerRegistration = db.collection("users").document(user_id)
                .addSnapshotListener{querySnapshot: DocumentSnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching User",
                            cause = firebaseFirestoreException
                        )
                        return@addSnapshotListener
                    }
                    val map = querySnapshot!!.toUser()
                    offer(map)
                }
            awaitClose {
                Log.d(TAG, "Cancelling user listener")
                listenerRegistration.remove()
            }
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun getCampaigns(): Flow<List<Campaign>> {
        val db = FirebaseFirestore.getInstance()
        return callbackFlow {
            val listenerRegistration = db.collection("campaigns")
                .addSnapshotListener { querySnapshot: QuerySnapshot?, firebaseFirestoreException: FirebaseFirestoreException? ->
                    if (firebaseFirestoreException != null) {
                        cancel(
                            message = "Error fetching campaigns",
                            cause = firebaseFirestoreException
                        )
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

    fun addDonation(campaign_id: String, amount: Long, context: Context) {
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("campaigns").document(campaign_id)
        ref.update("raised", FieldValue.increment(amount)).addOnSuccessListener {
            Toast.makeText(context,"Donation Successfully Made", Toast.LENGTH_LONG ).show()
        }

    }

    fun addProject (
        context: Context,
        ownerId: String,
        title: String,
        location: String,
        target: Long,
        description: String,
        img: String = "",
        raised: Long = 0L) {
        val postId =  UUID.randomUUID().toString()
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("campaigns").document(postId)

        val values = hashMapOf(
            "owner_id" to ownerId,
            "title" to title,
            "location" to location,
            "body" to description,
            "target" to target,
            "raised" to raised,
            "img" to img
        )

        ref.set(values)
            .addOnSuccessListener {
                Log.d(TAG, "Document Snapshot successfully written")
                Toast.makeText(context,"Project Successfully posted!", Toast.LENGTH_LONG ).show()
            }
            .addOnFailureListener{ Log.d(TAG, " Error writing document")
                Toast.makeText(context,"Project posting failed, please try again", Toast.LENGTH_LONG ).show()
            }
    }
}