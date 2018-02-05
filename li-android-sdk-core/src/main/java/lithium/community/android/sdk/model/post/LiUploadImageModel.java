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

package lithium.community.android.sdk.model.post;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import lithium.community.android.sdk.auth.LiAuthConstants;

/**
 * Request body for POST call to upload an image to the community.
 * Created by shoureya.kant on 11/29/16.
 */

public class LiUploadImageModel extends LiBasePostModel {

    private String type;
    private String title;
    private String field;
    private String visibility;
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toJsonString() {
        Gson gson = new Gson();
        Data data = new Data(this);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("request", data);
        } catch (JSONException e) {
            Log.e(LiAuthConstants.LOG_TAG, e.getMessage());
        }
        return gson.toJson(jsonObject);
    }
}
