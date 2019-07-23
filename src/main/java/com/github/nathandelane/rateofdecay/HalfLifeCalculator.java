package com.github.nathandelane.rateofdecay;

import java.math.BigDecimal;

public class HalfLifeCalculator {
  
  private HalfLifeCalculator() {
    // No-op
  }
  
  /**
   * Calculate the current number of milligrams in the human body based on initial amount ({@code initialMg}), the half-life 
   * of the substance in hours (whole numbers only, {@code halfLifeInHours}), and the number of hours since the dosage 
   * ({@code hoursSinceDosage}).
   * @param initialMg
   * @param halfLifeInHours
   * @param hoursSinceDosage
   * @return
   */
  public static BigDecimal calculateRemainingMgAtTime(final int initialMg, final int halfLifeInHours, final int hoursSinceDosage) {
    final BigDecimal bd_initialMg = BigDecimal.valueOf(initialMg);
    final BigDecimal oneHalf = BigDecimal.valueOf(0.5);

    final double d_oneHalf = oneHalf.doubleValue();
    final double exponent = (double) hoursSinceDosage / (double) halfLifeInHours;
    final double result = Math.pow(d_oneHalf, exponent);
    
    final BigDecimal bd_result = BigDecimal.valueOf(result);
    final BigDecimal currentMgAtTime = bd_initialMg.multiply(bd_result);
    
    final BigDecimal truncated = currentMgAtTime.setScale(DefaultScale.INT_SCALE, DefaultScale.ROUNDING_MODE);

    return truncated;
  }
  
  /**
   * Calculate the current number of milligrams in the human body based on initial amount, half-life in hours (whole numbers only), 
   * Hours from previous does to next regular dose, and the number of doses.
   * @param initialMg
   * @param halfLifeInHours
   * @param hoursAtNextDosage
   * @param numberOfDoses
   * @return
   */
  public static BigDecimal calculateAccumulatedMgAtTimeBasedOnRegularDosing(
    final int initialMg,
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
        final int accumulatedMg = result.intValue();
        final BigDecimal remainingAtNextDosage = calculateRemainingMgAtTime(accumulatedMg, halfLifeInHours, hoursAtNextDosage);
        
        result = result.add(remainingAtNextDosage);
      }
      
      final BigDecimal truncatedResult = result.setScale(DefaultScale.INT_SCALE, DefaultScale.ROUNDING_MODE);
      
      return truncatedResult;
    }
  }
  
}
