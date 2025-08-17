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
import org.springframework.core.Ordered;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE) // @Transactional 보다 우선 실행되도록 순서 설정
@Component
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAspect {

    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(kr.hhplus.be.server.common.aop.DistributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {

        List<RLock> rLocks = new ArrayList<>();
        List<String> keys = new ArrayList<>();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DistributedLock multiDistributedLock = signature.getMethod().getAnnotation(DistributedLock.class);

        for(String keyExpression : multiDistributedLock.keys()){
            Object value = getLockKey(joinPoint, keyExpression);

            if (value instanceof Iterable<?> iterable) {  // 리스트/컬렉션 처리
                for (Object item : iterable) {
                    String key = multiDistributedLock.prefix() + item.toString();
                    rLocks.add(redissonClient.getLock(key));
                    keys.add(key);
                }
            } else {  // 단일 값 처리
                String key = multiDistributedLock.prefix() + value.toString();
                rLocks.add(redissonClient.getLock(key));
                keys.add(key);
            }
        }

        RLock multiLock = redissonClient.getMultiLock(rLocks.toArray(new RLock[0]));
        log.info("멀티락 획득 시도.. keys : {}", keys);

        try{
            if(!multiLock.tryLock(multiDistributedLock.waitTime(), multiDistributedLock.leaseTime(), TimeUnit.SECONDS)){
                log.info("멀티락 획득 실패.. keys : {}", keys);
                throw new RuntimeException("락을 획득에 실패하였습니다.");
            }
            log.info("멀티락 획득 성공.. keys : {}", keys);
            return joinPoint.proceed();
        } finally {
            if(multiLock.isHeldByCurrentThread()){
                log.info("멀티락 해제.. keys : {}", keys);
                multiLock.unlock();
            }
        }
    }

    private Object getLockKey(ProceedingJoinPoint joinPoint, String keyExpression) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        EvaluationContext context = new StandardEvaluationContext();
        String[] paramNames = nameDiscoverer.getParameterNames(signature.getMethod());
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < Objects.requireNonNull(paramNames).length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        return parser.parseExpression(keyExpression).getValue(context);
    }
}