package net.intelie.challenges;

import java.util.*;

/**
 * To solve the challenge, I've used synchronizedMap and synchronizedCollection functions of the
 * Collections Framework (https://docs.oracle.com/javase/8/docs/technotes/guides/collections/overview.html)
 *
 * This framework provides strong support for the multi-threaded scenario through different
 * synchronization wrappers implemented within the Collections class.
 *
 * synchronized views are easily implemented using theses wrappers
 *
 */
public class SynchronizedEventStore implements EventStore {

    /**
     * I've created class that has a synchronizedMap with string as key,
     * theses keys are the diferents events types.
     * As a value o the synchronizedMap, I've used a synchronizedCollection that will store the Events itselves.
     */
    private Map<String, Collection<Event>> syncMap;

    public SynchronizedEventStore() {
        this.syncMap = Collections.synchronizedMap(new HashMap<String, Collection<Event>>());
    }

    /**
     * To insert an Event into the map, first it checks if the type has been created,
     * and instantiate a list to it
     *
     */
    @java.lang.Override
    public void insert(Event event) {
        if (!this.syncMap.containsKey(event.type())) {
            this.syncMap.put(event.type(), Collections.synchronizedCollection(new ArrayList<Event>()));
        }
        this.syncMap.get(event.type()).add(event);
    }

    @java.lang.Override
    public void removeAll(String type) {
        if (this.syncMap.get(type) != null) {
            this.syncMap.get(type).retainAll(new ArrayList<Event>());
        }
    }

    /**
     *  To query events, it creates an instance of the iterator 'SynchronizedEventIterator'
     *  that will loop over the list of events of the type informed
     */
    @java.lang.Override
    public EventIterator query(String type, long startTime, long endTime) {
        if (this.syncMap.get(type) != null) {
            return new SynchronizedEventIterator(this.syncMap.get(type), startTime, endTime);
        } else {
            return new SynchronizedEventIterator(new ArrayList<Event>(), startTime, endTime);
        }
    }

    public Map<String, Collection<Event>> getSyncMap() {
        return syncMap;
    }
}
