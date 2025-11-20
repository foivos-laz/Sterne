package com.example.sterne.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.sterne.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.MultiFactorResolver
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneMultiFactorGenerator
import com.google.firebase.auth.PhoneMultiFactorInfo
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val firebaseAuth = Firebase.firestore

    // To store the resolver between steps
    var mfaResolver: MultiFactorResolver? = null
    private var verificationId: String? = null

    // --- SIGN UP FUNCTION (No changes needed here) ---
    fun signup(
        email: String,
        password: String,
        name: String,
        phoneNumber: String,
        onResult: (Boolean, String) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userID = task.result?.user?.uid
                val userModel = UserModel(name, email, phoneNumber, userID!!)
                firebaseAuth.collection("users").document(userID)
                    .set(userModel)
                    .addOnCompleteListener { dbTask ->
                        if (dbTask.isSuccessful) {
                            onResult(true, "User registered")
                        } else {
                            onResult(false, dbTask.exception?.message ?: "DB error")
                        }
                    }
            } else {
                onResult(false, task.exception?.message ?: "Unknown error")
            }
        }
    }

    // --- LOGIN FUNCTION (Modified for 2FA) ---
    fun login(email: String, password: String, onResult: (LoginState) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // If login is successful without 2FA, the user is logged in.
                    onResult(LoginState.Success("User logged in"))
                } else {
                    val exception = task.exception
                    if (exception is FirebaseAuthMultiFactorException) {
                        // The user has 2FA enabled, so we need to start the SMS flow.
                        mfaResolver = exception.resolver
                        val hint =
                            mfaResolver?.hints?.firstOrNull() // Get the first enrolled 2FA method
                        if (hint != null) {
                            onResult(
                                LoginState.MultiFactorRequired(
                                    hint.displayName ?: "Unknown 2FA Method"
                                )
                            )
                        } else {
                            onResult(LoginState.Failure("Multi-factor authentication required, but no factor found."))
                        }
                    } else {
                        // A standard login error occurred.
                        onResult(LoginState.Failure(exception?.message ?: "Unknown error"))
                    }
                }
            }
    }

    // --- NEW: FUNCTION TO SEND SMS VERIFICATION CODE ---
    fun sendSmsVerification(activity: Activity, onResult: (Boolean, String) -> Unit) {
        // Safely cast the hint to PhoneMultiFactorInfo
        val phoneMultiFactorHint = mfaResolver?.hints?.firstOrNull() as? PhoneMultiFactorInfo

        if (phoneMultiFactorHint == null || mfaResolver == null) {
            onResult(
                false,
                "A phone-based 2FA method was not found or the MFA resolver is unavailable."
            )
            return
        }

        val phoneAuthOptions = PhoneAuthOptions.newBuilder(auth)
            .setMultiFactorHint(phoneMultiFactorHint) // Correctly passing PhoneMultiFactorInfo
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                // ... rest of your callback logic remains the same
                override fun onVerificationCompleted(credential: com.google.firebase.auth.PhoneAuthCredential) {
                    verifySmsCodeWithCredential(credential, onResult)
                }

                override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                    onResult(false, "SMS verification failed: ${e.message}")
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    this@AuthViewModel.verificationId = verificationId
                    onResult(true, "SMS code sent successfully.")
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(phoneAuthOptions)
    }

    // --- NEW: FUNCTION TO VERIFY THE SMS CODE AND COMPLETE LOGIN ---
    fun verifySmsCode(code: String, onResult: (Boolean, String) -> Unit) {
        if (verificationId == null) {
            onResult(false, "Verification ID is missing.")
            return
        }
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        verifySmsCodeWithCredential(credential, onResult)
    }

    private fun verifySmsCodeWithCredential(
        credential: com.google.firebase.auth.PhoneAuthCredential,
        onResult: (Boolean, String) -> Unit
    ) {
        // Correctly create the MultiFactorAssertion from the credential
        val multiFactorAssertion = PhoneMultiFactorGenerator.getAssertion(credential)

        mfaResolver?.resolveSignIn(multiFactorAssertion)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "2FA successful, user logged in.")
                } else {
                    onResult(false, "Failed to verify SMS code: ${task.exception?.message}")
                }
            }
    }
}


    // --- NEW: SEALED CLASS TO MANAGE LOGIN STATE IN THE UI ---
sealed class LoginState {
    data class Success(val message: String) : LoginState()
    data class Failure(val error: String) : LoginState()
    data class MultiFactorRequired(val hint: String) : LoginState()
}
