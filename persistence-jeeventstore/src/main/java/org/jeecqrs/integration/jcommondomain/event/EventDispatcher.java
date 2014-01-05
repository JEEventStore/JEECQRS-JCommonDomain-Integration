package org.jeecqrs.integration.jcommondomain.event;

import java.util.Iterator;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import org.jeecqrs.common.event.Event;
import org.jeecqrs.common.event.EventBus;
import org.jeeventstore.ChangeSet;
import org.jeeventstore.EventStoreCommitListener;
import org.jeeventstore.EventStoreCommitNotification;

/**
 * Bridge between JEEventStore and EventBus.
 */
public class EventDispatcher implements EventStoreCommitListener {

    @EJB(name="eventBus")
    private EventBus eventBus;

    @Override
    @Lock(LockType.READ)
    public void receive(EventStoreCommitNotification notification) {
        ChangeSet changes = notification.changes();
        Iterator<?> it = changes.events();
        while (it.hasNext())
            eventBus.dispatch((Event) it.next());
    }
    
}
