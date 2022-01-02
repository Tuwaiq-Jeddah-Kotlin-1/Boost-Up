package com.mahila.motivationalQuotesApp.model.repository

import android.app.Application
import android.provider.Settings.Global.getString
import android.provider.Settings.Secure.getString
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.mahila.motivationalQuotesApp.R
import com.mahila.motivationalQuotesApp.model.entities.Notification
import com.mahila.motivationalQuotesApp.model.entities.Notification.Companion.toNotification
import com.mahila.motivationalQuotesApp.model.entities.Quote
import com.mahila.motivationalQuotesApp.model.entities.Quote.Companion.toQuote
import com.mahila.motivationalQuotesApp.model.entities.User
import com.mahila.motivationalQuotesApp.model.entities.User.Companion.toUser
import kotlinx.coroutines.tasks.await


object FirebaseUserService {
    private const val TAG = "FirebaseUserService"
    private val db by lazy { FirebaseFirestore.getInstance() }
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var application: Application
    fun firebaseUserService(_application: Application) {
        application = _application
    }

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

    suspend fun getNotifications(): List<Notification>? {
        return try {
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .collection("notificationsList").get().await()
                    .documents.mapNotNull {
                        it.toNotification()
                    }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error getting the notifications list", e)

            null
        }
    }

    suspend fun addNotification(notification: Notification) {
        try {
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .collection("notificationsList")
                    .document(notification.notificationId).set(notification).await()
            }


        } catch (e: Exception) {
            Log.e(TAG, "Error adding an notification", e)
        }
    }

    suspend fun deleteNotification(notification: Notification) {
        try {
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid)
                    .collection("notificationsList")
                    .document(notification.notificationId).delete().await()
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error deleting an favorite quote", e)
        }
    }

    suspend fun setToInactiveNotification(notification: Notification) {
        try {
            val isActive=!notification.active
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid).collection("notificationsList")
                    .document(notification.notificationId).update(
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
                    Toast.makeText(application, R.string.successfully_sign_up, Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(application, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error add the user ", task.exception!!)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sign up ", e)
        }
    }


    fun signIn(email: String, password: String,view:View) {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                      //  firebaseUserMutableLiveData.postValue(auth.currentUser)
                        Toast.makeText(application, R.string.successfully_sign_in, Toast.LENGTH_SHORT)
                            .show()
                        Navigation.findNavController(view).navigate(R.id.action_signinFragment_to_quotesFragment)

                    } else {
                        Toast.makeText(application, task.exception!!.message, Toast.LENGTH_SHORT)
                            .show()
                        Log.e(TAG, task.exception!!.message, task.exception!!)
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, "Error sign in ", e)
        }
    }

    suspend fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(application, "Password Reset Email sent.", Toast.LENGTH_SHORT)
                        .show()
                    Log.d(TAG, "Password Reset Email sent.")
                } else {
                    Toast.makeText(
                        application,
                        "Something wrong happened, try again!",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d(TAG, "Password Reset Email has not been sent.")
                }
            }.await()
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