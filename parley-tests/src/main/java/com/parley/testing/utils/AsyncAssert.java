package com.parley.testing.utils;

/**
 * Class that provide method for asynchronous assert.
 * */
public class AsyncAssert {

    /**
     * Asynchronous assert method. Waits success result to make assert successful.
     * If current assert is failed then method
     * waits successful result.
     * When waiting time is exceeded and method has still failed result then assert is failed.
     *
     * @param tryTimes number of tries.
     * @param delay delay in milliseconds between tries.
     * @param assertExecutor assert
     * @throws Throwable when any failure
     */
    public static void waitForSuccess(int tryTimes, int delay, AssertExecutor assertExecutor) throws Throwable {
        int tryNumber = 0;
        while (tryNumber <= tryTimes) {
            try {
                assertExecutor.execute();
                break;
            } catch (Throwable e) {
                if (tryNumber == tryTimes) {
                    throw e;
                }
                tryNumber++;
                Thread.sleep(delay);
            }
        }

    }

    /**
     * Assert executor.
     */
    public static interface AssertExecutor {
        void execute() throws Throwable;
    }
}
