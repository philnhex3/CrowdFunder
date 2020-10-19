package com.training.crowdfunder

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SignUpFragment : Fragment() {

    private lateinit var auth : FirebaseAuth

    private lateinit var fullNameET : EditText
    private lateinit var emailET : EditText
    private lateinit var passwordET : EditText
    private lateinit var signUpBtn : Button
    private lateinit var cancelBtn : Button

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

        signUpBtn = view.findViewById(R.id.signUp_buttonSignUp)
        cancelBtn = view.findViewById(R.id.button_sign_up_cancel)

        fullNameET = view.findViewById(R.id.signUp_fullName)
        emailET = view.findViewById(R.id.signUp_email)
        passwordET = view.findViewById(R.id.signUp_password)


        signUpBtn.setOnClickListener {
            val name = fullNameET.text.toString()
            val email = emailET.text.toString()
            val password = passwordET.text.toString()

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(MainActivity(), OnCompleteListener {task ->
                if (task.isSuccessful){
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
}