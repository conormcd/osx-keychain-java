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

package com.mcdermottroe.apple;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/** An interface to the OS X Keychain. The names of functions and parameters
 *	will mostly match the functions listed in the <a href="http://developer.apple.com/library/mac/#documentation/Security/Reference/keychainservices/Reference/reference.html">Keychain Services Reference</a>.
 *
 *	@author Conor McDermottroe
 */
public class OSXKeychain {
	/** The singleton instance of the keychain. Lazily loaded in
	 *	{@link #getInstance()}.
	 */
	private static OSXKeychain instance;

	/** Prevent this class from being instantiated directly. */
	private OSXKeychain() {
	}

	/** Get an instance of the keychain.
	 *
	 *	@return	An instance of this class.
	 *	@throws	OSXKeychainException	If it's not possible to connect to the
	 *									keychain.
	 */
	public static OSXKeychain getInstance()
	throws OSXKeychainException
	{
		if (instance == null) {
			try {
				loadSharedObject();
			} catch (IOException e) {
				throw new OSXKeychainException("Failed to load osxkeychain.so", e);
			}
			instance = new OSXKeychain();
		}
		return instance;
	}


	/** Find an Internet Password in the keychain.
	 *
	 *	@param	serverName		The value which should be passed as the
	 *							serverName parameter to
	 *							SecKeychainFindInternetPassword.
	 *	@param	securityDomain	This will be passed for the securityDomain
	 *							parameter to SecKeychainFindInternetPassword.
	 *	@param	accountName		The value for the accountName parameter to
	 *							SecKeychainFindInternetPassword.
	 *	@param	path			This will be passed as the path parameter to
	 *							SecKeychainFindInternetPassword.
	 *	@param	port			The port parameter value for
	 *							SecKeychainFindInternetPassword.
	 *	@return					The password which matches the details supplied.
	 */
	public String findInternetPassword(String serverName, String securityDomain, String accountName, String path, int port) {
		return _findInternetPassword(serverName, securityDomain, accountName, path, port);
	}

	/** See Java_com_mcdermottroe_apple_OSXKeychain__1findInternetPassword for
	 *	the implementation of this and use {@link #findInternetPassword(String,
	 *	String, String, String, int)} to call this.
	 *
	 *	@param	serverName		The value which should be passed as the
	 *							serverName parameter to
	 *							SecKeychainFindInternetPassword.
	 *	@param	securityDomain	This will be passed for the securityDomain
	 *							parameter to SecKeychainFindInternetPassword.
	 *	@param	accountName		The value for the accountName parameter to
	 *							SecKeychainFindInternetPassword.
	 *	@param	path			This will be passed as the path parameter to
	 *							SecKeychainFindInternetPassword.
	 *	@param	port			The port parameter value for
	 *							SecKeychainFindInternetPassword.
	 *	@return					The password which matches the details supplied.
	 */
	private native String _findInternetPassword(String serverName, String securityDomain, String accountName, String path, int port);

	/** Load the shared object which contains the implementations for the native
	 *	methods in this class.
	 *
	 *	@throws	IOException	If the shared object could not be loaded.
	 */
	private static void loadSharedObject()
	throws IOException
	{
		// Stream the library out of the JAR
		InputStream soInJarStream = OSXKeychain.class.getResourceAsStream("osxkeychain.so");

		// Put the library in a temp file.
		File soInTmp = File.createTempFile("osxkeychain", ".so");
		soInTmp.deleteOnExit();
		OutputStream soInTmpStream = new FileOutputStream(soInTmp);

		// Copy the .so
		byte[] buffer = new byte[4096];
		int bytesRead;
		while ((bytesRead = soInJarStream.read(buffer)) > 0) {
			soInTmpStream.write(buffer, 0, bytesRead);
		}

		// Clean up
		soInJarStream.close();
		soInTmpStream.close();

		// Now load the library
		System.load(soInTmp.getAbsolutePath());
	}
}
