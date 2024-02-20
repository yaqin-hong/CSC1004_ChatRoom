package org.example;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.RandomAccess;

public class Vector extends AbstractList<String> implements List<String>, RandomAccess, Cloneable, Serializable {
    public void main(String[] args) {
        Vector<String> v = new Vector<>(10);

    }
}
