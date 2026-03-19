package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;import com.sky.context.BaseContext;import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;import java.lang.reflect.Method;import java.time.LocalDateTime;

/**
 * 自定义切面类，完成创建时间修改时间等公共字段的填充
 */
@Aspect
@Component
@Slf4j

public class AutoFillAspect {

    /**
     * 切入点 （切面 = 切入点 + 通知）
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointcut(){
    }

    /**
     * 前置通知，在通知中填充公共字段
     * @param joinPoint
     */
    @Before("autoFillPointcut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始填充公共字段");

        // 首先获取当前切入的是哪个操作 INSERT 还是 UPDATE
        MethodSignature signature = (MethodSignature) joinPoint.getSignature(); // 直接取到方法签名（本身只是Signature）
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class); //获取方法的注解对象
        OperationType operationType = autoFill.value();

        // 获取当前拦截的方法的参数实体（entity），也就是说要为哪个实体填充？要拿到这个实体的id
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            log.error("填充公共字段接收不到实体！");
        }

        Object entity = args[0];

        // 填充数据 时间可以直接获取；user id 通过上下文BaseContext获取
        LocalDateTime now = LocalDateTime.now();
        Long currentUserId = BaseContext.getCurrentId();

        if (operationType == OperationType.INSERT) {
            //为create和update字段都填充值
            try {
                // 从实体处获取方法
                Method setCreateUser = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setCreateTime = entity.getClass().getMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);

                // 通过反射为对象赋值
                setCreateUser.invoke(entity, currentUserId);
                setCreateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentUserId);
                setUpdateTime.invoke(entity, now);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            //只为update字段填充值
            try {
                // 从实体处获取方法
                Method setUpdateUser = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);

                // 通过反射为对象赋值
                setUpdateUser.invoke(entity, currentUserId);
                setUpdateTime.invoke(entity, now);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
