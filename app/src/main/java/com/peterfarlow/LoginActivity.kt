package com.peterfarlow

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.peterfarlow.skeleton.BuildConfig
import com.peterfarlow.skeleton.R
import org.tinylog.kotlin.Logger

class LoginActivity : ComponentActivity() {
    private lateinit var oneTapClient: SignInClient
    private lateinit var signUpRequest: BeginSignInRequest

    private val getLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            onResult(it)
        }

    private fun onResult(activityResult: ActivityResult) {
        try {
            val credential = oneTapClient.getSignInCredentialFromIntent(activityResult.data)
            val idToken = credential.googleIdToken
            Logger.debug { "hello, ${credential.displayName}!" }
        } catch (e: ApiException) {
            Logger.debug(e) { "uh oh couldn't read credential" }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        oneTapClient = Identity.getSignInClient(this)
        signUpRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(BuildConfig.web_client_id)
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
        // ...

        findViewById<View>(R.id.textView).setOnClickListener {
            oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(this) {
                    getLauncher.launch(IntentSenderRequest.Builder(it.pendingIntent).build())
                }
                .addOnFailureListener(this) {
                    Logger.debug(it) { "failed to being sign up" }
                }
        }
    }
}
