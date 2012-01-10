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

/* This short program is used to generate:
 *		OSXKeychainAuthenticationType.java
 *		OSXKeychainProtocolType.java
 *		OSXKeychainItemType.java
 *		OSXKeychainItemAttributeType.java
 * which are simply mirrors of:
 *		kSecAuthenticationType*
 *		kSecProtocolType*
 *		kSec*ItemClass
 *		kSec*ItemAttr
 */

#include <Security/Security.h>

#include "enum_template.h"
#include "enum_template_typed.h"

/* Create OSXKeychainAuthenticationType.java. */
void generateOSXKeychainAuthenticationType(const char* filename) {
	FILE* file;

	file = fopen(filename, "w");

	ENUM_CLASS_HEAD(file, "OSXKeychainAuthenticationType");
	ENUM_VALUE_DEF(file, "Any", kSecAuthenticationTypeAny);
	ENUM_VALUE_DEF(file, "DPA", kSecAuthenticationTypeDPA);
	ENUM_VALUE_DEF(file, "Default", kSecAuthenticationTypeDefault);
	ENUM_VALUE_DEF(file, "HTMLForm", kSecAuthenticationTypeHTMLForm);
	ENUM_VALUE_DEF(file, "HTTPBasic", kSecAuthenticationTypeHTTPBasic);
	ENUM_VALUE_DEF(file, "HTTPDigest", kSecAuthenticationTypeHTTPDigest);
	ENUM_VALUE_DEF(file, "MSN", kSecAuthenticationTypeMSN);
	ENUM_VALUE_DEF(file, "NTLM", kSecAuthenticationTypeNTLM);
	ENUM_VALUE_LAST(file, "RPA", kSecAuthenticationTypeRPA);
	ENUM_CLASS_TAIL(file, "OSXKeychainAuthenticationType");

	fclose(file);
}

/* Create OSXKeychainProtocolType.java. */
void generateOSXKeychainProtocolType(const char* filename) {
	FILE* file;

	file = fopen(filename, "w");

	ENUM_CLASS_HEAD(file, "OSXKeychainProtocolType");
	ENUM_VALUE_DEF(file, "AFP", kSecProtocolTypeAFP);
	ENUM_VALUE_DEF(file, "Any", kSecProtocolTypeAny);
	ENUM_VALUE_DEF(file, "AppleTalk", kSecProtocolTypeAppleTalk);
	ENUM_VALUE_DEF(file, "CIFS", kSecProtocolTypeCIFS);
	ENUM_VALUE_DEF(file, "CVSpserver", kSecProtocolTypeCVSpserver);
	ENUM_VALUE_DEF(file, "DAAP", kSecProtocolTypeDAAP);
	ENUM_VALUE_DEF(file, "EPPC", kSecProtocolTypeEPPC);
	ENUM_VALUE_DEF(file, "FTP", kSecProtocolTypeFTP);
	ENUM_VALUE_DEF(file, "FTPAccount", kSecProtocolTypeFTPAccount);
	ENUM_VALUE_DEF(file, "FTPProxy", kSecProtocolTypeFTPProxy);
	ENUM_VALUE_DEF(file, "FTPS", kSecProtocolTypeFTPS);
	ENUM_VALUE_DEF(file, "HTTP", kSecProtocolTypeHTTP);
	ENUM_VALUE_DEF(file, "HTTPProxy", kSecProtocolTypeHTTPProxy);
	ENUM_VALUE_DEF(file, "HTTPS", kSecProtocolTypeHTTPS);
	ENUM_VALUE_DEF(file, "HTTPSProxy", kSecProtocolTypeHTTPSProxy);
	ENUM_VALUE_DEF(file, "IMAP", kSecProtocolTypeIMAP);
	ENUM_VALUE_DEF(file, "IMAPS", kSecProtocolTypeIMAPS);
	ENUM_VALUE_DEF(file, "IPP", kSecProtocolTypeIPP);
	ENUM_VALUE_DEF(file, "IRC", kSecProtocolTypeIRC);
	ENUM_VALUE_DEF(file, "IRCS", kSecProtocolTypeIRCS);
	ENUM_VALUE_DEF(file, "LDAP", kSecProtocolTypeLDAP);
	ENUM_VALUE_DEF(file, "LDAPS", kSecProtocolTypeLDAPS);
	ENUM_VALUE_DEF(file, "NNTP", kSecProtocolTypeNNTP);
	ENUM_VALUE_DEF(file, "NNTPS", kSecProtocolTypeNNTPS);
	ENUM_VALUE_DEF(file, "POP3", kSecProtocolTypePOP3);
	ENUM_VALUE_DEF(file, "POP3S", kSecProtocolTypePOP3S);
	ENUM_VALUE_DEF(file, "RTSP", kSecProtocolTypeRTSP);
	ENUM_VALUE_DEF(file, "RTSPProxy", kSecProtocolTypeRTSPProxy);
	ENUM_VALUE_DEF(file, "SMB", kSecProtocolTypeSMB);
	ENUM_VALUE_DEF(file, "SMTP", kSecProtocolTypeSMTP);
	ENUM_VALUE_DEF(file, "SOCKS", kSecProtocolTypeSOCKS);
	ENUM_VALUE_DEF(file, "SSH", kSecProtocolTypeSSH);
	ENUM_VALUE_DEF(file, "SVN", kSecProtocolTypeSVN);
	ENUM_VALUE_DEF(file, "Telnet", kSecProtocolTypeTelnet);
	ENUM_VALUE_LAST(file, "TelnetS", kSecProtocolTypeTelnetS);
	ENUM_CLASS_TAIL(file, "OSXKeychainProtocolType");

	fclose(file);
}

/* Create OSXKeychainItemType.java. */
void generateOSXKeychainItemType(const char* filename) {
	FILE* file;

	file = fopen(filename, "w");

	ENUM_CLASS_HEAD(file, "OSXKeychainItemType");
	ENUM_VALUE_DEF(file, "AppleSharePassword", kSecAppleSharePasswordItemClass);
	ENUM_VALUE_DEF(file, "Certificate", kSecCertificateItemClass);
	ENUM_VALUE_DEF(file, "GenericPassword", kSecGenericPasswordItemClass);
	ENUM_VALUE_DEF(file, "InternetPassword", kSecInternetPasswordItemClass);
	ENUM_VALUE_DEF(file, "PrivateKey", kSecPrivateKeyItemClass);
	ENUM_VALUE_DEF(file, "PublicKey", kSecPublicKeyItemClass);
	ENUM_VALUE_LAST(file, "SymmetricKey", kSecSymmetricKeyItemClass);
	ENUM_CLASS_TAIL(file, "OSXKeychainItemType");

	fclose(file);
}

/* Create OSXKeychainItemAttributeType.java. */
void generateOSXKeychainItemAttributeType(const char* filename) {
	FILE* file;

	file = fopen(filename, "w");

	TYPED_ENUM_CLASS_HEAD(file, "OSXKeychainItemAttributeType", "\nimport java.util.Date;\n\n");
	TYPED_ENUM_VALUE_DEF(file, "Account", kSecAccountItemAttr, "String.class")
	TYPED_ENUM_VALUE_DEF(file, "Address", kSecAddressItemAttr, "String.class")
	TYPED_ENUM_VALUE_DEF(file, "Alias", kSecAlias, "Object.class")
	TYPED_ENUM_VALUE_DEF(file, "AuthenticationType", kSecAuthenticationTypeItemAttr, "OSXKeychainAuthenticationType.class")
	TYPED_ENUM_VALUE_DEF(file, "CertificateEncoding", kSecCertificateEncoding, "Object.class")
	TYPED_ENUM_VALUE_DEF(file, "CertificateType", kSecCertificateType, "Object.class")
	TYPED_ENUM_VALUE_DEF(file, "Comment", kSecCommentItemAttr, "String.class")
	TYPED_ENUM_VALUE_DEF(file, "CreationDate", kSecCreationDateItemAttr, "Date.class")
	TYPED_ENUM_VALUE_DEF(file, "Creator", kSecCreatorItemAttr, "Object.class")
	TYPED_ENUM_VALUE_DEF(file, "CrlEncoding", kSecCrlEncoding, "Object.class")
	TYPED_ENUM_VALUE_DEF(file, "CrlType", kSecCrlType, "Object.class")
	TYPED_ENUM_VALUE_DEF(file, "CustomIcon", kSecCustomIconItemAttr, "Object.class")
	TYPED_ENUM_VALUE_DEF(file, "Description", kSecDescriptionItemAttr, "String.class")
	TYPED_ENUM_VALUE_DEF(file, "Generic", kSecGenericItemAttr, "Object.class")
	TYPED_ENUM_VALUE_DEF(file, "Invisible", kSecInvisibleItemAttr, "Boolean.class")
	TYPED_ENUM_VALUE_DEF(file, "Label", kSecLabelItemAttr, "String.class")
	TYPED_ENUM_VALUE_DEF(file, "ModDate", kSecModDateItemAttr, "Date.class")
	TYPED_ENUM_VALUE_DEF(file, "Negative", kSecNegativeItemAttr, "Boolean.class")
	TYPED_ENUM_VALUE_DEF(file, "Path", kSecPathItemAttr, "String.class")
	TYPED_ENUM_VALUE_DEF(file, "Port", kSecPortItemAttr, "Integer.class")
	TYPED_ENUM_VALUE_DEF(file, "Protocol", kSecProtocolItemAttr, "OSXKeychainProtocolType.class")
	TYPED_ENUM_VALUE_DEF(file, "ScriptCode", kSecScriptCodeItemAttr, "Object.class")
	TYPED_ENUM_VALUE_DEF(file, "SecurityDomain", kSecSecurityDomainItemAttr, "Object.class")
	TYPED_ENUM_VALUE_DEF(file, "Server", kSecServerItemAttr, "String.class")
	TYPED_ENUM_VALUE_DEF(file, "Service", kSecServiceItemAttr, "String.class")
	TYPED_ENUM_VALUE_DEF(file, "Signature", kSecSignatureItemAttr, "Object.class")
	TYPED_ENUM_VALUE_DEF(file, "Type", kSecTypeItemAttr, "Object.class")
	TYPED_ENUM_VALUE_LAST(file, "Volume", kSecVolumeItemAttr, "String.class")
	TYPED_ENUM_CLASS_TAIL(file, "OSXKeychainItemAttributeType");

	fclose(file);
}

/* The program takes four arguments, the first is the path to
 * OSXKeychainAuthenticationType.java, the second is the path to
 * OSXKeychainProtocolType.java, the third is the path to
 * OSXKeychainItemType.java and the fourth is the path to
 * OSXKeychainItemAttributeType.java.
 */
int main(int argc, char** argv, char** envp) {
	if (argc != 5) {
		printf("Usage: %s /path/to/OSXKeychainAuthenticationType.java /path/to/OSXKeychainProtocolType.java /path/to/OSXKeychainItemType.java /path/to/OSXKeychainItemAttributeType.java\n", argv[0]);
		return 1;
	}

	generateOSXKeychainAuthenticationType(argv[1]);
	generateOSXKeychainProtocolType(argv[2]);
	generateOSXKeychainItemType(argv[3]);
	generateOSXKeychainItemAttributeType(argv[4]);

	return 0;
}
