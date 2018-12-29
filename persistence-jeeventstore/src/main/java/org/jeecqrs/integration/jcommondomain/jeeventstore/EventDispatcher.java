package org.jeecqrs.integration.jcommondomain.jeeventstore;

import org.jeecqrs.common.event.Event;
import org.jeecqrs.common.event.EventBus;
import org.jeeventstore.ChangeSet;
import org.jeeventstore.EventStoreCommitListener;
import org.jeeventstore.EventStoreCommitNotification;

import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import java.io.Serializable;
import java.util.Iterator;

/**
 * Bridge between JEEventStore and EventBus.
 */
public class EventDispatcher implements EventStoreCommitListener {

    @EJB
    private EventBus eventBus;

    @Override
    @Lock(LockType.READ)
    public void receive(EventStoreCommitNotification notification) {
        ChangeSet changes = notification.changes();
        Iterator<Serializable> it = changes.events();
        while (it.hasNext())
            eventBus.dispatch((Event) it.next());
    }
    
}
