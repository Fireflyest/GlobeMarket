package com.fireflyest.market.util;

import org.bukkit.Bukkit;

import java.util.logging.Logger;

/**
 * @author Fireflyest
 * 2021/4/10 12:05
 */

public class LoggerUtils {

    private final Logger logger = Bukkit.getLogger();

    private LoggerUtils(){
    }

    public void e(String tag, String text){

    }

    public void w(String tag, String text){
        logger.warning(String.format("%s -> %s", tag, text));
    }

}
