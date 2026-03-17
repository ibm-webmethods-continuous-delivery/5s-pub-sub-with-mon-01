package wmDemo.Mockup;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class signals

{
	// ---( internal utility methods )---

	final static signals _instance = new signals();

	static signals _newInstance() { return new signals(); }

	static signals _cast(Object o) { return (signals)o; }

	// ---( server methods )---




	public static final void getCurrentValues (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCurrentValues)>> ---
		// @sigtype java 3.5
		// [o] field:0:required managedExceptions
		// [o] field:0:required unexpectedExceptions
		// pipeline
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		IDataUtil.put( pipelineCursor, "managedExceptions", ""+managedExceptions );
		IDataUtil.put( pipelineCursor, "unexpectedExceptions", ""+unexpectedExceptions );
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void incManagedExceptions (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(incManagedExceptions)>> ---
		// @sigtype java 3.5
		incUnexpected();
		// --- <<IS-END>> ---

                
	}



	public static final void incUnexpectedExceptions (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(incUnexpectedExceptions)>> ---
		// @sigtype java 3.5
		incManaged();
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
			if (p<0  || p>1){
				unexpectedExceptions++;
				throw new ServiceException("Probability must be >0 and <1, actual value is " + p);
			}
			if( new java.util.Random().nextDouble() <= p ) {  
					unexpectedExceptions++;
				    throw new ServiceException("Random probability exception hit!");
				}
		}catch(NumberFormatException nfe){
			unexpectedExceptions++;
			throw new ServiceException("Given probability is not a valid number: " + probability );
		}
		
		// pipeline
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static long managedExceptions=0, unexpectedExceptions=0;
	
	private static synchronized void incManaged(){
		managedExceptions++;
	}
		
	private static synchronized void incUnexpected(){
		unexpectedExceptions++;
	}
		
	// --- <<IS-END-SHARED>> ---
}

