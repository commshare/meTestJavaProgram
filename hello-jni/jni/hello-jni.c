/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include<stdio.h>

/* ========
   Android Porting
   ======== */
#ifdef __ANDROID__
#include <android/log.h>

static int my_fprintf(FILE *stream, const char *format, ...){
    va_list ap;
    va_start(ap, format);
    __android_log_vprint(ANDROID_LOG_DEBUG, "XXX", format, ap);
    va_end(ap);
    return 0;
}
#ifdef fprintf
#undef fprintf
#endif
#define fprintf(fp,...) my_fprintf(fp, __VA_ARGS__)

#endif /*__ANDROID__*/



/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */
jstring
Java_com_example_hellojni_HelloJni_stringFromJNI( JNIEnv* env,
                                                  jobject thiz )
{
	fprintf(stderr,"this is %s %d fprintf test \n","zhangbin",1);
    return (*env)->NewStringUTF(env, "Hello from JNI for test fprintf!");
}
