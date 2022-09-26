package com.redxun.system.util.kettle;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RepositoryTree {

    private String id;

    private String parent;

    private String text;

    private String icon;

    private Object state;

    private String type;

    private boolean isLasted;

    private String path;
}
