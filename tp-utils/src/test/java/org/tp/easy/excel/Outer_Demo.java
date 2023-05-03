package org.tp.easy.excel;

/**
 * @author <a href="mailto:mm_8023@hotmail.com">gunten<a/>
 * 2018/10/19
 */
public class Outer_Demo {

    int num;

    // inner class
    private class Inner_Demo {
        public void print() {
            System.out.println("This is an inner class");
        }
    }

    // Accessing he inner class from the method within
    void display_Inner() {
        Inner_Demo inner = new Inner_Demo();
        inner.print();
    }
}


