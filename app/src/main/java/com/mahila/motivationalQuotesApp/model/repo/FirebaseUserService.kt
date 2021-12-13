package com.mahila.motivationalQuotesApp.model.repo

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mahila.motivationalQuotesApp.model.entity.User

object FirebaseUserService {
    private const val TAG = "FirebaseUserService"
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUp(name: String, email: String, password: String) {

        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Add user to FireStore
                    val user = User(auth.currentUser!!.uid, name, email)

                    db.collection("users").add(user)
                } else {
                    Log.e(TAG, "Error add the user ", task.exception!!)
                    //    Toast.makeText(this ,task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }


        } catch (e: Exception) {
            Log.e(TAG, "Error sign up ", e)

        }

    }

    fun signIn(email: String, password: String) {

        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Toast
                } else {
                    Log.e(TAG, task.exception!!.message, task.exception!!)
                    //    Toast.makeText(this ,"", Toast.LENGTH_SHORT).show()
                }
            }


        } catch (e: Exception) {
            Log.e(TAG, "Error sign in ", e)

        }

    }

    fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Toast msg
                } else {
                    //Toast msg
                }
            }
    }

    fun resetPassword() {

    }

    fun signOut() {
        auth.signOut()
    }


}