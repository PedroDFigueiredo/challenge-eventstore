package net.intelie.challenges;

import org.junit.Test;
import net.intelie.challenges.SynchronizedEventStore;

import static org.junit.Assert.assertEquals;

public class EventTest {
    @Test
    public void threadSafeTest() throws Exception {
        String type = "some_type";

        SynchronizedEventStore sync = new SynchronizedEventStore();
        Event eventa = new Event(type, 123L);
        Event eventb = new Event(type, 225L);
        Event eventc = new Event(type, 300L);
        Event eventd = new Event(type, 25L);

        Runnable operations = () -> {
            sync.insert(eventa);
            sync.insert(eventb);
            sync.insert(eventc);
            sync.insert(eventd);
        };

        /**
         *  Demonstration that the class SynchronizedEventStore returns a thread-safe collection
         *  creating two threads, passing a Runnable instance as a parameter
         */
        Thread thread1 = new Thread(operations);
        Thread thread2 = new Thread(operations);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        /**
         * Here we can check that each thread added 8 elements to the SynchronizedEventStore instance
         */
        assertEquals(sync.getSyncMap().get(type).size(), 8);
    }

    @Test
    public void queryTest() throws Exception {
        String some_type = "some_type";
        String other_type = "other_type";

        /**
         *  Demonstration that the class SynchronizedEventStore returns a an iterator corresponding to the actual query
         *  creating 6 diferents Events, 4 of 'some_type' and 2 of 'other_type', with diferents timestamp values
         */
        SynchronizedEventStore sync = new SynchronizedEventStore();
        sync.insert(new Event(some_type, 123L));
        sync.insert(new Event(some_type, 225L));
        sync.insert(new Event(some_type, 300L));
        sync.insert(new Event(some_type, 25L));
        sync.insert(new Event(other_type, 250L));
        sync.insert(new Event(other_type, 480L));

        SynchronizedEventIterator iterator = (SynchronizedEventIterator) sync.query(some_type, 123L, 2250L);
        int count = 0;
        while (iterator.moveNext()) {
            System.out.println(iterator.current());
            count ++;
        }

        assertEquals(count, 3);

    }
}