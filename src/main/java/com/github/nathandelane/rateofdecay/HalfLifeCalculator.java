package com.github.nathandelane.rateofdecay;

import java.math.BigDecimal;
import java.math.BigInteger;

public class HalfLifeCalculator {
  
  private HalfLifeCalculator() {
    // No-op
  }
  
  /**
   * Calculate the current number of milligrams in the human body based on initial amount ({@code initialMg}), the half-life 
   * of the substance in hours (whole numbers only, {@code halfLifeInHours}), and the number of hours since the dosage 
   * ({@code hoursSinceDosage}). This is based on the formula {@code A = P * 0.5^(t/T)} where <i>A</i> is the amount remaining, 
   * <i>P</i> is the initial dosage, <i>t</i> is the hourse since the dosage, and <i>T</i> is the half-life in hours. 
   * @param initialMg
   * @param halfLifeInHours
   * @param hoursSinceDosage
   * @return
   */
  public static BigDecimal calculateRemainingMgAtTime(final BigInteger initialMg, final int halfLifeInHours, final int hoursSinceDosage) {
    final BigDecimal bd_initialMg = new BigDecimal(initialMg);
    final BigDecimal oneHalf = BigDecimal.valueOf(0.50000);
    final BigDecimal bd_halfLifeInHours = BigDecimal.valueOf(halfLifeInHours);
    final BigDecimal bd_hoursSinceDosage = BigDecimal.valueOf(hoursSinceDosage);
    final BigDecimal exponent = bd_hoursSinceDosage.divide(bd_halfLifeInHours, DefaultScale.INT_SCALE, DefaultScale.ROUNDING_MODE);

    final double d_oneHalf = oneHalf.doubleValue();
    final double d_exponent = exponent.doubleValue();
    final double result = Math.pow(d_oneHalf, d_exponent);
    
    final BigDecimal bd_result = BigDecimal.valueOf(result);
    final BigDecimal currentMgAtTime = bd_initialMg.multiply(bd_result);
    
    final BigDecimal truncated = currentMgAtTime.setScale(DefaultScale.INT_SCALE, DefaultScale.ROUNDING_MODE);

    return truncated;
  }
  
  /**
   * Calculate the current number of milligrams in the human body based on initial amount, half-life in hours (whole numbers only), 
   * Hours from previous does to next regular dose, and the number of doses. This utilizes {@link #calculateRemainingMgAtTime(int, int, int)}
   * to determine the current number of milligrams base don accumulation.
   * @param initialMg
   * @param halfLifeInHours
   * @param hoursAtNextDosage
   * @param numberOfDoses
   * @return
   */
  public static BigDecimal calculateAccumulatedMgAtTimeBasedOnRegularDosing(
    final BigInteger initialMg,
    final int halfLifeInHours,
    final int hoursAtNextDosage,
    final int numberOfDoses
  ) {
    if (numberOfDoses < 1) {
      return BigDecimal.ZERO;
    }
    else {
      BigDecimal result = calculateRemainingMgAtTime(initialMg, halfLifeInHours, hoursAtNextDosage);
      
      for (int dose = 1; dose < numberOfDoses; dose++) {
        final BigInteger accumulatedMg = result.toBigInteger();
        final BigDecimal remainingAtNextDosage = calculateRemainingMgAtTime(accumulatedMg, halfLifeInHours, hoursAtNextDosage);
        
        result = result.add(remainingAtNextDosage);
      }
      
      final BigDecimal truncatedResult = result.setScale(DefaultScale.INT_SCALE, DefaultScale.ROUNDING_MODE);
      
      return truncatedResult;
    }
  }
  
  /**
   * Calculate the number of hours that the substance will remain in the body assuming no further dosing is performed.
   * @param currentMg
   * @param halfLifeInHours
   * @return
   */
  public static int calculateHoursRemainingUntilNeglegible(final BigInteger currentMg, final int halfLifeInHours) {
    final BigDecimal neglegibilityThreshold = BigDecimal.valueOf(0.01000).setScale(DefaultScale.INT_SCALE, DefaultScale.ROUNDING_MODE);
    
    int accumulatedHours = 0;
    
    BigDecimal milligramsRemaining = new BigDecimal(currentMg);
    
    while (milligramsRemaining.compareTo(neglegibilityThreshold) > 0) {
      accumulatedHours += 1;
      
      final BigInteger milligramsRemainingAsBigInt = milligramsRemaining.toBigInteger();
      final BigDecimal calculatedMgs = calculateRemainingMgAtTime(milligramsRemainingAsBigInt, halfLifeInHours, accumulatedHours);
      
      milligramsRemaining = calculatedMgs.setScale(DefaultScale.INT_SCALE, DefaultScale.ROUNDING_MODE);
    }
    
    return accumulatedHours;
  }
  
}
