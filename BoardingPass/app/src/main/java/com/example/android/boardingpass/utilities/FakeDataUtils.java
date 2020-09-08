package com.example.android.boardingpass.utilities;

import com.example.android.boardingpass.BoardingPassInfo;
import com.example.android.boardingpass.R;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class FakeDataUtils {

    public static BoardingPassInfo generateFakeBoardingInfo(){


        BoardingPassInfo bpi = new BoardingPassInfo();


        bpi.passengerName = "MR. KEATS SALAZAR";
        bpi.flightCode = "UD 777";
        bpi.originCode = "PAT";
        bpi.destCode = "BBS";

        long now = System.currentTimeMillis();

        // Anything from 0 minutes up to (but not including) 30 minutes
        long randomMinuteUntilBoarding = (long) (Math.random() * 30);

        // Standard 40 minute boarding time
        long totalBoardingMinutes = 40;
        // Anything from 1 hours up to (but not including) 6 hours
        long randomFlightLengthHours = (long) (Math.random() * 5 + 1);

        long boardingMillis = now + minutesToMillis(randomMinuteUntilBoarding);
        long departure = boardingMillis + minutesToMillis(totalBoardingMinutes);
        long arrival = departure + hoursToMillis(randomFlightLengthHours);

        bpi.boardingTime = new Timestamp(boardingMillis);
        bpi.departureTime = new Timestamp(departure);
        bpi.arrivalTime = new Timestamp(arrival);
        bpi.departureTerminal = "3A";
        bpi.departureGate = "33C";
        bpi.seatNumber = "1A";
        bpi.barCodeImageResource = R.drawable.ic_plane;

        return bpi;
    }

    private static long minutesToMillis(long minutes) {
        return TimeUnit.MINUTES.toMillis(minutes);
    }

    private static long hoursToMillis(long hours) {
        return TimeUnit.HOURS.toMillis(hours);
    }

}
