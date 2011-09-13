package com.idobjects.api.md;

public class IdObjectReferenceMD{

    private final Class< ? extends IdObjectMD> source;
    private final Class< ? extends IdObjectMD> destination;
    private final String referenceName;
    private final String inverseReferenceType;
    private final boolean bidirectional;

    public IdObjectReferenceMD( Class< ? extends IdObjectMD> source, Class< ? extends IdObjectMD> destination, String referenceName, String inverseReferenceType,
            boolean bidirectional ){
        this.source = source;
        this.destination = destination;
        this.referenceName = referenceName;
        this.inverseReferenceType = inverseReferenceType;
        this.bidirectional = bidirectional;
    }

    public String getInverseReferenceType(){
        return inverseReferenceType;
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

    public String getReferenceName(){
        return referenceName;
    }

    @Override
    public String toString(){
        return "IdObjectReference Metadata [source=" + source + ", destination=" + destination + ", referenceTypeName=" + referenceName + "]";
    }

}
