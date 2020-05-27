package edu.hust.soict.cbls.common.datastructure;

public class Pair<K, V> {

    private K k;

    private V v;

    public Pair(K k, V v){
        this.k = k;
        this.v = v;
    }

    public void set(K k, V v){
        this.k = k;
        this.v = v;
    }

    public K getK(){
        return k;
    }

    public V getV(){
        return v;
    }
}
