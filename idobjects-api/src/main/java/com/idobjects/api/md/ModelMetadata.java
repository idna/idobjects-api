package com.idobjects.api.md;

import java.util.ArrayList;
import java.util.List;

import com.idobjects.api.IdObjectException;

public abstract class ModelMetadata{

    private final List<IdObjectMD> objects = new ArrayList<IdObjectMD>();

    public List<IdObjectMD> getIdObjects(){
        return null;

    }

    public IdObjectMD getIdObjectMDByClass( Class idObjectMDClass ){
        for( IdObjectMD idObjectMD : objects ){
            if( idObjectMD.getClass().equals( idObjectMDClass ) ) return idObjectMD;
        }
        throw new IdObjectException( "No IdObjectMD instance for class " + idObjectMDClass );
    }

    protected void addIdObject( IdObjectMD idObjectMD ){
        objects.add( idObjectMD );
    }

}
