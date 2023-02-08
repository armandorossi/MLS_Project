package com.example.mls_project.Database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPassword {
    //This class is designated to encrypt a string received and return it to the method
    public static String hashAPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.reset();
        md.update(password.getBytes());
        byte[] mdArray = md.digest();
        StringBuilder sb = new StringBuilder(mdArray.length * 2);
        for(byte b : mdArray) {
            int v = b & 0xff;
            if(v < 16)
                sb.append('0');
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();
    }
}
