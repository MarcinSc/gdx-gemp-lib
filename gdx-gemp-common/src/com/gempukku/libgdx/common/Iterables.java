package com.gempukku.libgdx.common;

import java.util.Iterator;

public class Iterables {
    public static <Input, Result> Iterable<Result> mapIterable(final Iterable<? extends Input> iterable, final Function<Input, Result> mapping) {
        return new Iterable<Result>() {
            @Override
            public Iterator<Result> iterator() {
                final Iterator<? extends Input> iterator = iterable.iterator();
                return new Iterator<Result>() {
                    @Override
                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    @Override
                    public Result next() {
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
