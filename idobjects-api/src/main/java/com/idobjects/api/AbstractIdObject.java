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

    private void setSingleReference( IdObjectReferenceMD referenceMD, IdObject value, boolean addInverse ){
        IdObjectReference oldReference = getSingleIdObjectReference( referenceMD );
        if( oldReference != null ){
            oldReference.clear();

        }
        IdObjectReference newReference = new IdObjectReference( this.getId(), value.getId(), getModelScope(), value.getModelScope(), referenceMD );
        references.put( referenceMD, oneElementList( newReference ) );

        if( referenceMD.isBidirectional() && addInverse ){
            ( ( AbstractIdObject )value ).addReferenceImpl( referenceMD.getInverseReferenceMD(), this, false );
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
        IdObject destinationObject = toRemove.getDestinationObject();
        toRemove.clear();
        references.remove( referenceMD );

        if( referenceMD.isBidirectional() && removeInverse ){
            ( ( AbstractIdObject )destinationObject ).removeReferenceImpl( referenceMD.getInverseReferenceMD(), this, false );
        }

    }

    private void addListReference( IdObjectReferenceMD referenceMD, IdObject value, boolean addInverse ){
        List<IdObjectReference> list = references.get( referenceMD );
        if( list == null ){
            list = new ArrayList<IdObjectReference>();
            references.put( referenceMD, list );
        }

        IdObjectReference newReference = new IdObjectReference( this.getId(), value.getId(), getModelScope(), value.getModelScope(), referenceMD );
        list.add( newReference );

        if( referenceMD.isBidirectional() && addInverse ){
            ( ( AbstractIdObject )value ).addReferenceImpl( referenceMD.getInverseReferenceMD(), this, false );
        }

    }

    private void removeListReference( IdObjectReferenceMD referenceMD, IdObject value, boolean removeInverse ){
        if( value == null ) throw new NullPointerException( "value is null" );
        List<IdObjectReference> list = references.get( referenceMD );
        if( list == null ) return;
        IdObjectReference removed = searchAndRemoveReference( list, value.getId() );
        if( removed != null ){
            removed.clear();
        }
        if( list.size() == 0 ) references.remove( referenceMD );

        if( referenceMD.isBidirectional() && removeInverse ){
            ( ( AbstractIdObject )value ).removeReferenceImpl( referenceMD.getInverseReferenceMD(), this, false );
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
    public void addReference( IdObjectReferenceMD referenceMD, IdObject value ){
        addReferenceImpl( referenceMD, value, true );
    }

    private void addReferenceImpl( IdObjectReferenceMD referenceMD, IdObject value, boolean addInverse ){
        switch ( referenceMD.getReferenceType() ){
            case SINGLE:
                setSingleReference( referenceMD, value, addInverse );
                break;
            case LIST:
                addListReference( referenceMD, value, addInverse );
                break;
            default:
                throw new IdObjectException( "Unknown referenceType: " + referenceMD.getReferenceType() );
        }
    }

    @Override
    public void removeReference( IdObjectReferenceMD referenceMD, IdObject value ){
        removeReferenceImpl( referenceMD, value, true );
    }

    private void removeReferenceImpl( IdObjectReferenceMD referenceMD, IdObject value, boolean removeInverse ){
        switch ( referenceMD.getReferenceType() ){
            case SINGLE:
                removeSingleReference( referenceMD, removeInverse );
                break;
            case LIST:
                removeListReference( referenceMD, value, removeInverse );
                break;
            default:
                throw new IdObjectException( "Unknown referenceType: " + referenceMD.getReferenceType() );
        }
    }

    @Override
    public List<IdObject> getReferences( IdObjectReferenceMD referenceMD ){
        List<IdObjectReference> idObjectReferences = this.references.get( referenceMD );
        if( idObjectReferences == null ) return new ArrayList<IdObject>();
        List<IdObject> result = new ArrayList<IdObject>();
        for( IdObjectReference idObjectReference : idObjectReferences ){
            result.add( idObjectReference.getDestinationObject() );
        }
        return result;
    }

    protected <T> List<T> getCastedReferences( IdObjectReferenceMD referenceMD, Class<T> resultType ){
        return ReflectionUtil.cast( getReferences( referenceMD ), resultType );
    }

    protected IdObject getSingleReference( IdObjectReferenceMD referenceMD ){
        List<IdObject> referingObjects = getReferences( referenceMD );
        return referingObjects.size() > 0 ? referingObjects.get( 0 ) : null;
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
