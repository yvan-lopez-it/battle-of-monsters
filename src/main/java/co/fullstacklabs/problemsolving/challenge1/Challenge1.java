package co.fullstacklabs.problemsolving.challenge1;

import java.util.Arrays;
import java.util.Map;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class Challenge1 {

    public static Map<String, Float> numbersFractionCalculator(Integer[] numbers) {
        long countPositives = Arrays.stream(numbers).filter(n -> n > 0).count();
        long countNegatives = Arrays.stream(numbers).filter(n -> n < 0).count();
        long countZeros = Arrays.stream(numbers).filter(n -> n == 0).count();
        long countTotal = numbers.length;

        float fractionPositives = (float) countPositives / countTotal;
        float fractionNegatives = (float) countNegatives / countTotal;
        float fractionZeros = (float) countZeros / countTotal;

        //Be careful with the "negative" key word.
        return Map.of(
                "positives", (float) Math.round(fractionPositives * 1000000) / 1000000,
                "negative", (float) Math.round(fractionNegatives * 1000000) / 1000000,
                "zeros", (float) Math.round(fractionZeros * 1000000) / 1000000
        );
    }
}
