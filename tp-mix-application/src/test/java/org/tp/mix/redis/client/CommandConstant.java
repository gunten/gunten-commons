package org.tp.mix.redis.client;

/**
 **/
public class CommandConstant {

    public static final String START="*";

    public static final String LENGTH="$";

    public static final String LINE="\r\n";

    public enum CommandEnum{
        SET,
        GET
    }
}
