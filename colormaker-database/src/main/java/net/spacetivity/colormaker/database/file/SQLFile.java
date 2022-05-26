package net.spacetivity.colormaker.database.file;

import lombok.Data;

@Data
public class SQLFile {

    private String hostname;
    private String port;
    private String database;
    private String user;
    private String password;

}
