package com.github.nathandelane.rateofdecay;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.Test;

public class HalfLifeTests {
  
  @Test
  public void testHalfLife1() {
    final int fullDose = 20;
    final int halfLifeInHours = 35;
    final int hoursSinceDosage = 35;
    final BigDecimal remainingExpected = BigDecimal.valueOf(10.00000).setScale(5, RoundingMode.DOWN);

    final BigDecimal actual = HalfLifeCalculator.calculateRemainingMgAtTime(fullDose, halfLifeInHours, hoursSinceDosage);

    assertEquals(remainingExpected, actual);
  }

  @Test
  public void testHalfLifeAt24Hours() {
    final int fullDose = 20;
    final int halfLifeInHours = 35;
    final int hoursSinceDosage = 24;
    final BigDecimal remainingExpected = BigDecimal.valueOf(12.43395);

    final BigDecimal actual = HalfLifeCalculator.calculateRemainingMgAtTime(fullDose, halfLifeInHours, hoursSinceDosage);
    
    assertEquals(remainingExpected, actual);
  }
  
  @Test
  public void testHalfLifeAtNextDosingTimeWithNextDose() {
    final int fullDose = 20;
    final int halfLifeInHours = 35;
    final int hoursAtNextDosage = 24;
    final int numberOfDoses = 2;
    final BigDecimal remainingExpected = BigDecimal.valueOf(19.89432);

    final BigDecimal actual = HalfLifeCalculator.calculateAccumulatedMgAtTimeBasedOnRegularDosing(fullDose, halfLifeInHours, hoursAtNextDosage, numberOfDoses);

    assertEquals(remainingExpected, actual);    
  }
  
  @Test
  public void testSpecific() {
    final BigDecimal afterTwoYearsRemaining = HalfLifeCalculator.calculateAccumulatedMgAtTimeBasedOnRegularDosing(20, 35, 24, 770);
    
    System.out.format("Current amount in system is: %s", afterTwoYearsRemaining);
  }

}
