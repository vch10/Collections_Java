package com.endava.internship.collections;

import java.util.*;
import java.util.function.Consumer;

public class StudentList implements List<Student>, Iterable<Student> {
    private int size = 0 ;
    private static final int DEFAULT_CAPACITY = 10;
    private Student[] list;

    public StudentList() {
        this.list = new Student[DEFAULT_CAPACITY];
    }


    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o){
        if(o == null){
            throw new UnsupportedOperationException(" Student cannot be null !!!");
        }else{
            for(int i = 0; i < size; i++){
                if(list[i].equals(o))
                    return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<Student> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<Student>{
         int size = StudentList.this.size;
         int current = 0;
         int lastPos = -1;

        public Itr() {
        }

        @Override
        public boolean hasNext() {
            return size - current >= 1;
        }

        @Override
        public Student next() {
            if(!hasNext())
                throw new NoSuchElementException();
            Student t = StudentList.this.get(lastPos=current);
            current+=1;
            return t;
        }

        @Override
        public void remove() {
            if(lastPos < 0){
                throw new IllegalStateException();
            }
           StudentList.this.remove(lastPos);
            current = lastPos;
            lastPos = -1;

        }

        @Override
        public void forEachRemaining(Consumer<? super Student> action) {
            Iterator.super.forEachRemaining(action);
        }
    }

    @Override
    public Object[] toArray() {
        Object[] array = Arrays.copyOfRange(list,0,this.size);
        return array;
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        if(ts.length < size){
            return (T[]) Arrays.copyOf(list, size, ts.getClass());
        }
        System.arraycopy(this.list, 0 , ts,0,this.size);
        if(ts.length > size){
            ts[size] = null;
        }
        return ts;
    }

    @Override
    public boolean add(Student student) {
        checkForAdd();
       list[size++] = student;
       return true;
    }

    private void checkForAdd(){
        if (size == list.length) {
            ensureCapacity();
        }
    }

    public void ensureCapacity(){
        int newSize = list.length * 2;
        list = Arrays.copyOf(list, newSize);
    }

    @Override
    public boolean remove(Object o) {
        int i=0;
        found:{
            for (; i < size; i++) {
                if (list[i].equals(o)) {
                    break found;
                }
            }
            return false;
        }

        fastRemove(i);
        return true;
    }

    private void fastRemove(int i){
        int numElem = size - (i+1);
        System.arraycopy(list, i+1, list, i, numElem);
        size--;
    }


    @Override
    public void clear() {
        for(int i = 0; i < size; i++){
            list[i] = null;
            size = 0;
        }
    }

    @Override
    public Student get(int i) {
        if(i > size || i < 0){
            throw new IndexOutOfBoundsException("Index:"  + i + ", Size " + i);
        }
        return list[i];
    }

    @Override
    public Student set(int i, Student student) {
        rangeCheckForAdd(i);
        checkForAdd();
        if(student != null) {
            this.list[i] = student;
            return student;
        }else{
            throw new UnsupportedOperationException("Student cannot be null !!!");
        }
    }

    @Override
    public void add(int i, Student student) {
        rangeCheckForAdd(i);
        checkForAdd();
        System.arraycopy(list, i, list,i+1, size - i  );
        list[i] = student;
        size++;
    }
    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException("Index out of Bounds !!!");
    }



    @Override
    public Student remove(int i) {
        rangeCheckForAdd(i);
        Student student  = list[i];
        fastRemove(i);
        return student;
    }

    @Override
    public int indexOf(Object o) {
           for(int i = 0; i < size; i++){
               if(list[i].equals(o)){
                   return i;
               }
           }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        int lastIndex = -1;
        for(int i = 0; i < size; i++){
            if(list[i].equals(o)){
                lastIndex =  i;
            }
        }
        return lastIndex;
    }

    @Override
    public ListIterator<Student> listIterator() {
        return new ListItr();
    }

    @Override
    public ListIterator<Student> listIterator(int i) {
        return new ListItr(i);
    }


    private class ListItr extends Itr implements ListIterator<Student>{
        public ListItr(){}
        public ListItr(int index){
            super();
            current = index;
        }

        @Override
        public boolean hasPrevious() {
            return current != 0;
        }

        @Override
        public Student previous() {
            int i = current - 1 ;
//            if(i < 0){
//                throw new NoSuchElementException();
//            }
            current = i;
            lastPos = i;
            return StudentList.this.get(i);
        }

        @Override
        public int nextIndex() {
            return current;
        }

        @Override
        public int previousIndex() {
            return current - 1;
        }

        @Override
        public void remove() {
            if(lastPos >= 0){
                current = lastPos;
                StudentList.this.remove(lastPos);
                lastPos = -1;
            }else{
                throw new UnsupportedOperationException("The method is not supported !!!");
            }
        }

        @Override
        public void set(Student student) {
            if(lastPos >= 0){
                StudentList.this.set(lastPos, student);

            }else{
                throw new UnsupportedOperationException("The method is not supported !!!");
            }
        }

        @Override
        public void add(Student student) {
            int i = current;
            StudentList.this.add(i,student);
            current = i+1;
        }
    }

    @Override
    public List<Student> subList(int i, int i1) {
        if(i >= 0 && i < size && i1 > i && i1 < size ){
            return new ArrayList<>(Arrays.asList(Arrays.copyOfRange(list,i,i1)));
        }else{
            throw new UnsupportedOperationException("The method is not supported !!!");
        }
    }


    @Override
    public boolean addAll(Collection<? extends Student> collection) {
        if( collection == null){
            throw new UnsupportedOperationException("The method is not supported !!!");
        }
        Iterator<Student> itr = (Iterator<Student>) collection.iterator();
        while(itr.hasNext()){
             StudentList.this.add(itr.next());
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int i, Collection<? extends Student> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        //Ignore this for homework
        throw new UnsupportedOperationException();
    }
}
