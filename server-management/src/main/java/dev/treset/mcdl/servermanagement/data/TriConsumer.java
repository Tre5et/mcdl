package dev.treset.mcdl.servermanagement.data;

public interface TriConsumer<A,B,C> {
    void accept(A a, B b, C c);
}
