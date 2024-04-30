package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class SinglyLinkedList<E> implements List<E> {

    private static class Node<E> {

        private final E element;            // reference to the element stored at this node

        /**
         * A reference to the subsequent node in the list
         */
        private Node<E> next;         // reference to the subsequent node in the list

        /**
         * Creates a node with the given element and next node.
         *
         * @param e the element to be stored
         * @param n reference to a node that should follow the new node
         */
        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        // Accessor methods

        /**
         * Returns the element stored at the node.
         *
         * @return the element stored at the node
         */
        public E getElement() {
            return element;
        }

        /**
         * Returns the node that follows this one (or null if no such node).
         *
         * @return the following node
         */
        public Node<E> getNext() {
            return next;
        }

        // Modifier methods

        /**
         * Sets the node's next reference to point to Node n.
         *
         * @param n the node that should follow this one
         */
        public void setNext(Node<E> n) {
            next = n;
        }
    } //----------- end of nested Node class -----------

    /**
     * The head node of the list
     */
    private Node<E> head = null;               // head node of the list (or null if empty)


    /**
     * Number of nodes in the list
     */
    private int size = 0;                      // number of nodes in the list

    public SinglyLinkedList() {
    }              // constructs an initially empty list

    //@Override
    public int size() {
        // TODO
        return size;
    }

    //@Override
    public boolean isEmpty() {
        // TODO
        if (size == 0) {
            return true;
        } else {

            return false;
        }
    }

    @Override
    public E get(int position) {
        // TODO
        Node<E> curr = head;
        if (position >= size() || position < 0) {
            throw new IndexOutOfBoundsException("wrong element");
        }
        for(int i = 0; i < position; i++)
        {
            curr = curr.getNext();
        }
        return curr.getElement();
    }

    @Override
    public void add(int position, E e) {
        // TODO
        Node<E> newData = new Node<>(e, null);//make new node
        if(position > size()-1|| position < 0)//check if position is out of bounds
        {
            throw new IndexOutOfBoundsException("out of bounds");
        }
        if(position == 0)//check if node needs to be inserted at the start
        {
            newData.setNext((head));//set the new node to the next head
            head = newData;//update
        }
        else{
            Node<E> curr = head;//start from head
            for(int i = 0; i < position-1; i++)
            {
                curr = curr.getNext();//move to next node

            }
            newData.setNext(curr.getNext());//insert new node
            curr.setNext(newData);//link the node
        }
        size++;//increase size
    }


    @Override
    public void addFirst(E e) {
        // TODO
        head = new Node<E>(e, head);//create new head node

        size++;
    }

    @Override
    public void addLast(E e) {
        // TODO
        Node<E> newest = new Node<E>(e, null);//create new ode
        Node<E> last = head;//make last node the head
        if(isEmpty()){//check for empty list
            head = newest;
        }
        else{
            while(last.getNext() != null)//loop until the next node is null
            {
                last = last.getNext();

            }
            last.setNext(newest);//set last node to new node

        }
        size++;//increase size

    }

    @Override
    public E remove(int position) {
        // TODO
        if(position >= size() -1 || position < 0|| head == null)//check if out of bounds
        {
            System.out.println("Error");
        }
        if(position == 0)//check if element is at the start
        {
            E removedData = head.getElement();//store element
            head = head.getNext();//set head to the next element
            size--;//decrease size
            return removedData;//return reomved element
        }
        Node<E> prev = null;
        Node<E> curr = head;
        for(int i = 0; i < position; i++)//iterate through positions
        {
            prev = curr;//set the previous to the current
            curr = curr.getNext();//curr to the next
        }

        prev.setNext(curr.getNext());//set the next of the previous node to the next of the current
        size--;//decrease size
        return curr.getElement();//return what was the curr element

    }

    @Override
    public E removeFirst() {
        // TODO
        if(head == null)//check if head is null
        {
            return null;
        }
        E removedData = head.getElement();
        head = head.getNext();//set the head to the second node
        size--;
        return removedData;
    }

    @Override
    public E removeLast() {
        // TODO
        if(head == null)//check if list is empty
        {
            return null;
        }
        Node<E> last = head;//start with last node being head
        Node<E> previous = null;//intiliase prev as null
        while(last.getNext() != null)//loop until last node is reached
        {
            previous = last;//update previous
            last = last.getNext();//move to the next node
        }
        E removedData = last.getElement();//store data to be removed
        if(previous == null)//if the list has one element
        {
            head = null;

        }else{
            previous.setNext(null);//set the second last node to null
        }
        size--;
        return removedData;
    }
    public void reverse() {
        Node<E> prev = null;
        Node<E> curr = head;
        Node<E> next;

        while (curr != null) {
            next = curr.getNext();
            curr.setNext(prev);
            prev = curr;
            curr = next;
        }
        head = prev;
    }


    //@Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator<E>();
    }

    private class SinglyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public E next() {
            E res = curr.getElement();
            curr = curr.next;
            return res;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.getElement());
            if (curr.getNext() != null)
                sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        /*SinglyLinkedList<Integer> ll = new SinglyLinkedList<Integer>();
        System.out.println("ll " + ll + " isEmpty: " + ll.isEmpty());
        //LinkedList<Integer> ll = new LinkedList<Integer>();

        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addFirst(3);
        ll.addFirst(4);
        ll.addLast(-1);
        //ll.removeLast();
        //ll.removeFirst();
        //System.out.println("I accept your apology");
        //ll.add(3, 2);
        System.out.println(ll);
        ll.remove(5);
        System.out.println(ll);

//        for(Integer i : ll) {
//            System.out.println(i);
//        }
        /*
        ll.addFirst(-100);
        ll.addLast(+100);
        System.out.println(ll);

        ll.removeFirst();
        ll.removeLast();
        System.out.println(ll);

        // Removes the item in the specified index
        ll.remove(2);
        System.out.println(ll);

         */
        SinglyLinkedList<Integer> ll = new SinglyLinkedList<>();
        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addFirst(3);
        ll.addFirst(4);
        ll.addLast(-1);

        System.out.println("Original list: " + ll);
        ll.reverse();
        System.out.println("Reversed list: " + ll);
    }
}
