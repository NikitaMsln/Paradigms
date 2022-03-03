package queue;

public class MyArrayQueueADTTest {
    public static void main(String[] args) {
        QueueTest();
        DequeTest();
    }

    private static void DequeTest() {
        ArrayQueueADT deque1 = new ArrayQueueADT();
        ArrayQueueADT deque2 = new ArrayQueueADT();
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(deque1, "aqadt_1_" + i);
            ArrayQueueADT.push(deque2, "aqadt_2_" + i);
        }
        System.out.println(ArrayQueueADT.peek(deque1));
        System.out.println(ArrayQueueADT.indexOf(deque1, "aqadt_1_1"));
        System.out.println(ArrayQueueADT.lastIndexOf(deque2, "aqadt_1_12"));
        while (!ArrayQueueADT.isEmpty(deque1)) {
            System.out.println(ArrayQueueADT.dequeue(deque1));
        }
        while (!ArrayQueueADT.isEmpty(deque2)) {
            System.out.println(ArrayQueueADT.remove(deque2));
        }
        /**
         * in System.out:
         *  aqadt_1_4
         *  1
         *  -1
         *  aqadt_1_0
         *  aqadt_1_1
         *  aqadt_1_2
         *  aqadt_1_3
         *  aqadt_1_4
         *  aqadt_2_0
         *  aqadt_2_1
         *  aqadt_2_2
         *  aqadt_2_3
         *  aqadt_2_4
         */
    }

    private static void QueueTest() {
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        for (int i = 0; i < 7; i++) {
            ArrayQueueADT.enqueue(queue1, "aqadt_1_" + i);
            ArrayQueueADT.enqueue(queue2, "aqadt_2_" + i);
        }
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(queue1, "aqadt_1_" + (i + 7));
            System.out.println(ArrayQueueADT.dequeue(queue2));
        }
        System.out.println(ArrayQueueADT.isEmpty(queue2));
        System.out.println(ArrayQueueADT.size(queue1));
        System.out.println(ArrayQueueADT.element(queue1));
        ArrayQueueADT.clear(queue1);
        System.out.println(ArrayQueueADT.size(queue1));
        while (!ArrayQueueADT.isEmpty(queue2)) {
            System.out.println(ArrayQueueADT.dequeue(queue2));
        }
        /**
         * in System.out:
         *  aqadt_2_0
         *  aqadt_2_1
         *  aqadt_2_2
         *  aqadt_2_3
         *  aqadt_2_4
         *  false
         *  12
         *  aqadt_1_0
         *  0
         *  aqadt_2_5
         *  aqadt_2_6
         */
    }
}
