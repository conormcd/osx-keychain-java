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

#define ENUM_CLASS_HEAD(file, classname) fprintf(file, "\
package com.mcdermottroe.apple;\n\
\n\
/** Auto-generated, see the Keychain Services Reference for descriptions of what\n\
 *	these constants mean.\n\
 */\n\
public enum %s {\n", classname);
#define ENUM_VALUE(file, name, value) fprintf(file, "\t/** " #value " */\n\t" name "(\"" name "\", %d)", value)
#define ENUM_VALUE_DEF(file, name, value) ENUM_VALUE(file, name, value); fprintf(file, ",\n\n");
#define ENUM_VALUE_LAST(file, name, value) ENUM_VALUE(file, name, value); fprintf(file, ";\n");
#define ENUM_CLASS_TAIL(file, classname) fprintf(file, "\
\n\
\t/** The name of the constant. */\n\
\tprivate final String symbol;\n\
\n\
\t/** The value of the constant. */\n\
\tprivate final int value;\n\
\n\
\t/** Create the constant. \n\
\t *\n\
\t *\t@param sym The name of the constant.\n\
\t *\t@param val The value of the constant.\n\
\t */\n\
\t" classname "(String sym, int val) {\n\
\t\tsymbol = sym;\n\
\t\tvalue = val;\n\
\t}\n\
\n\
\t/** Get the value of the constant.\n\
\t *\n\
\t *\t@return	The value of the constant.\n\
\t */\n\
\tpublic int getValue() {\n\
\t\treturn value;\n\
\t}\n\
\n\
\t/** {@inheritDoc} */\n\
\t@Override\n\
\tpublic String toString() {\n\
\t\treturn symbol;\n\
\t}\n\
}\n\
");
