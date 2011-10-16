package com.idobjects.api.md;

public class IdObjectReferenceMD{

    private final Class< ? extends IdObjectMD> source;
    private final Class< ? extends IdObjectMD> destination;
    private final String name;
    private final String inverseReferenceName;
    private final boolean bidirectional;
    private final ReferenceType referenceType;
    private final Class modelMetadataClass;

    public IdObjectReferenceMD( Class< ? extends IdObjectMD> source, Class< ? extends IdObjectMD> destination, String name, String inverseReferenceName, boolean bidirectional,
            ReferenceType referenceType, Class modelMetadataClass ){

        if( bidirectional && inverseReferenceName == null ) throw new IllegalArgumentException( "is bidirectional, but inverseReferenceName is null" );

        this.source = source;
        this.destination = destination;
        this.name = name;
        this.inverseReferenceName = inverseReferenceName;
        this.bidirectional = bidirectional;
        this.referenceType = referenceType;
        this.modelMetadataClass = modelMetadataClass;

    }

    public ReferenceType getReferenceType(){
        return referenceType;
    }

    public String getInverseReferenceName(){
        return inverseReferenceName;
    }

    public boolean isBidirectional(){
        return bidirectional;
    }

    public IdObjectMD getSource(){
        return ReflectionUtil.getIdObjectMD( source );
    }

    public IdObjectMD getDestination(){
        return ReflectionUtil.getIdObjectMD( destination );
    }

    public String getName(){
        return name;
    }

    public IdObjectReferenceMD getInverseReferenceMD(){
        if( !bidirectional ) return null;
        return getDestination().getReferenceByName( inverseReferenceName );
    }

}
