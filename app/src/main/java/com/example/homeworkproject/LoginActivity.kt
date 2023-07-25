package com.example.homeworkproject

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hbb20.CountryCodePicker
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    lateinit var phoneNumber: String
     lateinit var auth: FirebaseAuth
    private lateinit var vId: String
    private var requestcode = 1234
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
      //   for phone auth && Email (auth inilise)
        auth = Firebase.auth

        //        for google auth (FirebaseApp.initializeApp, FirebaseAuth.getInstance() inilise)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        val  userEmailInput = findViewById<EditText>(R.id.login_email)
        val userPassInput = findViewById<EditText>(R.id.login_phone)
        val LogBtn=findViewById<Button>(R.id.log_in)


        val phone = findViewById<EditText>(R.id.phone_number)
        val ccp = findViewById<CountryCodePicker>(R.id.ccp)
        val sendOtpButton = findViewById<Button>(R.id.sendOtp)



//        for phone auth
        sendOtpButton.setOnClickListener {
            ccp.registerCarrierNumberEditText(phone)
            phoneNumber = ccp.fullNumberWithPlus.replace(" ", "")
            initiateOtp()
        }

//        email && password auth
        LogBtn.setOnClickListener {
            auth.signInWithEmailAndPassword(userEmailInput.text.toString() , userPassInput.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully Login", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,DashBoardActivity::class.java))
                }.addOnFailureListener{
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            userEmailInput.text.clear()
            userPassInput.text.clear()

        }

      // verify otp


        val enterOtp=findViewById<EditText>(R.id.otp)
        val verifyButton=findViewById<Button>(R.id.verifyOtp)
        verifyButton.setOnClickListener {

            val credential = PhoneAuthProvider.getCredential(vId, enterOtp.text.toString())
            auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    Toast.makeText(this, "verified", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Registation successfull", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,DashBoardActivity::class.java))
                }
                .addOnFailureListener {
                    Toast.makeText(this, "not verify", Toast.LENGTH_SHORT).show()
                }

            enterOtp.text.clear()
        }
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)

        signInButton.setOnClickListener {
            auth.signOut()
            val googleIntent = googleSignInClient.signInIntent
            startActivityForResult(googleIntent, requestcode)
        }


    }
    // send otp message
    private fun initiateOtp() {

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(30L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                override fun onCodeSent(
                    verificationId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    vId = verificationId
                    Toast.makeText(this@LoginActivity, "send $verificationId", Toast.LENGTH_SHORT).show()

                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(e: FirebaseException) {

                }

            })

            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }


    override fun onActivityResult(ActivityRequestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(ActivityRequestCode, resultCode, data)

        if (ActivityRequestCode == requestcode) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnSuccessListener { it ->
                val credencial = GoogleAuthProvider.getCredential(it.idToken, null)
                auth.signInWithCredential(credencial)
                    .addOnSuccessListener {
                        Toast.makeText(this, "" + it.user?.displayName+ it.user?.email+" Succesfull", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this,DashBoardActivity::class.java))
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                    }
            }
                .addOnFailureListener {
                    Toast.makeText(this, "" + it.message, Toast.LENGTH_SHORT).show()
                }
        }


    }
}