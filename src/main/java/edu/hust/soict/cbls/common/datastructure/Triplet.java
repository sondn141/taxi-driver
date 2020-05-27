package edu.hust.soict.cbls.common.datastructure;

public class Triplet<E, P, Q> {

    private E e;

    private P p;

    private Q q;

    public Triplet(E e, P p, Q q){
        this.e = e;
        this.p = p;
        this.q = q;
    }


    public E get1() {
        return e;
    }

    public P get2() {
        return p;
    }

    public Q get3() {
        return q;
    }
}
