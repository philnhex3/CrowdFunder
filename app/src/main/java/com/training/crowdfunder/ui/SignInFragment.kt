package com.training.crowdfunder.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.training.crowdfunder.R

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class SignInFragment : Fragment() {

    companion object {
        const val TAG = "Login Fragment"
        const val SIGN_IN_RESULT_CODE = 1010
    }

    private lateinit var auth: FirebaseAuth

    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText

    private lateinit var signInBtn: Button
    private lateinit var signUpBtn: Button

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val inflater = inflater.inflate(R.layout.fragment_sign_in, container, false)


        return inflater
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        emailEt = view.findViewById(R.id.signInEmailET)
        passwordEt = view.findViewById(R.id.signInPasswordET)

        signInBtn = view.findViewById(R.id.SignIn_buttonSignIn)
        signUpBtn = view.findViewById(R.id.SignIn_buttonSignUp)

        auth = FirebaseAuth.getInstance()

        signInBtn.setOnClickListener {
            val email: String = emailEt.text.toString()
            val password: String = passwordEt.text.toString()


            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_LONG).show()
            } else {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity(), OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Successfully logged in", Toast.LENGTH_LONG).show()
                        val intent = Intent(context, CampaignListActivity::class.java)
                        startActivity(intent)
                        MainActivity().finish()
                    }

                    if(!task.isSuccessful){
                        Toast.makeText(context, "Email and password combination incorrect, please try again",
                            Toast.LENGTH_LONG).show()
                        passwordEt.getText().clear()
                    }
                })
            }
        }

        signUpBtn.setOnClickListener {
            findNavController().navigate(R.id.action_SignInFragment_to_SignUpFragment)
        }

    }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == SIGN_IN_RESULT_CODE) {
//            val response = IdpResponse.fromResultIntent(data)
//            if (resultCode == Activity.RESULT_OK) {
//                Log.i(
//                    TAG,
//                    "Successfully signed in, welcome " +
//                            "${FirebaseAuth.getInstance().currentUser?.displayName}!"
//                )
//                val intent = Intent(context, MainActivity::class.java)
//                startActivity(intent)
//            } else {
//                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
//            }
//        }
//    }
}