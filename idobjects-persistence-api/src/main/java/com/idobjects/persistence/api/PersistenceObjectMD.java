package com.idobjects.persistence.api;

import java.util.ArrayList;
import java.util.List;

import com.idobjects.api.md.IdObjectMD;
import com.idobjects.api.md.IdObjectPropertyMD;
import com.idobjects.api.md.IdObjectReferenceMD;

public class PersistenceObjectMD{

    protected static class InitData{

        public InitData(){

        }

        public List<PersistencePropertyMD> propertiesMD;
        public List<PersistenceReferenceMD> referencesMD;

        public IdObjectMD idObjectMD;
        public Class persistenceObjectClass;
        public Class persistenceReferencesClass;
        public Class objectVersionClass;
        public Class referenceVersionClass;

        public String modelVersionField;
        public String entityField;
        public String entityReferenceField;
    }

    private final List<PersistencePropertyMD> propertiesMD = new ArrayList<PersistencePropertyMD>();
    private final List<PersistenceReferenceMD> referencesMD = new ArrayList<PersistenceReferenceMD>();

    private final IdObjectMD idObjectMD;
    private final Class persistenceObjectClass;
    private final Class persistenceReferencesClass;
    private final Class objectVersionClass;
    private final Class referenceVersionClass;

    private final String modelVersionField;
    private final String entityField;
    private final String entityReferenceField;

    protected PersistenceObjectMD( InitData initData ){
        this.persistenceObjectClass = initData.persistenceObjectClass;
        this.idObjectMD = initData.idObjectMD;
        this.persistenceReferencesClass = initData.persistenceReferencesClass;
        this.objectVersionClass = initData.objectVersionClass;
        this.referenceVersionClass = initData.referenceVersionClass;

        this.modelVersionField = initData.modelVersionField;
        this.entityField = initData.entityField;
        this.entityReferenceField = initData.entityReferenceField;

        this.propertiesMD.addAll( initData.propertiesMD );
        this.referencesMD.addAll( initData.referencesMD );
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

    public Class getObjectVersionClass(){
        return objectVersionClass;
    }

    public Class getReferenceVersionClass(){
        return referenceVersionClass;
    }

    public String getModelVersionField(){
        return modelVersionField;
    }

    public String getEntityField(){
        return entityField;
    }

    public String getEntityReferenceField(){
        return entityReferenceField;
    }

    public IdObjectMD getIdObjectMD(){
        return idObjectMD;
    }

}
