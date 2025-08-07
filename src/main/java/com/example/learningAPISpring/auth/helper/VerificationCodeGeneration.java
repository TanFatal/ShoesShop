package com.example.learningAPISpring.auth.helper;

import java.util.Random;

public class VerificationCodeGeneration {
    public static String generateCode(){
        Random random=new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
