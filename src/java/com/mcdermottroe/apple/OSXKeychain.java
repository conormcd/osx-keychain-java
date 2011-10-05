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
import java.util.HashMap;
import java.util.Map;

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

	/** Add a non-internet password to the keychain.
	 *
	 *	@param	serviceName				The name of the service the password is
	 *									for.
	 *	@param	accountName				The account name/username for the
	 *									service.
	 *	@param	password				The password for the service.
	 *	@throws OSXKeychainException	If an error occurs when communicating
	 *									with the OS X keychain.
	 */
	public void addGenericPassword(String serviceName, String accountName, String password)
	throws OSXKeychainException
	{
		_addGenericPassword(serviceName, accountName, password);
	}

	/** Add an internet password to the keychain.
	 *
	 *	@param	url						The URL to associate the password with.
	 *	@param	accountName				The username for the password. Pass
	 *									null here if the username is provided
	 *									in the URL.
	 *	@param	password				The password to add to the keychain.
	 *	@throws	OSXKeychainException	If an error occurs when communicating
	 *									with the OS X keychain.
	 */
	public void addInternetPassword(URL url, String accountName, String password)
	throws OSXKeychainException
	{
		// Work out the username if we can.
		String username = getUsername(accountName, url);
		if (username == null) {
			throw new OSXKeychainException("No account name supplied.");
		}

		// Figure out the protocol and port
		int port = url.getPort();
		OSXKeychainProtocolType protocol = getProtocol(port, url.getProtocol());
		if (port <= 0) {
			port = getPort(port, protocol);
		}

		// Now add the password
		addInternetPassword(
			url.getHost(),
			null,
			username,
			url.getPath(),
			port,
			protocol,
			OSXKeychainAuthenticationType.Any,
			password
		);
	}

	/** Add an intenet password to the keychain.
	 *
	 *	@param	serverName				The name of the server which the
	 *									password is for.
	 *	@param	securityDomain			The security domain which some
	 *									protocols need.
	 *	@param	accountName				The account name/username for the
	 *									password.
	 *	@param	path					The path on the server for which the
	 *									credentials should be used.
	 *	@param	port					Only return the password if connecting
	 *									to this port.
	 *	@param	protocol				Only return the password for this
	 *									protocol.
	 *	@param	authenticationType		The type of authentication the password
	 *									is for.
	 *	@param	password				The password to add.
	 *	@throws OSXKeychainException	If an error occurs when communicating
	 *									with the OS X keychain.
	 */
	public void addInternetPassword(String serverName, String securityDomain, String accountName, String path, int port, OSXKeychainProtocolType protocol, OSXKeychainAuthenticationType authenticationType, String password)
	throws OSXKeychainException
	{
		_addInternetPassword(serverName, securityDomain, accountName, path, port, protocol.getValue(), authenticationType.getValue(), password);
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
		String username = getUsername(accountName, url);
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

	/** See Java_com_mcdermottroe_apple_OSXKeychain__1addGenericPassword for
	 *	the implementation of this and use {@link #addGenericPassword(String,
	 *	String, String)} to call this.
	 *
	 *	@param	serviceName				The value which should be passed as the
	 *									serviceName parameter to
	 *									SecKeychainAddGenericPassword.
	 *	@param	accountName				The value which should be passed as the
	 *									accountName parameter to
	 *									SecKeychainAddGenericPassword.
	 *	@param	password				The value which should be passed as the
	 *									password parameter to
	 *									SecKeychainAddGenericPassword.
	 *	@throws	OSXKeychainException	If an error occurs when communicating
	 *									with the OS X keychain.
	 */
	private native void _addGenericPassword(String serviceName, String accountName, String password)
	throws OSXKeychainException;

	/** See Java_com_mcdermottroe_apple_OSXKeychain__1addInternetPassword for
	 *	the implementation of this and use {@link #addInternetPassword(String,
	 *	String, String, String, int, OSXKeychainProtocolType,
	 *	OSXKeychainAuthenticationType, String)} to call this.
	 */
	private native void _addInternetPassword(String serverName, String securityDomain, String accountName, String path, int port, int protocol, int authenticationType, String password)
	throws OSXKeychainException;

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

	/* ********************************* */
	/* Private utilities from here down. */
	/* ********************************* */

	/** A fixed mapping of ports to known protocols. */
	private static final Map<Integer, OSXKeychainProtocolType> PROTOCOLS;
	static {
		PROTOCOLS = new HashMap<Integer, OSXKeychainProtocolType>(32);
		PROTOCOLS.put(548, OSXKeychainProtocolType.AFP);
		PROTOCOLS.put(3020, OSXKeychainProtocolType.CIFS);
		PROTOCOLS.put(2401, OSXKeychainProtocolType.CVSpserver);
		PROTOCOLS.put(3689, OSXKeychainProtocolType.DAAP);
		PROTOCOLS.put(3031, OSXKeychainProtocolType.EPPC);
		PROTOCOLS.put(21, OSXKeychainProtocolType.FTP);
		PROTOCOLS.put(990, OSXKeychainProtocolType.FTPS);
		PROTOCOLS.put(80, OSXKeychainProtocolType.HTTP);
		PROTOCOLS.put(443, OSXKeychainProtocolType.HTTPS);
		PROTOCOLS.put(143, OSXKeychainProtocolType.IMAP);
		PROTOCOLS.put(993, OSXKeychainProtocolType.IMAPS);
		PROTOCOLS.put(631, OSXKeychainProtocolType.IPP);
		PROTOCOLS.put(6667, OSXKeychainProtocolType.IRC);
		PROTOCOLS.put(994, OSXKeychainProtocolType.IRCS);
		PROTOCOLS.put(389, OSXKeychainProtocolType.LDAP);
		PROTOCOLS.put(636, OSXKeychainProtocolType.LDAPS);
		PROTOCOLS.put(119, OSXKeychainProtocolType.NNTP);
		PROTOCOLS.put(563, OSXKeychainProtocolType.NNTPS);
		PROTOCOLS.put(110, OSXKeychainProtocolType.POP3);
		PROTOCOLS.put(995, OSXKeychainProtocolType.POP3S);
		PROTOCOLS.put(554, OSXKeychainProtocolType.RTSP);
		PROTOCOLS.put(25, OSXKeychainProtocolType.SMTP);
		PROTOCOLS.put(1080, OSXKeychainProtocolType.SOCKS);
		PROTOCOLS.put(22, OSXKeychainProtocolType.SSH);
		PROTOCOLS.put(3690, OSXKeychainProtocolType.SVN);
		PROTOCOLS.put(23, OSXKeychainProtocolType.Telnet);
		PROTOCOLS.put(992, OSXKeychainProtocolType.TelnetS);
	}

	/** Resolve a username from either a supplied username or from the username
	 *	portion of a URL.
	 *
	 *	@param	username				The username to return if it's not
	 *									null.
	 *	@param	url						The URL to check iff username is null.
	 *	@return							If the username is not null, the
	 *									username will be returned. If the
	 *									username is null and the url contains a
	 *									username then the username from the URL
	 *									will be returned.
	 *	@throws	OSXKeychainException	If username is null and the URL
	 *									contains no username.
	 */
	private static String getUsername(String username, URL url)
	throws OSXKeychainException
	{
		if (username != null) {
			return username;
		}

		String urlUsername = url.getUserInfo();
		if (urlUsername != null) {
			if (urlUsername.indexOf(':') > 0) {
				return urlUsername.substring(0, urlUsername.indexOf(':'));
			} else {
				return urlUsername;
			}
		} else {
			throw new OSXKeychainException("Could not return a username.");
		}
	}

	/** Resolve a port from either a supplied port or from a protocol.
	 *
	 *	@param	port
	 *	@param	protocol
	 *	@return
	 *	@throws	OSXKeychainException
	 */
	private static int getPort(int port, OSXKeychainProtocolType protocol)
	throws OSXKeychainException
	{
		if (port > 0) {
			return port;
		}
		for (Map.Entry<Integer, OSXKeychainProtocolType> entry : PROTOCOLS.entrySet()) {
			if (entry.getValue() == protocol) {
				return entry.getKey();
			}
		}
		throw new OSXKeychainException("Could not determine port.");
	}

	/** Figure out the protocol given the port and or the protocol portion of
	 *	the URL.
	 *
	 *	@param	port					An IP port number.
	 *	@param	protocol				The protocol part of a URL.
	 *	@return							The protocol type which matches either
	 *									the port or the protocol prefix from
	 *									the URL.
	 *	@throws	OSXKeychainException	If there's no known protocol which
	 *									matches the port or protocol prefix.
	 */
	private static OSXKeychainProtocolType getProtocol(int port, String protocol)
	throws OSXKeychainException
	{
		// Try to figure it out from the port, if available.
		for (Map.Entry<Integer, OSXKeychainProtocolType> pp : PROTOCOLS.entrySet()) {
			if (pp.getKey() == port) {
				return pp.getValue();
			}
		}

		// See if we got a protocol prefix.
		if (protocol != null) {
			// Try to match the name of the protocol
			for (OSXKeychainProtocolType pt : OSXKeychainProtocolType.values()) {
				if (pt.toString().toLowerCase().equals(protocol.toLowerCase())) {
					return pt;
				}
			}

			// A few aliases/ones we couldn't handle in the map above.
			if ("smb".equals(protocol)) {
				return OSXKeychainProtocolType.SMB;
			}
		}

		// Give up.
		throw new OSXKeychainException("Could not determine protocol.");
	}
}
