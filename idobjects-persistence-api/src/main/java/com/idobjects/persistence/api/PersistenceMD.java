package com.idobjects.persistence.api;

import java.util.ArrayList;
import java.util.List;

import com.idobjects.api.md.ModelMetadata;

public class PersistenceMD{

    private final List<PersistenceObjectMD> objects = new ArrayList<PersistenceObjectMD>();
    
    private final ModelMetadata modelMD;

    public PersistenceMD(ModelMetadata modelMD, List<PersistenceObjectMD> objects ){
        this.modelMD = modelMD;
        this.objects.addAll( objects );
    }

    public List<PersistenceObjectMD> getObjects(){
        return new ArrayList<PersistenceObjectMD>( objects );
    }

    public PersistenceObjectMD getPersistenceObjectMD( Class idObjectClass ){
        for( PersistenceObjectMD pObjectMD : objects ){
            if( pObjectMD.getIdObjectClass().equals( idObjectClass ) ) return pObjectMD;
        }
        return null;
    }
    
    
    public ModelMetadata getModelMD() {
        return modelMD;
    }

}
