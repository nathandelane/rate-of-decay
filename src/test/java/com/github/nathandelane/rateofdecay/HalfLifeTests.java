package com.github.nathandelane.rateofdecay;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import org.junit.Test;

public class HalfLifeTests {
  
  @Test
  public void testHalfLife1() {
    final BigInteger fullDose = BigInteger.valueOf(20);
    final int halfLifeInHours = 35;
    final int hoursSinceDosage = 35;
    final BigDecimal remainingExpected = BigDecimal.valueOf(10.00000).setScale(5, RoundingMode.DOWN);

    final BigDecimal actual = HalfLifeCalculator.calculateRemainingMgAtTime(fullDose, halfLifeInHours, hoursSinceDosage);

    assertEquals(remainingExpected, actual);
  }

  @Test
  public void testHalfLifeAt24Hours() {
    final BigInteger fullDose = BigInteger.valueOf(20);
    final int halfLifeInHours = 35;
    final int hoursSinceDosage = 24;
    final BigDecimal remainingExpected = BigDecimal.valueOf(12.43399);

    final BigDecimal actual = HalfLifeCalculator.calculateRemainingMgAtTime(fullDose, halfLifeInHours, hoursSinceDosage);
    
    assertEquals(remainingExpected, actual);
  }
  
  @Test
  public void testHalfLifeAtNextDosingTimeWithNextDose() {
    final BigInteger fullDose = BigInteger.valueOf(20);
    final int halfLifeInHours = 35;
    final int hoursAtNextDosage = 24;
    final int numberOfDoses = 2;
    final BigDecimal remainingExpected = BigDecimal.valueOf(19.89438);

    final BigDecimal actual = HalfLifeCalculator.calculateAccumulatedMgAtTimeBasedOnRegularDosing(fullDose, halfLifeInHours, hoursAtNextDosage, numberOfDoses);

    assertEquals(remainingExpected, actual);    
  }
  
  @Test
  public void testHalfLifeAtNextDosingTimeWithNextDoses() {
    final BigInteger fullDose = BigInteger.valueOf(20);
    final int halfLifeInHours = 35;
    final int hoursAtNextDosage = 24;
    final int numberOfDoses = 5;
    final BigDecimal remainingExpected = BigDecimal.valueOf(82.06434);

    final BigDecimal actual = HalfLifeCalculator.calculateAccumulatedMgAtTimeBasedOnRegularDosing(fullDose, halfLifeInHours, hoursAtNextDosage, numberOfDoses);

    assertEquals(remainingExpected, actual);    
  }
  
  @Test
  public void testSpecific() {
    final BigInteger fullDose = BigInteger.valueOf(20);
    final int halfLifeInHours = 35;
    final int hoursAtNextDosage = 24;
    final int numberOfDoses = (7 * 110);
    final BigDecimal afterTwoYearsRemaining = HalfLifeCalculator.calculateAccumulatedMgAtTimeBasedOnRegularDosing(fullDose, halfLifeInHours, hoursAtNextDosage, numberOfDoses);
    final int hoursRemaining = HalfLifeCalculator.calculateHoursRemainingUntilNeglegible(afterTwoYearsRemaining.toBigInteger(), halfLifeInHours);

    System.out.format("Current amount in system is: %s, number of hours remaining: %s, days: %s, weeks: %s", afterTwoYearsRemaining, hoursRemaining, (hoursRemaining / 24), (hoursRemaining / 24 / 7));
  }

}
