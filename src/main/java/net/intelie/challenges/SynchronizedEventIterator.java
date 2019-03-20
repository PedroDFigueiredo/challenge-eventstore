package net.intelie.challenges;

import java.util.*;

public class SynchronizedEventIterator implements EventIterator {
    private long startTime;
    private long endTime;
    private Collection<Event> list;
    private Iterator<Event> iterator;
    private Event current;

    /**
     *  SynchronizedEventIterator receives a copy of a list contaning only the events of especific type
     */
    public SynchronizedEventIterator(Collection<Event> listEvents, long startTime, long endTime) {
        this.list = Collections.synchronizedCollection(listEvents);
        this.startTime = startTime;
        this.endTime = endTime;
        this.iterator = this.list.iterator();
    }

    /**
     * To move across the list, it checks if the events are in the range specified by the query
     *
     */
    @java.lang.Override
    public boolean moveNext() {
        while (this.iterator.hasNext()){
            Event event = this.iterator.next();
            if (event.timestamp() >= this.startTime && event.timestamp() <= this.endTime){
                this.current = event;
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    public Event current() {
        if (this.current == null){
            this.moveNext();
            return this.current;
        }
        return this.current;
    }

    @java.lang.Override
    public void remove() {
        this.iterator.remove();
    }

    @java.lang.Override
    public void close() { }

    public boolean hasNext(){
        return this.iterator.hasNext();
    }

}
