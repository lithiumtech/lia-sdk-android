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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lithium.community.android.manager.LiClientManager;
import com.lithium.community.android.manager.LiSDKManager;
import com.lithium.community.android.TestHelper;
import com.lithium.community.android.api.LiClient;
import com.lithium.community.android.exception.LiRestResponseException;
import com.lithium.community.android.model.post.LiPostLogoutModel;
import com.lithium.community.android.model.post.LiSubscriptionPostModel;
import com.lithium.community.android.model.request.LiClientRequestParams;
import com.lithium.community.android.rest.LiBaseResponse;
import com.lithium.community.android.rest.LiRestV2Request;
import com.lithium.community.android.rest.LiRestv2Client;

import static org.mockito.Mockito.mock;

/**
 * @author kunal.shrivastava
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.crypto.*"})
@PrepareForTest(LiRestv2Client.class)
public class LiClientManagerTest {

    private static final String LI_ARTICLES_CLIENT_TYPE = "message";
    private static final String LI_ARTICLES_BROWSE_CLIENT_TYPE = "message";
    private static final String GET = "GET";
    private static final String DELETE = "DELETE";
    private static final String PUT = "PUT";

    @Mock
    private Context mContext;
    private LiRestv2Client liRestv2Client;

    @Before
    public void setUpTest() throws Exception {
        MockitoAnnotations.initMocks(this);

        mContext = TestHelper.createMockContext();
        LiSDKManager.init(mContext, TestHelper.getTestAppCredentials());
        liRestv2Client = PowerMockito.mock(LiRestv2Client.class);
        PowerMockito.mockStatic(LiRestv2Client.class);
        BDDMockito.given(LiRestv2Client.getInstance()).willReturn(liRestv2Client);
    }

    @Test
    public void testGetMessagesClient() {
        LiClientRequestParams liClientRequestParams = new LiClientRequestParams.LiMessagesClientRequestParams(mContext);
        LiClient liClient = null;
        try {
            liClient = LiClientManager.getMessagesClient(liClientRequestParams);
            liClient.processSync();
        } catch (LiRestResponseException e) {
        }

        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_ARTICLES_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testGetMessagesByBoardIdClient() {
        LiClientRequestParams liClientRequestParams = new LiClientRequestParams.LiMessagesByBoardIdClientRequestParams(
                mContext, "1");
        LiClient liClient = null;
        LiRestV2Request liRestV2Request = mock(LiRestV2Request.class);
        LiBaseResponse liBaseResponse = new LiBaseResponse();
        try {
            PowerMockito.when(liRestv2Client.processSync(liRestV2Request)).thenReturn(liBaseResponse);
            liClient = LiClientManager.getMessagesByBoardIdClient(liClientRequestParams);
            liClient.processSync();
        } catch (LiRestResponseException e) {
        }

        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_ARTICLES_BROWSE_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testDeleteMessageClient() {
        String id = "33";
        LiClientRequestParams liClientRequestParams = new LiClientRequestParams.LiMessageDeleteClientRequestParams(
                mContext, id);
        LiClient liClient = null;
        try {
            liClient = LiClientManager.getMessageDeleteClient(liClientRequestParams);
            liClient.processSync();
        } catch (LiRestResponseException e) {
        }
        PowerMockito.verifyStatic();
        Assert.assertNull(liClient.getType());
        Assert.assertEquals(DELETE, "" + liClient.getRequestType());
    }

    @Test
    public void testUpdateMessageClient() {
        String messageId = "33";
        String subject = "Test";
        String body = "This is test";
        LiClientRequestParams liClientRequestParams = new LiClientRequestParams.LiUpdateMessageClientRequestParams(
                mContext, messageId, subject, body);
        LiClient liClient = null;
        try {
            liClient = LiClientManager.getUpdateMessageClient(liClientRequestParams);
            liClient.processSync();
        } catch (LiRestResponseException e) {
        }

        PowerMockito.verifyStatic();
        Assert.assertNull(liClient.getType());
        Assert.assertEquals(PUT, "" + liClient.getRequestType());
    }

    @Test
    public void testPostSubscriptionClient(){
        LiSubscriptionPostModel.Target target = new LiSubscriptionPostModel.MessageTarget("message:hello");
        LiClientRequestParams.LiPostSubscriptionParams params = new LiClientRequestParams.LiPostSubscriptionParams(mContext, target);
        LiClient liClient = null;
        try {
            liClient = LiClientManager.getSubscriptionPostClient(params);
            liClient.processSync();
        }catch (LiRestResponseException lrre){
        }
        PowerMockito.verifyStatic();
        Assert.assertNotNull(liClient);
    }

    @Test
    public void testLogout(){
        String deviceId = "device-id-13413233";
        LiClientRequestParams.LiLogoutRequestParams params = new LiClientRequestParams.LiLogoutRequestParams(mContext, deviceId);
        Assert.assertNotNull(params);
        Assert.assertNotNull(params.getContext());
        Assert.assertNotNull(params.getDeviceId());
        Assert.assertEquals(deviceId, params.getDeviceId());
        LiClient client = null;
        try{
            client = LiClientManager.getLogoutClient(params);
            client.processSync();
        }catch (LiRestResponseException lrre){
            lrre.printStackTrace();
        }
        PowerMockito.verifyStatic();
    }
/*
    @Test
    public void testGetSubscriptionClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getUserSubscriptionsClient();
        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_SUBSCRIPTIONS_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testGetBrowseClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getCategoryBoardsClient("1");
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_BROWSE_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testGetMessageChildrenClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getRepliesClient(new Long("1"));
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_MESSAGE_CHILDREN_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testGetSearchClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getSearchClient("test");
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_SEARCH_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testgetQuestionsClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getUserMessagesClient(new Long("1"), "test");
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_QUESTIONS_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testGetCategoryClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getCategoryClient();
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_CATEGORY_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testGetUserDetailsClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getUserDetailsClient("test");
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_USER_DETAILS_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testGetMessageClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getMessageClient(new Long("1"));
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_MESSAGE_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testGetMessagesByIdsClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getMessagesByIdsClient(new HashSet<String>(Arrays.asList("a", "b")));
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_MESSAGE_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testGetKudoClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getKudoClient("1");
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(null, liClient.getType());
        Assert.assertEquals(POST, "" + liClient.getRequestType());
    }

    @Test
    public void testGetAcceptSolutionClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getAcceptSolutionClient(new Long("1"));
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(null, liClient.getType());
        Assert.assertEquals(POST, "" + liClient.getRequestType());
    }

    @Test
    public void testGetPostQuestionClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getCreateMessageClient("test", "test", "test", "test", "test");
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(null, liClient.getType());
        Assert.assertEquals(POST, "" + liClient.getRequestType());
    }

    @Test
    public void testGetReplyMessageClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getCreateReplyClient("test", new Long("1"), null, "");
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(null, liClient.getType());
        Assert.assertEquals(POST, "" + liClient.getRequestType());
    }

    @Test
    public void testGetMarkAbuseClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getMarkAbuseClient("1", "1", "test");
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(null, liClient.getType());
        Assert.assertEquals(POST, "" + liClient.getRequestType());
    }

    @Test
    public void testGenericPostClient() throws LiRestResponseException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("test_v", "test_p");
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getGenericPostClient("test_path", requestBody);
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(null, liClient.getType());
        Assert.assertEquals(POST, "" + liClient.getRequestType());
    }

    @Test
    public void testGenericGetClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getGenericWithLiqlGetClient("test query");
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(null, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testGetSdkSettingsClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getSdkSettingsClient("clientId");
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(LI_SDK_SETTINGS_CLIENT_TYPE, liClient.getType());
        Assert.assertEquals(GET, "" + liClient.getRequestType());
    }

    @Test
    public void testUploadImageClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiClient liClient = instance.getUploadImageClient("test", "test", "test", "test");
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals(null, liClient.getType());
        Assert.assertEquals(POST, "" + liClient.getRequestType());
    }

    @Test
    public void testGetClient() throws LiRestResponseException {
        LiClientManager instance = liClientManger.getInstance();
        LiQueryRequestParams.Builder builder = LiQueryRequestParams.getBuilder();
        builder.setClient(LiClientManager.Client.LI_ARTICLES_CLIENT);
        builder.setLimit(10);
        LiQueryOrdering liQueryOrdering = new LiQueryOrdering(LiQueryOrdering.Articles.POST_TIME, LiQueryOrdering
        .Order.ASC);
        LiQueryClause liQueryClause = new LiQueryClause(LiQuerySetting.LiWhereClause.EQUALS, LiQueryWhereClause
        .Articles.DEPTH, "1", "AND");
        LiQueryWhereClause liQueryWhereClause = new LiQueryWhereClause();
        liQueryWhereClause.addClause(liQueryClause);
        builder.setLiQueryOrdering(liQueryOrdering);
        builder.setLiQueryWhereClause(liQueryWhereClause);
        LiQueryRequestParams liQueryRequestParams = builder.build();
        LiClient liClient = instance.getGenericWithLiqlGetClient(liQueryRequestParams);
        liClient.processSync();
        PowerMockito.verifyStatic();
        Assert.assertEquals("message", liClient.getType());
    }*/

}
