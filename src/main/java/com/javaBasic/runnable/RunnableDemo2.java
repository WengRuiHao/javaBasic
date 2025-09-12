package com.javaBasic.javaBasic.runnable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RunnableDemo2 {
    public static void main(String[] args) {
        Runnable job1 = () -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mmLss.S E");
            System.out.println("現在時刻 : " + sdf.format(new Date()));
        };

        Runnable job2 = () -> {
            System.out.println("我要上廁所");
        };

        // 單工模式
//        job1.run();
//        job2.run();

        // 多執行緒
        new Thread(job1).start();
        new Thread(job2).start();
    }
}
