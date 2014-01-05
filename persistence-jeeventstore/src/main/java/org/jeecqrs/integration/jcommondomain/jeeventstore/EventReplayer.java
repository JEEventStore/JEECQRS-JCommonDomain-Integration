package org.jeecqrs.integration.jcommondomain.jeeventstore;

import java.io.Serializable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import org.jeecqrs.common.event.Event;
import org.jeecqrs.common.event.EventBus;
import org.jeeventstore.ChangeSet;
import org.jeeventstore.EventStorePersistence;

/**
 * Replays events from the event store persistence to the event bus.
 */
public class EventReplayer {

    private static final Logger log = Logger.getLogger(EventReplayer.class.getName());

    @EJB(name="eventStorePersistence")
    private EventStorePersistence persistence;

    @EJB(name="eventBus")
    private EventBus eventBus;

    public void replay(String bucketId) {
        log.log(Level.INFO, "Event Replayer replaying for bucket {0}", bucketId);
        Iterator<ChangeSet> it = persistence.allChanges(bucketId);
        while (it.hasNext()) {
            ChangeSet cs = it.next();
            Iterator<Serializable> eit = cs.events();
            while (eit.hasNext()) {
                Event e = (Event) eit.next();
                eventBus.dispatch(e);
            }
        }
    }
    
}
