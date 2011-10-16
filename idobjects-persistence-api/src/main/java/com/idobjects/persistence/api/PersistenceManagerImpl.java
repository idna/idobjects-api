package com.idobjects.persistence.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.idobjects.api.AbstractIdObject;
import com.idobjects.api.GuidObjectIdentifier;
import com.idobjects.api.IdObject;
import com.idobjects.api.IdObjectReference;
import com.idobjects.api.ModelScope;
import com.idobjects.api.ModelScopeIdentifier;
import com.idobjects.api.md.IdObjectMD;
import com.idobjects.api.md.IdObjectPropertyMD;
import com.idobjects.api.md.IdObjectReferenceMD;

public class PersistenceManagerImpl implements PersistenceManager{

    private final SessionFactory sessionFactory;

    private final PersistenceModelMD persistenceMD;

    private static final Logger logger = LoggerFactory.getLogger( PersistenceManagerImpl.class );

    public PersistenceManagerImpl( SessionFactory sessionFactory, PersistenceModelMD persistenceMD ){
        this.sessionFactory = sessionFactory;
        this.persistenceMD = persistenceMD;
    }

    public void saveModelScope( ModelScope modelScope ){
        Transaction tx = null;

        try{

            tx = getCurrentSession().beginTransaction();

            ModelScopeKeyPO modelScopeKeyPO = getModelSopeKeyPO( modelScope );
            ModelScopeVersionPO latestVersion = getLatestVersion( modelScopeKeyPO );
            ModelScopeVersionPO modelScopeVersion = getNewModelScopeVersion( modelScopeKeyPO, latestVersion );

            // insertNewVersion( modelScopeVersion, latestVersion );

            saveNewVersion( modelScopeVersion, modelScope );

            tx.commit();

        }
        catch( Exception e ){
            logger.error( "exception", e );
            if( tx != null ) tx.rollback();
            throw new RuntimeException( e );
        }
        finally{
            if( getCurrentSession() != null ) getCurrentSession().close();
        }

    }

    private void insertNewVersion( ModelScopeVersionPO versionPO, ModelScopeVersionPO latestVersion ){
        if( latestVersion == null ) return;
        for( PersistenceObjectMD poMD : persistenceMD.getObjects() ){
            Class clazz = poMD.getObjectVersionClass();
            String tableName = getTableName( clazz );

            String objectFKColumn = getColumnName( clazz, poMD.getEntityField() );
            String versionFKColumn = getColumnName( clazz, poMD.getModelVersionField() );

            String sql = "insert into " + tableName;
            sql += " (" + objectFKColumn + ", " + versionFKColumn + ")";
            sql += " select " + versionPO.getId() + ", " + objectFKColumn + " from " + tableName;
            sql += " where " + versionFKColumn + " = " + latestVersion.getId();
            logger.info( "SQL: " + sql );

            SQLQuery sqlQuery = getCurrentSession().createSQLQuery( sql );
            int updated = sqlQuery.executeUpdate();
            logger.info( "updated: " + updated );
        }

    }

    private String getColumnName( Class clazz, String propertyName ){
        AbstractEntityPersister classMetadata = ( AbstractEntityPersister )sessionFactory.getClassMetadata( clazz );
        String[] propertyColumnNames = classMetadata.getPropertyColumnNames( propertyName );
        return propertyColumnNames[ 0 ];
    }

    private String getTableName( Class clazz ){
        return ( ( AbstractEntityPersister )sessionFactory.getClassMetadata( clazz ) ).getTableName();
    }

    private ModelScopeVersionPO getNewModelScopeVersion( ModelScopeKeyPO modelScopeKeyPO, ModelScopeVersionPO latestVersion ) throws InstantiationException, IllegalAccessException{
        int nextVersionNumber = latestVersion != null ? latestVersion.getVersion() + 1 : 1;

        logger.info( "Inserting new version {}", nextVersionNumber );
        ModelScopeVersionPO newVersionEntry = ( ModelScopeVersionPO )persistenceMD.getModelScopeVersionClass().newInstance();
        newVersionEntry.setModelScopeKey( modelScopeKeyPO );
        newVersionEntry.setVersion( nextVersionNumber );
        getCurrentSession().save( newVersionEntry );

        return newVersionEntry;
    }

    private ModelScopeVersionPO getLatestVersion( ModelScopeKeyPO modelScopeKeyPO ){
        Criteria criteria = getCurrentSession().createCriteria( persistenceMD.getModelScopeVersionClass() );
        criteria.add( Restrictions.eq( persistenceMD.getKeyPropertyName(), modelScopeKeyPO ) );
        criteria.addOrder( Order.desc( "version" ) );
        criteria.setMaxResults( 1 );

        ModelScopeVersionPO latestVersion = ( ModelScopeVersionPO )criteria.uniqueResult();
        return latestVersion;
    }

    private ModelScopeKeyPO getModelSopeKeyPO( ModelScope modelScope ) throws InstantiationException, IllegalAccessException{
        ModelScopeKeyPO modelScopeKey = readeModelScopeKey( modelScope.getId() );
        if( modelScopeKey != null ){ return modelScopeKey; }

        logger.info( "No modelScopeKey entry found for {} in {} ... inserting new", modelScope.getId().toString(), persistenceMD.getModelScopeKeyClass().getSimpleName() );

        ModelScopeKeyPO newModelScopeKey = ( ModelScopeKeyPO )persistenceMD.getModelScopeKeyClass().newInstance();
        newModelScopeKey.setModelScopeKey( modelScope.getId().toString() );
        getCurrentSession().save( newModelScopeKey );

        return newModelScopeKey;
    }

    private ModelScopeKeyPO readeModelScopeKey( ModelScopeIdentifier modelScopeIdentifier ){
        Criteria criteria = getCurrentSession().createCriteria( persistenceMD.getModelScopeKeyClass() );
        criteria.add( Restrictions.eq( "modelScopeKey", modelScopeIdentifier.toString() ) );
        ModelScopeKeyPO modelScopeKey = ( ModelScopeKeyPO )criteria.uniqueResult();
        return modelScopeKey;
    }

    private List<ConvertedIdObject> saveNewVersion( ModelScopeVersionPO modelScopeVersion, ModelScope modelScope ) throws Exception{
        List<IdObject> objects = modelScope.getObjects();

        List<ConvertedIdObject> result = new ArrayList<ConvertedIdObject>();

        for( IdObject idObject : objects ){
            AbstractIdObject abstractIdObject = ( AbstractIdObject )idObject;
            PersistenceObjectMD persistenceObjectMD = persistenceMD.getPersistenceObjectMD( abstractIdObject.getClass() );

            ConvertedIdObject convertResult = new ConvertedIdObject();

            convertResult.persistenceObject = createPO( abstractIdObject, persistenceObjectMD );
            getCurrentSession().save( convertResult.persistenceObject );

            ObjectVersionPO objectVersionPO = ( ObjectVersionPO )persistenceObjectMD.getObjectVersionClass().newInstance();
            objectVersionPO.setPersistenceObject( convertResult.persistenceObject );
            objectVersionPO.setVersion( modelScopeVersion );
            getCurrentSession().save( objectVersionPO );

            convertResult.references = createReferencePOs( abstractIdObject, persistenceObjectMD );
            for( ReferencePO referencePO : convertResult.references ){
                getCurrentSession().save( referencePO );

                ReferenceVersionPO referenceVersionPO = ( ReferenceVersionPO )persistenceObjectMD.getReferenceVersionClass().newInstance();
                referenceVersionPO.setReference( referencePO );
                referenceVersionPO.setVersion( modelScopeVersion );

                getCurrentSession().save( referenceVersionPO );
            }

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

    }

    private List<String> getAllVersionTables(){
        List<String> result = new ArrayList<String>();
        for( PersistenceObjectMD poMD : persistenceMD.getObjects() ){
            Class clazz = poMD.getObjectVersionClass();
            String tableName = ( ( AbstractEntityPersister )sessionFactory.getClassMetadata( clazz ) ).getTableName();
            result.add( tableName );
        }

        return result;
    }

    private Session getCurrentSession(){
        return sessionFactory.getCurrentSession();
    }

    private class ConvertedIdObject{

        AbstractPO persistenceObject;
        List<ReferencePO> references;
    }

    public ModelScope readLatestModelScope( ModelScopeIdentifier modelScopeIdentifier ){
        Transaction tx = null;
        try{

            tx = getCurrentSession().beginTransaction();

            ModelScope result = new ModelScope( modelScopeIdentifier );

            ModelScopeKeyPO modelScopeKey = readeModelScopeKey( modelScopeIdentifier );
            if( modelScopeKey == null ){
                logger.debug( "No modelScope found with id {} ... returning empty modelScope", modelScopeIdentifier );
                return result;
            }

            ModelScopeVersionPO latestVersion = getLatestVersion( modelScopeKey );

            for( PersistenceObjectMD poMD : persistenceMD.getObjects() ){
                createIdObject( result, latestVersion, poMD );

            }
            for( PersistenceObjectMD poMD : persistenceMD.getObjects() ){
                createReferences( result, latestVersion, poMD );
            }

            tx.commit();

            return result;
        }
        catch( Exception e ){
            logger.error( "exception", e );
            if( tx != null ) tx.rollback();
            throw new RuntimeException( e );
        }
        finally{
            if( getCurrentSession() != null ) getCurrentSession().close();
        }

    }

    private void createReferences( ModelScope result, ModelScopeVersionPO latestVersion, PersistenceObjectMD poMD ){
        Class referenceClass = poMD.getPersistenceReferencesClass();
        Class versionReferenceClass = poMD.getReferenceVersionClass();

        String queryStr = "select ref from " + getEntityName( versionReferenceClass ) + " as versionRef ";
        queryStr += "inner join " + "versionRef." + poMD.getEntityReferenceField() + " as ref ";
        queryStr += " where versionRef." + poMD.getModelVersionField() + " = :modelScopeVersion";
        logger.info( "query:" + queryStr );
        Query query = getCurrentSession().createQuery( queryStr );
        query.setEntity( "modelScopeVersion", latestVersion );

        List<ReferencePO> references = query.list();

        for( ReferencePO referencePO : references ){
            GuidObjectIdentifier sourceId = new GuidObjectIdentifier( referencePO.getSourceId() );
            GuidObjectIdentifier destinationId = new GuidObjectIdentifier( referencePO.getDestinationId() );
            IdObjectReferenceMD referenceMD = poMD.getIdObjectMD().getReferenceByName( referencePO.getReferenceName() );

            IdObject sourceObject = result.getObject( sourceId );
            sourceObject.addReference( referenceMD, destinationId );
        }
    }

    private void createIdObject( ModelScope result, ModelScopeVersionPO latestVersion, PersistenceObjectMD poMD ) throws InstantiationException, IllegalAccessException{
        Class clazz = poMD.getPersistenceObjectClass();
        Class versionClass = poMD.getObjectVersionClass();

        String queryStr = "select po from " + getEntityName( versionClass ) + " as versionPO ";
        queryStr += "inner join " + "versionPO." + poMD.getEntityField() + " as po ";
        queryStr += " where versionPO." + poMD.getModelVersionField() + " = :modelScopeVersion";
        logger.info( "query:" + queryStr );
        Query query = getCurrentSession().createQuery( queryStr );
        query.setEntity( "modelScopeVersion", latestVersion );
        List<AbstractPO> persistentObjects = query.list();

        for( AbstractPO po : persistentObjects ){
            AbstractIdObject idObject = convert( poMD, po, result );
        }
    }

    private AbstractIdObject convert( PersistenceObjectMD poMD, AbstractPO persistenceObject, ModelScope modelScope ) throws InstantiationException, IllegalAccessException{
        GuidObjectIdentifier objectId = new GuidObjectIdentifier( persistenceObject.getObjectId() );
        AbstractIdObject idObject = ( AbstractIdObject )poMD.getIdObjectMD().createNewIdObject( modelScope, objectId );
        Map<PersistencePropertyMD, Object> properties = persistenceObject.getPropertyValues();
        for( PersistencePropertyMD propertyMD : properties.keySet() ){
            Object value = properties.get( propertyMD );
            idObject.setPropertyValue( propertyMD.getIdObjectPropertyMD(), value );
        }

        return idObject;
    }

    private String getEntityName( Class clazz ){
        Entity entityAnnotation = ( Entity )clazz.getAnnotation( Entity.class );
        if( entityAnnotation.name() != null && !"".equals( entityAnnotation.name().trim() ) ) return entityAnnotation.name();
        return clazz.getSimpleName();
    }
}
