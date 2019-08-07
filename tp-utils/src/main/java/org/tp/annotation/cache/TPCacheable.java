package org.tp.annotation.cache;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TPCacheable {

        /**
         * Alias for {@link #cacheNames}.
         */
        @AliasFor("cacheNames")
        String[] domains();

        /**
         * Names of the caches in which method invocation results are stored.
         * <p>Names may be used to determine the target cache (or caches), matching
         * the qualifier value or bean name of a specific bean definition.
         * @since 4.2
         * @see #domains
         * @see CacheConfig#cacheNames
         */
        @AliasFor("value")
        String[] cacheNames() default {};

        /**
         * Spring Expression Language (SpEL) expression for computing the key dynamically.
         * <p>Default is {@code ""}, meaning all method parameters are considered as a key,
         * <p>The SpEL expression evaluates against a dedicated context that provides the
         * following meta-data:
         * <ul>
         * <li>{@code #root.method}, {@code #root.target}, and {@code #root.caches} for
         * references to the {@link java.lang.reflect.Method method}, target object, and
         * affected cache(s) respectively.</li>
         * <li>Shortcuts for the method name ({@code #root.methodName}) and target class
         * ({@code #root.targetClass}) are also available.
         * <li>Method arguments can be accessed by index. For instance the second argument
         * can be accessed via {@code #root.args[1]}, {@code #p1} or {@code #a1}. Arguments
         * can also be accessed by name if that information is available.</li>
         * </ul>
         */
        String key() default "";

        /**
         * Spring Expression Language (SpEL) expression used for making the method
         * caching conditional.
         * <p>Default is {@code ""}, meaning the method result is always cached.
         * <p>The SpEL expression evaluates against a dedicated context that provides the
         * following meta-data:
         * <ul>
         * <li>{@code #root.method}, {@code #root.target}, and {@code #root.caches} for
         * references to the {@link java.lang.reflect.Method method}, target object, and
         * affected cache(s) respectively.</li>
         * <li>Shortcuts for the method name ({@code #root.methodName}) and target class
         * ({@code #root.targetClass}) are also available.
         * <li>Method arguments can be accessed by index. For instance the second argument
         * can be accessed via {@code #root.args[1]}, {@code #p1} or {@code #a1}. Arguments
         * can also be accessed by name if that information is available.</li>
         * </ul>
         */
        String condition() default "";

        /**
         * Spring Expression Language (SpEL) expression used to veto method caching.
         * <p>Unlike {@link #condition}, this expression is evaluated after the method
         * has been called and can therefore refer to the {@code result}.
         * <p>Default is {@code ""}, meaning that caching is never vetoed.
         * <p>The SpEL expression evaluates against a dedicated context that provides the
         * following meta-data:
         * <ul>
         * <li>{@code #result} for a reference to the result of the method invocation. For
         * supported wrappers such as {@code Optional}, {@code #result} refers to the actual
         * object, not the wrapper</li>
         * <li>{@code #root.method}, {@code #root.target}, and {@code #root.caches} for
         * references to the {@link java.lang.reflect.Method method}, target object, and
         * affected cache(s) respectively.</li>
         * <li>Shortcuts for the method name ({@code #root.methodName}) and target class
         * ({@code #root.targetClass}) are also available.
         * <li>Method arguments can be accessed by index. For instance the second argument
         * can be accessed via {@code #root.args[1]}, {@code #p1} or {@code #a1}. Arguments
         * can also be accessed by name if that information is available.</li>
         * </ul>
         * @since 3.2
         */
        String unless() default "";

        int expireTime() default 60;
}
