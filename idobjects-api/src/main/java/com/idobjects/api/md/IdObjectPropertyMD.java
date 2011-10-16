package com.idobjects.api.md;

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

    @Override
    public String toString(){
        return "IdObjectPropertyMD [name=" + name + ", type=" + type + "]";
    }
}
