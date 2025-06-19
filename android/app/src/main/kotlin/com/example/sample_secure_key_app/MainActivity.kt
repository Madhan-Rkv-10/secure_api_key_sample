package com.example.sample_secure_key_app

import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    external fun getFacebookAppId(): String

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        val appId = getFacebookAppId()

        // If you want to initialize the Facebook SDK in native code:
        // FacebookSdk.setApplicationId(appId)
        // FacebookSdk.sdkInitialize(applicationContext)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "secure_channel")
            .setMethodCallHandler { call, result ->
                when (call.method) {
                    "getFacebookAppId" -> result.success(appId)
                    else -> result.notImplemented()
                }
            }
    }
}
