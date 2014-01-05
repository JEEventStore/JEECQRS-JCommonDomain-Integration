package org.jeecqrs.integration.jcommondomain.persistence.jeeventstore;

/**
 *
 */
public interface BucketIdProvider {

    /**
     * Provides the bucketId for the domain model (aggregate roots).
     * 
     * @return domain model bucket id
     */
    String domainModelBucketId();

    /**
     * Provides the bucketId for the sagas that use event sourcing.
     * 
     * @return saga bucket id
     */
    String sagaBucketId();

    /**
     * Provides the bucketId for all other events that need to be persisted.
     * Provides a persistent event bus.
     * 
     * @return event bucket id
     */
    String eventBucketId();
    
}
