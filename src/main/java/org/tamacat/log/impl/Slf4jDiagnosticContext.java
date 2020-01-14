package org.tamacat.log.impl;

import org.slf4j.MDC;
import org.tamacat.log.DiagnosticContext;

public class Slf4jDiagnosticContext implements DiagnosticContext {

    @Override
    public void setNestedContext(String data) {
        MDC.put(Thread.currentThread().getName(), data);
    }

    @Override
    public void setMappedContext(String key, String data) {
        MDC.put(key, data);
    }

    @Override
    public void remove() {
        MDC.clear();
    }

    @Override
    public void remove(String key) {
        MDC.remove(key);
    }
}