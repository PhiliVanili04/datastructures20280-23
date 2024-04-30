package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class DoublyLinkedList<E> implements List<E> {

    private static class Node<E> {
        private final E data;
        private Node<E> next;
        private Node<E> prev;

        public Node(E e, Node<E> p, Node<E> n) {
            data = e;
            prev = p;
            next = n;
        }

        public E getData() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrev() {
            return prev;
        }
        public void setNext(Node<E> n)
        {
            next = n;
        }
        public void setPrev(Node<E> p)
        {
            prev = p;
        }

    }

    private Node<E> head;
    private Node<E> tail;
    private int size = 0;

    public DoublyLinkedList() {
        head = new Node<E>(null, null, null);
        tail = new Node<E>(null, head, null);
        head.next = tail;
    }

    private void addBetween(E e, Node<E> pred, Node<E> succ) {
        // TODO
        Node<E> newNode = new Node<>(e, pred, succ);
        pred.setNext(newNode);
        succ.setPrev(newNode);
        size++;
    }

    @Override
    public int size() {
        // TODO
        return size;
    }

    @Override
    public boolean isEmpty() {
        // TODO
        if (size == 0) {
            return true;
        } else {

            return false;
        }
    }

    @Override
    public E get(int i) {
        // TODO
        if(i >= size || i < 0 ) {
        System.out.println("Error");
        }
        DoublyLinkedList.Node<E> curr = head.getNext();
        for(int p = 0; p < i; p++)
        {
            curr = curr.getNext();
        }

        return curr.getData();
    }

    @Override
    public void add(int i, E e) {
        if (i < 0 || i > size()) {
            System.out.println("Error: Invalid index");
            return;
        }
        if (i == 0) {
            addFirst(e);
            return;
        } else if (i == size()) {
            addLast(e);
            return;
        }
        DoublyLinkedList.Node<E> curr = head;
        for (int j = 0; j < i; j++) {
            curr = curr.getNext();
        }
        DoublyLinkedList.Node<E> newNode = new DoublyLinkedList.Node<>(e, curr, curr.getNext());
        curr.getNext().setPrev(newNode);
        curr.setNext(newNode);
        size++;
    }


    @Override

    public E remove(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Error");
        }
        Node<E> curr = head.getNext();
        for (int j = 0; j < i; j++) {
            curr = curr.getNext();
        }
        return remove(curr);
    }

    private class DoublyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head.next;

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
        return new DoublyLinkedListIterator<E>();
    }

    private E remove(Node<E> n) {
        // TODO
       Node<E> pred = n.getPrev();
       Node<E> succ = n.getNext();
        if (pred != null) {
            pred.setNext(succ);
        }
        if (succ != null) {
            succ.setPrev(pred);
        }
        size--;
        return n.getData();
    }

    public E first() {
        if (isEmpty()) {
            return null;
        }
        return head.next.getData();
    }

    public E last() {
        // TODO
        if(isEmpty())
        {
            return null;
        }
        return tail.getPrev().getData();

    }

    @Override
    public E removeFirst() {
        // TODO
        if(isEmpty())
        {
            return null;
        }
        return remove(head.getNext());

    }

    @Override
    public E removeLast() {
        // TODO

        if(isEmpty())
        {
            return null;
        }
        return remove(tail.getPrev());
    }

    @Override
    public void addLast(E e) {
        // TODO
        addBetween(e, tail.getPrev(), tail);
    }

    @Override
    public void addFirst(E e) {
        // TODO
        addBetween(e, head, head.getNext());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head.next;
        while (curr != tail) {
            sb.append(curr.data);
            curr = curr.next;
            if (curr != tail) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<Integer>();
        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addLast(-1);
        System.out.println(ll);

        ll.removeFirst();
        System.out.println(ll);

        ll.removeLast();
        System.out.println(ll);

        for (Integer e : ll) {
            System.out.println("value: " + e);
        }
    }
}