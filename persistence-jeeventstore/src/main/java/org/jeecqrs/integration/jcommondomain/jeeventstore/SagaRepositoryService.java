package org.jeecqrs.integration.jcommondomain.jeeventstore;

import javax.annotation.Resource;
import javax.ejb.EJB;
import org.jeecqrs.common.commands.CommandBus;
import org.jeecqrs.common.event.Event;
import org.jeecqrs.common.persistence.jeeventstore.AbstractJEEventStoreSagaRepository;
import org.jeecqrs.common.sagas.SagaTimeoutProvider;
import org.jeecqrs.integration.jcommondomain.sagas.AbstractSaga;
import org.jeecqrs.sagas.Saga;
import org.jeecqrs.sagas.SagaRepository;
import org.jeeventstore.EventStore;

/**
 *
 */
public class SagaRepositoryService extends AbstractJEEventStoreSagaRepository<AbstractSaga<?>> 
    implements SagaRepository<Event> {

    @EJB(name="eventStore")
    private EventStore eventStore;

    @EJB(name="commandBus")
    private CommandBus commandBus;

    @EJB(name="sagaTimeoutProvider")
    private SagaTimeoutProvider timeoutProvider;

    @Resource(name="sagaBucketId")
    private String sagaBucketId = DefaultBucketIds.SAGAS;

    @Override
    protected EventStore eventStore() {
        return this.eventStore;
    }

    @Override
    protected String bucketId() {
        return sagaBucketId;
    }

    @Override
    public <T extends Saga<Event>> T sagaOfIdentity(Class<T> clazz, String sagaId) {
        if (!AbstractSaga.class.isAssignableFrom(clazz))
            throw new UnsupportedOperationException("Unsupported saga type: " + clazz);
        AbstractSaga<T> saga = (AbstractSaga<T>) super.ofIdentity((Class) clazz, sagaId);
        if (saga != null) {
            saga.sagaId(sagaId);
            injectDependencies(saga);
        }
        return (T) saga;
    }

    @Override
    public <T extends Saga<Event>> void add(T saga, String commitId) {
        if (!AbstractSaga.class.isAssignableFrom(saga.getClass()))
            throw new UnsupportedOperationException("Unsupported saga type: " + saga.getClass());
        SagaCommitId scid = new SagaCommitId(commitId);
        super.add((AbstractSaga) saga, scid);
    }

    @Override
    public <T extends Saga<Event>> void save(T saga, String commitId) {
        if (!AbstractSaga.class.isAssignableFrom(saga.getClass()))
            throw new UnsupportedOperationException("Unsupported saga type: " + saga.getClass());
        SagaCommitId scid = new SagaCommitId(commitId);
        super.save((AbstractSaga) saga, scid);
    }

    protected void injectDependencies(AbstractSaga<?> saga) {
        saga.setCommandBus(commandBus);
        saga.setTimeoutProvider(timeoutProvider);
    }

    @Override
    @Deprecated
    public AbstractSaga ofIdentity(String id) {
        throw new UnsupportedOperationException("Not supported, since desired class is needed."); 
    }

}
