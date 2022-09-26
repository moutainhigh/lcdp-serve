package com.redxun.system.util.kettle;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class KRepository {

    private String name;

    private String resUser;

    private String resPwd;

    private String dbType;

    private String host;

    private String port;

    private String databaseName;

    private String user;

    private String password;


}
