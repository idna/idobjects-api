package com.idobjects.persistence.api;

import java.util.ArrayList;
import java.util.List;

import com.idobjects.api.md.IdObjectMD;
import com.idobjects.api.md.IdObjectPropertyMD;
import com.idobjects.api.md.IdObjectReferenceMD;

public class PersistenceObjectMD{

    private final IdObjectMD idObjectMD = null;

    private final List<PersistencePropertyMD> propertiesMD = new ArrayList<PersistencePropertyMD>();
    private final List<PersistenceReferenceMD> referencesMD = new ArrayList<PersistenceReferenceMD>();

    private final Class idObjectClass;
    private final Class persistenceObjectClass;
    private final Class persistenceReferencesClass;

    protected PersistenceObjectMD( Class idObjectClass, Class persistenceObjectClass, Class persistenceReferencesClass, List<PersistencePropertyMD> propertiesMD,
            List<PersistenceReferenceMD> referencesMD ){
        this.persistenceObjectClass = persistenceObjectClass;
        this.idObjectClass = idObjectClass;
        this.persistenceReferencesClass = persistenceReferencesClass;
        this.propertiesMD.addAll( propertiesMD );
        this.referencesMD.addAll( referencesMD );
    }

    public Class getIdObjectClass(){
        return idObjectClass;
    }

    public Class getPersistenceObjectClass(){
        return persistenceObjectClass;
    }

    public Class getPersistenceReferencesClass(){
        return persistenceReferencesClass;
    }

    public PersistencePropertyMD getProperty( IdObjectPropertyMD idObjectPropertyMD ){
        for( PersistencePropertyMD propertyMD : propertiesMD ){
            if( propertyMD.getIdObjectPropertyMD().equals( idObjectPropertyMD ) ) return propertyMD;
        }
        return null;
    }

    public PersistenceReferenceMD getReference( IdObjectReferenceMD idObjectReferenceMD ){
        for( PersistenceReferenceMD referenceMD : referencesMD ){
            if( referenceMD.getIdObjectReferenceMD().equals( idObjectReferenceMD ) ) return referenceMD;
        }
        return null;
    }

}
