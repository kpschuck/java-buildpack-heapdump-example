package com.vmware.javabuildpackheapdumpexample;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class OOM {
    private Boolean triggered = false;

    OOM() {}

    OOM(Boolean triggered) {
        this.triggered = triggered;
    }

    public boolean isTriggered() {
        return this.triggered;
    }

    public void trigger() throws InterruptedException, OutOfMemoryError {
        int dummyArraySize = 15;
        log.info("Max JVM memory: " + Runtime.getRuntime().maxMemory());
        long memoryConsumed = 0;
        try {
            long[] memoryAllocated = null;
            for (int loop = 0; loop < Integer.MAX_VALUE; loop++) {
                memoryAllocated = new long[dummyArraySize];
                memoryAllocated[0] = 0;
                memoryConsumed += dummyArraySize * Long.SIZE;
                log.info("Memory Consumed till now: " + memoryConsumed);
                dummyArraySize *= dummyArraySize * 2;
                Thread.sleep(500);
            }
        } catch (OutOfMemoryError | InterruptedException ex) {
            log.info("Caught out of memory error");
            //Log the information,so that we can generate the statistics (latter on).
            throw ex;
        }
    }
}