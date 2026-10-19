package com.example.data.firebase

import android.content.Context
import com.example.R

/**
 * The Google Web client ID is generated from app/google-services.json
 * (OAuth client_type 3) as the string resource default_web_client_id.
 * It is never hardcoded. When that file is absent the resource does not exist
 * and Google sign-in stays disabled.
 */
object GoogleWebClientId {
    fun read(context: Context): String? {
        val packages = linkedSetOf(context.packageName)
        runCatching {
            packages.add(context.resources.getResourcePackageName(R.string.app_name))
        }
        for (pkg in packages) {
            val id = context.resources.getIdentifier("default_web_client_id", "string", pkg)
            if (id == 0) continue
            val value = runCatching { context.getString(id).trim() }.getOrNull()
            if (!value.isNullOrEmpty()) return value
        }
        return null
    }
}
