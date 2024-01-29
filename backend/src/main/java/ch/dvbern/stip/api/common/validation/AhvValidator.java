package ch.dvbern.stip.api.common.validation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class AhvValidator {
    public static final String START_DIGITS = "756";

    public boolean isValid(@NotNull String ahvNummer) {
        String cleanedAhv = ahvNummer.replace(".", "");
        char[] digitsArray = cleanedAhv.toCharArray();

        if (digitsArray.length != 13) {
            return false;
        }

        int[] relevantDigits = new int[12];
        for (int i = 0; i < 12; i++) {
            relevantDigits[i] = Character.getNumericValue(digitsArray[11 - i]);
        }

        int relevantDigitsSum = 0;
        for (int i = 0; i < relevantDigits.length; i++) {
            int multiplier = i % 2 == 0 ? 3 : 1;
            relevantDigitsSum += relevantDigits[i] * multiplier;
        }

        int relevantDigitsRounded = (int) Math.ceil(relevantDigitsSum / 10.0) * 10;
        int calculatedCheckDigit = relevantDigitsRounded - relevantDigitsSum;
        int checkDigit = Character.getNumericValue(digitsArray[12]);

        String startDigits = ahvNummer.substring(0, 3);

        return checkDigit == calculatedCheckDigit && startDigits.equals(START_DIGITS);
    }
}
