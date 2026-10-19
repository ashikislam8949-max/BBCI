package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.firebase.GoogleWebClientId
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SBTECH Investor Desk: Saudi Business Setup", appName)
  }

  @Test
  fun `web client id is absent without google-services json`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    assertEquals(0, context.resources.getIdentifier("default_web_client_id", "string", context.packageName))
    assertNull(GoogleWebClientId.read(context))
  }

}
