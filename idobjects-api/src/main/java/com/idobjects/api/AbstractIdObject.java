package com.idobjects.api;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.idobjects.api.md.IdObjectPropertyMD;
import com.idobjects.api.md.IdObjectReferenceMD;

public abstract class AbstractIdObject implements IdObject{

    private ObjectIdentifier id;

    private ModelScope modelScope;

    private final Map<IdObjectPropertyMD, Object> propertyValues = new LinkedHashMap<IdObjectPropertyMD, Object>();

    private final Map<IdObjectReferenceMD, IdObjectReference> references = new LinkedHashMap<IdObjectReferenceMD, IdObjectReference>();

    protected AbstractIdObject( ModelScope modelScope, ObjectIdentifier objectId ){
        if( modelScope == null ) throw new NullPointerException( "modelScope is null" );
        if( objectId == null ) throw new NullPointerException( "objectId is null" );

        this.modelScope = modelScope;
        this.id = objectId;
        modelScope.addObject( this );
    }

    @Override
    public final ObjectIdentifier getId(){
        return id;
    }

    public void setId( ObjectIdentifier newId ){
        if( newId == null ) throw new NullPointerException( "newId is null" );

        if( id.equals( newId ) ) return;
        ObjectIdentifier oldId = this.id;
        this.id = newId;
        modelScope.idChanged( this, oldId, newId );
    }

    protected Object getPropertyValue( IdObjectPropertyMD chief ){
        return propertyValues.get( chief );
    }

    protected void setPropertyValue( IdObjectPropertyMD property, Object value ){
        propertyValues.put( property, value );
    }

    protected IdObject getReference( IdObjectReferenceMD referenceMD ){
        if( !references.containsKey( referenceMD ) ) return null;
        return references.get( referenceMD ).getDestinationObject();
    }

    protected void setReference( IdObjectReferenceMD referenceMD, IdObject value ){
        IdObjectReference oldReference = references.get( referenceMD );
        if( oldReference != null ){
            oldReference.clear();
        }
        IdObjectReference newReference = new IdObjectReference( this.getId(), value.getId(), getModelScope(), value.getModelScope(), referenceMD );
        references.put( referenceMD, newReference );
    }

    protected void removeReference( IdObjectReferenceMD referenceMD ){
        IdObjectReference toRemove = references.remove( referenceMD );
        if(toRemove != null) toRemove.clear();
    }
    
    @Override
    public void addReference( IdObjectReferenceMD referenceMD, ObjectIdentifier destinationId ){
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void removeReference( IdObjectReferenceMD referenceMd, ObjectIdentifier destinationId ){
        // TODO Auto-generated method stub
        
    }
    

    @Override
    public ModelScope getModelScope(){
        return modelScope;
    }
    

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( modelScope == null ) ? 0 : modelScope.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ){
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( getClass() != obj.getClass() ) return false;
        AbstractIdObject other = ( AbstractIdObject )obj;
        if( id == null ){
            if( other.id != null ) return false;
        }
        else if( !id.equals( other.id ) ) return false;
        if( modelScope == null ){
            if( other.modelScope != null ) return false;
        }
        else if( !modelScope.equals( other.modelScope ) ) return false;
        return true;
    }

}
