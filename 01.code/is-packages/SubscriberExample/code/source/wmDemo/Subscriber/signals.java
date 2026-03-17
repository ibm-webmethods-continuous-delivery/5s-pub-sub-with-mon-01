package wmDemo.Subscriber;

// -----( IS Java Code Template v1.2

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
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



	public static final void getStalenessMetrics (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(getStalenessMetrics)>> ---
		// @sigtype java 3.5
		// [o] record:1:required stalenessMetrics
		// [o] - field:0:required consumingServiceNs
		// [o] - field:0:required currentAgeMillis
		// [o] - field:0:required lastPublishedAvgAge
		// [o] - field:0:required minAgeMillis
		// [o] - field:0:required maxAgeMillis
		// [o] - field:0:required messageCount
		ConcurrentHashMap<String, MessageStalenessMetrics> metricsMap = getStalenessMetrics();

		// output pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();

		// Create IData array to hold all consumer metrics
		IData[] consumerMetricsArray = new IData[metricsMap.size()];
		int index = 0;

		// Traverse the HashMap and populate each entry
		for (java.util.Map.Entry<String, MessageStalenessMetrics> entry : metricsMap.entrySet()) {
			String triggerName = entry.getKey();
			MessageStalenessMetrics metrics = entry.getValue();

			// Create IData for this consumer's metrics
			IData consumerMetrics = IDataFactory.create();
			IDataCursor consumerMetricsCursor = consumerMetrics.getCursor();

			// Populate all metric fields
			IDataUtil.put(consumerMetricsCursor, "$key", triggerName);
			IDataUtil.put(consumerMetricsCursor, "consumingServiceNs", triggerName);

			// UM Age metrics
			IDataUtil.put(consumerMetricsCursor, "currentUmAgeMillis", String.valueOf(metrics.getCurrentUmAgeMillis()));
			IDataUtil.put(consumerMetricsCursor, "lastPublishedAvgUmAge", String.valueOf(metrics.getLastUmPublishedAvgAge()));
			IDataUtil.put(consumerMetricsCursor, "minUmAgeMillis", String.valueOf(metrics.getMinUmAgeMillis()));
			IDataUtil.put(consumerMetricsCursor, "maxUmAgeMillis", String.valueOf(metrics.getMaxUmAgeMillis()));

			// Measured Age metrics
			IDataUtil.put(consumerMetricsCursor, "currentMeasuredAgeMillis", String.valueOf(metrics.getCurrentMeasuredAgeMillis()));
			IDataUtil.put(consumerMetricsCursor, "lastPublishedAvgMeasuredAge", String.valueOf(metrics.getLastMeasuredPublishedAvgAge()));
			IDataUtil.put(consumerMetricsCursor, "minMeasuredAgeMillis", String.valueOf(metrics.getMinMeasuredAgeMillis()));
			IDataUtil.put(consumerMetricsCursor, "maxMeasuredAgeMillis", String.valueOf(metrics.getMaxMeasuredAgeMillis()));

			// Common metrics
			IDataUtil.put(consumerMetricsCursor, "messageCount", String.valueOf(metrics.getMessageCount()));
			IDataUtil.put(consumerMetricsCursor, "timeSinceLastMessageMillis", String.valueOf(metrics.getTimeSinceLastMessageMillis()));
			consumerMetricsCursor.destroy();

			// Add to array
			consumerMetricsArray[index++] = consumerMetrics;
		}


		// Put the array in the pipeline
		IDataUtil.put(pipelineCursor, "stalenessMetrics", consumerMetricsArray);
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



	public static final void measureMessageStaleness (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(measureMessageStaleness)>> ---
		// @sigtype java 3.5
		// [i] object:0:optional age
		// [i] object:0:optional enqueueTime
		// [i] object:0:optional recvTime
		// [i] field:0:required triggerServiceNS
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			Object	age = IDataUtil.get( pipelineCursor, "age" );
			Object	enqueueTime = IDataUtil.get( pipelineCursor, "enqueueTime" );
			Object	recvTime = IDataUtil.get( pipelineCursor, "recvTime" );
			String	triggerServiceNS = IDataUtil.getString( pipelineCursor, "triggerServiceNS" );
		pipelineCursor.destroy();

		// Calculate message staleness and update metrics
		if (triggerServiceNS == null || triggerServiceNS.isEmpty())
			triggerServiceNS="undeclared";

		long umAgeMillis = 0;
		long measuredAgeMillis = 0;
		recvTime= new java.util.Date();
		if (enqueueTime instanceof java.util.Date && recvTime instanceof java.util.Date) {
			measuredAgeMillis = ((java.util.Date) recvTime).getTime() - ((java.util.Date) enqueueTime).getTime();
		}


		// Try to get age from different sources
		if (age != null && age instanceof Integer) {
			umAgeMillis = (long) age;
		} else if (enqueueTime != null && recvTime != null) {
			// Calculate age from timestamps if available
			umAgeMillis = measuredAgeMillis;
		}

		// Update metrics for this trigger
		updateMessageStaleness(triggerServiceNS, umAgeMillis, measuredAgeMillis);


		// pipeline
		// --- <<IS-END>> ---


	}

	// --- <<IS-START-SHARED>> ---
	private static long retries=0;

	private static synchronized void incRetries(){
		retries++;
	}

	// Message staleness tracking - thread-safe and high-performance
	private static final ConcurrentHashMap<String, MessageStalenessMetrics> stalenessMetrics =
		new ConcurrentHashMap<>();

	// Throttle updates to once every 5 seconds per trigger
	private static final long UPDATE_INTERVAL_MS = 5000;

	/**
	 * Thread-safe metrics holder for message staleness per trigger
	 */
	private static class MessageStalenessMetrics {
		private final AtomicLong messageCount = new AtomicLong(0);
		private final AtomicLong totalUmAgeMillis = new AtomicLong(0);
		private final AtomicLong totalMeasuredAgeMillis = new AtomicLong(0);
		private volatile long minUmAgeMillis = Long.MAX_VALUE;
		private volatile long maxUmAgeMillis = 0;
		private volatile long minMeasuredAgeMillis = Long.MAX_VALUE;
		private volatile long maxMeasuredAgeMillis = 0;
		private volatile long currentUmAgeMillis = 0;
		private volatile long currentMeasuredAgeMillis = 0;
		private volatile long lastUpdateTime = 0;
		private volatile long lastUmPublishedAvgAge = 0;
		private volatile long lastMeasuredPublishedAvgAge = 0;
		private volatile long lastMessageTimestamp = 0;

		// Temporary accumulators for batch updates
		private final AtomicLong tempMessageCount = new AtomicLong(0);
		private final AtomicLong tempUmTotalAge = new AtomicLong(0);
		private final AtomicLong tempMeasuredTotalAge = new AtomicLong(0);
		private volatile long tempUmMinAge = Long.MAX_VALUE;
		private volatile long tempUmMaxAge = 0;
		private volatile long tempMeasuredMinAge = Long.MAX_VALUE;
		private volatile long tempMeasuredMaxAge = 0;

		synchronized void recordMessage(long umAgeMillis, long measuredAgeMillis) {
			// Record timestamp of this message
			lastMessageTimestamp = System.currentTimeMillis();

			// Always update current age (lightweight)
			currentUmAgeMillis = umAgeMillis;

			// Accumulate in temporary counters
			tempMessageCount.incrementAndGet();
			tempUmTotalAge.addAndGet(umAgeMillis);

			// Update min/max in temp
			if (umAgeMillis < tempUmMinAge) {
				tempUmMinAge = umAgeMillis;
			}
			if (umAgeMillis > tempUmMaxAge) {
				tempUmMaxAge = umAgeMillis;
			}
			// Always update current age (lightweight)
			currentMeasuredAgeMillis = measuredAgeMillis;

			// Accumulate in temporary counters
			tempMeasuredTotalAge.addAndGet(measuredAgeMillis);

			// Update min/max in temp
			if (umAgeMillis < tempMeasuredMinAge) {
				tempMeasuredMinAge = measuredAgeMillis;
			}
			if (umAgeMillis > tempUmMaxAge) {
				tempMeasuredMaxAge = measuredAgeMillis;
			}
		}

		synchronized boolean shouldPublish(long currentTime) {
			return (currentTime - lastUpdateTime) >= UPDATE_INTERVAL_MS;
		}

		synchronized void publishMetrics(long currentTime) {
			// Move temp values to published values
			long count = tempMessageCount.getAndSet(0);
			long totalUm = tempUmTotalAge.getAndSet(0);
			long totalMeasured = tempMeasuredTotalAge.getAndSet(0);

			if (count > 0) {
				messageCount.addAndGet(count);
				totalUmAgeMillis.addAndGet(totalUm);

				// Update min/max
				if (tempUmMinAge < minUmAgeMillis) {
					minUmAgeMillis = tempUmMinAge;
				}
				if (tempUmMaxAge > maxUmAgeMillis) {
					maxUmAgeMillis = tempUmMaxAge;
				}

				// Calculate and store average
				lastUmPublishedAvgAge = totalUm / count;

				// Reset temp min/max
				tempMeasuredMinAge = Long.MAX_VALUE;
				tempMeasuredMaxAge = 0;
				totalMeasuredAgeMillis.addAndGet(totalMeasured);

				// Update min/max
				if (tempUmMinAge < minUmAgeMillis) {
					minMeasuredAgeMillis = tempMeasuredMinAge;
				}
				if (tempUmMaxAge > maxUmAgeMillis) {
					maxMeasuredAgeMillis = tempMeasuredMaxAge;
				}

				// Calculate and store average
				lastMeasuredPublishedAvgAge = totalMeasured / count;

				// Reset temp min/max
				tempMeasuredMinAge = Long.MAX_VALUE;
				tempMeasuredMaxAge = 0;
			}

			lastUpdateTime = currentTime;
		}

		// UM Age getters
		long getUmAverageAgeMillis() {
			long count = messageCount.get();
			if (count == 0) return 0;
			return totalUmAgeMillis.get() / count;
		}

		long getLastUmPublishedAvgAge() {
			return lastUmPublishedAvgAge;
		}

		long getCurrentUmAgeMillis() {
			return currentUmAgeMillis;
		}

		long getMinUmAgeMillis() {
			return minUmAgeMillis == Long.MAX_VALUE ? 0 : minUmAgeMillis;
		}

		long getMaxUmAgeMillis() {
			return maxUmAgeMillis;
		}

		// Measured Age getters
		long getMeasuredAverageAgeMillis() {
			long count = messageCount.get();
			if (count == 0) return 0;
			return totalMeasuredAgeMillis.get() / count;
		}

		long getLastMeasuredPublishedAvgAge() {
			return lastMeasuredPublishedAvgAge;
		}

		long getCurrentMeasuredAgeMillis() {
			return currentMeasuredAgeMillis;
		}

		long getMinMeasuredAgeMillis() {
			return minMeasuredAgeMillis == Long.MAX_VALUE ? 0 : minMeasuredAgeMillis;
		}

		long getMaxMeasuredAgeMillis() {
			return maxMeasuredAgeMillis;
		}

		long getMessageCount() {
			return messageCount.get();
		}

		long getTimeSinceLastMessageMillis() {
			if (lastMessageTimestamp == 0) {
				return -1; // No message received yet
			}
			return System.currentTimeMillis() - lastMessageTimestamp;
		}
	}

	/**
	 * Update message staleness metrics for a trigger
	 * Thread-safe and optimized for high throughput
	 */
	private static void updateMessageStaleness(String triggerServiceNS, long umAgeMillis, long measuredAgeMillis) {
		// Get or create metrics for this trigger
		MessageStalenessMetrics metrics = stalenessMetrics.computeIfAbsent(
			triggerServiceNS,
			k -> new MessageStalenessMetrics()
		);

		// Record the message (always fast, no blocking)
		metrics.recordMessage(umAgeMillis, measuredAgeMillis);

		// Check if we should publish (throttled to every 5 seconds)
		long currentTime = System.currentTimeMillis();
		if (metrics.shouldPublish(currentTime)) {
			metrics.publishMetrics(currentTime);
		}
	}

	/**
	 * Get all staleness metrics for Prometheus export
	 * Called by the Prometheus endpoint service
	 */
	public static ConcurrentHashMap<String, MessageStalenessMetrics> getStalenessMetrics() {
		return stalenessMetrics;
	}





	// --- <<IS-END-SHARED>> ---
}

