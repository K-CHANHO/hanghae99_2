package kr.hhplus.be.server.common.aop;

import java.lang.reflect.Method;

public interface LockKeyGenerator {
    String generateKey(Method method, Object[] args);
}
