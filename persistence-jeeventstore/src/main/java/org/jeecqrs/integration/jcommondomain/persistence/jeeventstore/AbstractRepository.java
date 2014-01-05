package org.jeecqrs.integration.jcommondomain.persistence.jeeventstore;

import javax.ejb.EJB;
import org.jeecqrs.common.Identity;
import org.jeecqrs.common.domain.model.AbstractEventSourcedAggregateRoot;
import org.jeecqrs.common.persistence.jeeventstore.AbstractJEEventStoreARRepository;
import org.jeeventstore.EventStore;

/**
 *
 * @param <T>  the AR type
 * @param <ID>  the type used to identify the ARs
 */
public abstract class AbstractRepository<T extends AbstractEventSourcedAggregateRoot<T, ID>, ID extends Identity>
        extends AbstractJEEventStoreARRepository<T, ID> {

    @EJB
    private EventStore eventStore;

    protected abstract BucketIdProvider bucketIdProvider();

    @Override
    protected String bucketId() {
        return bucketIdProvider().domainModelBucketId();
    }

    @Override
    protected EventStore eventStore() {
        return eventStore;
    }

}
