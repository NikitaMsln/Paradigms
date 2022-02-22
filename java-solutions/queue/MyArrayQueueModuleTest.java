package queue;

public class MyArrayQueueModuleTest {
    public static void main(String[] args) {
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
