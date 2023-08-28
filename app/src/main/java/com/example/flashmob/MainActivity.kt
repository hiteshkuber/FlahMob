package com.example.flashmob

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.example.flashmob.ui.theme.FlashMobTheme
import com.example.flashmob.ui.theme.MyViewModel
import com.example.flashmob.ui.theme.composables.CustomCircularProgressIndicator
import com.example.flashmob.ui.theme.composables.RoundedCornerButton
import com.example.flashmob.ui.theme.darkGray
import com.example.flashmob.ui.theme.green


class MainActivity : ComponentActivity() {

    private var myViewModel: MyViewModel? = null
    private var mCameraManager: CameraManager? = null
    private var mCameraId: String? = null
    private val handler = Handler(Looper.getMainLooper())
    private var flashDuration: Long = 500

    private val flashRunnable = object : Runnable {
        override fun run() {

            this@MainActivity.myViewModel?.let {

                if (it.isOn.value == true) {
                    toggleFlashLight()
                    handler.postDelayed(this, it.speed.value ?: flashDuration)
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cameraManager = applicationContext.getSystemService(Context.CAMERA_SERVICE)
        mCameraManager = if (cameraManager is CameraManager) { cameraManager } else { null }
        mCameraId = mCameraManager?.cameraIdList?.get(0)
        myViewModel = ViewModelProvider(this)[MyViewModel::class.java]

        setContent {
            FlashMobTheme {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(darkGray),
                    contentAlignment = Alignment.Center,
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CustomCircularProgressIndicator(
                            initialValue = 50,
                            primaryColor = green,
                            secondaryColor = Color.Yellow,
                            circleRadius = 250f,
                            onPositionChange = { position ->
                                handler.removeCallbacks(flashRunnable)
                                if(position != 0) {
                                    myViewModel?.updateSpeed(flashDuration - position*5 + 1)
                                    handler.post(flashRunnable)
                                } else {
                                    myViewModel?.updateSpeed(0)
                                    switchFlashLight(myViewModel?.isOn?.value?:false)
                                }
                            },
                            modifier = Modifier
                                .size(250.dp)
                                .background(darkGray)
                        )

                        RoundedCornerButton(
                            onButtonToggle = { value ->
                                myViewModel?.updateTorchState(value)
                                handler.removeCallbacks(flashRunnable)
                                switchFlashLight(value)

                                if(value) {
                                    myViewModel!!.speed.value?.let {
                                        if (it.toInt() != 0) {
                                            myViewModel?.updateSpeed(it)
                                            handler.post(flashRunnable)
                                        }
                                    }
                                }
                            }
                        )
                    }
                }

            }
        }
    }

    private fun switchFlashLight(status: Boolean) {
        try {
            mCameraManager!!.setTorchMode(mCameraId!!, status)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(flashRunnable)
    }

    fun toggleFlashLight() {
        mCameraManager!!.setTorchMode(mCameraId!!, myViewModel?.discoOn?.value ?: false)
        myViewModel?.updateDiscoState(!(myViewModel?.discoOn?.value ?: true))
    }
}