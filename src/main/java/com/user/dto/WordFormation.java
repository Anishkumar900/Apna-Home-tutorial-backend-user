package com.user.dto;

import org.springframework.stereotype.Service;

@Service
public class WordFormation {
    public static String emailFormation(String email){
        if (email == null) {
            return null;
        }
        return email.trim().toLowerCase();
    }

    public static String nameFormation(String name) {
        if (name == null || name.trim().isEmpty()) {
            return name;
        }

        String[] words = name.trim().toLowerCase().split("\\s+");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            formattedName.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        return formattedName.toString().trim();
    }

}
