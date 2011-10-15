package com.mcdermottroe.apple;

import junit.framework.TestCase;

/** Test the OSXKeychainTest class.
 *
 *	@author	Conor McDermottroe
 */
public class OSXKeychainTest
extends TestCase
{
	/** The service name for testing generic passwords. */
	private static final String SERVICE_NAME = "Test OS X Keychain from Java";

	/** The test username to use for each password. */
	private static final String USERNAME = "Test OS X Keychain User";

	/** The test password to use. */
	private static final String PASSWORD = "Test OS X Keychain Password";

	/** The keychain instance to test. */
	private OSXKeychain keychain;

	/** Try to insert, read and delete a generic password. */
	public void testRoundTripGenericPassword() {
		initKeychain();

		// Add it to the keychain.
		try {
			keychain.addGenericPassword(SERVICE_NAME, USERNAME, PASSWORD);
		} catch (OSXKeychainException e) {
			fail("Failed to add a generic password.");
		}

		// Retrieve it from the keychain
		try {
			String pass = keychain.findGenericPassword(SERVICE_NAME, USERNAME);
			assertEquals("Retrieved password did not match.", PASSWORD, pass);
		} catch (OSXKeychainException e) {
			fail("Failed to retrieve generic password");
		}

		// Delete it from the keychain.
		try {
			keychain.deleteGenericPassword(SERVICE_NAME, USERNAME);
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
