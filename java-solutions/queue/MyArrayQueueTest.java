package queue;

public class MyArrayQueueTest {
    public static void main(String[] args) {
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        for (int i = 0; i < 7; i++) {
            queue1.enqueue("aq_1_" + i);
            queue2.enqueue("aq_2_" + i);
        }
        for (int i = 0; i < 5; i++) {
            queue1.enqueue( "aq_1_" + (i + 7));
            System.out.println(queue2.dequeue());
        }
        System.out.println(queue2.isEmpty());
        System.out.println(queue1.size());
        System.out.println(queue1.element());
        queue1.clear();
        System.out.println(queue1.size());
        while (!queue2.isEmpty()) {
            System.out.println(queue2.dequeue());
        }
        /**
         * in System.out:
         *  aq_2_0
         *  aq_2_1
         *  aq_2_2
         *  aq_2_3
         *  aq_2_4
         *  false
         *  12
         *  aq_1_0
         *  0
         *  aq_2_5
         *  aq_2_6
         */
    }
}
