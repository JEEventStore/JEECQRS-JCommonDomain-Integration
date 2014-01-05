package org.jeecqrs.bundle.jcommondomain.sagas;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.jeecqrs.common.event.Event;
import org.jeecqrs.sagas.Saga;
import org.jeecqrs.sagas.SagaIdentificationStrategy;

/**
 * A saga identification strategy that implements the {@link Map} interface
 * and requires {@link SagaIdentifier}s to be registered for each event.
 * 
 * @param <S>  the saga type
 */
public class MapSagaIdentificationStrategy<S extends Saga<Event>> implements
        SagaIdentificationStrategy<S, Event>,
        Map<Class<? extends Event>, SagaIdentifier<? extends Event>> {

    private final Map<Class<? extends Event>, SagaIdentifier<? extends Event>> delegate = new HashMap<>();
    
    @Override
    public String identifySaga(Event event) {
        Class<? extends Event> evClass = event.getClass();
        SagaIdentifier<Event> idf = (SagaIdentifier<Event>) this.get(evClass);
        if (idf == null)
            throw new IllegalStateException("No identifier registered for event type " + event);
        return idf.sagaIdFor(event);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return delegate.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return delegate.containsValue(value);
    }

    @Override
    public SagaIdentifier<? extends Event> get(Object key) {
        return delegate.get(key);
    }

    @Override
    public SagaIdentifier<? extends Event> put(Class<? extends Event> key,
            SagaIdentifier<? extends Event> value) {
        return delegate.put(key, value);
    }

    @Override
    public SagaIdentifier<? extends Event> remove(Object key) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void putAll(Map<? extends Class<? extends Event>, ? extends SagaIdentifier<? extends Event>> m) {
        delegate.putAll(m);
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Set<Class<? extends Event>> keySet() {
        return delegate.keySet();
    }

    @Override
    public Collection<SagaIdentifier<? extends Event>> values() {
        return delegate.values();
    }

    @Override
    public Set<Entry<Class<? extends Event>, SagaIdentifier<? extends Event>>> entrySet() {
        return delegate.entrySet();
    }

}
