package com.example.desktopproject;

import com.example.desktopproject.model.ChangeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ChangeTypeTest {

    private ChangeType changeType;
    private Map<String, Double> rates;

    @BeforeEach
    void setUp() {
        // Initialisation de données de test
        rates = new HashMap<>();
        rates.put("USD", 1.1);
        rates.put("GBP", 0.85);
        rates.put("JPY", 160.0);

        changeType = new ChangeType(
                "EUR",       // devise de base
                rates,       // taux de change
                "Banque",    // source
                "2025-06-03", // date locale
                "2025-06-03"  // date de mise à jour
        );
    }

    @Test
    @DisplayName("Test de récupération d'un taux de change existant")
    void testGetRateExistingCurrency() {
        // Act
        Double rate = changeType.getRate("USD");

        // Assert
        assertEquals(1.1, rate, "Le taux de change pour USD devrait être 1.1");
    }

    @Test
    @DisplayName("Test de récupération d'un taux de change inexistant")
    void testGetRateNonExistingCurrency() {
        // Act
        Double rate = changeType.getRate("CAD");

        // Assert
        assertNull(rate, "Le taux de change pour une devise inexistante devrait être null");
    }

    @Test
    @DisplayName("Test de conversion vers la même devise")
    void testConvertSameCurrency() {
        // Act
        Double result = changeType.convert(100.0, "EUR", "EUR");

        // Assert
        assertEquals(100.0, result, "La conversion vers la même devise devrait retourner le montant initial");
    }

    @Test
    @DisplayName("Test de conversion de la devise de base vers une autre devise")
    void testConvertFromBaseCurrency() {
        // Act
        Double result = changeType.convert(100.0, "EUR", "USD");

        // Assert
        assertEquals(110.0, result, 0.001, "100 EUR devraient être convertis en 110 USD");
    }

    @Test
    @DisplayName("Test de conversion d'une devise vers la devise de base")
    void testConvertToBaseCurrency() {
        // Act
        Double result = changeType.convert(110.0, "USD", "EUR");

        // Assert
        assertEquals(100.0, result, 0.001, "110 USD devraient être convertis en 100 EUR");
    }

    @Test
    @DisplayName("Test de conversion entre deux devises qui ne sont pas la devise de base")
    void testConvertBetweenNonBaseCurrencies() {
        // Act
        Double result = changeType.convert(100.0, "USD", "GBP");

        // Assert
        // 100 USD -> 90.91 EUR -> 77.27 GBP
        assertEquals(77.27, result, 0.01, "100 USD devraient être convertis en environ 77.27 GBP");
    }

    @ParameterizedTest
    @CsvSource({
            "100.0, EUR, USD, 110.0",
            "100.0, USD, EUR, 90.91",
            "100.0, GBP, JPY, 18823.53",
            "1000.0, JPY, GBP, 5.31"
    })
    @DisplayName("Test paramétré de différentes conversions")
    void testVariousConversions(double amount, String fromCurrency, String toCurrency, double expected) {
        // Act
        Double result = changeType.convert(amount, fromCurrency, toCurrency);

        // Assert
        assertEquals(expected, result, 0.01,
                String.format("%s %s devraient être convertis en environ %s %s",
                        amount, fromCurrency, expected, toCurrency));
    }
}