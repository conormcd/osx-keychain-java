package com.mcdermottroe.apple;

import junit.framework.TestCase;

/** Test the OSXKeychainTest class.
 *
 *	@author	Conor McDermottroe
 */
public class OSXKeychainTest
extends TestCase
{

	/** The keychain instance to test. */
	private OSXKeychain keychain;

	/** Try to insert, read and delete a generic password. */
	public void testRoundTripGenericPassword() {
		initKeychain();
		
		final String serviceName = "testRoundTripGenericPassword_service";
		final String userName = "testRoundTripGenericPassword_username";
		final String password = "testRoundTripGenericPassword_password";
		
		// Add it to the keychain.
		try {
			keychain.addGenericPassword(serviceName, userName, password);
		} catch (OSXKeychainException e) {
			fail("Failed to add a generic password.");
		}

		// Retrieve it from the keychain
		try {
			String pass = keychain.findGenericPassword(serviceName, userName);
			assertEquals("Retrieved password did not match.", password, pass);
		} catch (OSXKeychainException e) {
			fail("Failed to retrieve generic password");
		}

		// Delete it from the keychain.
		try {
			keychain.deleteGenericPassword(serviceName, userName);
		} catch (OSXKeychainException e) {
			fail("Failed to delete generic password");
		}
	}

	/** Try to insert, read and delete a generic password. */
	public void testUpdateGenericPassword() {
		initKeychain();
		
		final String serviceName = "testUpdateGenericPassword_service";
		final String userName = "testUpdateGenericPassword_username";
		final String password1 = "testUpdateGenericPassword_pw1";
		final String password2 = "testUpdateGenericPassword_pw2";

		// Add it to the keychain.
		try {
			keychain.addGenericPassword(serviceName, userName, password1);
		} catch (OSXKeychainException e) {
			fail("Failed to add a generic password.");
		}

		// Retrieve it from the keychain
		try {
			String pass = keychain.findGenericPassword(serviceName, userName);
			assertEquals("Retrieved password did not match.", password1, pass);
		} catch (OSXKeychainException e) {
		    e.printStackTrace();
			fail("Failed to retrieve generic password");
		}

		// Modify the existing item in the keychain.
		try {
			keychain.modifyGenericPassword(serviceName, userName, password2);
		} catch (OSXKeychainException e) {
			fail("Failed to update a generic password.");
		}

		// Retrieve it from the keychain, expect the updated password now
		try {
			String pass = keychain.findGenericPassword(serviceName, userName);
			assertEquals("Retrieved password did not match.", password2, pass);
		} catch (OSXKeychainException e) {
			fail("Failed to retrieve generic password");
		}

		// Delete it from the keychain.
		try {
			keychain.deleteGenericPassword(serviceName, userName);
		} catch (OSXKeychainException e) {
			fail("Failed to delete generic password");
		}
	}

	/** Initialize the keychain for testing. */
	private void initKeychain() {
		try {
			if (keychain == null) {
				keychain = OSXKeychain.getInstance();
			}
		} catch (OSXKeychainException e) {
			fail("Failed to initialize keychain");
		}
	}
}
