package com.example.envUtils;


import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvBuilder;

import java.io.FileWriter;
import java.io.IOException;

public class DotenvConfig {
    private static final Dotenv dotenv = new DotenvBuilder().filename("serverParameters.env").ignoreIfMissing().load();

    public static String get(String key) {
        return dotenv.get(key);
    }

}
