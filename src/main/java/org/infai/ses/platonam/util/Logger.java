/*
 * Copyright 2021 InfAI (CC SES)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.infai.ses.platonam.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.*;


public class Logger {

    static private final Map<String, Level> levelMap;
    static private Level loggerLevel;
    static private Handler loggerHandler;

    static {
        levelMap = new HashMap<>();
        levelMap.put("error", Level.SEVERE);
        levelMap.put("warning", Level.WARNING);
        levelMap.put("info", Level.INFO);
        levelMap.put("debug", Level.FINEST);
    }

    static public void setup(String level) {
        loggerHandler = new ConsoleHandler();
        loggerHandler.setFormatter(new MsgFormatter());
        loggerHandler.setLevel(levelMap.get(level));
        loggerLevel = levelMap.get(level);
    }

    static public java.util.logging.Logger getLogger(String name) {
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(name);
        logger.addHandler(loggerHandler);
        logger.setLevel(loggerLevel);
        logger.setUseParentHandlers(false);
        return logger;
    }

    static class MsgFormatter extends Formatter {

        @Override
        public String format(LogRecord rec) {
            return calcDate(rec.getMillis()) +
                    " - " +
                    rec.getLevel() +
                    ": [" +
                    rec.getLoggerName() +
                    "] " +
                    formatMessage(rec) +
                    "\n";
        }

        public String getHead(Handler h) {
            return super.getHead(h);
        }

        public String getTail(Handler h) {
            return super.getTail(h);
        }

        private String calcDate(long millisecs) {
            SimpleDateFormat date_format = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss a");
            Date resultdate = new Date(millisecs);
            return date_format.format(resultdate);
        }
    }
}