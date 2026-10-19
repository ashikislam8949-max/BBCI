package com.example.data.firebase

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.data.consulting.InvestorInterestProfile
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

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
    private val webClientId: String?

    val googleSignInAvailable: Boolean
        get() = firebaseAuth != null && !webClientId.isNullOrBlank()

    init {
        webClientId = runCatching { GoogleWebClientId.read(context) }.getOrNull()
        try {
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
            }
            if (FirebaseApp.getApps(context).isNotEmpty()) {
                AppCheckInstaller.install()
                firebaseAuth = FirebaseAuth.getInstance()
                firestore = FirebaseFirestore.getInstance()
                firebaseAuth?.currentUser?.let { user ->
                    _currentUser.value = user.toUserProfile(DEFAULT_COMPANY, null, null)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Firebase initialization deferred: ${e.message}")
            firebaseAuth = null
            firestore = null
        }
    }

    /**
     * Button-triggered Sign in with Google through Credential Manager.
     * The ID token is exchanged for a Firebase Auth session. Returns null when
     * the user cancels or Firebase / the Web client ID is not configured.
     */
    suspend fun signInWithGoogle(activityContext: Context): UserProfile? {
        _isLoading.value = true
        _authError.value = null
        try {
            val auth = firebaseAuth
            val clientId = webClientId
            if (auth == null || clientId.isNullOrBlank()) {
                _authError.value = "Google sign-in is unavailable until Firebase is configured with a Web client ID."
                return null
            }
            val activity = activityContext.findActivity()
            if (activity == null) {
                _authError.value = "Google sign-in needs a visible screen."
                return null
            }
            val option = GetSignInWithGoogleOption.Builder(clientId)
                .setNonce(hashedNonce())
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(option)
                .build()
            val result = CredentialManager.create(activity).getCredential(activity, request)
            val credential = result.credential
            val isGoogleIdToken = credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL ||
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_SIWG_CREDENTIAL
            if (!isGoogleIdToken) {
                _authError.value = "Google did not return an ID token."
                return null
            }
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val firebaseCredential = GoogleAuthProvider.getCredential(googleId.idToken, null)
            val signedIn = auth.signInWithCredential(firebaseCredential).await().user
                ?: throw IllegalStateException("Firebase did not return a user.")
            val profile = signedIn.toUserProfile(
                companyName = _currentUser.value?.companyName ?: DEFAULT_COMPANY,
                fallbackName = googleId.displayName,
                fallbackEmail = googleId.id
            )
            _currentUser.value = profile
            return profile
        } catch (_: GetCredentialCancellationException) {
            return null
        } catch (_: NoCredentialException) {
            _authError.value = "No Google account is available on this device."
            return null
        } catch (e: GetCredentialException) {
            _authError.value = e.errorMessage?.toString() ?: e.localizedMessage ?: "Google sign-in failed."
            return null
        } catch (e: Exception) {
            Log.w(TAG, "Google sign-in failed: ${e.message}")
            _authError.value = e.localizedMessage ?: "Google sign-in failed."
            return null
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun readInvestorProfile(uid: String, fallback: InvestorInterestProfile): LinkedInvestorProfile {
        return try {
            val snapshot = firestore?.collection(INVESTOR_PROFILES)?.document(uid)?.get()?.await()
            val data = snapshot?.data
            if (snapshot == null || !snapshot.exists() || data == null) {
                LinkedInvestorProfile(fallback, fromCloud = false)
            } else {
                LinkedInvestorProfile(investorProfileFromFirestore(data, fallback), fromCloud = true)
            }
        } catch (e: Exception) {
            Log.d(TAG, "Profile read skipped: ${e.message}")
            LinkedInvestorProfile(fallback, fromCloud = false)
        }
    }

    suspend fun writeInvestorProfile(uid: String, profile: InvestorInterestProfile, photoUrl: String?) {
        try {
            firestore?.collection(INVESTOR_PROFILES)
                ?.document(uid)
                ?.set(profile.toFirestoreFields(uid, photoUrl), SetOptions.merge())
                ?.await()
            applyCompany(profile.companyName)
            val name = profile.investorName
            if (name.isNotBlank() && _currentUser.value?.displayName != name) {
                _currentUser.value = _currentUser.value?.copy(displayName = name)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Profile write skipped: ${e.message}")
        }
    }

    suspend fun updateDisplayName(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        try {
            val request = UserProfileChangeRequest.Builder().setDisplayName(trimmed).build()
            firebaseAuth?.currentUser?.updateProfile(request)?.await()
        } catch (e: Exception) {
            Log.w(TAG, "Display name update skipped: ${e.message}")
        }
        _currentUser.value = _currentUser.value?.copy(displayName = trimmed)
    }

    suspend fun syncDossierToFirestore(dossierId: Int, companyName: String, crNumber: String, status: String) {
        try {
            val user = _currentUser.value ?: return
            firestore?.collection(INVESTOR_DOSSIERS)
                ?.document("dossier_$dossierId")
                ?.set(
                    mapOf(
                        "userId" to user.uid,
                        "companyName" to companyName,
                        "crNumber" to crNumber,
                        "status" to status,
                        "updatedAt" to System.currentTimeMillis()
                    )
                )?.await()
        } catch (e: Exception) {
            Log.d(TAG, "Firestore dossier sync notice: ${e.message}")
        }
    }

    suspend fun signOut() {
        try {
            firebaseAuth?.signOut()
        } catch (e: Exception) {
            Log.w(TAG, "Sign out notice: ${e.message}")
        }
        clearCredentialState()
        _currentUser.value = null
        _authError.value = null
    }

    /**
     * Deletes the Firestore investor profile and the Firebase Auth user.
     * Play requires an in-app account deletion path when accounts exist.
     */
    suspend fun deleteAccount(): Boolean {
        _isLoading.value = true
        _authError.value = null
        try {
            val user = firebaseAuth?.currentUser
            if (user == null) {
                _authError.value = "No Firebase account is signed in."
                return false
            }
            val uid = user.uid
            try {
                firestore?.collection(INVESTOR_PROFILES)?.document(uid)?.delete()?.await()
                val dossiers = firestore?.collection(INVESTOR_DOSSIERS)
                    ?.whereEqualTo("userId", uid)
                    ?.get()
                    ?.await()
                dossiers?.documents?.forEach { document ->
                    document.reference.delete().await()
                }
            } catch (e: Exception) {
                Log.w(TAG, "Could not delete Firestore data: ${e.message}")
            }
            user.delete().await()
            clearCredentialState()
            _currentUser.value = null
            return true
        } catch (_: FirebaseAuthRecentLoginRequiredException) {
            _authError.value = "Sign in with Google again, then delete the account."
            return false
        } catch (e: Exception) {
            Log.w(TAG, "Account deletion failed: ${e.message}")
            _authError.value = e.localizedMessage ?: "Could not delete the account."
            return false
        } finally {
            _isLoading.value = false
        }
    }

    private suspend fun clearCredentialState() {
        try {
            CredentialManager.create(context).clearCredentialState(ClearCredentialStateRequest())
        } catch (e: Exception) {
            Log.d(TAG, "Credential state clear skipped: ${e.message}")
        }
    }

    private fun applyCompany(companyName: String) {
        val current = _currentUser.value ?: return
        _currentUser.value = current.copy(companyName = companyName.ifBlank { DEFAULT_COMPANY })
    }

    private fun FirebaseUser.toUserProfile(
        companyName: String,
        fallbackName: String?,
        fallbackEmail: String?
    ): UserProfile {
        val name = displayName?.takeIf { it.isNotBlank() }
            ?: fallbackName?.takeIf { it.isNotBlank() }
            ?: email?.substringBefore("@")?.takeIf { it.isNotBlank() }
            ?: "Investor"
        val mail = email?.takeIf { it.isNotBlank() }
            ?: fallbackEmail?.takeIf { it.isNotBlank() }
            ?: ""
        return UserProfile(
            uid = uid,
            displayName = name,
            email = mail,
            companyName = companyName.ifBlank { DEFAULT_COMPANY },
            photoUrl = photoUrl?.toString(),
            isVerified = isEmailVerified
        )
    }

    private fun hashedNonce(): String {
        val raw = UUID.randomUUID().toString()
        val digest = MessageDigest.getInstance("SHA-256").digest(raw.toByteArray())
        return digest.joinToString("") { byte -> "%02x".format(byte) }
    }

    private fun Context.findActivity(): Activity? {
        var current: Context = this
        while (current is ContextWrapper) {
            if (current is Activity) return current
            current = current.baseContext
        }
        return null
    }

    private companion object {
        const val TAG = "FirebaseAuthManager"
        const val DEFAULT_COMPANY = "International Investor"
    }
}
