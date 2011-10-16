package com.idobjects.persistence.api;

import java.util.ArrayList;
import java.util.List;

import com.idobjects.api.md.ModelMetadata;

public class PersistenceModelMD{

    private final List<PersistenceObjectMD> objects = new ArrayList<PersistenceObjectMD>();

    private final ModelMetadata modelMD;

    private final Class modelScopeKeyClass;
    private final Class modelScopeVersionClass;
    private final String keyPropertyName;

    public PersistenceModelMD( ModelMetadata modelMD, Class modelScopeKeyClass, Class modelScopeVersionClass, String keyPropertyName, List<PersistenceObjectMD> objects ){
        this.modelMD = modelMD;
        this.modelScopeKeyClass = modelScopeKeyClass;
        this.modelScopeVersionClass = modelScopeVersionClass;
        this.keyPropertyName = keyPropertyName;
        this.objects.addAll( objects );
    }

    public List<PersistenceObjectMD> getObjects(){
        return new ArrayList<PersistenceObjectMD>( objects );
    }

    public PersistenceObjectMD getPersistenceObjectMD( Class idObjectClass ){
        for( PersistenceObjectMD pObjectMD : objects ){
            if( pObjectMD.getIdObjectMD().getIdObjectClass().equals( idObjectClass ) ) return pObjectMD;
        }
        return null;
    }

    public List<Class> getPersistenceClasses(){
        List<Class> result = new ArrayList<Class>();
        for( PersistenceObjectMD persistenceObjectMD : objects ){
            result.add( persistenceObjectMD.getPersistenceObjectClass() );
            result.add( persistenceObjectMD.getPersistenceReferencesClass() );
            result.add( persistenceObjectMD.getObjectVersionClass() );
            result.add( persistenceObjectMD.getReferenceVersionClass() );
        }

        result.add( modelScopeKeyClass );
        result.add( modelScopeVersionClass );
        return result;
    }

    public ModelMetadata getModelMD(){
        return modelMD;
    }

    public Class getModelScopeKeyClass(){
        return modelScopeKeyClass;
    }

    public Class getModelScopeVersionClass(){
        return modelScopeVersionClass;
    }

    public String getKeyPropertyName(){
        return keyPropertyName;
    }

}
