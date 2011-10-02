package com.idobjects.api.md;

import java.awt.List;

public class IdObjectPropertyMD{

    private final String name;

    private final Class type;
    

    public IdObjectPropertyMD( String name, Class type ){
        super();
        this.name = name;
        this.type = type;
    }

    public Class getType(){
        return type;
    }

    public String getName(){
        return name;
    }
    

}
