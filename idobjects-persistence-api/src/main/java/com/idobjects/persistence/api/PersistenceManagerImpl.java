package com.idobjects.persistence.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import com.idobjects.api.AbstractIdObject;
import com.idobjects.api.IdObject;
import com.idobjects.api.IdObjectReference;
import com.idobjects.api.ModelScope;
import com.idobjects.api.md.IdObjectMD;
import com.idobjects.api.md.IdObjectPropertyMD;
import com.idobjects.api.md.IdObjectReferenceMD;

public class PersistenceManagerImpl implements PersistenceManager{

    private final SessionFactory sessionFactory;

    private final PersistenceModelMD persistenceMD;

    public PersistenceManagerImpl( SessionFactory sessionFactory, PersistenceModelMD persistenceMD ){
        this.sessionFactory = sessionFactory;
        this.persistenceMD = persistenceMD;
    }

    public void saveModelScope( ModelScope modelScope ){
        Session session = null;
        try{

            session = sessionFactory.openSession();
            List<ConvertedIdObject> convertedMS = convert( modelScope );

            for( ConvertedIdObject convertResult : convertedMS ){
                session.save( convertResult.persistenceObject );

                for( ReferencePO persistenceReference : convertResult.references ){
                    session.save( persistenceReference );
                }
            }

        }
        catch( Exception e ){
            throw new RuntimeException( e );
        }
        finally{
            if( session != null ){
                session.close();
            }
        }

    }

    private List<ConvertedIdObject> convert( ModelScope modelScope ) throws Exception{
        List<IdObject> objects = modelScope.getObjects();

        List<ConvertedIdObject> result = new ArrayList<ConvertedIdObject>();

        for( IdObject idObject : objects ){
            AbstractIdObject abstractIdObject = ( AbstractIdObject )idObject;
            PersistenceObjectMD persistenceObjectMD = persistenceMD.getPersistenceObjectMD( abstractIdObject.getClass() );

            ConvertedIdObject convertResult = new ConvertedIdObject();
            convertResult.persistenceObject = createPO( abstractIdObject, persistenceObjectMD );
            convertResult.references = createReferencePOs( abstractIdObject, persistenceObjectMD );

            result.add( convertResult );
        }
        return result;

    }

    private List<ReferencePO> createReferencePOs( AbstractIdObject abstractIdObject, PersistenceObjectMD persistenceObjectMD ) throws InstantiationException,
            IllegalAccessException{
        List<ReferencePO> result = new ArrayList<ReferencePO>();

        Map<IdObjectReferenceMD, List<IdObjectReference>> references = abstractIdObject.getReferences();

        for( IdObjectReferenceMD idObjectReferenceMD : references.keySet() ){
            PersistenceReferenceMD persistenceReferenceMD = persistenceObjectMD.getReference( idObjectReferenceMD );

            List<IdObjectReference> idObjectReferenceList = references.get( idObjectReferenceMD );
            for( IdObjectReference idObjectReference : idObjectReferenceList ){

                ReferencePO referencePO = ( ReferencePO )persistenceObjectMD.getPersistenceReferencesClass().newInstance();
                referencePO.setSourceId( idObjectReference.getSourceObjectId().toString() );
                referencePO.setDestinationId( idObjectReference.getDestinationObjectId().toString() );
                referencePO.setReferenceName( persistenceReferenceMD.getIdObjectReferenceMD().getName() );
                result.add( referencePO );
            }
        }
        return result;
    }

    private AbstractPO createPO( AbstractIdObject abstractIdObject, PersistenceObjectMD persistenceObjectMD ) throws Exception{
        Map<IdObjectPropertyMD, Object> propertyValues = abstractIdObject.getPropertyValues();

        IdObjectMD idObjectMD = persistenceMD.getModelMD().getIdObjectMDByIdObjectClass( abstractIdObject.getClass() );
        List<IdObjectPropertyMD> allProperties = idObjectMD.getProperties();

        AbstractPO persistenceObject = ( AbstractPO )persistenceObjectMD.getPersistenceObjectClass().newInstance();

        // convert the properties from idObject to persistenceObject
        for( IdObjectPropertyMD propertyMD : allProperties ){
            Object propertyValue = propertyValues.get( propertyMD );
            PersistencePropertyMD persistencePropertyMD = persistenceObjectMD.getProperty( propertyMD );
            persistenceObject.setPropertyValue( persistencePropertyMD, propertyValue );
        }

        persistenceObject.setObjectId( abstractIdObject.getId().toString() );

        return persistenceObject;
    }

    public void saveModelSope( ModelScope modelScope, ChangeRecorder changes ){
        // TODO Auto-generated method stub

    }

    private class ConvertedIdObject{

        AbstractPO persistenceObject;
        List<ReferencePO> references;
    }

}
