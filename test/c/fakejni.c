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

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "fakejni.h"

/* A replacement for JNI's (*env)->FindClass. Don't use the result of this
 * function for anything, bad things will happen if you do.
 */
void* fakejni_FindClass(void* env, const char* exceptionClass) {
	return (void*)"Don't use this";
}

/* A replacement for JNI's (*env)->GetStringLength. */
int fakejni_GetStringLength(void* env, jstring str) {
	return strlen(str);
}

/* A replacement for JNI's (*env)->GetStringUTFRegion. */
void fakejni_GetStringUTFRegion(void* env, jstring src, int offset, int length, char* dst) {
	int dstidx;
	int srcidx;
	for (dstidx = 0, srcidx = offset; dstidx < length; srcidx++, dstidx++) {
		dst[dstidx] = src[srcidx];
	}
}

/* A replacement for JNI's (*env)->NewStringUTF. */
char* fakejni_NewStringUTF(void* env, char* str) {
	return str;
}

/* A replacement for JNI's (*env)->ThrowNew. */
void fakejni_ThrowNew(void* env, jclass cls, const char* message) {
	printf("Exception: %s\n", message);
	exit(1);
}

/* Initialise a fakejni_env. */
void fakejni_init(fakejni_env* env) {
	env->FindClass = &fakejni_FindClass;
	env->GetStringLength = &fakejni_GetStringLength;
	env->GetStringUTFRegion = &fakejni_GetStringUTFRegion;
	env->NewStringUTF = &fakejni_NewStringUTF;
	env->ThrowNew = &fakejni_ThrowNew;
}
