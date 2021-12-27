package com.mahila.motivationalQuotesApp.model.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
            auth.currentUser?.let {
                db.collection("users")
                    .document(auth.currentUser!!.uid).collection("notificationsList")
                    .document(notification.notificationId).update(
                        mapOf(
                            "active" to false
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
                } else {
                    Log.d(TAG, "User password has not been updated.")
                    //Toast msg
                }
            }?.await()
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

    suspend fun resetEmail(newEmail: String) {
        auth.currentUser?.updateEmail(newEmail)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email updated.")
                } else {
                    Log.d(TAG, "User email has not been updated.")
                    //Toast msg
                }
            }?.await()
    }

    //
    fun signOut() {
        auth.signOut()
    }

    fun checkSignInState() = auth.currentUser != null


}