package com.gempukku.libgdx.common;

import java.util.Iterator;

public class Iterables {
    public static <T, R> Iterable<R> mapIterable(final Iterable<? extends T> iterable, final Function<T, R> mapping) {
        return new Iterable<R>() {
            @Override
            public Iterator<R> iterator() {
                final Iterator<? extends T> iterator = iterable.iterator();
                return new Iterator<R>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public R next() {
                        return mapping.evaluate(iterator.next());
                    }

                    @Override
                    public void remove() {
                        iterator.remove();
                    }
                };
            }
        };
    }
}
