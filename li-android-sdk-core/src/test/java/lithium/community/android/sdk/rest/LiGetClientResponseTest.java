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

package lithium.community.android.sdk.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import lithium.community.android.sdk.exception.LiRestResponseException;
import lithium.community.android.sdk.model.LiBaseModel;
import lithium.community.android.sdk.model.response.LiBrowse;

/**
 * Created by shoureya.kant on 12/6/16.
 */

public class LiGetClientResponseTest {

    Gson gson;
    JsonObject jsonObject = new Gson().fromJson("{\n" +
            "  \"status\" : \"success\",\n" +
            "  \"message\" : \"\",\n" +
            "  \"http_code\" : 200,\n" +
            "  \"data\" : {\n" +
            "    \"type\" : \"nodes\",\n" +
            "    \"list_item_type\" : \"node\",\n" +
            "    \"size\" : 1,\n" +
            "    \"items\" : [ {\n" +
            "      \"type\" : \"node\",\n" +
            "      \"id\" : \"board:scott\",\n" +
            "      \"href\" : \"/nodes/board:scott\",\n" +
            "      \"node_type\" : \"board\",\n" +
            "      \"conversation_style\" : \"blog\",\n" +
            "      \"title\" : \"Best Practice: the Community Blog\",\n" +
            "      \"short_title\" : \"BP\",\n" +
            "      \"description\" : \"Exploring interactions of organizations, individuals and ideas on the outer "
            + "edge of the enterprise.\",\n"
            +
            "      \"parent\" : {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"category:Blogs\",\n" +
            "        \"href\" : \"/nodes/category:Blogs\"\n" +
            "      },\n" +
            "      \"root_category\" : {\n" +
            "        \"type\" : \"node\",\n" +
            "        \"id\" : \"category:Archive\",\n" +
            "        \"href\" : \"/nodes/category:Archive\"\n" +
            "      },\n" +
            "      \"ancestors\" : {\n" +
            "        \"query\" : \"SELECT * FROM nodes WHERE ancestors.id = 'board:scott'\"\n" +
            "      },\n" +
            "      \"depth\" : 3,\n" +
            "      \"position\" : 2,\n" +
            "      \"hidden\" : false,\n" +
            "      \"views\" : 0\n" +
            "    } ]\n" +
            "  },\n  \"metadata\" : { }\n" +
            "}", JsonObject.class);
    private LiBaseResponse liBaseResponse;
    private String type;
    private Class<? extends LiBaseModel> baseModelClass;
    private LiGetClientResponse liGetClientResponse;

    @Before
    public void setUp() throws LiRestResponseException {
        gson = LiRestv2Client.getInstance().getGson();
    }

    @Test
    public void testGetResponse() throws LiRestResponseException {
        liBaseResponse = new LiBaseResponse();
        liBaseResponse.setData(jsonObject);
        type = "node";
        baseModelClass = LiBrowse.class;
        liGetClientResponse = new LiGetClientResponse(liBaseResponse, type, baseModelClass, gson);
        Assert.assertEquals(1, liGetClientResponse.getResponse().size());
    }

    @Test
    public void testException() throws LiRestResponseException {
        liBaseResponse = new LiBaseResponse();
        liBaseResponse.setData(jsonObject);
        type = "anything";
        baseModelClass = LiBrowse.class;
        LiGetClientResponse liGetClientResponse1 = new LiGetClientResponse(liBaseResponse, type, baseModelClass, gson);
        List<LiBaseModel> model = liGetClientResponse1.getResponse();
        Assert.assertNull(model);
    }

    @Test
    public void testMapTransformation() throws LiRestResponseException {
        liBaseResponse = new LiBaseResponse();
        liBaseResponse.setData(jsonObject);
        type = "node";
        baseModelClass = LiBrowse.class;
        LiGetClientResponse liGetClientResponse2 = new LiGetClientResponse(liBaseResponse, type, baseModelClass, gson);
        Assert.assertEquals(1, liGetClientResponse2.getTransformedResponse().size());
    }

}
