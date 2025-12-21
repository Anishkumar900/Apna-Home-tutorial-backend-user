package com.user.dto;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;

@Service
public class OTPGeneration {

    private static final SecureRandom random = new SecureRandom();

    public static String generateOTP(int num){
        StringBuilder otp = new StringBuilder(num);
        for (int i = 0; i < num; i++) {
            otp.append(random.nextInt(10)); // 0–9
        }
//        System.out.println(otp);
        return otp.toString();
    }

}
