package project20280.stacksqueues;

import project20280.interfaces.Stack;
import project20280.list.DoublyLinkedList;

public class LinkedStack<E> implements Stack<E> {

    DoublyLinkedList<E> ll;

    public static void main(String[] args) {
    }

    public LinkedStack() {
        // TODO
ll = new DoublyLinkedList<>();
    }

    @Override
    public int size() {
        return ll.size();
    }

    @Override
    public boolean isEmpty() {
        return ll.isEmpty();
    }

    @Override
    public void push(E e) {
        // TODO
        ll.addFirst(e);
    }

    @Override
    public E top() {
        // TODO
if(isEmpty())
{
    return null;
}
        return ll.first();

    }

    @Override
    public E pop() {
        // TODO
        if(isEmpty())
        {
            return null;
        }
        return ll.removeFirst();
    }

    public String toString() {
        return ll.toString();
    }
}
