package com.example.desktopproject.model;

import java.util.Map;

public record ChangeType(
        String base,
        Map<String, Double> rates,
        String source,
        String localISODate,
        String putISODate
) {
    /**
     * Récupère le taux de change pour une devise spécifique
     */
    public Double getRate(String currencyCode) {
        return rates.getOrDefault(currencyCode, null);
    }

    /**
     * Convertit un montant d'une devise à une autre
     */
    public Double convert(Double amount, String fromCurrency, String toCurrency) {
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        // Si on convertit vers/depuis la devise de base
        if (fromCurrency.equals(base)) {
            return amount * getRate(toCurrency);
        } else if (toCurrency.equals(base)) {
            return amount / getRate(fromCurrency);
        }
        // Conversion entre deux devises différentes de la base
        else {
            return amount / getRate(fromCurrency) * getRate(toCurrency);
        }
    }
}