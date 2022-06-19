package queue;

public class MyArrayQueueModuleTest {
    public static void main(String[] args) {
        QueueTest();
        DequeTest();
    }

    private static void DequeTest() {
        ArrayQueueModule.clear();
        for (int i = 0; i < 3; i++) {
            ArrayQueueModule.push("aqm_" + (2 * i));
            ArrayQueueModule.enqueue("aqm_" + (2 * i + 1));
        }
        System.out.println(ArrayQueueModule.peek());
        System.out.println(ArrayQueueModule.lastIndexOf("aqm_1"));
        System.out.println(ArrayQueueModule.indexOf("aes"));
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.remove());
        }
        /**
         * in System.out:
         *  aqm_5
         *  3
         *  -1
         *  aqm_5
         *  aqm_3
         *  aqm_1
         *  aqm_0
         *  aqm_2
         *  aqm_4
         */
    }

    private static void QueueTest() {
        for (int i = 0; i < 10; i++) {
            ArrayQueueModule.enqueue("aqm_" + i);
        }
        System.out.println(ArrayQueueModule.size());
        for (int i = 0; i < 10; i++) {
            ArrayQueueModule.enqueue("aqm_" + i);
            if (i == 5) {
                System.out.println(ArrayQueueModule.element());
                ArrayQueueModule.clear();
                System.out.println(ArrayQueueModule.isEmpty());
            }
        }
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.dequeue());
        }
        /**
         * in System.out:
         *  10
         *  aqm_0
         *  true
         *  aqm_6
         *  aqm_7
         *  aqm_8
         *  aqm_9
         */
    }
}
