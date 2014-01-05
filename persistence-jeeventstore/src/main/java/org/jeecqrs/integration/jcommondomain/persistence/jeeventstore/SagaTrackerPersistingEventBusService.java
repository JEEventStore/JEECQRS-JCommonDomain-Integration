package org.jeecqrs.integration.jcommondomain.persistence.jeeventstore;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.jeecqrs.common.event.Event;
import org.jeecqrs.event.EventBus;
import org.jeecqrs.integration.jcommondomain.event.EventDispatcher;
import org.jeeventstore.ConcurrencyException;
import org.jeeventstore.DuplicateCommitException;
import org.jeeventstore.EventStore;
import org.jeeventstore.WritableEventStream;

/**
 * Provides a persistent event bus for the saga tracker that guarantees that
 * events are not lost.
 * We use it mainly as a data store for events that are published by the data tracker,
 * since we need to guarantee that no event is getting lost.
 * The events saved here are never queried for reading, except on the event replay
 * on application startup.  They are, however, automatically published
 * to the real event bus once they have successfully been committed to the
 * JEEventstore persistence (via {@link EventDispatcher}.
 */
public class SagaTrackerPersistingEventBusService implements EventBus<Event> {

    private static final Logger log = Logger.getLogger(SagaTrackerPersistingEventBusService.class.getName());

    @EJB(name="eventStore")
    private EventStore eventStore;

    @EJB(name="bucketIdProvider")
    private BucketIdProvider bucketIdProvider;

    @Override
    public void dispatch(Event event) {
        log.fine("Dispatch event #" + event.id() + ", " + event);
        String eventId = event.id().toString();
        String streamId = String.format("%s:%s", event.getClass().getCanonicalName(), eventId);
        String bucketId = bucketIdProvider.eventBucketId();
        WritableEventStream stream = eventStore.createStream(bucketId, streamId);
        stream.append(event);
        try {
            stream.commit(streamId);
        } catch (ConcurrencyException | DuplicateCommitException e) {
            // this simply means the event has been stored already, this is good,
            // we can ignore these errors
            log.log(Level.FINE, "Caught exception saving stream with id {0}, but ignored on purpose: {1}",
                    new Object[]{streamId, e});
        }
    }
    
}
