/*
 * Copyright 2018 Lithium Technologies Pvt Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lithium.community.android.manager;

import android.content.Context;

import com.lithium.community.android.manager.LiSecuredPrefManager;
import com.lithium.community.android.TestHelper;
import com.lithium.community.android.exception.LiInitializationException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.core.classloader.annotations.PowerMockIgnore;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author adityasharat
 */
@PowerMockIgnore({"javax.crypto.*"})
public class LiSecuredPrefManagerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Context context;

    @Before
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field instance = LiSecuredPrefManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        Field isInitialized = LiSecuredPrefManager.class.getDeclaredField("isInitialized");
        isInitialized.setAccessible(true);
        isInitialized.set(null, new AtomicBoolean(false));
        context = TestHelper.createMockContext();
    }

    @Test
    public void initialize() throws Exception {
        LiSecuredPrefManager.initialize(UUID.randomUUID().toString());
        Assert.assertNotNull(LiSecuredPrefManager.getInstance());
    }

    @Test
    public void initialize_null() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        //noinspection ConstantConditions for test
        LiSecuredPrefManager.initialize(null);
    }

    @Test
    public void init() throws LiInitializationException {
        LiSecuredPrefManager.initialize("qwerty");
        Assert.assertNotNull(LiSecuredPrefManager.getInstance());
    }

    @Test
    public void init_null() {
        thrown.expect(IllegalArgumentException.class);
        //noinspection ConstantConditions for test
        LiSecuredPrefManager instance = LiSecuredPrefManager.init(null);
        Assert.assertNotNull(instance);
    }

    @Test
    public void getString() throws Exception {
        LiSecuredPrefManager.initialize(UUID.randomUUID().toString());
        LiSecuredPrefManager instance = LiSecuredPrefManager.getInstance();
        assert instance != null;
        String string = instance.getString(context, "test");
        Assert.assertNull(string);
    }

    @Test
    public void getString_null_context() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        LiSecuredPrefManager.initialize("ThisIsASecretKey");
        LiSecuredPrefManager instance = LiSecuredPrefManager.getInstance();
        assert instance != null;
        //noinspection ConstantConditions for test
        instance.getString(null, "test");
    }

    @Test
    public void getString_null_key() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        LiSecuredPrefManager.initialize(UUID.randomUUID().toString());
        LiSecuredPrefManager instance = LiSecuredPrefManager.getInstance();
        assert instance != null;
        //noinspection ConstantConditions for test
        instance.getString(context, null);
    }

    @Test
    public void getString_null_context_null_key() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        LiSecuredPrefManager.initialize(UUID.randomUUID().toString());
        LiSecuredPrefManager instance = LiSecuredPrefManager.getInstance();
        assert instance != null;
        //noinspection ConstantConditions for test
        instance.getString(null, null);
    }

    @Test
    public void putString_getString() throws Exception {
        LiSecuredPrefManager.initialize(UUID.randomUUID().toString());
        LiSecuredPrefManager instance = LiSecuredPrefManager.getInstance();
        assert instance != null;
        String key = "test";
        String value = "value";
        instance.putString(context, key, value);
        String string = instance.getString(context, key);
        Assert.assertEquals(string, value);
    }

    @Test
    public void remove() throws Exception {
        LiSecuredPrefManager.initialize(UUID.randomUUID().toString());
        LiSecuredPrefManager instance = LiSecuredPrefManager.getInstance();
        assert instance != null;
        String key = "test";
        String value = "value";
        instance.putString(context, key, value);
        instance.remove(context, key);
        String string = instance.getString(context, key);
        Assert.assertNull(string);
    }

    @Test
    public void remove_null_context() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        LiSecuredPrefManager.initialize(UUID.randomUUID().toString());
        LiSecuredPrefManager instance = LiSecuredPrefManager.getInstance();
        assert instance != null;
        String key = "test";
        String value = "value";
        instance.putString(context, key, value);
        //noinspection ConstantConditions for test
        instance.remove(null, key);
    }

    @Test
    public void remove_null_key() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        LiSecuredPrefManager.initialize(UUID.randomUUID().toString());
        LiSecuredPrefManager instance = LiSecuredPrefManager.getInstance();
        assert instance != null;
        String key = "test";
        String value = "value";
        instance.putString(context, key, value);
        //noinspection ConstantConditions for test
        instance.remove(context, null);
    }

    @Test
    public void remove_null_context_null_key() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        LiSecuredPrefManager.initialize(UUID.randomUUID().toString());
        LiSecuredPrefManager instance = LiSecuredPrefManager.getInstance();
        assert instance != null;
        String key = "test";
        String value = "value";
        instance.putString(context, key, value);
        //noinspection ConstantConditions for test
        instance.remove(null, null);
    }

    @Test
    public void encrypt() throws Exception {
    }

    @Test
    public void decrypt() throws Exception {
    }

}