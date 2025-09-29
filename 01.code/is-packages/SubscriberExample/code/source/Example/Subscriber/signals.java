package Example.Subscriber;

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




	public static final void getCurrentRetries (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCurrentRetries)>> ---
		// @sigtype java 3.5
		// [o] field:0:required currentRetries
		// pipeline
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		IDataUtil.put( pipelineCursor, "currentRetries", ""+retries );
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void incRetries (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(incRetries)>> ---
		// @sigtype java 3.5
		incRetries();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static long retries=0;
	
	private static synchronized void incRetries(){
		retries++;
	}
		
	// --- <<IS-END-SHARED>> ---
}

