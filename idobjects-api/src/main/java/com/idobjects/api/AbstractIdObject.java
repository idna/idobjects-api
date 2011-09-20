package com.idobjects.api;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.idobjects.api.md.IdObjectPropertyMD;
import com.idobjects.api.md.IdObjectReferenceMD;
import com.idobjects.api.md.ReflectionUtil;

public abstract class AbstractIdObject implements IdObject{

    private ObjectIdentifier id;

    private ModelScope modelScope;

    private final Map<IdObjectPropertyMD, Object> propertyValues = new LinkedHashMap<IdObjectPropertyMD, Object>();

    private final Map<IdObjectReferenceMD, List<IdObjectReference>> references = new LinkedHashMap<IdObjectReferenceMD, List<IdObjectReference>>();

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

    protected IdObject getReferencedObject( IdObjectReferenceMD referenceMD ){
        if( !references.containsKey( referenceMD ) ) return null;
        return references.get( referenceMD ).get( 0 ).getDestinationObject();
    }

    private void setSingleReference( IdObjectReferenceMD referenceMD, ObjectIdentifier destinationId, boolean addInverse ){
        IdObjectReference oldReference = getSingleIdObjectReference( referenceMD );
        if( oldReference != null ){
            oldReference.clear();

        }
        IdObjectReference newReference = new IdObjectReference( this.getId(), destinationId, getModelScope(), referenceMD );
        references.put( referenceMD, oneElementList( newReference ) );

        IdObject destination = modelScope.getObject( destinationId );
        if( destination != null && referenceMD.isBidirectional() && addInverse ){
            ( ( AbstractIdObject )destination ).addReferenceImpl( referenceMD.getInverseReferenceMD(), getId(), false );
        }
    }

    private IdObjectReference getSingleIdObjectReference( IdObjectReferenceMD referenceMD ){
        List<IdObjectReference> list = references.get( referenceMD );
        if( list == null ) return null;
        return list.get( 0 );
    }

    private List<IdObjectReference> oneElementList( IdObjectReference idObjectReference ){
        List<IdObjectReference> result = new ArrayList<IdObjectReference>();
        result.add( idObjectReference );
        return result;
    }

    private void removeSingleReference( IdObjectReferenceMD referenceMD, boolean removeInverse ){
        IdObjectReference toRemove = getSingleIdObjectReference( referenceMD );
        if( toRemove == null ) return;
        toRemove.clear();
        references.remove( referenceMD );

        IdObject destinationObject = toRemove.getDestinationObject();
        if( destinationObject != null && referenceMD.isBidirectional() && removeInverse ){
            ( ( AbstractIdObject )destinationObject ).removeReferenceImpl( referenceMD.getInverseReferenceMD(), getId(), false );
        }

    }

    private void addListReference( IdObjectReferenceMD referenceMD, ObjectIdentifier destinationId, boolean addInverse ){
        List<IdObjectReference> list = references.get( referenceMD );
        if( list == null ){
            list = new ArrayList<IdObjectReference>();
            references.put( referenceMD, list );
        }

        IdObjectReference newReference = new IdObjectReference( this.getId(), destinationId, getModelScope(), referenceMD );
        list.add( newReference );
        IdObject destination = modelScope.getObject( destinationId );
        if( destination != null && referenceMD.isBidirectional() && addInverse ){

            ( ( AbstractIdObject )destination ).addReferenceImpl( referenceMD.getInverseReferenceMD(), getId(), false );
        }

    }

    private void removeListReference( IdObjectReferenceMD referenceMD, ObjectIdentifier destinationId, boolean removeInverse ){
        List<IdObjectReference> list = references.get( referenceMD );
        if( list == null ) return;
        IdObjectReference removed = searchAndRemoveReference( list, destinationId );
        if( removed != null ){
            removed.clear();
        }
        if( list.size() == 0 ) references.remove( referenceMD );

        IdObject destination = modelScope.getObject( destinationId );
        if( destination != null && referenceMD.isBidirectional() && removeInverse ){
            ( ( AbstractIdObject )destination ).removeReferenceImpl( referenceMD.getInverseReferenceMD(), getId(), false );
        }
    }

    private IdObjectReference searchAndRemoveReference( List<IdObjectReference> searchIn, ObjectIdentifier destObjectId ){
        for( Iterator<IdObjectReference> it = searchIn.iterator(); it.hasNext(); ){
            IdObjectReference idObjectReference = it.next();
            if( idObjectReference.getDestinationObjectId().equals( destObjectId ) ){
                it.remove();
                return idObjectReference;
            }
        }
        return null;
    }

    @Override
    public void addReference( IdObjectReferenceMD referenceMD, ObjectIdentifier destinationId ){
        addReferenceImpl( referenceMD, destinationId, true );
    }

    protected void addReference( IdObjectReferenceMD referenceMD, IdObject value ){
        addReferenceImpl( referenceMD, value.getId(), true );
    }

    void addReferenceImpl( IdObjectReferenceMD referenceMD, ObjectIdentifier destinationId, boolean addInverse ){
        switch ( referenceMD.getReferenceType() ){
            case SINGLE:
                setSingleReference( referenceMD, destinationId, addInverse );
                break;
            case LIST:
                addListReference( referenceMD, destinationId, addInverse );
                break;
            default:
                throw new IdObjectException( "Unknown referenceType: " + referenceMD.getReferenceType() );
        }
    }

    @Override
    public void removeReference( IdObjectReferenceMD referenceMD, ObjectIdentifier destinationId ){
        removeReferenceImpl( referenceMD, destinationId, true );
    }

    private void removeReferenceImpl( IdObjectReferenceMD referenceMD, ObjectIdentifier destinationId, boolean removeInverse ){
        switch ( referenceMD.getReferenceType() ){
            case SINGLE:
                removeSingleReference( referenceMD, removeInverse );
                break;
            case LIST:
                removeListReference( referenceMD, destinationId, removeInverse );
                break;
            default:
                throw new IdObjectException( "Unknown referenceType: " + referenceMD.getReferenceType() );
        }
    }

    protected List<IdObject> getReferenceDestinations( IdObjectReferenceMD referenceMD ){
        List<IdObjectReference> idObjectReferences = this.references.get( referenceMD );
        if( idObjectReferences == null ) return new ArrayList<IdObject>();
        List<IdObject> result = new ArrayList<IdObject>();
        for( IdObjectReference idObjectReference : idObjectReferences ){
            result.add( idObjectReference.getDestinationObject() );
        }
        return result;
    }

    @Override
    public Map<IdObjectReferenceMD, List<IdObjectReference>>getReferences(){
        return new LinkedHashMap<IdObjectReferenceMD, List<IdObjectReference>>( references );
    }

    protected <T> List<T> getCastedReferences( IdObjectReferenceMD referenceMD, Class<T> resultType ){
        return ReflectionUtil.cast( getReferenceDestinations( referenceMD ), resultType );
    }

    protected IdObject getSingleReference( IdObjectReferenceMD referenceMD ){
        List<IdObject> referingObjects = getReferenceDestinations( referenceMD );
        return referingObjects.size() > 0 ? referingObjects.get( 0 ) : null;
    }

    @Override
    public ModelScope getModelScope(){
        return modelScope;
    }

    AbstractIdObject copy( ModelScope modelScope ){
        AbstractIdObject result = ReflectionUtil.newIdObject( getClass(), modelScope, getId() );
        result.propertyValues.putAll( propertyValues );
        return result;
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
