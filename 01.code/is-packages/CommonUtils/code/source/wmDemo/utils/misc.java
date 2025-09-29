package wmDemo.utils;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;
import java.util.Map;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
// --- <<IS-END-IMPORTS>> ---

public final class misc

{
	// ---( internal utility methods )---

	final static misc _instance = new misc();

	static misc _newInstance() { return new misc(); }

	static misc _cast(Object o) { return (misc)o; }

	// ---( server methods )---




	public static final void getCurrentHostName (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCurrentHostName)>> ---
		// @sigtype java 3.5
		// [o] field:0:required currentHostName
		IDataCursor pipelineCursor = pipeline.getCursor();
		IDataUtil.put( pipelineCursor, "currentHostName", crtHostName );
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void probabilityException (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(probabilityException)>> ---
		// @sigtype java 3.5
		// [i] field:0:required probability
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	probability = IDataUtil.getString( pipelineCursor, "probability" );
		pipelineCursor.destroy();
		
		if( null == probability || "".equals(probability))
			throw new ServiceException("Null or empty probability number!");
		
		try{
			Double p = Double.parseDouble(probability);
			if (p<0  || p>1)
				throw new ServiceException("Probability must be >0 and <1, actual value is " + p);
			if( new java.util.Random().nextDouble() <= p ) {  
				   throw new ServiceException("Random probability exception hit!");
				}
		}catch(NumberFormatException nfe){
			throw new ServiceException("Given probability is not a valid number: " + probability );
		}
		
		// pipeline
			
		// --- <<IS-END>> ---

                
	}



	public static final void sleep (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(sleep)>> ---
		// @sigtype java 3.5
		// [i] field:0:required sleepMillis
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	sleepMillis = IDataUtil.getString( pipelineCursor, "sleepMillis" );
		pipelineCursor.destroy();
		
		if ( null != sleepMillis && (! "".equals(sleepMillis)))
			try {
				Long l=Long.parseLong(sleepMillis);
				Thread.sleep(l);
			} catch (InterruptedException e) {
				throw new ServiceException("Sleeping beauty awakened!");
			} catch (NumberFormatException nfe){
				throw new ServiceException("Cannot sleep for a non number value: " + sleepMillis);
			}
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	static private String getCrtHostName(){
		String s = null;
		try {
			s = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	static final private String crtHostName=getCrtHostName();
		
	// --- <<IS-END-SHARED>> ---
}

