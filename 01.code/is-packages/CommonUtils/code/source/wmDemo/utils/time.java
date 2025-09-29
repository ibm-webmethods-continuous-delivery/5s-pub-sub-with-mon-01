package wmDemo.utils;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
// --- <<IS-END-IMPORTS>> ---

public final class time

{
	// ---( internal utility methods )---

	final static time _instance = new time();

	static time _newInstance() { return new time(); }

	static time _cast(Object o) { return (time)o; }

	// ---( server methods )---




	public static final void getCurrentTimeMillis (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCurrentTimeMillis)>> ---
		// @sigtype java 3.5
		// [o] field:0:required currentTimeMillis
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		IDataUtil.put( pipelineCursor, "currentTimeMillis", "" + System.currentTimeMillis() );
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static long retries=0;
	
	private static synchronized void incRetries(){
		retries++;
	}
		
		
	// --- <<IS-END-SHARED>> ---
}

