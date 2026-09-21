/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.cineplex.system.utils;

public class Validations {

    public Validations() {

    }

    public Boolean validateTextEmpty(String text) {
        boolean isEmpty = false;

        if (text.isEmpty() == true || text.isBlank() == true) {
            isEmpty = true;
        }

        return isEmpty;
    }

    public Boolean validateTextLength(String text, int textMax) {

        return text.length() <= textMax;
    }

    public Boolean equalsText(String textOriginal, String textCompare) {
        return textOriginal.equals(textCompare);
    }

    public Boolean validateNumber(String text) {
        try {
            int number = Integer.parseInt(text);
            return number > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
