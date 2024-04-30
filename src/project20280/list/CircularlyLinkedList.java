package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class CircularlyLinkedList<E> implements List<E> {

    private class Node<T> {
        private final T data;
        private Node<T> next;

        public Node(T e, Node<T> n) {
            data = e;
            next = n;
        }

        public T getData() {
            return data;
        }

        public void setNext(Node<T> n) {
            next = n;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    private Node<E> tail = null;
    private int size;

    public CircularlyLinkedList() {

    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {
        // TODO
        if(i < 0|| i >= size)
        {
            System.out.println("Error");
        }
        Node<E> curr = tail.next;
        for(int j = 0; j < i; j++)
        {
            curr = curr.next;
        }

        return curr.data;
    }

    /**
     * Inserts the given element at the specified index of the list, shifting all
     * subsequent elements in the list one position further to make room.
     *
     * @param i the index at which the new element should be stored
     * @param e the new element to be stored
     */
    @Override
    public void add(int i, E e) {
        if(i < 0 || i > size())
        {
            System.out.println("Error");
        }
            Node<E> newNode = new Node<>(e, null);
        if(isEmpty())
        {
            newNode.next = newNode;
            tail = newNode;
        }else if(i==0)
        {
            newNode.next = tail.next;
            tail.next=newNode;
        }else{
            Node<E> curr = tail.next;
            for(int j = 0; j<i-1;j++)
            {
                curr = curr.next;
            }
            newNode.next = curr.next;
            curr.next = newNode;
            if(i == size)
            {
                tail = newNode;
            }
        }
        size++;


        // TODO
    }

    @Override
    public E remove(int i) {
        // TODO
        if(i < 0|| i >= size||isEmpty())
        {
            System.out.println("Error");
        }
        Node<E> curr = tail.next;
        E removedData = null;
        if(i == 0) {
            removedData = curr.data;


            if (i == 1) {
                tail = null;
            } else {

                tail.next = curr.next;
            }
        }
        else{
            for(int j = 0; j < i-1;j++)
            {
                curr= curr.next;
            }
            removedData = curr.next.data;
            if(i==size -1)
            {
                tail = curr;
            }
            curr.next = curr.next.next;
        }
        size--;

        return removedData;
    }

    public void rotate() {
        // TODO
        if(tail != null)
        {
            tail= tail.getNext();
        }

    }

    private class CircularlyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) tail;

        @Override
        public boolean hasNext() {
            return curr != tail;
        }

        @Override
        public E next() {
            E res = curr.data;
            curr = curr.next;
            return res;
        }

    }

    @Override
    public Iterator<E> iterator() {
        return new CircularlyLinkedListIterator<E>();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E removeFirst() {
        // TODO
        if(isEmpty())
        {
            return null;
        }
        Node<E> head = tail.getNext();
        if(head == tail){
            tail = null;
        }else{
            tail.setNext((head.getNext()));
        }
        size--;

        return head.getData();
    }

    @Override
    public E removeLast() {
        // TODO
        if(isEmpty())
        {
            return null;
        }
        if(size == 1)
        {
            return removeFirst();
        }else{
            Node<E> curr = tail.getNext();
            while(curr.getNext() != tail)
            {
                curr = curr.getNext();
            }
            E removedData = tail.getData();
            tail = curr;
            tail.setNext(curr.getNext().getNext());
            size--;
            return removedData;
        }


    }

    @Override
    public void addFirst(E e) {
        // TODO
        if(size == 0)
        {
            tail = new Node<>(e, null);
            tail.setNext(tail);

        }else{
            Node<E> newNode = new Node<>(e, tail.getNext());
            tail.setNext(newNode);
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        // TODO
        addFirst(e);
        tail= tail.getNext();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = tail;
        do {
            curr = curr.next;
            sb.append(curr.data);
            if (curr != tail) {
                sb.append(", ");
            }
        } while (curr != tail);
        sb.append("]");
        return sb.toString();
    }


    public static void main(String[] args) {
        CircularlyLinkedList<Integer> ll = new CircularlyLinkedList<Integer>();
        for (int i = 10; i < 20; ++i) {
            ll.addLast(i);
        }

        System.out.println(ll);

        ll.removeFirst();
        System.out.println(ll);

        ll.removeLast();
        System.out.println(ll);

        ll.rotate();
        System.out.println(ll);

        ll.removeFirst();
        ll.rotate();
        System.out.println(ll);

        ll.removeLast();
        ll.rotate();
        System.out.println(ll);

        for (Integer e : ll) {
            System.out.println("value: " + e);
        }

    }
}
