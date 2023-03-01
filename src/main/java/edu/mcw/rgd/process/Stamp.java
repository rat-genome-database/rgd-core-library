package edu.mcw.rgd.process;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jdepons
 * Date: Jun 5, 2008
 * Time: 10:42:25 AM
 */
public class Stamp {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");

    public static void it(String description) {
        System.out.println(description + " " + sdf.format(new Date()));
    }
    
}
