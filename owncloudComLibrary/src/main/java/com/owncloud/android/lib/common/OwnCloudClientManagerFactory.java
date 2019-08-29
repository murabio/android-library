/* ownCloud Android Library is available under MIT license
 *   Copyright (C) 2019 ownCloud GmbH.
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *   EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 *   MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *   NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 *   BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 *   ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 *   CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 *
 */
package com.owncloud.android.lib.common;

public class OwnCloudClientManagerFactory {

    private static Policy sDefaultPolicy = Policy.ALWAYS_NEW_CLIENT;
    private static OwnCloudClientManager sDefaultSingleton;
    private static String sUserAgent;

    private static OwnCloudClientManager newDefaultOwnCloudClientManager() {
        return newOwnCloudClientManager(sDefaultPolicy);
    }

    private static OwnCloudClientManager newOwnCloudClientManager(Policy policy) {
        switch (policy) {
            case ALWAYS_NEW_CLIENT:
                return new SimpleFactoryManager();

            case SINGLE_SESSION_PER_ACCOUNT:
                return new SingleSessionManager();

            default:
                throw new IllegalArgumentException("Unknown policy");
        }
    }

    public static OwnCloudClientManager getDefaultSingleton() {
        if (sDefaultSingleton == null) {
            sDefaultSingleton = newDefaultOwnCloudClientManager();
        }
        return sDefaultSingleton;
    }

    public static void setDefaultPolicy(Policy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Default policy cannot be NULL");
        }
        if (defaultSingletonMustBeUpdated(policy)) {
            sDefaultSingleton = null;
        }
        sDefaultPolicy = policy;
    }

    public static String getUserAgent() {
        return sUserAgent;
    }

    public static void setUserAgent(String userAgent) {
        sUserAgent = userAgent;
    }

    private static boolean defaultSingletonMustBeUpdated(Policy policy) {
        if (sDefaultSingleton == null) {
            return false;
        }
        return policy == Policy.ALWAYS_NEW_CLIENT &&
                !(sDefaultSingleton instanceof SimpleFactoryManager);
    }

    public enum Policy {
        ALWAYS_NEW_CLIENT,
        SINGLE_SESSION_PER_ACCOUNT
    }
}
