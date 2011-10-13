package com.idobjects.api.md;

import java.util.ArrayList;
import java.util.List;

import com.idobjects.api.IdObjectException;

public abstract class ModelMetadata{

    private final List<IdObjectMD> objects = new ArrayList<IdObjectMD>();

    public ModelMetadata( List<IdObjectMD> objects ){
        this.objects.addAll( objects );
    }

    public List<IdObjectMD> getIdObjects(){
        return new ArrayList<IdObjectMD>( objects );

    }

    public IdObjectMD getIdObjectMDByIdObjectClass( Class idObjectClass ){
        for( IdObjectMD idObjectMD : objects ){
            if( idObjectMD.getIObjectClass().equals( idObjectClass ) ) return idObjectMD;
        }
        throw new IdObjectException( "No IdObjectMD instance for class " + idObjectClass );
    }

    protected void addIdObject( IdObjectMD idObjectMD ){
        objects.add( idObjectMD );
    }

}
