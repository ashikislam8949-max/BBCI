package com.example.data.firebase

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val uid: String,
    val displayName: String,
    val email: String,
    val companyName: String = "International Investor",
    val photoUrl: String? = null,
    val isVerified: Boolean = true
)

class FirebaseAuthManager(private val context: Context) {

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private var firebaseAuth: FirebaseAuth? = null
    private var firestore: FirebaseFirestore? = null

    init {
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                AppCheckInstaller.install()
            }
            firebaseAuth = FirebaseAuth.getInstance()
            firestore = FirebaseFirestore.getInstance()

            // Check if user is already signed in
            firebaseAuth?.currentUser?.let { user ->
                _currentUser.value = UserProfile(
                    uid = user.uid,
                    displayName = user.displayName ?: user.email?.substringBefore("@") ?: "Investor",
                    email = user.email ?: "investor@vision2030.sa",
                    photoUrl = user.photoUrl?.toString()
                )
            }
        } catch (e: Exception) {
            Log.w("FirebaseAuthManager", "Firebase initialization deferred: ${e.message}")
            // Fallback default investor demo profile
            _currentUser.value = UserProfile(
                uid = "saudi_inv_8821",
                displayName = "Alexandre Dupont",
                email = "a.dupont@vanceholdings.com",
                companyName = "Vance International Ltd"
            )
        }
    }

    suspend fun signInWithDemoAccount(email: String, name: String, company: String) {
        _isLoading.value = true
        _authError.value = null
        try {
            val profile = UserProfile(
                uid = "inv_${System.currentTimeMillis() % 100000}",
                displayName = name.ifBlank { "Senior Investor" },
                email = email.ifBlank { "investor@we-consulting.sa" },
                companyName = company.ifBlank { "Global Enterprises Group" }
            )
            _currentUser.value = profile

            // Try saving to Firestore if available
            try {
                firestore?.collection("investor_profiles")
                    ?.document(profile.uid)
                    ?.set(mapOf(
                        "name" to profile.displayName,
                        "email" to profile.email,
                        "company" to profile.companyName,
                        "lastActive" to System.currentTimeMillis()
                    ))?.await()
            } catch (fsErr: Exception) {
                Log.d("FirebaseAuthManager", "Firestore sync skipped: ${fsErr.message}")
            }
        } catch (e: Exception) {
            _authError.value = e.localizedMessage
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun syncDossierToFirestore(dossierId: Int, companyName: String, crNumber: String, status: String) {
        try {
            val user = _currentUser.value ?: return
            firestore?.collection("investor_dossiers")
                ?.document("dossier_$dossierId")
                ?.set(mapOf(
                    "userId" to user.uid,
                    "companyName" to companyName,
                    "crNumber" to crNumber,
                    "status" to status,
                    "updatedAt" to System.currentTimeMillis()
                ))?.await()
        } catch (e: Exception) {
            Log.d("FirebaseAuthManager", "Firestore dossier sync notice: ${e.message}")
        }
    }

    fun signOut() {
        try {
            firebaseAuth?.signOut()
        } catch (e: Exception) {
            Log.w("FirebaseAuthManager", "Sign out notice: ${e.message}")
        }
        _currentUser.value = null
    }
}
