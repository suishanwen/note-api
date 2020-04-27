package com.sw.note.util;

import java.util.concurrent.Executors;

public class IdGenerator {

    private static SnowflakeIdWorker snowflakeIdWorker;

    public static long next() {
        if (snowflakeIdWorker == null) {
            synchronized (IdGenerator.class) {
                if (snowflakeIdWorker == null) {
                    // 创建线程池
                    snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
                }
            }
        }
        return snowflakeIdWorker.nextId();
    }
}
