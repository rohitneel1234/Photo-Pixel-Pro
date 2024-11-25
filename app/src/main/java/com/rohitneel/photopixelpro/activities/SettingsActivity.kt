package com.rohitneel.photopixelpro.activities

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rohitneel.photopixelpro.BuildConfig
import com.rohitneel.photopixelpro.R
import com.rohitneel.photopixelpro.constant.CommonKeys
import com.rohitneel.photopixelpro.helper.SessionManager
import com.rohitneel.photopixelpro.photocollage.dialog.RateDialog
import com.rohitneel.photopixelpro.photoeditor.ShowSavedFilePath
import com.rohitneel.photopixelpro.tutorials.TutorialActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis


class SettingsActivity : AppCompatActivity() {
    private var mSwitchCompat: SwitchCompat? = null
    private var mSession: SessionManager? = null
    private var constraintLayout: ConstraintLayout? = null
    private var mClSaveOption : ConstraintLayout? = null
    private var mClHelpAndSupport : ConstraintLayout? = null
    private var mClRateUs : ConstraintLayout? = null
    private var mClShareApp : ConstraintLayout? = null
    private var mClMoreApps : ConstraintLayout? = null
    private var mClDeveloper : ConstraintLayout? = null
    private var mSwitchFullScreen: SwitchCompat? = null
    private var mSwitchAppPermission: SwitchCompat? = null
    private val REQUEST_PERMISSION_CODE = 7
    private var mTxtTutorial: TextView? = null
    private var mTxtDark: TextView? = null
    private var mTxtFullScreen: TextView? = null
    private var mTxtSave: TextView? = null
    private var mTxtPermission: TextView? = null
    private var mTxtContactUs: TextView? = null
    private var mTxtAppSettingsTitle: TextView? = null
    private var mTxtSaveTitle: TextView? = null
    private var mTxtRateUs: TextView? = null
    private var mTxtShareApp: TextView? = null
    private var mTxtMoreApps: TextView? = null
    private var mTxtDeveloper: TextView? = null
    private var mTxtVersion: TextView? = null
    private var mTxtAppHelpTitle: TextView? = null
    private var txtAppVersion: TextView? = null
    private var mTxtAboutTitle: TextView? = null
    private var versionName: String? = null
    private var imgDarkMode: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        mSession = SessionManager(applicationContext)
        mSwitchCompat = findViewById<View>(R.id.switchTheme) as SwitchCompat
        mSwitchFullScreen = findViewById<View>(R.id.switchFullScreen) as SwitchCompat
        mSwitchAppPermission = findViewById<View>(R.id.switchAppPermission) as SwitchCompat
        constraintLayout = findViewById<ConstraintLayout>(R.id.clTutorial)
        mClSaveOption = findViewById<ConstraintLayout>(R.id.clSaveOption)
        mClHelpAndSupport = findViewById<ConstraintLayout>(R.id.clContactUs)
        mClRateUs = findViewById<ConstraintLayout>(R.id.clRateUs)
        mClShareApp = findViewById<ConstraintLayout>(R.id.clShareApp)
        mClMoreApps = findViewById<ConstraintLayout>(R.id.clMoreApps)
        mClDeveloper = findViewById<ConstraintLayout>(R.id.clDeveloper)
        mTxtTutorial = findViewById<TextView>(R.id.txtTutorial)
        mTxtDark = findViewById<TextView>(R.id.txtDarkMode)
        mTxtFullScreen = findViewById<TextView>(R.id.txtFullScreen)
        mTxtSave = findViewById<TextView>(R.id.txtSaveOption)
        mTxtPermission = findViewById<TextView>(R.id.txtAppPermission)
        mTxtContactUs =  findViewById<TextView>(R.id.txtContactUs)
        mTxtRateUs =  findViewById<TextView>(R.id.txtRateUs)
        mTxtShareApp =  findViewById<TextView>(R.id.txtShareApp)
        mTxtMoreApps =  findViewById<TextView>(R.id.txtMoreApps)
        mTxtDeveloper =  findViewById<TextView>(R.id.txtDeveloper)
        mTxtVersion =  findViewById<TextView>(R.id.txtVersion)
        mTxtAppSettingsTitle = findViewById(R.id.txtAppSettingsTitle)
        mTxtSaveTitle = findViewById(R.id.txtSaveTitle)
        mTxtAppHelpTitle = findViewById(R.id.txtHelpTitle)
        mTxtAboutTitle = findViewById(R.id.txtAboutTitle);
        txtAppVersion = findViewById(R.id.txtVersionName)
        imgDarkMode = findViewById(R.id.imgTheme)

        constraintLayout!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, TutorialActivity::class.java)
            intent.putExtra(CommonKeys.mSettingsKey, "settings")
            startActivity(intent)
        })

        mClSaveOption!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(applicationContext, ShowSavedFilePath::class.java))
        })

        mClHelpAndSupport!!.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ContactUs::class.java))
        })

        mClRateUs!!.setOnClickListener(View.OnClickListener {
            RateDialog(this, false).show()
        })

        mClShareApp!!.setOnClickListener(View.OnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Photo Pixel Pro")
            var shareMessage = "Let me recommend you this application. It is easy and amazing app for creative photo editing. Download & check it out!\n".trimIndent()
            shareMessage = """
                    ${shareMessage} https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}
                    """.trimIndent()
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        })

        mClMoreApps!!.setOnClickListener(View.OnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_string))))
            } catch (e: ActivityNotFoundException) {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_developer_string))))
                } catch (e: Throwable) {
                    Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Throwable) {
                Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show()
            }
        })

        mClDeveloper!!.setOnClickListener(View.OnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://zaap.bio/rohitneel")
                startActivity(intent)
            } catch (e: Throwable) {
                Toast.makeText(this, "something went wrong!", Toast.LENGTH_SHORT).show()
            }
        })

        val manager = this.packageManager
        try {
            val info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
            versionName = info.versionName
            txtAppVersion?.text = String.format("%s", "v$versionName")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        if (mSession!!.loadState()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            setTheme(R.style.darkTheme)
            mTxtTutorial!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtDark!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtFullScreen!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtSave!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtPermission!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtContactUs!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtRateUs!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtShareApp!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtMoreApps!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtDeveloper!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtVersion!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            txtAppVersion!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtAppSettingsTitle!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtSaveTitle!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtAppHelpTitle!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            mTxtAboutTitle!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.white))
            imgDarkMode!!.setColorFilter(ContextCompat.getColor(this, R.color.white))
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            setTheme(R.style.AppTheme)
            mTxtTutorial!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtDark!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtFullScreen!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtSave!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtPermission!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtContactUs!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtRateUs!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtShareApp!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtMoreApps!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtDeveloper!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            mTxtVersion!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
            txtAppVersion!!.setTextColor(ContextCompat.getColor(applicationContext, R.color.settingsTextColor))
        }

        //calling dark theme state to load and save
        loadDarkThemeState()

        if (mSession!!.loadFullScreenState()) {
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            this.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        //Calling full screen state to load and save
        loadFullScreenState()

        loadAppPermission()

        GlobalScope.launch {
            val time = measureTimeMillis {
                val one = sampleOne()
                val two = sampleTwo()
                println("The answer is ${one + two}")
            }
            println("Completed in $time ms")
        }
        println("EOF")
    }

    private suspend fun sampleOne(): Int {
        println( "sampleOne"+System.currentTimeMillis())
        delay(1000L) // pretend we are doing something useful here
        return 10
    }

    private suspend fun sampleTwo(): Int {
        println( "sampleTwo"+System.currentTimeMillis())
        delay(1000L) // pretend we are doing something useful here, too
        return 10
    }


    private fun loadAppPermission() {
        if (mSession!!.loadAppPermissionState() && checkingPermissionIsEnabledOrNot()) {
            mSwitchAppPermission!!.isChecked = true
        }
        mSwitchAppPermission!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mSession!!.saveAppPermissionState(true)
                requestMultiplePermission()
            }
            else {
                mSession!!.saveAppPermissionState(false)
            }
        }
    }

    private fun requestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ), REQUEST_PERMISSION_CODE)
    }

    private fun checkingPermissionIsEnabledOrNot(): Boolean {
        val firstPermissionResult = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
        val secondPermissionResult = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return firstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                secondPermissionResult == PackageManager.PERMISSION_GRANTED
    }

    private fun loadFullScreenState() {
        if (mSession!!.loadFullScreenState()) {
            mSwitchFullScreen!!.isChecked = true
        }

        mSwitchFullScreen!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                this.window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                this.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mSession!!.saveFullScreenState(true)
            } else {
                this.window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                this.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                mSession!!.saveFullScreenState(false)
            }
        }
    }

    private fun loadDarkThemeState() {
        if (mSession!!.loadState()) {
            mSwitchCompat!!.isChecked = true
        }

        mSwitchCompat!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                mSession!!.saveState(true)
                recreate()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                mSession!!.saveState(false)
            }
        }
    }

}