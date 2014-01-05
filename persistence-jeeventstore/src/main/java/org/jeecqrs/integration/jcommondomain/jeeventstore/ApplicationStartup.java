package org.jeecqrs.integration.jcommondomain.jeeventstore;

import java.util.Iterator;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.jeecqrs.common.event.Event;
import org.jeecqrs.sagas.SagaTracker;
import org.jeecqrs.startup.AbstractApplicationStartup;
import org.jeeventstore.EventStoreCommitNotifier;

/**
 *
 */
public class ApplicationStartup extends AbstractApplicationStartup {

    private static final Logger log = Logger.getLogger(ApplicationStartup.class.getName());

    @EJB(name="sagaTracker")
    private SagaTracker<Event> sagaTracker;

    @EJB(name="eventReplayer")
    private EventReplayer replayer;

    @Inject
    private Instance<RegisterBucketId> autoRegisterBucketId;

    @EJB(name="eventDispatcher")
    private EventDispatcher eventDispatcher;

    @EJB(name="eventStoreCommitNotifier")
    private EventStoreCommitNotifier commitNotifier;

    @Resource(name="sagaTimeoutEventBucketId")
    private String sagaTimeoutEventBucketId = DefaultBucketIds.SAGA_TIMEOUT_EVENTS;

    protected void wireUpEventListeners() {
        // nothing to do
    }

    @Override
    protected void wireUpCommandHandlers() {
        // nothing to do
    }

    @Override
    protected void wireUpDispatchScheduler() {
        log.info("Wire up dispatch scheduler");
        Iterator<RegisterBucketId> it = autoRegisterBucketId.iterator();
        while (it.hasNext()) {
            RegisterBucketId rbi = it.next();
            if (rbi.autoDispatch()) {
                log.info("Register dispatch for bucketId " + rbi.bucketId());
                commitNotifier.addListener(rbi.bucketId(), eventDispatcher);
            }
        }
        log.info("Register dispatch for saga timeout events in bucket " + sagaTimeoutEventBucketId);
        commitNotifier.addListener(sagaTimeoutEventBucketId, eventDispatcher);
    }

    @Override
    protected void wireUpSagaTracker() {
        // nothing to do
    }

    @Override
    protected void replayEvents() {
        log.info("Starting event replay.");
        Iterator<RegisterBucketId> it = autoRegisterBucketId.iterator();
        while (it.hasNext()) {
            RegisterBucketId rbi = it.next();
            if (rbi.autoReplay()) {
                log.info("Starting event replay for events in bucket " + rbi.bucketId());
                replayer.replay(rbi.bucketId());
            }
        }
        log.info("Starting event replay for saga timeout events in bucket " + sagaTimeoutEventBucketId);
        replayer.replay(sagaTimeoutEventBucketId);
        log.info("Event replay done");
    }

    @Override
    protected void startDispatchScheduler() {
        // nothing to do
    }

    @Override
    protected void startSagaTracker() {
        log.info("Start saga tracker");
        sagaTracker.startPublication();
    }

}
