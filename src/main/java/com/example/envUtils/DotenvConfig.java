package com.example.envUtils;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;

public class DotenvConfig {
    //testcommit
    private static final Dotenv dotenv = new DotenvBuilder().filename("serverParameters.env").ignoreIfMissing().load();

    public static String get(String key) {
        return dotenv.get(key);
    }

}
