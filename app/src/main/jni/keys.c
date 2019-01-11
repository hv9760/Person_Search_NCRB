#include <jni.h>
JNIEXPORT jstring JNICALL

// pattern must be same as below Java_<package name>_<java class name>_<method to access values>
Java_com_ncrb_samapre_myapplication_MCoCoRy_getCocoSeed(JNIEnv *env, jobject instance){

 return (*env)->  NewStringUTF(env, "Q29jb1NlZWQ1ZWNyZXRQQHNzdzByZDA1");
}


JNIEXPORT jstring JNICALL

// pattern must be same as below Java_<package name>_<java class name>_<method to access values>
Java_com_ncrb_samapre_myapplication_MCoCoRy_getCocoSeed0(JNIEnv *env, jobject instance){

 return (*env)->NewStringUTF(env, "Q29jb1NlZWQ1ZWNyZXRQQHNzdzByZDU1");
}
