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

/* This short program is used to generate OSXKeychainAuthenticationType.java
 * and OSXKeychainProtocolType.java which are simply mirrors of
 * kSecAuthenticationType* and kSecProtocolType*.
 */

#include <Security/Security.h>

#define ENUM_CLASS_HEAD(file, classname) fprintf(file, "\
package com.mcdermottroe.apple;\n\
\n\
public enum %s {\n", classname);
#define ENUM_VALUE(file, name, value) fprintf(file, "\t" name "(\"" name "\", %d),\n", value)
#define ENUM_VALUE_LAST(file, name, value) fprintf(file, "\t" name "(\"" name "\", %d);\n", value)
#define ENUM_CLASS_TAIL(file, classname) fprintf(file, "\
\n\
\tprivate final String symbol;\n\
\n\
\tprivate final int value;\n\
\n\
\t" classname "(String sym, int val) {\n\
\t\tsymbol = sym;\n\
\t\tvalue = val;\n\
\t}\n\
\n\
\tpublic int getValue() {\n\
\t\treturn value;\n\
\t}\n\
\n\
\t@Override\n\
\tpublic String toString() {\n\
\t\treturn symbol;\n\
\t}\n\
}\n\
");

/* Create OSXKeychainAuthenticationType.java. */
void generateOSXKeychainAuthenticationType(const char* filename) {
	FILE* file;

	file = fopen(filename, "w");

	ENUM_CLASS_HEAD(file, "OSXKeychainAuthenticationType");
	ENUM_VALUE(file, "Any", kSecAuthenticationTypeAny);
	ENUM_VALUE(file, "DPA", kSecAuthenticationTypeDPA);
	ENUM_VALUE(file, "Default", kSecAuthenticationTypeDefault);
	ENUM_VALUE(file, "HTMLForm", kSecAuthenticationTypeHTMLForm);
	ENUM_VALUE(file, "HTTPBasic", kSecAuthenticationTypeHTTPBasic);
	ENUM_VALUE(file, "HTTPDigest", kSecAuthenticationTypeHTTPDigest);
	ENUM_VALUE(file, "MSN", kSecAuthenticationTypeMSN);
	ENUM_VALUE(file, "NTLM", kSecAuthenticationTypeNTLM);
	ENUM_VALUE_LAST(file, "RPA", kSecAuthenticationTypeRPA);
	ENUM_CLASS_TAIL(file, "OSXKeychainAuthenticationType");

	fclose(file);
}

/* Create OSXKeychainProtocolType.java. */
void generateOSXKeychainProtocolType(const char* filename) {
	FILE* file;

	file = fopen(filename, "w");

	ENUM_CLASS_HEAD(file, "OSXKeychainProtocolType");
	ENUM_VALUE(file, "AFP", kSecProtocolTypeAFP);
	ENUM_VALUE(file, "Any", kSecProtocolTypeAny);
	ENUM_VALUE(file, "AppleTalk", kSecProtocolTypeAppleTalk);
	ENUM_VALUE(file, "CIFS", kSecProtocolTypeCIFS);
	ENUM_VALUE(file, "CVSpserver", kSecProtocolTypeCVSpserver);
	ENUM_VALUE(file, "DAAP", kSecProtocolTypeDAAP);
	ENUM_VALUE(file, "EPPC", kSecProtocolTypeEPPC);
	ENUM_VALUE(file, "FTP", kSecProtocolTypeFTP);
	ENUM_VALUE(file, "FTPAccount", kSecProtocolTypeFTPAccount);
	ENUM_VALUE(file, "FTPProxy", kSecProtocolTypeFTPProxy);
	ENUM_VALUE(file, "FTPS", kSecProtocolTypeFTPS);
	ENUM_VALUE(file, "HTTP", kSecProtocolTypeHTTP);
	ENUM_VALUE(file, "HTTPProxy", kSecProtocolTypeHTTPProxy);
	ENUM_VALUE(file, "HTTPS", kSecProtocolTypeHTTPS);
	ENUM_VALUE(file, "HTTPSProxy", kSecProtocolTypeHTTPSProxy);
	ENUM_VALUE(file, "IMAP", kSecProtocolTypeIMAP);
	ENUM_VALUE(file, "IMAPS", kSecProtocolTypeIMAPS);
	ENUM_VALUE(file, "IPP", kSecProtocolTypeIPP);
	ENUM_VALUE(file, "IRC", kSecProtocolTypeIRC);
	ENUM_VALUE(file, "IRCS", kSecProtocolTypeIRCS);
	ENUM_VALUE(file, "LDAP", kSecProtocolTypeLDAP);
	ENUM_VALUE(file, "LDAPS", kSecProtocolTypeLDAPS);
	ENUM_VALUE(file, "NNTP", kSecProtocolTypeNNTP);
	ENUM_VALUE(file, "NNTPS", kSecProtocolTypeNNTPS);
	ENUM_VALUE(file, "POP3", kSecProtocolTypePOP3);
	ENUM_VALUE(file, "POP3S", kSecProtocolTypePOP3S);
	ENUM_VALUE(file, "RTSP", kSecProtocolTypeRTSP);
	ENUM_VALUE(file, "RTSPProxy", kSecProtocolTypeRTSPProxy);
	ENUM_VALUE(file, "SMB", kSecProtocolTypeSMB);
	ENUM_VALUE(file, "SMTP", kSecProtocolTypeSMTP);
	ENUM_VALUE(file, "SOCKS", kSecProtocolTypeSOCKS);
	ENUM_VALUE(file, "SSH", kSecProtocolTypeSSH);
	ENUM_VALUE(file, "SVN", kSecProtocolTypeSVN);
	ENUM_VALUE(file, "Telnet", kSecProtocolTypeTelnet);
	ENUM_VALUE_LAST(file, "TelnetS", kSecProtocolTypeTelnetS);
	ENUM_CLASS_TAIL(file, "OSXKeychainProtocolType");

	fclose(file);
}

/* The program takes two arguments, the first is the path to
 * OSXKeychainAuthenticationType.java, the second is the path to
 * OSXKeychainProtocolType.java.
 */
int main(int argc, char** argv, char** envp) {
	if (argc != 3) {
		printf("Usage: %s /path/to/OSXKeychainAuthenticationType.java /path/to/OSXKeychainProtocolType.java\n", argv[0]);
		return 1;
	}

	generateOSXKeychainAuthenticationType(argv[1]);
	generateOSXKeychainProtocolType(argv[2]);

	return 0;
}
