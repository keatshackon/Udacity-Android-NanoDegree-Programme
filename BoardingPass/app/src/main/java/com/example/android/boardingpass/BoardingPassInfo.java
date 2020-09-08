package com.example.android.boardingpass;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class BoardingPassInfo {


    public String passengerName;
    public String flightCode;
    public String originCode;
    public String destCode;


    public Timestamp boardingTime;
    public Timestamp departureTime;
    public Timestamp arrivalTime;

    public String departureTerminal;
    public String departureGate;
    public String seatNumber;

    public int barCodeImageResource;

    public long getMinutesUntilBoarding(){
        long millisUnitBoarding = boardingTime.getTime()  - System.currentTimeMillis();
        return TimeUnit.MILLISECONDS.toMinutes(millisUnitBoarding);
    }


}
