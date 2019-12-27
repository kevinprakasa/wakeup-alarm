#include <jni.h>
#include <string>

extern "C" JNIEXPORT jint JNICALL
Java_id_ac_ui_cs_mobileprogramming_kevinprakasa_wakeup_CreateAlarmActivity_addition(
        JNIEnv *env,
        jobject instance, jint a, jint b) {
    return a+b;
}
