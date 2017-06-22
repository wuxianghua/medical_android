package com.palmap.library.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by aoc on 2016/5/12.
 */
public class SynchronizedLinkedQueue<T> implements Queue<T> {

    private final LinkedList<T> list = new LinkedList<T>();
    private final int maxSize;

    public SynchronizedLinkedQueue(int size) {
        this.maxSize = size;
    }

    @Override
    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public synchronized boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public synchronized Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public synchronized int size() {
        return list.size();
    }

    @Override
    public synchronized boolean add(T e) {
        if (checkFull()) list.remove(0);
        return list.add(e);
    }

    private synchronized boolean checkFull() {
        return size() >= maxSize;
    }


    @Override
    public synchronized boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public synchronized boolean containsAll(Collection<?> c) {
        return list.containsAll(c);
    }

    @Override
    public synchronized boolean addAll(Collection<? extends T> c) {
        for (T t : c) {
            this.add(t);
        }
        return true;
    }

    @Override
    public synchronized boolean removeAll(Collection<?> c) {
        return list.removeAll(c);
    }

    @Override
    public synchronized boolean retainAll(Collection<?> c) {
        return list.retainAll(c);
    }

    @Override
    public synchronized void clear() {
        list.clear();
    }

    @Override
    public synchronized String toString() {
        return list.toString();
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        SynchronizedLinkedQueue<?> other = (SynchronizedLinkedQueue<?>) obj;
        return list.equals(other.list);
    }

    @Override
    public synchronized T peek() {
        return list.peek();
    }

    @Override
    public synchronized T element() {
        return list.element();
    }

    @Override
    public synchronized T poll() {
        return list.poll();
    }

    @Override
    public synchronized T remove() {
        return list.remove();
    }

    @Override
    public synchronized boolean offer(T e) {
        return list.offer(e);
    }

    @Override
    public synchronized Object clone() {
        SynchronizedLinkedQueue<T> q = new SynchronizedLinkedQueue<T>(maxSize);
        q.addAll(list);
        return q;
    }

    @Override
    public synchronized Object[] toArray() {
        return list.toArray();
    }

    @Override
    public synchronized <R> R[] toArray(R[] a) {
        return list.toArray(a);
    }

    public int getMaxSize() {
        return maxSize;
    }
}
