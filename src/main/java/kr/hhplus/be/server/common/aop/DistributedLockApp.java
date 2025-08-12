package kr.hhplus.be.server.common.aop;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect
@Order(1) // @Transactional 보다 우선 실행되도록 순서 설정
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockApp {

    private static final String REDISSON_LOCK_PREFIX = "hhplus:";
    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(distributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = getLockKey(joinPoint, distributedLock.key());
        log.info("락 획득 시도.. lockKey : {}", key);

        RLock rLock = redissonClient.getLock(key);

        try{
            if(!rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), TimeUnit.SECONDS)){
                log.info("락 획득 실패..");
                throw new RuntimeException("락을 획득에 실패하였습니다.");
            }
            log.info("락 획득 성공..");
            return joinPoint.proceed();
        } finally {
            if(rLock.isHeldByCurrentThread()){
                log.info("락 해제..");
                rLock.unlock();
            }
        }
    }

    private String getLockKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = nameDiscoverer.getParameterNames(signature.getMethod());
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < Objects.requireNonNull(paramNames).length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return parser.parseExpression(keyExpression).getValue(context, String.class);
    }
}