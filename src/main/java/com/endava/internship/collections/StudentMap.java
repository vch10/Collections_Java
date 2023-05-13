package com.endava.internship.collections;


import java.util.*;


public class StudentMap implements Map<Object, Student> {
    private int capacity;
    private int size = 0;
    private Node<Object, Student>[] map;

    public StudentMap() {
        this.capacity = 8;
        this.map = new Node[this.capacity];
    }

    public StudentMap(int capacity) {
        this.capacity = capacity;
        this.map = new Node[capacity];
    }

    public int size (){
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = index(key);
        Node<Object,Student> entry = map[index];
        while(entry != null){
            if(entry.getKey().equals(key)){
                return true;
            }
            entry = entry.getNext();
        }
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        for(int i=0; i < capacity; i++){
            if(map[i] != null){
                Node<Object,Student> currentNode = map[i];
                while(currentNode != null){
                    if(currentNode.getValue().equals(value)){
                        return true;
                    }
                    currentNode = currentNode.getNext();
                }
            }
        }
        return false;
    }

    @Override
    public Student get(Object key) {
        int index = index(key);
        Student value = null;
        Node<Object, Student> entry = map[index];

        while (entry != null){
            if(entry.getKey().equals(key)){
                value = entry.getValue();
                break;
            }
            entry = entry.getNext();
        }
        return value;
    }

    @Override
    public Student put(Object key, Student value) {
        if((capacity*0.75f) <= size){
             resize();
        }
        int index = index(key);
        Node newEntry = new Node(key,value, null);
        if (map[index] == null){
            map[index] = newEntry;
            this.size++;
            return value;
        }else {
            Node<Object, Student> previousNode = null;
            Node<Object, Student> currentNode = map[index];
            while(currentNode != null){
                if(currentNode.getKey().equals(key)){
                    currentNode.setValue(value);
                    this.size++;
                    return value;
                }
                previousNode = currentNode;
                currentNode = currentNode.getNext();
            }

            if(previousNode != null){
                previousNode.setNext(newEntry);
                size++;
                return value;
            }

        }

        return null;
    }


    @Override
    public Student remove(Object key) {
        int index = index(key);
        Node<Object,Student> previousNode = null;
        Node<Object,Student> entry = map[index];

        while(entry != null) {
            if (entry.getKey().equals(key)) {
                if (previousNode == null) {
                    map[index] = entry.getNext();
                    this.size--;
                    return entry.getValue();
                }else{
                    previousNode.setNext(entry.getNext());
                    this.size--;
                    return entry.getValue();
                }
            }
            previousNode = entry;
            entry = entry.getNext();
        }
        return null;
    }

    @Override
    public void clear() {
        Node<Object,Student>[] tab;
        if((tab=map)!=null && size > 0){
            for( int i = 0; i < capacity; i++){
                if(tab[i] != null){
                    tab[i] = null;
                }
            }
            size = 0;
        }
    }

    @Override
    public void putAll(Map<? extends Object, ? extends Student> m) {
        for(Map.Entry<? extends Object, ? extends Student> e : m.entrySet()){
            if(e != null){
                put(e.getKey(),e.getValue());
            }
        }
    }

    @Override
    public Set<Object> keySet() {
        Set<Object> keySet = new HashSet<>();
        for(int i = 0; i < capacity; i++){
            if(map[i] != null){
                Node<Object,Student> entry = map[i];
                while(entry != null){
                    keySet.add(entry.getValue());
                    entry = entry.getNext();
                }
            }
        }

        return keySet;
    }

    @Override
    public Collection<Student> values() {
        Collection<Student> values = new AbstractCollection<Student>(){
            public Iterator<Student> iterator() {
                return new Iterator<Student>() {
                    private Iterator<Entry<Object,Student>> i = new HashIterator();

                    public boolean hasNext() {
                        return i.hasNext();
                    }

                    public Student next() {
                        return i.next().getValue();
                    }
                };
            }

            public int size() {
                return StudentMap.this.size();
            }

            public boolean isEmpty() {
                return StudentMap.this.isEmpty();
            }

            public void clear() {
                StudentMap.this.clear();
            }

            public boolean contains(Object v) {
                return StudentMap.this.containsValue(v);
            }
        };
        return values;
    }


    @Override
    public Set<Entry<Object, Student>> entrySet() {
        Set<Entry<Object, Student>> entrySet = new AbstractSet<Entry<Object, Student>>() {
            @Override
            public Iterator<Entry<Object, Student>> iterator() {
                return new HashIterator();
            }

            @Override
            public int size() {
                return StudentMap.this.size();
            }
        };

        return entrySet;
    }


    private class HashIterator implements Iterator<Entry<Object, Student>> {
        int size = StudentMap.this.size();
        int capacity = StudentMap.this.capacity;
        int currentPosition,expectedEntries;
        Node<Object, Student> entry, currentNode;

        public HashIterator() {
            this.currentPosition = 0;
            this.expectedEntries = 0;
            this.entry = null;
            this.currentNode = map[0];
        }

        @Override
        public boolean hasNext() {
            return expectedEntries < size;
        }

        @Override
        public Entry<Object, Student> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
                while (currentPosition < capacity) {
                    while (currentNode != null) {
                        if (currentNode.getNext() != null) {
                            entry = currentNode;
                            currentNode = currentNode.getNext();
                            expectedEntries++;
                            return entry;
                        } else {
                            entry = currentNode;
                            expectedEntries++;
                            if(currentPosition + 1 != capacity) {
                                currentNode = map[++currentPosition];
                            }
                           return entry;
                        }
                    }
                    if(currentPosition + 1 != capacity) {
                        currentNode = map[++currentPosition];
                    }

            }
            return entry;
        }
    }



    public void display(){
        for(int i = 0; i < this.capacity; i++){
            if(map[i] != null){
                Node<Object, Student> currentNode = map[i];
                while (currentNode != null){
                    System.out.println(String
                            .format("Key is %s and value is %s", currentNode.getKey(), currentNode.getValue()));
                    currentNode = currentNode.getNext();
                }
            }
        }
    }

    public  void resize(){
            this.size = 0;
            int oldCapacity = this.capacity;
            int newCapacity = oldCapacity * 2;
            this.capacity = newCapacity;
            Node<Object, Student>[] oldMap = this.map;
            this.map = new Node[newCapacity];
            for(int i=0; i < oldCapacity; i++){
                if(oldMap[i] != null){
                    Node<Object,Student> currentNode = oldMap[i];
                        while (currentNode != null) {
                            put(currentNode.getKey(), currentNode.getValue());
                            currentNode = currentNode.getNext();
                        }

                }
            }
    }


    private int index(Object key){
        if(key == null){
            return 0;
        }
        return Math.abs(key.hashCode() % this.capacity);
    }

    public int getCapacity(){
        return this.capacity;
    }




    static class Node<Object,Student> implements Map.Entry<Object,Student>{
        private Object key;
        private Student value;
        private Node<Object, Student> next;

        public Node(Object key, Student value, Node<Object, Student> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public Object getKey() {
            return key;
        }

        public void setKey(Object key) {
            this.key = key;
        }

        @Override
        public Student getValue() {
            return value;
        }

        @Override
        public Student setValue(Student value) {
            this.value = value;
            return value;
        }

        public Node<Object, Student> getNext() {
            return next;
        }

        public void setNext(Node<Object, Student> next) {
            this.next = next;
        }
    }
}
