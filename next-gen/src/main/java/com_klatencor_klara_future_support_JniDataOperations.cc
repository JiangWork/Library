#include "com_klatencor_klara_future_jni_JniDataOperator.h"
#include <string.h>

class Point
{
  public:
      setPoint(int x, int y) {
        xPos = x;
        yPos = y;
      }
  private:
      int xPos;
      int yPos;
}

JNIEXPORT jboolean JNICALL Java_com_klatencor_klara_future_jni_JniDataOperator_jniGenerateXml
  (JNIEnv *env, jobject object, jstring inPath, jstring outPath) {
    const char *instr = env->GetStringUTFChars(inPath, 0); 
    const char *outstr = env->GetStringUTFChars(outPath, 0); 
    // generate the XML here.
    env->ReleaseStringUTFChars(inPath, instr);
    env->ReleaseStringUTFChars(outPath, outstr);
    return true;
  }


JNIEXPORT jobject JNICALL Java_com_klatencor_klara_future_jni_JniDataOperator_jniLoadRecipe
  (JNIEnv *env, jobject obj, jstring string) {
    Point* point = new Point();
    return env->NewDirectByteBuffer(point, 0);
  }

JNIEXPORT jobject JNICALL Java_com_klatencor_klara_future_jni_JniDataOperator_jniLoadInspection
  (JNIEnv *env, jobject obj, jstring string) {
    Point* point = new Point();
    return env->NewDirectByteBuffer(point, 0);
  }


JNIEXPORT jbyteArray JNICALL Java_com_klatencor_klara_future_jni_JniDataOperator_jniGetImageData
  (JNIEnv *env, jobject obj, jobject handler, jint defectIndex) {
     Point* point= (Point*) env->GetDirectBufferAddress(handler);
     jbyteArray result;
     // get the byte array from Inspection defectIndex
     byte[] imageData = new byte[10];
     int size = sizeof(imageData);
     result = env->NewByteArray(env, size);
     if (result == NULL) {
       return NULL; /* out of memory error thrown */
     }
     env->SetByteArrayRegion(env, result, 0, size, (jbyte*) imageData);
     return result;
 }