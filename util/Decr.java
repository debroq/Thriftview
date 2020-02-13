/*
 * Tool to decrypt the input and output examples
 */

package com.company;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import java.io.*;

public class Decr {

    public Decr(String fname) throws IOException {
        InputStream inputStream = new FileInputStream(fname);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer strbuf = new StringBuffer();
        String inline;
        while ((inline = in.readLine()) != null) {
            strbuf.append(inline);
        }
        in.close();

        String input = strbuf.toString();
        String SALT_PW1 = "733hC617Hs";
        String SALT_PW2 = "7Af06Sl01d";

        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(SALT_PW1 + SALT_PW2);
        String enc = encryptor.decrypt(input);
        System.out.println(enc);
    }

    public static void main(String[] args) throws Exception {
        Decr decr = new Decr("/home/dbrock/jasypt-1.9.2/bin/out.enc");
    }
}
