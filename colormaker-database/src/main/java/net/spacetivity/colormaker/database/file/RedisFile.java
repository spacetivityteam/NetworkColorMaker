package net.spacetivity.colormaker.database.file;

import lombok.Data;

@Data
public class RedisFile {

    private String hostname;
    private String port;
    private String password;

}
