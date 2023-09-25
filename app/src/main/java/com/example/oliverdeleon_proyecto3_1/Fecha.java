package com.example.oliverdeleon_proyecto3_1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Fecha {
    public String getFecha(long l){

        Date now = new Date();

        long currentDateTime = System.currentTimeMillis();

        //creating Date from millisecond
        Date currentDate = new Date(currentDateTime);

        //printing value of Date
        System.out.println("current Date: " + currentDate);

//        DateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");

        SimpleDateFormat df = new SimpleDateFormat("dd:MM:yy:HH:mm:ss");


        //formatted value of current Date
        System.out.println("Milliseconds to Date: " + df.format(currentDate));

//        String fe = df.format(currentDate);


        long days = TimeUnit.DAYS.toDays(now.getTime());

        SimpleDateFormat format = new SimpleDateFormat( "h:mm a", Locale.getDefault());

//        "yyyy.MM.dd G 'at' HH:mm:ss z"

       String fecha = format.format(new Date());

       String day = Long.toString(days);

        String fe = format.format(days);



        String ff = format.format(new java.util.Date(days*1000));

        Date hol = new Date();

        SimpleDateFormat holaf = new SimpleDateFormat( "h:mm:ss a", Locale.getDefault());

        int hold = (int) hol.getTime();


        return holaf.format(hold);
    }
}
