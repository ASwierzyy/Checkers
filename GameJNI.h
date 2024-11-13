/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class GameJNI */

#ifndef _Included_GameJNI
#define _Included_GameJNI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     GameJNI
 * Method:    initializeGame
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_GameJNI_initializeGame
  (JNIEnv *, jobject);

/*
 * Class:     GameJNI
 * Method:    makeMove
 * Signature: (IIII)Z
 */
JNIEXPORT jboolean JNICALL Java_GameJNI_makeMove
  (JNIEnv *, jobject, jint, jint, jint, jint);

/*
 * Class:     GameJNI
 * Method:    getBoardState
 * Signature: ()[[I
 */
JNIEXPORT jobjectArray JNICALL Java_GameJNI_getBoardState
  (JNIEnv *, jobject);

/*
 * Class:     GameJNI
 * Method:    isGameOver
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_GameJNI_isGameOver
  (JNIEnv *, jobject);

/*
 * Class:     GameJNI
 * Method:    getCurrentPlayer
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_GameJNI_getCurrentPlayer
  (JNIEnv *, jobject);

/*
 * Class:     GameJNI
 * Method:    resetGame
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_GameJNI_resetGame
  (JNIEnv *, jobject);

/*
 * Class:     GameJNI
 * Method:    setBoardState
 * Signature: ([[I)V
 */
JNIEXPORT void JNICALL Java_GameJNI_setBoardState
  (JNIEnv *, jobject, jobjectArray);

#ifdef __cplusplus
}
#endif
#endif
