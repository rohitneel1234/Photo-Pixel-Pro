package com.rohitneel.photopixelpro.app

import android.app.Application
import android.content.Context

class ProjectApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

    companion object {
        @JvmStatic
        var appContext: Context? = null
            private set
    }



}



