package com.idobjects.persistence.api;

import com.idobjects.api.md.IdObjectPropertyMD;

public class PersistencePropertyMD{

    private final IdObjectPropertyMD idObjectPropertyMD;
    private final Class type;

    public PersistencePropertyMD( IdObjectPropertyMD idObjectPropertyMD, Class type ){
        this.idObjectPropertyMD = idObjectPropertyMD;
        this.type = type;
    }

    public IdObjectPropertyMD getIdObjectPropertyMD(){
        return idObjectPropertyMD;
    }

    public Class getType(){
        return type;
    }
}
