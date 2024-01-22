package org.middle.earth.lotr.application

import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.android.gms.security.ProviderInstaller
import dagger.hilt.android.HiltAndroidApp

private const val TAG = "LOTRApplication"

@HiltAndroidApp
class TheLordOfTheRingsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        upgradeSecurityProvider()
    }

    private fun upgradeSecurityProvider() {
        ProviderInstaller.installIfNeededAsync(this@TheLordOfTheRingsApplication, object : ProviderInstaller.ProviderInstallListener {
            override fun onProviderInstalled() {
                // Intentionally Left Blank
            }

            override fun onProviderInstallFailed(errorCode: Int, recoveryIntent: Intent?) {
                val message = " override fun onProviderInstallFailed($errorCode: Int, $recoveryIntent: Intent?) {}"

                Log.e(TAG, "onProviderInstallFailed: $message", RuntimeException(message))
            }
        })
    }
}