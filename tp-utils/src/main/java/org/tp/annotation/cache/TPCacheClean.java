package org.tp.annotation.cache;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TPCacheClean {

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
         * <p>Default is {@code ""}, meaning all method parameters are considered as a key.
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
         * Spring Expression Language (SpEL) expression used for making the cache
         * eviction operation conditional.
         * <p>Default is {@code ""}, meaning the cache eviction is always performed.
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
         * Whether all the entries inside the cache(s) are removed.
         * <p>By default, only the value under the associated key is removed.
         * <p>Note that setting this parameter to {@code true} and specifying a
         * {@link #key} is not allowed.
         */
        boolean allEntries() default false;

        /**
         * Whether the eviction should occur before the method is invoked.
         * <p>Setting this attribute to {@code true}, causes the eviction to
         * occur irrespective of the method outcome (i.e., whether it threw an
         * exception or not).
         * <p>Defaults to {@code false}, meaning that the cache eviction operation
         * will occur <em>after</em> the advised method is invoked successfully (i.e.,
         * only if the invocation did not throw an exception).
         */
        boolean beforeInvocation() default false;
}
