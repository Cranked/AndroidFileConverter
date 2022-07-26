package com.cranked.androidfileconverter.ui.splash

import android.Manifest
import android.animation.AnimatorSet
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.cranked.androidcorelibrary.extension.openAppPermissionPage
import com.cranked.androidcorelibrary.local.PrefManager
import com.cranked.androidcorelibrary.ui.raw.RawActivity
import com.cranked.androidfileconverter.R
import com.cranked.androidfileconverter.ui.main.MainActivity
import com.cranked.androidfileconverter.utils.AnimationX
import com.cranked.androidfileconverter.utils.Constants
import com.cranked.androidfileconverter.utils.localization.LocalizationUtil
import net.codecision.startask.permissions.Permission
import java.util.*


class SplashActivity : RawActivity() {
    var animatorSet: AnimatorSet = AnimationX().getNewAnimatorSet()
    lateinit var permissionTemp: Permission
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale()
        setContentView(R.layout.activity_splash)
        permissionTemp =
            Permission.Builder(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).setShouldShowRationale(true)
                .setShouldRequestAutomatically(true)
                .setRequestCode(Constants.PERMISSION_REQUEST_CODE)
                .build()

//        val imageView = findViewById<ImageView>(R.id.imageView)
//
//        val pulseAnimation = object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator?) = Unit
//            override fun onAnimationEnd(animation: Animator?) {
//                imageView.clearAnimation()
//                startActivity(Intent(baseContext, MainActivity::class.java))
//                finish()
//            }
//            override fun onAnimationCancel(animation: Animator?) = Unit
//            override fun onAnimationRepeat(animation: Animator?) = Unit
//        }
//        val zoomInRightAnimation = object : Animator.AnimatorListener {
//            override fun onAnimationStart(animation: Animator?) = Unit
//            override fun onAnimationEnd(animation: Animator?) {
//                val animatorSetX =
//                    AnimationXUtils.pulse(imageView, AnimationX().getNewAnimatorSet())
//                imageView.animationStart(2000, animatorSetX, pulseAnimation)
//            }
//            override fun onAnimationCancel(animation: Animator?) = Unit
//            override fun onAnimationRepeat(animation: Animator?) = Unit
//        }
//        val animatorSetX =
//            AnimationXUtils.zoomInRight(imageView, animatorSet)
//        imageView.animationStart(1000, animatorSetX, zoomInRightAnimation)

        if (permissionTemp.isGranted(this)) {
            startActivity(MainActivity::class.java, true)
        } else {
            permissionTemp.request(this)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionTemp.onRequestPermissionsResult(this, requestCode, grantResults)
            .onGranted {
                startActivity(MainActivity::class.java, true)
            }.onDenied {
                permissionTemp.request(this)
            }.onNeverAskAgain {
                val builder = AlertDialog.Builder(this)
                builder.setCancelable(false)
                builder.setMessage(getString(R.string.necessary_permissions_description))
                builder.setPositiveButton(
                    getString(R.string.settings)
                ) { _, _ ->
                    openAppPermissionPage()
                }
                builder.setNegativeButton(
                    getString(R.string.ok)
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                builder.show()
            }
    }

    fun setLocale() {
        val prefManager = PrefManager(this)
        val language = if (prefManager.getLanguage()
                .isEmpty()
        ) Constants.DEFAULT_LANGUAGE else prefManager.getLanguage()
        LocalizationUtil.applyLanguageContext(this, Locale(language))

    }
}