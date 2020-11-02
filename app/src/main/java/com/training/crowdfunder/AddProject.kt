package com.training.crowdfunder

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.training.crowdfunder.viewmodels.AddProjectViewModel
import java.util.*

class AddProject : AppCompatActivity() {

    private lateinit var postTitle : EditText
    private lateinit var postLocation : EditText
    private lateinit var postTarget : EditText
    private lateinit var postDescription : EditText
    private lateinit var postProjectBtn : Button
    private lateinit var postImage : ImageView

    private lateinit var addProjectViewModel: AddProjectViewModel
    private lateinit var auth: FirebaseAuth

    private var imgTitle : String = ""

    companion object{
        private const val GALLERY_REQUEST_CODE = 1231
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!.uid

        addProjectViewModel = ViewModelProvider(this).get(AddProjectViewModel::class.java)

        postTitle = findViewById(R.id.addProject_titleET)
        postLocation = findViewById(R.id.addProject_locationET)
        postTarget = findViewById(R.id.addProject_targetET)
        postDescription = findViewById(R.id.addProject_descriptionET)
        postProjectBtn = findViewById(R.id.button_post_project)
        postImage = findViewById(R.id.add_project_img)

        postImage.setOnClickListener {
            uploadNewPic()
        }


        postProjectBtn.setOnClickListener{
            val title = postTitle.text.toString()
            val location = postLocation.text.toString()
            val target = postTarget.text.toString().toLong()
            val description = postDescription.text.toString()
            val image = imgTitle

            addProjectViewModel.addProject(this, user, title, location, target, description, image)
            onBackPressed()
        }

    }

    private fun uploadNewPic() {
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
            val file_uri = data.data
            uploadImageToFirebase(file_uri)
        }
    }

    private fun uploadImageToFirebase(fileUri: Uri?) {
        if (fileUri != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"
            imgTitle = fileName

            val refStorage = FirebaseStorage.getInstance().reference.child("campaignImgs/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener {
                    Glide.with(this)
                        .load(refStorage)
                        .into(postImage)
                }
        }
    }
}