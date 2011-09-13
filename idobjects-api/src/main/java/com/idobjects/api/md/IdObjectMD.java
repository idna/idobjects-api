package com.idobjects.api.md;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IdObjectMD{

    private final String className;

    private final List<IdObjectReferenceMD> references = new ArrayList<IdObjectReferenceMD>();

    private final List<IdObjectPropertyMD> properties = new ArrayList<IdObjectPropertyMD>();

    public IdObjectMD( String className, Collection<IdObjectPropertyMD> properties, Collection<IdObjectReferenceMD> references ){
        this.className = className;
        this.properties.addAll( properties );
        this.references.addAll( references );
    }

    public String getClassName(){
        return className;
    }

    public List<IdObjectReferenceMD> getReferences(){
        return new ArrayList<IdObjectReferenceMD>( references );
    }

    public List<IdObjectPropertyMD> getProperties(){
        return new ArrayList<IdObjectPropertyMD>( properties );
    }

}