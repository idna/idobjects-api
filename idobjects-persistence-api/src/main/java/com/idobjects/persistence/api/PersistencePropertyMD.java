package com.idobjects.persistence.api;

import com.idobjects.api.md.IdObjectPropertyMD;

public class PersistencePropertyMD{

    private final IdObjectPropertyMD idObjectPropertyMD;

    public PersistencePropertyMD( IdObjectPropertyMD idObjectPropertyMD ){
        this.idObjectPropertyMD = idObjectPropertyMD;
    }

    public IdObjectPropertyMD getIdObjectPropertyMD(){
        return idObjectPropertyMD;
    }
}
