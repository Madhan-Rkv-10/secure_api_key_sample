<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Secure Facebook App ID via NDK (Flutter Android)</title>
</head>
<body>
  <h1>🏗️ Secure Facebook App ID via Android NDK</h1>
  <p>This guide helps you store your Facebook App ID in native C++ (.so), and retrieve it in Flutter without exposing the key in <code>strings.xml</code> or the manifest.</p>

  <h2>📁 1. Project Structure</h2>
  <pre>
android/app/src/main/
├── cpp/
│   ├── native-lib.cpp
│   └── CMakeLists.txt
└── kotlin/com/example/sample_secure_key_app/
    └── MainActivity.kt
  </pre>

  <h2>🧩 2. native-lib.cpp</h2>
  <pre><code>#include &lt;jni.h&gt;

// If your package name or class has underscores (\_),
// escape each underscore as '\_1' in the JNI function name.
// e.g., com.example.sample_secure_key_app becomes:
// Java_com_example_sample_1secure_1key_1app_MainActivity_getFacebookAppId

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_sample_1secure_1key_1app_MainActivity_getFacebookAppId(
JNIEnv* env, jobject /* this \*/) {
return env->NewStringUTF("123456789012345"); // ← YOUR REAL APP ID
}
</code></pre>

  <p><em>Escaping with “_1” is required by JNI naming rules when handling underscores in package or class names.</em> :contentReference[oaicite:1]{index=1}</p>

  <h2>🔧 3. CMakeLists.txt</h2>
  <pre><code>cmake_minimum_required(VERSION 3.4.1)
add_library(native-lib SHARED native-lib.cpp)
find_library(log-lib log)
target_link_libraries(native-lib ${log-lib})
</code></pre>

  <h2>⚙️ 4. build.gradle.kts</h2>
  <pre><code>android {
  compileSdk = 34

defaultConfig {
applicationId = "com.example.sample_secure_key_app"
minSdk = 21
targetSdk = 34

    externalNativeBuild {
      cmake {
        path = file("src/main/cpp/CMakeLists.txt")
        cppFlags("-fexceptions", "-frtti")
      }
    }

    ndk {
      abiFilters += setOf("armeabi-v7a", "arm64-v8a")
    }

}
}</code></pre>

  <h2>📱 5. MainActivity.kt</h2>
  <pre><code>package com.example.sample_secure_key_app

import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
companion object {
init { System.loadLibrary("native-lib") }
}

external fun getFacebookAppId(): String

override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
super.configureFlutterEngine(flutterEngine)
val appId = getFacebookAppId()

    MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "secure_channel")
      .setMethodCallHandler { call, result ->
        when (call.method) {
          "getFacebookAppId" -> result.success(appId)
          else -> result.notImplemented()
        }
      }

}
}
</code></pre>

  <h2>🧩 6. Flutter (Dart) Usage</h2>
  <pre><code>import 'package:flutter/services.dart';

class SecureApi {
static const \_chan = MethodChannel('secure_channel');
static Future<String> getFacebookAppId() async {
final id = await \_chan.invokeMethod<String>('getFacebookAppId');
return id!;
}
}

// Usage example:
final fbAppId = await SecureApi.getFacebookAppId();
print('Facebook App ID: $fbAppId');
</code></pre>

  <h2>🚀 7. Build & Verify</h2>
  <ul>
    <li>Run <code>flutter clean</code> and <code>flutter build apk --release</code></li>
    <li>Inspect the APK to confirm the App ID isn’t in XML</li>
    <li>Test fetching via MethodChannel in Dart</li>
  </ul>

  <h2>🔒 Security Notes</h2>
  <ul>
    <li>Storing the key in native code makes it harder to reverse engineer, though not impervious :contentReference[oaicite:2]{index=2}</li>
    <li>Removing all XML references ensures it's not trivially exposed</li>
  </ul>
</body>
</html>
