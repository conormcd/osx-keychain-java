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
import java.net.URL;

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

	/** Find a password in the keychain which is not an Internet Password.
	 *
	 *	@param	serviceName				The name of the service the password is
	 *									for.
	 *	@param	accountName				The account name/username for the
	 *									service.
	 *	@return							The password which matches the details
	 *									supplied.
	 *	@throws	OSXKeychainException	If an error occurs when communicating
	 *									with the OS X keychain.
	 */
	public String findGenericPassword(String serviceName, String accountName)
	throws OSXKeychainException
	{
		return _findGenericPassword(serviceName, accountName);
	}

	/** Find an Internet Password in the keychain. This is a convenience method
	 *	wrapping {@link #findInternetPassword(String,String,String,String,int)}
	 *	for one of the most common cases.
	 *
	 *	@param	url						The URL of which requires the password.
	 *	@param	accountName				The account name/username. e.g.
	 *									"conormcd".
	 *	@return							The first password which matches the
	 *									details supplied.
	 *	@throws	OSXKeychainException	If an error occurs when communicating
	 *									with the OS X keychain.
	 */
	public String findInternetPassword(URL url, String accountName)
	throws OSXKeychainException
	{
		String username = accountName;
		if (username == null) {
			username = url.getUserInfo();
			if (username.indexOf(':') > 0) {
				username = username.substring(0, username.indexOf(':'));
			}
		}
		if (username == null) {
			throw new OSXKeychainException("No account name supplied.");
		}
		return findInternetPassword(url.getHost(), username, url.getPath());
	}

	/** Find an Internet Password in the keychain. This is a convenience method
	 *	wrapping {@link #findInternetPassword(String,String,String,String,int)}
	 *	for one of the most common cases.
	 *
	 *	@param	serverName				The name of the server. e.g.
	 *									"github.com".
	 *	@param	accountName				The account name/username. e.g.
	 *									"conormcd".
	 *	@param	path					The path to the password protected
	 *									resource on the server. e.g. "/login".
	 *	@return							The first password which matches the
	 *									details supplied.
	 *	@throws	OSXKeychainException	If an error occurs when communicating
	 *									with the OS X keychain.
	 */
	public String findInternetPassword(String serverName, String accountName, String path)
	throws OSXKeychainException
	{
		return _findInternetPassword(serverName, null, accountName, path, 0);
	}

	/** Find an Internet Password in the keychain.
	 *
	 *	@param	serverName				The name of the server. e.g.
	 *									"github.com".
	 *	@param	securityDomain			The security domain which is needed for
	 *									some protocols. Pass null if not
	 *									needed.
	 *	@param	accountName				The account name/username. e.g.
	 *									"conormcd".
	 *	@param	path					The path to the password protected
	 *									resource on the server. e.g. "/login".
	 *	@param	port					The port to connect to. Pass 0 if you
	 *									want the first result for any entry
	 *									matching the rest of the criteria.
	 *	@return							The first password which matches the
	 *									details supplied.
	 *	@throws	OSXKeychainException	If an error occurs when communicating
	 *									with the OS X keychain.
	 */
	public String findInternetPassword(String serverName, String securityDomain, String accountName, String path, int port)
	throws OSXKeychainException
	{
		return _findInternetPassword(serverName, securityDomain, accountName, path, port);
	}

	/* ************************* */
	/* JNI stuff from here down. */
	/* ************************* */

	/** See Java_com_mcdermottroe_apple_OSXKeychain__1findGenericPassword for
	 *	the implementation of this and use {@link #findGenericPassword(String,
	 *	String)} to call this.
	 *
	 *	@param	serviceName				The value which should be passed as the
	 *									serviceName parameter to
	 *									SecKeychainFindGenericPassword.
	 *	@param	accountName				The value for the accountName parameter
	 *									to SecKeychainFindGenericPassword.
	 *	@return							The first password which matches the
	 *									details supplied.
	 *	@throws OSXKeychainException	If an error occurs when communicating
	 *									with the OS X keychain.
	 */
	private native String _findGenericPassword(String serviceName, String accountName)
	throws OSXKeychainException;

	/** See Java_com_mcdermottroe_apple_OSXKeychain__1findInternetPassword for
	 *	the implementation of this and use {@link #findInternetPassword(String,
	 *	String, String, String, int)} to call this.
	 *
	 *	@param	serverName				The value which should be passed as the
	 *									serverName parameter to
	 *									SecKeychainFindInternetPassword.
	 *	@param	securityDomain			This will be passed for the
	 *									securityDomain parameter to
	 *									SecKeychainFindInternetPassword.
	 *	@param	accountName				The value for the accountName parameter
	 *									to SecKeychainFindInternetPassword.
	 *	@param	path					This will be passed as the path
	 *									parameter to
	 *									SecKeychainFindInternetPassword.
	 *	@param	port					The port parameter value for
	 *									SecKeychainFindInternetPassword.
	 *	@return							The first password which matches the
	 *									details supplied.
	 *	@throws OSXKeychainException	If an error occurs when communicating
	 *									with the OS X keychain.
	 */
	private native String _findInternetPassword(String serverName, String securityDomain, String accountName, String path, int port)
	throws OSXKeychainException;

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
