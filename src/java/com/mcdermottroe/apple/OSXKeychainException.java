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

/** An exception to be thrown if the native code interacting with the keychain
 *	encounters an error.
 *
 *	@author Conor McDermottroe
 */
public class OSXKeychainException
extends Exception
{
	/** Create a blank exception with no message. */
	public OSXKeychainException() {
		super();
	}

	/** Create an exception with a message.
	 *
	 *	@param	message	A message explaining why this exception must be thrown.
	 */
	public OSXKeychainException(String message) {
		super(message);
	}

	/** Create an exception with no message but with a link to the exception
	 *	which caused this one to be thrown.
	 *
	 *	@param	cause	The reason this exception is being created and thrown.
	 */
	public OSXKeychainException(Throwable cause) {
		super(cause);
	}

	/** Create an exception both with a message and a link to the exceptino
	 *	which caused this one to be thrown.
	 *
	 *	@param	message	A message explaining why this exception must be thrown.
	 *	@param	cause	The reason this exception is being created and thrown.
	 */
	public OSXKeychainException(String message, Throwable cause) {
		super(message, cause);
	}
}
