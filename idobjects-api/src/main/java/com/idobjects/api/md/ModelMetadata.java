package com.idobjects.api.md;

import java.util.ArrayList;
import java.util.List;


public abstract class ModelMetadata{
    
    private final List<IdObjectMD> objects = new ArrayList<IdObjectMD>();
    
    public List<IdObjectMD> getIdObjects(){
        return null;
        
    }
    
    protected void addIdObject(IdObjectMD idObjectMD){
        objects.add( idObjectMD );
    }

}
