package com.idobjects.persistence.api;

import java.util.ArrayList;
import java.util.List;

import com.idobjects.api.md.IdObjectMD;
import com.idobjects.api.md.IdObjectPropertyMD;

public class PersistenceObjectMD{

    private final IdObjectMD idObjectMD = null;

    private final List<PersistencePropertyMD> propertiesMD = new ArrayList<PersistencePropertyMD>();

    private final Class idObjectClass;
    private final Class persistenceObjectClass;

    protected PersistenceObjectMD( Class idObjectClass, Class persistenceObjectClass, List<PersistencePropertyMD> propertiesMD ){
        this.persistenceObjectClass = persistenceObjectClass;
        this.idObjectClass = idObjectClass;
        this.propertiesMD.addAll( propertiesMD );
    }

    public Class getIdObjectClass(){
        return idObjectClass;
    }

    public Class getPersistenceObjectClass(){
        return persistenceObjectClass;
    }

    public PersistencePropertyMD getProperty( IdObjectPropertyMD idObjectPropertyMD ){
        for(PersistencePropertyMD propertyMD : propertiesMD){
            if(propertyMD.getIdObjectPropertyMD().equals( idObjectPropertyMD )) return propertyMD;
        }
        return null;
    }

}
