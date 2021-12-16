package com.mahila.motivationalQuotesApp.model.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mahila.motivationalQuotesApp.model.entities.User
import com.mahila.motivationalQuotesApp.model.entities.User.Companion.toUser
import kotlinx.coroutines.tasks.await

object FirebaseUserService {
    private const val TAG = "FirebaseUserService"
    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    //get user data
    suspend fun getUserData(): User? {
        return try {
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid).get().await().toUser()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error getting the user details", e)

            null
        }
    }

    suspend fun signUp(name: String, email: String, password: String) {

        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Add user to FireStore
                    val user = User(auth.currentUser!!.uid, name, email)

                    db.collection("users").document(auth.currentUser!!.uid).set(user)
                } else {
                    Log.e(TAG, "Error add the user ", task.exception!!)
                    //    Toast.makeText(this ,task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }.await()

        } catch (e: Exception) {
            Log.e(TAG, "Error sign up ", e)

        }

    }

    suspend fun signIn(email: String, password: String) {

        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Toast
                } else {
                    Log.e(TAG, task.exception!!.message, task.exception!!)
                    //    Toast.makeText(this ,"", Toast.LENGTH_SHORT).show()
                }
            }.await()


        } catch (e: Exception) {
            Log.e(TAG, "Error sign in ", e)

        }

    }

    suspend fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Password Reset Email sent.")
                    //Toast msg
                } else {
                    Log.d(TAG, "Password Reset Email has not been sent.")

                    //Toast msg
                }
            }.await()
    }

    suspend fun resetPassword(newPassword: String) {
        auth.currentUser?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password updated.")
                }else {
                    Log.d(TAG, "User password has not been updated.")
                    //Toast msg
                }
            }?.await()
    }

    fun signOut() {
        auth.signOut()
    }

    fun checksignInState()= auth.currentUser != null


}