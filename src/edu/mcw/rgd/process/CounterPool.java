package edu.mcw.rgd.process;

import java.util.Enumeration;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author mtutaj
 * @since 4/11/2019
 * thread safe implementation of pool of counters
 */
public class CounterPool {

    private ConcurrentHashMap<String, AtomicInteger> _map = new ConcurrentHashMap<>();

    /// increment counter 'counterName' by 1
    public int increment(String counterName) {
        return add(counterName, 1);
    }

    /**
     * add 'delta' to counter 'counterName'
     * @param counterName counter name
     * @param delta value to be added to given counter
     * @return new value of given counter
     */
    public int add(String counterName, int delta) {
        return _map.computeIfAbsent(counterName, v -> new AtomicInteger(0)).addAndGet(delta);
    }

    /**
     * get the value of given counter
     * @param counterName counter name
     * @return value of given counter
     */
    public int get(String counterName) {
        return _map.computeIfAbsent(counterName, v -> new AtomicInteger(0)).intValue();
    }

    /// dump all counters in random order
    public String dump() {
        StringBuilder buf = new StringBuilder("========\n");
        for( Map.Entry<String, AtomicInteger> entry: _map.entrySet() ) {
            buf.append(entry.getKey())
                    .append(" : ")
                    .append(entry.getValue())
                    .append("\n");
        }
        buf.append("=========\n");
        return buf.toString();
    }

    /// dump all counters in alphabetic order; counts are formatted with thousand separator
    public String dumpAlphabetically() {

        TreeSet<String> sortedCounterNames = new TreeSet<>(_map.keySet());

        StringBuilder buf = new StringBuilder("========\n");
        for( String counterName: sortedCounterNames ) {
            buf.append(counterName)
                    .append(" : ")
                    .append(Utils.formatThousands(_map.get(counterName)))
                    .append("\n");
        }
        buf.append("=========\n");
        return buf.toString();
    }

    /// add values from another pool of counters
    public void merge(CounterPool p) {
        for( Map.Entry<String,AtomicInteger> entry: p._map.entrySet() ) {
           add(entry.getKey(), entry.getValue().intValue());
        }
    }

    public Enumeration<String> getCounterNames() {
        return _map.keys();
    }
}
