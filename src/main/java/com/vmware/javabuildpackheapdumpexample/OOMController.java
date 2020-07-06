package com.vmware.javabuildpackheapdumpexample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OOMController {
    @PostMapping("/oom")
    OOM newOOM() throws InterruptedException {
        OOM oom = new OOM();
        oom.trigger();
        return new OOM();
    }
}
