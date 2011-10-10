package com.idobjects.persistence.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;

import com.idobjects.api.AbstractIdObject;
import com.idobjects.api.IdObject;
import com.idobjects.api.ModelScope;
import com.idobjects.api.md.IdObjectMD;
import com.idobjects.api.md.IdObjectPropertyMD;

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
            List<AbstractPO> convertedMS = convert( modelScope );
            for( AbstractPO persistenceObject : convertedMS ){
                session.save( persistenceObject );
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

    private List<AbstractPO> convert( ModelScope modelScope ) throws Exception{
        List<IdObject> objects = modelScope.getObjects();

        List<AbstractPO> result = new ArrayList<AbstractPO>();

        for( IdObject idObject : objects ){
            AbstractIdObject abstractIdObject = ( AbstractIdObject )idObject;
            result.add( createPO( abstractIdObject ) );
        }
        return result;

    }

    private AbstractPO createPO( AbstractIdObject abstractIdObject ) throws Exception{
        Map<IdObjectPropertyMD, Object> propertyValues = abstractIdObject.getPropertyValues();
        PersistenceObjectMD persistenceObjectMD = persistenceMD.getPersistenceObjectMD( abstractIdObject.getClass() );

        AbstractPO persistenceObject = ( AbstractPO )persistenceObjectMD.getPersistenceObjectClass().newInstance();

        IdObjectMD idObjectMD = persistenceMD.getModelMD().getIdObjectMDByIdObjectClass( abstractIdObject.getClass() );
        List<IdObjectPropertyMD> allProperties = idObjectMD.getProperties();

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

}
