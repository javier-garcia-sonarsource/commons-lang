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

package org.apache.commons.lang3.mutable;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.time.DurationUtils;

/**
 * Prints out the difference time working with {@link MutableInt} and {@link AtomicInteger}.
 */
public class PrintAtomicVsMutable {

    public static void main(final String[] args) {
        final MutableInt mInt = new MutableInt();
        final int max = 100_000_000;
        System.out.println("MutableInt " + DurationUtils.of(() -> {
            for (int i = 0; i < max; i++) {
                mInt.incrementAndGet();
            }
        }));
        final AtomicInteger aInt = new AtomicInteger();
        System.out.println("AtomicInteger " + DurationUtils.of(() -> {
            for (int i = 0; i < max; i++) {
                aInt.incrementAndGet();
            }
        }));
    }
}
