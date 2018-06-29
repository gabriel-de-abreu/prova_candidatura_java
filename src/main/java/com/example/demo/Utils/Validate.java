/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.Utils;

/**
 *
 * @author gabriel
 */
public class Validate {

    private static final int MIN_VALUE = 2;
    private static final int MAX_VALUE = 300;

    public static boolean validateName(String name) {
        return name.length() >= 2 && name.length() <= 300;
    }
}
