package com.idobjects.api;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class GuidObjectIdentifier implements ObjectIdentifier{

    private final String guid;

    public GuidObjectIdentifier(){
        guid = GuidGenerator.newRandomGUID();
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( guid == null ) ? 0 : guid.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ){
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( getClass() != obj.getClass() ) return false;
        GuidObjectIdentifier other = ( GuidObjectIdentifier )obj;
        if( guid == null ){
            if( other.guid != null ) return false;
        }
        else if( !guid.equals( other.guid ) ) return false;
        return true;
    }

    @Override
    public String toString(){
        return guid;
    }

    private static class GuidGenerator{

        private static final Random myRand;
        private static final String localHostString;

        static{
            SecureRandom mySecureRand = new SecureRandom();
            long secureInitializer = mySecureRand.nextLong();
            myRand = new Random( secureInitializer );
            try{
                localHostString = InetAddress.getLocalHost().toString();
            }
            catch( UnknownHostException e ){
                // Should never happen
                e.printStackTrace();
                throw new RuntimeException( "getLocalHost returned with UnknownHostException" );
            }
        }

        public static String newRandomGUID(){
            MessageDigest md5 = null;
            StringBuffer sbValueBeforeMD5 = new StringBuffer();

            try{
                md5 = MessageDigest.getInstance( "MD5" );
            }
            catch( NoSuchAlgorithmException e ){
                e.printStackTrace();
                throw new RuntimeException( e );
            }

            long time = System.currentTimeMillis();
            long rand = myRand.nextLong();

            sbValueBeforeMD5.append( localHostString );
            sbValueBeforeMD5.append( ":" );
            sbValueBeforeMD5.append( Long.toString( time ) );
            sbValueBeforeMD5.append( ":" );
            sbValueBeforeMD5.append( Long.toString( rand ) );

            String valueBeforeMD5 = sbValueBeforeMD5.toString();
            md5.update( valueBeforeMD5.getBytes() );

            byte[] array = md5.digest();
            StringBuffer sb = new StringBuffer();
            for( int j = 0; j < array.length; ++j ){
                int b = array[ j ] & 0xFF;
                if( b < 0x10 ) sb.append( '0' );
                sb.append( Integer.toHexString( b ) );
            }

            String valueAfterMD5 = sb.toString();
            return toGuidFormat( valueAfterMD5 );

        }

        private static String toGuidFormat( String raw ){
            raw = raw.toUpperCase();
            StringBuffer sb = new StringBuffer();
            sb.append( raw.substring( 0, 8 ) );
            sb.append( "-" );
            sb.append( raw.substring( 8, 12 ) );
            sb.append( "-" );
            sb.append( raw.substring( 12, 16 ) );
            sb.append( "-" );
            sb.append( raw.substring( 16, 20 ) );
            sb.append( "-" );
            sb.append( raw.substring( 20 ) );

            return sb.toString();
        }
    }
}
