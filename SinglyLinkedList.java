public class SinglyLinkedList<E> implements Cloneable {
    /**
     * Nested Node class: Node of a singly linked list, which stores a reference to
     * its element and to the subsequent node in the list (or null if this is the last node).
     */
    private static class Node<E> {
        private E element; // reference to the element stored at this node
        private Node<E> next; // reference to the subsequent node in the list

        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        public E getElement() {
            return element;
        }

        public Node<E> getNext() {
            return next;
        }

        public void setNext(Node<E> n) {
            next = n;
        }
    }

    //----------- end of nested Node class -----------
    private Node<E> head = null; // The head node of the list (or null if empty)
    private Node<E> tail = null; // The last node of the list (or null if empty)
    private int size = 0; // Number of nodes in the list

    /** Constructs an initially empty list. */
    public SinglyLinkedList() {} // Constructor fixed by removing "void"

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public E first() {
        if (isEmpty()) return null;
        return head.getElement();
    }

    public E last() {
        if (isEmpty()) return null;
        return tail.getElement();
    }

    public void addFirst(E e) {
        head = new Node<>(e, head); // create and link a new node
        if (size == 0) {
            tail = head; // special case: new node becomes tail also
        }
        size++;
    }

    public void addLast(E e) {
        Node<E> newest = new Node<>(e, null);
        if (isEmpty()) {
            head = newest; // special case: previously empty list
        } else {
            tail.setNext(newest); // link new node after existing tail
        }
        tail = newest; // new node becomes the tail
        size++; // increment size
    }

    public E removeFirst() {
        if (isEmpty()) return null; // nothing to remove
        E answer = head.getElement();
        head = head.getNext();
        size--;
        if (size == 0) {
            tail = null; // special case as list is now empty
        }
        return answer;
    }
}
