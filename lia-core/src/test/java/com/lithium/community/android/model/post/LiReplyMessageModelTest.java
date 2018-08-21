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

package com.lithium.community.android.model.post;

import org.junit.Test;

import com.lithium.community.android.model.LiBaseModelImpl;
import com.lithium.community.android.model.post.LiReplyMessageModel;
import com.lithium.community.android.model.response.LiMessage;

import static org.junit.Assert.assertEquals;

/**
 * Created by shoureya.kant on 12/2/16.
 */

public class LiReplyMessageModelTest {

    private final String type = "reply_message";
    private final String body = "this is reply";
    private LiMessage parent = new LiMessage();
    private LiReplyMessageModel liReplyMessageModel = new LiReplyMessageModel();

    @Test
    public void getTypeTest() {
        liReplyMessageModel.setType(type);
        assertEquals(type, liReplyMessageModel.getType());
    }

    @Test
    public void getBodyTest() {
        liReplyMessageModel.setBody(body);
        assertEquals(body, liReplyMessageModel.getBody());
    }

    @Test
    public void getParentTest() {
        parent.setId((long)100);
        liReplyMessageModel.setParent(parent);
        assertEquals(parent, liReplyMessageModel.getParent());
    }
}
