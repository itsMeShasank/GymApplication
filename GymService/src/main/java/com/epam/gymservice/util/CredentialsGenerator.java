package com.epam.gymservice.util;

import com.epam.gymservice.helper.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class CredentialsGenerator {

    private static final String SPECIAL_CHARACTERS = "!@#$%^&*";
    private static final String LOWER_CASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_CASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";

    private final Random random;

    public CredentialsGenerator(Random random) {
        this.random = random;
    }

    public String generateUserName(String firstName,String lastName) {

        StringBuilder username = new StringBuilder();
        if(!isValidName(firstName)) {
            throw new UserException("First name you provided is empty or it contains characters not alphabets", HttpStatus.BAD_REQUEST);
        }
        if(!isValidName(lastName)) {
            throw new UserException("Last name you provided is empty or it contains characters not alphabets", HttpStatus.BAD_REQUEST);
        }

        username.append(firstName.toLowerCase());
        username.append(lastName.toLowerCase());
        username.append(random.nextInt(1000,9999));

        return username.toString();
    }

    public String generatePassword() {

        List<Character> passwordCharacters = new ArrayList<>();

        passwordCharacters.add(getRandomCharacter(SPECIAL_CHARACTERS));
        passwordCharacters.add(getRandomCharacter(LOWER_CASE_LETTERS));
        passwordCharacters.add(getRandomCharacter(UPPER_CASE_LETTERS));
        passwordCharacters.add(getRandomCharacter(NUMBERS));

        int remainingLength = 8 - passwordCharacters.size();
        for (int i = 0; i < remainingLength; i++) {
            String allCharacters = SPECIAL_CHARACTERS + LOWER_CASE_LETTERS + UPPER_CASE_LETTERS + NUMBERS;
            passwordCharacters.add(getRandomCharacter(allCharacters));
        }

        Collections.shuffle(passwordCharacters);

        StringBuilder password = new StringBuilder();
        for (Character c : passwordCharacters) {
            password.append(c);
        }

        return password.toString();
    }
    private char getRandomCharacter(String str) {
        return str.charAt(random.nextInt(str.length()));
    }
    private boolean isValidName(String name) {

        boolean result = name.trim().length() != 0;

        for(char character : name.toCharArray()) {
            if(!Character.isAlphabetic(character)) {
                result = false;
                break;
            }
        }
        return result;
    }
}
