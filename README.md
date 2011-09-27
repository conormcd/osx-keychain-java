Example:

    OSXKeychain keychain = OSXKeychain.getInstance();
    String password = keychain.findInternetPassword("github.com", null, "conormcd", "/login", 0);
