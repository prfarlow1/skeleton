package com.peterfarlow

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.tinylog.kotlin.Logger

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val oneTapClient: SignInClient = remember { Identity.getSignInClient(context) }
    val signUpRequest: BeginSignInRequest = remember {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(context.getString(R.string.web_client_id))
                    // Show all accounts on the device.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }
    var result by remember { mutableStateOf<Intent?>(null) }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()) {
            result = it.data
        }
    val displayName = try {
        val credential = oneTapClient.getSignInCredentialFromIntent(result)
        credential.displayName
    } catch (e: ApiException) {
        Logger.debug(e) { "failed to get creds from intent" }
        "unknown"
    }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        val coroutineScope = rememberCoroutineScope()
        Text(
            text = "signing you in...",
            modifier = Modifier.clickable {
                coroutineScope.launch {
                    val beingSignInResult = try {
                        oneTapClient.beginSignIn(signUpRequest).await()
                    } catch (e: Exception) {
                        Logger.debug(e) { "no google accounts found" }
                        return@launch
                    }
                    launcher.launch(
                        IntentSenderRequest.Builder(beingSignInResult.pendingIntent).build()
                    )

                }

            },
            style = LocalTextStyle.current
        )
        Text(
            text = "Hello, $displayName!",
            style = LocalTextStyle.current
        )

    }

}
