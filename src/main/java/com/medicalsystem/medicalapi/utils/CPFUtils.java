package com.medicalsystem.medicalapi.utils;


public class CPFUtils {

    // Método para verificar se o CPF é válido
    public static boolean isValidCPF(String cpf) {
        // Remove pontuações do CPF
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        // Verifica se o CPF é uma sequência de números repetidos
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        // Valida os dois dígitos verificadores
        int sum1 = 0, sum2 = 0;
        int[] weights1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weights2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        // Cálculo do primeiro dígito verificador
        for (int i = 0; i < 9; i++) {
            sum1 += Character.getNumericValue(cpf.charAt(i)) * weights1[i];
        }
        int digit1 = (sum1 * 10) % 11;
        if (digit1 == 10 || digit1 == 11) digit1 = 0;

        // Cálculo do segundo dígito verificador
        for (int i = 0; i < 10; i++) {
            sum2 += Character.getNumericValue(cpf.charAt(i)) * weights2[i];
        }
        int digit2 = (sum2 * 10) % 11;
        if (digit2 == 10 || digit2 == 11) digit2 = 0;

        return cpf.charAt(9) == digit1 + '0' && cpf.charAt(10) == digit2 + '0';
    }
}
