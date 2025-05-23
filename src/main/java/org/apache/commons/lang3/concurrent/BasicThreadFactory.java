/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.lang3.concurrent;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * An implementation of the {@link ThreadFactory} interface that provides some
 * configuration options for the threads it creates.
 * <p>
 * A {@link ThreadFactory} is used for instance by an {@link ExecutorService} to
 * create the threads it uses for executing tasks. In many cases users do not
 * have to care about a {@link ThreadFactory} because the default one used by an
 * {@link ExecutorService} will do. However, if there are special requirements
 * for the threads, a custom {@link ThreadFactory} has to be created.
 * </p>
 * <p>
 * This class provides some frequently needed configuration options for the
 * threads it creates. These are the following:
 * </p>
 * <ul>
 * <li>A name pattern for the threads created by this factory can be specified.
 * This is often useful if an application uses multiple executor services for
 * different purposes. If the names of the threads used by these services have
 * meaningful names, log output or exception traces can be much easier to read.
 * Naming patterns are <em>format strings</em> as used by the {@code
 * String.format()} method. The string can contain the place holder {@code %d}
 * which will be replaced by the number of the current thread ({@code
 * ThreadFactoryImpl} keeps a counter of the threads it has already created).
 * For instance, the naming pattern {@code "My %d. worker thread"} will result
 * in thread names like {@code "My 1. worker thread"}, {@code
 * "My 2. worker thread"} and so on.</li>
 * <li>A flag whether the threads created by this factory should be daemon
 * threads. This can impact the exit behavior of the current Java application
 * because the JVM shuts down if there are only daemon threads running.</li>
 * <li>The priority of the thread. Here an integer value can be provided. The
 * {@link Thread} class defines constants for valid ranges of priority
 * values.</li>
 * <li>The {@link UncaughtExceptionHandler} for the thread. This handler is
 * called if an uncaught exception occurs within the thread.</li>
 * </ul>
 * <p>
 * {@link BasicThreadFactory} wraps another thread factory which actually
 * creates new threads. The configuration options are set on the threads created
 * by the wrapped thread factory. On construction time the factory to be wrapped
 * can be specified. If none is provided, a default {@link ThreadFactory} is
 * used.
 * </p>
 * <p>
 * Instances of {@link BasicThreadFactory} are not created directly, but the
 * nested {@link Builder} class is used for this purpose. Using the builder only
 * the configuration options an application is interested in need to be set. The
 * following example shows how a {@link BasicThreadFactory} is created and
 * installed in an {@link ExecutorService}:
 * </p>
 *
 * <pre>
 * // Create a factory that produces daemon threads with a naming pattern and
 * // a priority
 * BasicThreadFactory factory = new BasicThreadFactory.Builder()
 *     .namingPattern(&quot;workerthread-%d&quot;)
 *     .daemon(true)
 *     .priority(Thread.MAX_PRIORITY)
 *     .build();
 * // Create an executor service for single-threaded execution
 * ExecutorService exec = Executors.newSingleThreadExecutor(factory);
 * </pre>
 *
 * @since 3.0
 */
public class BasicThreadFactory implements ThreadFactory {

    /**
     * A <em>builder</em> class for creating instances of {@code
     * BasicThreadFactory}.
     * <p>
     * Using this builder class instances of {@link BasicThreadFactory} can be
     * created and initialized. The class provides methods that correspond to
     * the configuration options supported by {@link BasicThreadFactory}. Method
     * chaining is supported. Refer to the documentation of {@code
     * BasicThreadFactory} for a usage example.
     * </p>
     */
    public static class Builder implements org.apache.commons.lang3.builder.Builder<BasicThreadFactory> {

        /** The wrapped factory. */
        private ThreadFactory factory;

        /** The uncaught exception handler. */
        private Thread.UncaughtExceptionHandler exceptionHandler;

        /** The naming pattern. */
        private String namingPattern;

        /** The priority. */
        private Integer priority;

        /** The daemon flag. */
        private Boolean daemon;

        /**
         * Constructs a new instance.
         *
         * @deprecated Use {@link BasicThreadFactory#builder()}.
         */
        @Deprecated
        public Builder() {
            // empty
        }

        /**
         * Creates a new {@link BasicThreadFactory} with all configuration
         * options that have been specified by calling methods on this builder.
         * After creating the factory {@link #reset()} is called.
         *
         * @return the new {@link BasicThreadFactory}
         */
        @Override
        public BasicThreadFactory build() {
            final BasicThreadFactory factory = new BasicThreadFactory(this);
            reset();
            return factory;
        }

        /**
         * Sets the daemon flag for the new {@link BasicThreadFactory} to {@code true} causing a new thread factory to create daemon threads.
         *
         * @return a reference to this {@link Builder}
         * @since 3.18.0
         */
        public Builder daemon() {
            return daemon(true);
        }

        /**
         * Sets the daemon flag for the new {@link BasicThreadFactory}. If this
         * flag is set to <strong>true</strong> the new thread factory will create daemon
         * threads.
         *
         * @param daemon the value of the daemon flag
         * @return a reference to this {@link Builder}
         */
        public Builder daemon(final boolean daemon) {
            this.daemon = Boolean.valueOf(daemon);
            return this;
        }

        /**
         * Sets the naming pattern to be used by the new {@code
         * BasicThreadFactory}.
         *
         * @param namingPattern the naming pattern (must not be <strong>null</strong>)
         * @return a reference to this {@link Builder}
         * @throws NullPointerException if the naming pattern is <strong>null</strong>
         */
        public Builder namingPattern(final String namingPattern) {
            this.namingPattern = Objects.requireNonNull(namingPattern, "pattern");
            return this;
        }

        /**
         * Sets the priority for the threads created by the new {@code
         * BasicThreadFactory}.
         *
         * @param priority the priority
         * @return a reference to this {@link Builder}
         */
        public Builder priority(final int priority) {
            this.priority = Integer.valueOf(priority);
            return this;
        }

        /**
         * Resets this builder. All configuration options are set to default
         * values. Note: If the {@link #build()} method was called, it is not
         * necessary to call {@code reset()} explicitly because this is done
         * automatically.
         */
        public void reset() {
            factory = null;
            exceptionHandler = null;
            namingPattern = null;
            priority = null;
            daemon = null;
        }

        /**
         * Sets the uncaught exception handler for the threads created by the
         * new {@link BasicThreadFactory}.
         *
         * @param exceptionHandler the {@link UncaughtExceptionHandler} (must not be
         * <strong>null</strong>)
         * @return a reference to this {@link Builder}
         * @throws NullPointerException if the exception handler is <strong>null</strong>
         */
        public Builder uncaughtExceptionHandler(
                final Thread.UncaughtExceptionHandler exceptionHandler) {
            this.exceptionHandler = Objects.requireNonNull(exceptionHandler, "handler");
            return this;
        }

        /**
         * Sets the {@link ThreadFactory} to be wrapped by the new {@code
         * BasicThreadFactory}.
         *
         * @param factory the wrapped {@link ThreadFactory} (must not be
         * <strong>null</strong>)
         * @return a reference to this {@link Builder}
         * @throws NullPointerException if the passed in {@link ThreadFactory}
         * is <strong>null</strong>
         */
        public Builder wrappedFactory(final ThreadFactory factory) {
            this.factory = Objects.requireNonNull(factory, "factory");
            return this;
        }
    }

    /**
     * Creates a new builder.
     *
     * @return a new builder.
     * @since 3.18.0
     */
    public static Builder builder() {
        return new Builder();
    }

    /** A counter for the threads created by this factory. */
    private final AtomicLong threadCounter;

    /** Stores the wrapped factory. */
    private final ThreadFactory wrappedFactory;

    /** Stores the uncaught exception handler. */
    private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    /** Stores the naming pattern for newly created threads. */
    private final String namingPattern;

    /** Stores the priority. */
    private final Integer priority;

    /** Stores the daemon status flag. */
    private final Boolean daemon;

    /**
     * Creates a new instance of {@link ThreadFactory} and configures it
     * from the specified {@link Builder} object.
     *
     * @param builder the {@link Builder} object
     */
    private BasicThreadFactory(final Builder builder) {
        wrappedFactory = builder.factory != null ? builder.factory : Executors.defaultThreadFactory();
        namingPattern = builder.namingPattern;
        priority = builder.priority;
        daemon = builder.daemon;
        uncaughtExceptionHandler = builder.exceptionHandler;
        threadCounter = new AtomicLong();
    }

    /**
     * Gets the daemon flag. This flag determines whether newly created
     * threads should be daemon threads. If <strong>true</strong>, this factory object
     * calls {@code setDaemon(true)} on the newly created threads. Result can be
     * <strong>null</strong> if no daemon flag was provided at creation time.
     *
     * @return the daemon flag
     */
    public final Boolean getDaemonFlag() {
        return daemon;
    }

    /**
     * Gets the naming pattern for naming newly created threads. Result can
     * be <strong>null</strong> if no naming pattern was provided.
     *
     * @return the naming pattern
     */
    public final String getNamingPattern() {
        return namingPattern;
    }

    /**
     * Gets the priority of the threads created by this factory. Result can
     * be <strong>null</strong> if no priority was specified.
     *
     * @return the priority for newly created threads
     */
    public final Integer getPriority() {
        return priority;
    }

    /**
     * Gets the number of threads this factory has already created. This
     * class maintains an internal counter that is incremented each time the
     * {@link #newThread(Runnable)} method is invoked.
     *
     * @return the number of threads created by this factory
     */
    public long getThreadCount() {
        return threadCounter.get();
    }

    /**
     * Gets the {@link UncaughtExceptionHandler} for the threads created by
     * this factory. Result can be <strong>null</strong> if no handler was provided.
     *
     * @return the {@link UncaughtExceptionHandler}
     */
    public final Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return uncaughtExceptionHandler;
    }

    /**
     * Gets the wrapped {@link ThreadFactory}. This factory is used for
     * actually creating threads. This method never returns <strong>null</strong>. If no
     * {@link ThreadFactory} was passed when this object was created, a default
     * thread factory is returned.
     *
     * @return the wrapped {@link ThreadFactory}
     */
    public final ThreadFactory getWrappedFactory() {
        return wrappedFactory;
    }

    /**
     * Initializes the specified thread. This method is called by
     * {@link #newThread(Runnable)} after a new thread has been obtained from
     * the wrapped thread factory. It initializes the thread according to the
     * options set for this factory.
     *
     * @param thread the thread to be initialized
     */
    private void initializeThread(final Thread thread) {
        if (getNamingPattern() != null) {
            final Long count = Long.valueOf(threadCounter.incrementAndGet());
            thread.setName(String.format(getNamingPattern(), count));
        }
        if (getUncaughtExceptionHandler() != null) {
            thread.setUncaughtExceptionHandler(getUncaughtExceptionHandler());
        }
        if (getPriority() != null) {
            thread.setPriority(getPriority().intValue());
        }
        if (getDaemonFlag() != null) {
            thread.setDaemon(getDaemonFlag().booleanValue());
        }
    }

    /**
     * Creates a new thread. This implementation delegates to the wrapped
     * factory for creating the thread. Then, on the newly created thread the
     * corresponding configuration options are set.
     *
     * @param runnable the {@link Runnable} to be executed by the new thread
     * @return the newly created thread
     */
    @Override
    public Thread newThread(final Runnable runnable) {
        final Thread thread = getWrappedFactory().newThread(runnable);
        initializeThread(thread);
        return thread;
    }
}
