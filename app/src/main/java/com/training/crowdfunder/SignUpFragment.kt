package com.training.crowdfunder

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SignUpFragment : Fragment() {

    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage
    private lateinit var db : FirebaseFirestore

    private lateinit var fullNameET : EditText
    private lateinit var emailET : EditText
    private lateinit var passwordET : EditText
    private lateinit var signUpBtn : Button
    private lateinit var cancelBtn : Button
    private lateinit var profileImg : ImageView
    private lateinit var uploadText : TextView

    private  var file_uri : Uri? = null

    companion object{
        private const val GALLERY_REQUEST_CODE = 1231
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        signUpBtn = view.findViewById(R.id.signUp_buttonSignUp)
        cancelBtn = view.findViewById(R.id.button_sign_up_cancel)

        fullNameET = view.findViewById(R.id.signUp_fullName)
        emailET = view.findViewById(R.id.signUp_email)
        passwordET = view.findViewById(R.id.signUp_password)
        profileImg = view.findViewById(R.id.profile_img_upload)

        uploadText = view.findViewById(R.id.signUp_uploadImageText)

        profileImg.setOnClickListener {
            uploadProfilePic()
        }



        signUpBtn.setOnClickListener {
            val _name = fullNameET.text.toString()
            val _email = emailET.text.toString()
            val _password = passwordET.text.toString()


            auth.createUserWithEmailAndPassword(_email, _password)
                .addOnCompleteListener(MainActivity(), OnCompleteListener {task ->
                if (task.isSuccessful){
                    val user = auth.currentUser!!
                    val uid = user.uid
                    val image = user.photoUrl.toString()

                    val user_data = HashMap<String, Any>()

                    user_data.put("full_name", _name)
                    user_data.put("email", _email)
                    user_data.put("profile_img", image)
                    user_data.put("donated", 0L)

                    val db = FirebaseFirestore.getInstance()
                    val dbRef = db.collection("users")
                    dbRef.document(uid).set(user_data)

                    uploadImageToFirebase(file_uri)

                    Toast.makeText(context, "Successfully Registered", Toast.LENGTH_LONG).show()
                    val intent = Intent(context, CampaignListActivity::class.java)
                    startActivity(intent)
//                    MainActivity().finish()
                } else {
                    Toast.makeText(context, "Registration Failed", Toast.LENGTH_LONG).show()
                }
            })
        }


        cancelBtn.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private fun uploadProfilePic() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Upload Image"
            ),
            GALLERY_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
            && data.data != null){
            file_uri = data.data

            Glide.with(this)
                .load(file_uri)
                .into(profileImg)

            uploadText.visibility = View.INVISIBLE
        }
    }

    private fun uploadImageToFirebase(fileUri: Uri?) {
        if (fileUri != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"

            val refStorage = FirebaseStorage.getInstance().reference.child("user_imgs/$fileName")
            val user = auth.currentUser!!

            db.collection("users").document(user.uid).update("profile_img", fileName)

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            val imageUrl = it.toString()
                            val imageUri = Uri.parse(imageUrl)
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setPhotoUri(imageUri)
                                .build()
                            user.updateProfile(profileUpdates)
                        }
                    })
        }
    }
}