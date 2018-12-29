package org.jeecqrs.integration.jcommondomain.jeeventstore;

import org.jeecqrs.common.event.Event;
import org.jeecqrs.sagas.tracker.SagaTrackerEventSerializer;
import org.jeeventstore.EventSerializer;

import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class JEEventStoreDelegatingSagaTrackerEventSerializer implements SagaTrackerEventSerializer<Event> {

    @EJB
    private EventSerializer eventSerializer;

    @Override
    public String serialize(Event event) {
        List<Event> list = new ArrayList<>();
        list.add(event);
        return eventSerializer.serialize(list);
    }

    @Override
    public List<? extends Event> deserialize(String body) {
        return (List) eventSerializer.deserialize(body);
    }
    
}
