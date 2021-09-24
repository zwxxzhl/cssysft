package com.atguigu.aclservice.util;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.stereotype.Component;

@Component
public class JasyptUtil {


    public static void main(String[] args) {

        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("PNG5egJcwiBrd+chupacabras=");

        String url = textEncryptor.encrypt("");
        String name = textEncryptor.encrypt("");
        String password = textEncryptor.encrypt("");

        System.out.println("database url: " + url);
        System.out.println("database name: " + name);
        System.out.println("database password: " + password);
    }

}
