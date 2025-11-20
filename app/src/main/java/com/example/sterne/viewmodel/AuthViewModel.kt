package com.example.sterne.viewmodel

import androidx.lifecycle.ViewModel
import com.example.sterne.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel : ViewModel() {

    private val auth = Firebase.auth
    private val  firebaseAuth = Firebase.firestore

    fun signup(email: String, password: String, name: String, phoneNumber: String, onResult: (Boolean, String) -> Unit){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                var userID = it.result?.user?.uid

                val userModel = UserModel(name, email, phoneNumber, userID!!)
                firebaseAuth.collection("users").document(userID)
                    .set(userModel)
                    .addOnCompleteListener {
                        dbTask -> if (dbTask.isSuccessful){
                            onResult(true, "User registered")
                        }else{
                            onResult(false, "User didn't register")
                        }
                    }
            }else{
                onResult(false, it.exception?.message ?: "Unknown error")
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                onResult(true, "User logged in")
            }else{
                onResult(false, it.exception?.message ?: "Unknown error")
            }
        }
    }
}