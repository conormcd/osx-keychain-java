/*
 * Copyright (c) 2011, Conor McDermottroe
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

/* Prevent the real jni.h from being included. */
#define _JAVASOFT_JNI_H_

/* Map the JNI types we use to some useful fakes. */
#define JNIEXPORT
#define JNICALL
#define JNIEnv fakejni_env*
#define jclass void*
#define jobject void*
#define jstring char*
#define jint int
#define jbyte char
#define jboolean int
#define jsize int

/* Something to use as an env* for JNI functions. */
typedef struct {
	void (*DeleteLocalRef)(void *env, jobject lref);
	void* (*FindClass)(void*, const char*);
	int (*GetStringLength)(void*, jstring);
	const jbyte * (*GetStringUTFChars)(void*, jstring, jboolean *);
	jsize (*GetStringUTFLength)(void *env, jstring string);
	void (*GetStringUTFRegion)(void*, jstring, int, int, char*);
	char* (*NewStringUTF)(void*, char*);
	void (*ReleaseStringUTFChars)(void *env, jstring string, const char *utf);
	void (*ThrowNew)(void*, jclass, const char*);
} fakejni_env;

/* Use this to initialise a fakejni_env. */
void fakejni_init(fakejni_env*);
