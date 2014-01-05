package org.jeecqrs.integration.jcommondomain.persistence.jeeventstore;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;

/**
 * BucketIdProvider that can be configured in the deployment descriptor.
 * 
 * Deploy as singleton bean.
 */
public class BucketIdProviderService implements BucketIdProvider {

    private static final Logger log = Logger.getLogger(BucketIdProviderService.class.getName());

    @Resource(name="domainModelBucketId")
    private String domainModelBucketId = "DEFAULT_DOMAIN";

    @Resource(name="domainModelBucketId")
    private String sagaBucketId = "DEFAULT_SAGAS";

    @Resource(name="domainModelBucketId")
    private String eventBucketId = "DEFAULT_EVENTS";

    @PostConstruct
    public void init() {
        log.log(Level.INFO, "Using bucketIds domainModel={0}, sagas={1}, events={2}",
                new Object[] { domainModelBucketId, sagaBucketId, eventBucketId });
    }

    @Override
    @Lock(LockType.READ)
    public String domainModelBucketId() {
        return this.domainModelBucketId;
    }

    @Override
    @Lock(LockType.READ)
    public String sagaBucketId() {
        return this.sagaBucketId;
    }

    @Override
    @Lock(LockType.READ)
    public String eventBucketId() {
        return this.eventBucketId;
    }

}
