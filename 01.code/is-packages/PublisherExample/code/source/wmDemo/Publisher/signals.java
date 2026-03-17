package wmDemo.Publisher;

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




	public static final void getCurrentSent (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getCurrentSent)>> ---
		// @sigtype java 3.5
		// [o] field:0:required currentSent
		// pipeline
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
		IDataUtil.put( pipelineCursor, "currentSent", ""+sent );
		pipelineCursor.destroy();
		// --- <<IS-END>> ---

                
	}



	public static final void incSent (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(incSent)>> ---
		// @sigtype java 3.5
		incSent();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	
	private static long sent=0;
	
	private static synchronized void incSent(){
		sent++;
	}
		
		
	// --- <<IS-END-SHARED>> ---
}

