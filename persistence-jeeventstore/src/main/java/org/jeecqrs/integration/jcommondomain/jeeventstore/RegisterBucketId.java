package org.jeecqrs.integration.jcommondomain.jeeventstore;

/**
 * Provides bucketIds that need to be handled during application startup.
 */
public interface RegisterBucketId {

    String bucketId();
    boolean autoDispatch();
    boolean autoReplay();
    
}
