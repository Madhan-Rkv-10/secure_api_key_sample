// #include <jni.h>

// extern "C" JNIEXPORT jstring JNICALL
// Java_com_example_sample_secure_key_app_MainActivity_getFacebookAppId(JNIEnv *env, jobject /* this */)
// {
//     return env->NewStringUTF("ourKey");
// }
#include <jni.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_sample_1secure_1key_1app_MainActivity_getFacebookAppId(JNIEnv *env, jobject /* this */)
{
    return env->NewStringUTF("123456789012345"); // Your real App ID
}