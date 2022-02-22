package queue;

public class MyArrayQueueADTTest {
    public static void main(String[] args) {
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
