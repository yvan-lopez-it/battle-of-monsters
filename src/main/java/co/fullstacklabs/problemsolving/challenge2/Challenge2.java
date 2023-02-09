package co.fullstacklabs.problemsolving.challenge2;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class Challenge2 {

    public static int diceFacesCalculator(int dice1, int dice2, int dice3) {

        int[] dicesNumbers = {dice1, dice2, dice3};

        if (Arrays.stream(dicesNumbers).anyMatch(n -> n < 1 || n > 6)) {
            throw new IllegalArgumentException("Dice numbers must be between 1 and 6");
        }

        List<Integer> dices = List.of(dice1, dice2, dice3);

        Map<Integer, Long> frequencyMap = dices.stream()
                .collect(Collectors
                        .groupingBy(Function.identity(), Collectors.counting()));

        int maxFrequency = frequencyMap.values().stream()
                .mapToInt(Long::intValue)
                .max()
                .orElseThrow(() -> new NoSuchElementException("No maximum frequency was found"));

        int result;
        if (maxFrequency == 3) {
            result = dices.stream()
                    .filter(dice -> frequencyMap.get(dice) == 3)
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No dice with frequency of 3 was found")) * 3;
        } else if (maxFrequency == 2) {
            result = dices.stream()
                    .filter(dice -> frequencyMap.get(dice) == 2)
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No dice with frequency of 2 was found")) * 2;
        } else {
            result = dices.stream()
                    .mapToInt(dice -> dice)
                    .max()
                    .orElseThrow(() -> new NoSuchElementException("No dice with maximum value was found"));
        }

        return result;

    }
}
