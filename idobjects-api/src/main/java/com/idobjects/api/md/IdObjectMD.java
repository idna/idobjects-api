package com.idobjects.api.md;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IdObjectMD{

    private final Class idObjectClass;

    private final List<IdObjectReferenceMD> references = new ArrayList<IdObjectReferenceMD>();

    private final List<IdObjectPropertyMD> properties = new ArrayList<IdObjectPropertyMD>();

    public IdObjectMD( Class idObjectClass, Collection<IdObjectPropertyMD> properties, Collection<IdObjectReferenceMD> references ){
        this.idObjectClass = idObjectClass;
        this.properties.addAll( properties );
        this.references.addAll( references );
    }

    public Class getIObjectClass(){
        return idObjectClass;
    }

    public List<IdObjectReferenceMD> getReferences(){
        return new ArrayList<IdObjectReferenceMD>( references );
    }

    public List<IdObjectPropertyMD> getProperties(){
        return new ArrayList<IdObjectPropertyMD>( properties );
    }

    public IdObjectPropertyMD getPropertyByName( String name ){
        for( IdObjectPropertyMD property : properties ){
            if( property.getName().equals( name ) ) return property;
        }
        return null;
    }

    public IdObjectReferenceMD getReferenceByName( String name ){
        for( IdObjectReferenceMD reference : references ){
            if( reference.getName().equals( name ) ) return reference;
        }
        return null;
    }

}
