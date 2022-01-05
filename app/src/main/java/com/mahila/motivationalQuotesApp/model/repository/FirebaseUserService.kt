package com.mahila.motivationalQuotesApp.model.repository

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.app.BoostUp.Companion.instant
import com.mahila.motivationalQuotesApp.model.entities.Reminder
import com.mahila.motivationalQuotesApp.model.entities.Reminder.Companion.toReminder
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.entities.Quote.Companion.toQuote
import com.mahila.motivationalQuotesApp.model.entities.User
import com.mahila.motivationalQuotesApp.model.entities.User.Companion.toUser
import kotlinx.coroutines.tasks.await


object FirebaseUserService {
    private const val TAG = "FirebaseUserService"
    private val db by lazy { FirebaseFirestore.getInstance() }
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    //get user data-------------------------------------
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

    suspend fun getNotifications(): List<Reminder>? {
        return try {
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .collection("notificationsList").get().await()
                    .documents.mapNotNull {
                        it.toReminder()
                    }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error getting the notifications list", e)

            null
        }
    }

    suspend fun addNotification(reminder: Reminder) {
        try {
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .collection("notificationsList")
                    .document(reminder.reminderId).set(reminder).await()
            }


        } catch (e: Exception) {
            Log.e(TAG, "Error adding an reminder", e)
        }
    }

    suspend fun deleteReminder(reminder: Reminder) {
        try {
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .collection("notificationsList")
                    .document(reminder.reminderId).delete().await()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error deleting an favorite quote", e)
        }
    }

    suspend fun updateReminderState(reminder: Reminder) {
        try {
            val isActive = !reminder.active
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid).collection("notificationsList")
                    .document(reminder.reminderId).update(
                        mapOf(
                            "active" to isActive
                        )
                    ).await()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error updating user name", e)
        }
    }

    suspend fun getFavoritesQuotes(): List<Quote>? {
        return try {
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .collection("favoritesList").get().await()
                    .documents.mapNotNull {
                        it.toQuote()
                    }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error getting the favorites quotes", e)

            null
        }
    }

    suspend fun addFavoriteQuote(quote: Quote) {
        try {
            // Create a reference to the collection
            val favoritesListRef = auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .collection("favoritesList")
            }
//Query to avoid the duplicate quote (that due to the quotes that fetch from the Api are without Id).
            val query = favoritesListRef?.whereEqualTo("text", quote.text)
            query?.get()?.addOnSuccessListener { documents ->
                if (documents.size() == 0) {
                    auth.currentUser?.let {
                        db.collection("users")
                            .document(auth.currentUser!!.uid)
                            .collection("favoritesList").add(quote)
                    }
                }
            }?.addOnFailureListener { e ->
                Log.e(TAG, "Error adding an favorite quote", e)
            }?.await()

        } catch (e: Exception) {
            Log.e(TAG, "Error adding an favorite quote", e)
        }
    }

    suspend fun deleteFavoriteQuote(quote: Quote) {
        try {
            // Create a reference to the collection
            val favoritesListRef = auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .collection("favoritesList")
            }
            // Create a query against the collection.
            val query = favoritesListRef?.whereEqualTo("text", quote.text)
            query?.get()?.addOnSuccessListener { documents ->
                if (documents.size() > 0) {
                    auth.currentUser?.let {
                        db.collection("users")
                            .document(auth.currentUser!!.uid)
                            .collection("favoritesList").document(documents.toList()[0].id).delete()
                            .addOnSuccessListener {
                                Log.d(
                                    TAG,
                                    "DocumentSnapshot successfully deleted!"
                                )
                            }.addOnFailureListener { e -> Log.w(TAG, "Error deleting quote", e) }
                    }
                }
            }?.addOnFailureListener { exception ->
                Log.e(TAG, "Error deleting an favorite quote", exception)
            }?.await()

        } catch (e: Exception) {
            Log.e(TAG, "Error deleting an favorite quote", e)
        }
    }

    //----------- Manage User Account
    fun signUp(name: String, email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Add user to FireStore
                    val user = User(auth.currentUser!!.uid, name, email)
                    db.collection("users").document(auth.currentUser!!.uid).set(user)
                    // firebaseUserMutableLiveData.postValue(auth.currentUser)
                    Toast.makeText(instant.applicationContext, R.string.successfully_sign_up, Toast.LENGTH_SHORT)
                        .show()

                } else {
                    Toast.makeText(instant.applicationContext, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error add the user ", task.exception!!)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sign up ", e)
        }
    }


    fun signIn(email: String, password: String, view: View) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //  firebaseUserMutableLiveData.postValue(auth.currentUser)
                        Toast.makeText(
                            instant.applicationContext,
                            R.string.successfully_sign_in,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        Navigation.findNavController(view)
                            .navigate(R.id.action_signinFragment_to_quotesFragment)

                    } else {
                        Toast.makeText(instant.applicationContext, task.exception!!.message, Toast.LENGTH_SHORT)
                            .show()
                        Log.e(TAG, task.exception!!.message, task.exception!!)
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error sign in ", e)
        }
    }

    suspend fun forgotPassword(email: String) {
        try {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            instant.applicationContext,
                            R.string.successfully_send,
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.d(TAG, "Password Reset Email sent.")
                    } else {
                        Toast.makeText(
                            instant.applicationContext,
                            task.exception!!.message,
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }.await()
        } catch (e: Exception) {
            Log.d(TAG, "Password Reset Email has not been sent.")
        }
    }

    suspend fun resetUserName(newName: String) {
        try {
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid).update(
                        mapOf(
                            "name" to newName
                        )
                    ).await()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error updating user name", e)
        }
    }

    fun signOut() {
        auth.signOut()
    }

    fun checkSignInState() = auth.currentUser != null


}