/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Note: This file copied from the Android CTS Tests
 */
package nc.opt.mobile.optmobile;

import junit.framework.Assert;

abstract class PollingCheck {
    private static final long TIME_SLICE = 70;
    private long mTimeout = 4000;

    public PollingCheck() {
    }

    PollingCheck(long timeout) {
        mTimeout = timeout;
    }

    protected abstract boolean check();

    void run() {
        if (check()) {
            return;
        }

        long timeout = mTimeout;
        while (timeout > 0) {

            if (check()) {
                return;
            }

            timeout -= TIME_SLICE;
        }

        Assert.fail("unexpected timeout");
    }
}
