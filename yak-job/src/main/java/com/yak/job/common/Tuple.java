/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 */
package com.yak.job.common;

public class Tuple<T, V> {
    private T v1;
    private V v2;

    public Tuple(T v1, V v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public T getV1() {
        return this.v1;
    }

    public void setV1(T v1) {
        this.v1 = v1;
    }

    public V getV2() {
        return this.v2;
    }

    public void setV2(V v2) {
        this.v2 = v2;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Tuple)) {
            return false;
        }
        Tuple other = (Tuple) o;
        if (!other.canEqual(this)) {
            return false;
        }
        T this$v1 = this.getV1();
        T other$v1 = other.getV1();
        if (this$v1 == null ? other$v1 != null : !this$v1.equals(other$v1)) {
            return false;
        }
        V this$v2 = this.getV2();
        V other$v2 = other.getV2();
        return !(this$v2 == null ? other$v2 != null : !this$v2.equals(other$v2));
    }

    protected boolean canEqual(Object other) {
        return other instanceof Tuple;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        T $v1 = this.getV1();
        result = result * 59 + ($v1 == null ? 43 : $v1.hashCode());
        V $v2 = this.getV2();
        result = result * 59 + ($v2 == null ? 43 : $v2.hashCode());
        return result;
    }

    public String toString() {
        return "Tuple(v1=" + this.getV1() + ", v2=" + this.getV2() + ")";
    }
}

