#include "GameJNI.h"
#include "GameLogic.h"
#include <vector>

GameLogic gameLogic;

JNIEXPORT void JNICALL Java_GameJNI_initializeGame(JNIEnv *env, jobject obj) {
    gameLogic.initializeGame();
}

JNIEXPORT jboolean JNICALL Java_GameJNI_makeMove(JNIEnv *env, jobject obj, jint fromRow, jint fromCol, jint toRow, jint toCol) {
    bool result = gameLogic.makeMove(fromRow, fromCol, toRow, toCol);
    return result ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jobjectArray JNICALL Java_GameJNI_getBoardState(JNIEnv *env, jobject obj) {
    const std::vector<std::vector<int>>& board = gameLogic.getBoardState();
    int rows = board.size();
    int cols = board[0].size();

    jclass intArrayClass = env->FindClass("[I");
    jobjectArray boardArray = env->NewObjectArray(rows, intArrayClass, nullptr);

    for (int i = 0; i < rows; ++i) {
        jintArray rowArray = env->NewIntArray(cols);
        env->SetIntArrayRegion(rowArray, 0, cols, board[i].data());
        env->SetObjectArrayElement(boardArray, i, rowArray);
        env->DeleteLocalRef(rowArray);
    }

    return boardArray;
}

JNIEXPORT jboolean JNICALL Java_GameJNI_isGameOver(JNIEnv *env, jobject obj) {
    bool result = gameLogic.isGameOver();
    return result ? JNI_TRUE : JNI_FALSE;
}

JNIEXPORT jint JNICALL Java_GameJNI_getCurrentPlayer(JNIEnv *env, jobject obj) {
    int player = gameLogic.getCurrentPlayer();
    return static_cast<jint>(player);
}

JNIEXPORT void JNICALL Java_GameJNI_resetGame(JNIEnv *env, jobject obj) {
    gameLogic.resetGame();
}

JNIEXPORT void JNICALL Java_GameJNI_setBoardState(JNIEnv *env, jobject obj, jobjectArray boardStateArray) {
    jsize rows = env->GetArrayLength(boardStateArray);
    std::vector<std::vector<int>> boardState(rows);

    for (jsize i = 0; i < rows; ++i) {
        jintArray rowArray = (jintArray)env->GetObjectArrayElement(boardStateArray, i);
        jsize cols = env->GetArrayLength(rowArray);
        std::vector<int> row(cols);

        env->GetIntArrayRegion(rowArray, 0, cols, row.data());
        boardState[i] = row;

        env->DeleteLocalRef(rowArray);
    }

    gameLogic.setBoardState(boardState);
}