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

package com.lithium.community.android.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lithium.community.android.auth.LiAppCredentials;
import com.lithium.community.android.example.utils.MiscUtils;
import com.lithium.community.android.example.utils.ToastUtils;
import com.lithium.community.android.exception.LiInitializationException;
import com.lithium.community.android.manager.LiSDKManager;
import com.lithium.community.android.notification.FirebaseTokenProvider;


public class InitializationActivity extends AppCompatActivity {

    private ViewModel model;

    private View layoutSdkCredentialsError;
    private Button btnInitialize;
    private Button btnReset;
    private TextInputEditText fieldClientName;
    private TextInputEditText fieldClientId;
    private TextInputEditText fieldClientSecret;
    private TextInputEditText fieldTenantId;
    private TextInputEditText fieldCommunityUrl;
    private View tipRestartApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LiSDKManager.isInitialized()) {
            proceed();
            return;
        }
        setContentView(R.layout.activity_initialization);
        model = new ViewModel();
        setFieldsOfViews();
        updateViews();
    }

    private void setFieldsOfViews() {
        fieldClientName = findViewById(R.id.field_client_name);
        fieldClientId = findViewById(R.id.field_client_id);
        fieldClientSecret = findViewById(R.id.field_client_secret);
        fieldTenantId = findViewById(R.id.field_tenant_id);
        fieldCommunityUrl = findViewById(R.id.field_community_url);

        layoutSdkCredentialsError = findViewById(R.id.layout_sdk_credentials_error);
        tipRestartApp = findViewById(R.id.tip_restart_app);

        btnInitialize = findViewById(R.id.btn_initialize_sdk);
        btnReset = findViewById(R.id.btn_reset);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        btnInitialize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LiSDKManager.isInitialized()) {
                    proceed();
                } else {
                    initialize();
                }
            }
        });
    }

    private void updateViews() {
        fieldClientName.setText(model.getClientName());
        fieldClientId.setText(model.getClientId());
        fieldClientSecret.setText(model.getClientSecret());
        fieldTenantId.setText(model.getTenantId());
        fieldCommunityUrl.setText(model.getCommunityUrl());

        if (model.areCredentialsProvided()) {
            layoutSdkCredentialsError.setVisibility(View.INVISIBLE);
        } else {
            layoutSdkCredentialsError.setVisibility(View.VISIBLE);
        }

        if (model.isInitialized()) {
            btnInitialize.setText(R.string.action_proceed);
            tipRestartApp.setVisibility(View.VISIBLE);
            btnReset.setEnabled(false);
            fieldClientName.setEnabled(false);
            fieldClientId.setEnabled(false);
            fieldClientSecret.setEnabled(false);
            fieldTenantId.setEnabled(false);
            fieldCommunityUrl.setEnabled(false);
        } else {
            btnInitialize.setText(R.string.action_initialize);
            tipRestartApp.setVisibility(View.INVISIBLE);
            btnReset.setEnabled(true);
            fieldClientName.setEnabled(true);
            fieldClientId.setEnabled(true);
            fieldClientSecret.setEnabled(true);
            fieldTenantId.setEnabled(true);
            fieldCommunityUrl.setEnabled(true);
        }
    }

    private void reset() {
        model.reset();
        updateViews();
    }

    private void initialize() {
        model.update();
        if (model.areCredentialsProvided()) {
            try {
                LiAppCredentials credentials = new LiAppCredentials(
                        model.getClientName(),
                        model.getClientId(),
                        model.getClientSecret(),
                        model.getTenantId(),
                        model.getCommunityUrl(),
                        MiscUtils.getInstanceId(this)
                );
                LiSDKManager.initialize(this, credentials);

                LiSDKManager sdk = LiSDKManager.getInstance();
                sdk.setFirebaseTokenProvider(getFirebaseTokenProvider());
                sdk.syncWithCommunity(this);

                proceed();
            } catch (LiInitializationException e) {
                ToastUtils.initializationError(this, e.getMessage());
                Log.e(MainApplication.class.getSimpleName(), "Lithium SDK initialization failed.");
                e.printStackTrace();
            }
        }
        updateViews();
    }

    private void proceed() {
        startActivity(new Intent(this, DevLoginActivity.class));
        finish();
    }

    @Nullable
    protected FirebaseTokenProvider getFirebaseTokenProvider() {
        final String senderId = getString(R.string.li_sender_id);
        if (!TextUtils.isEmpty(senderId)) {
            return new ReferenceFirebaseTokenProvider(senderId);
        }
        return null;
    }

    class ViewModel {

        private String clientName;
        private String clientId;
        private String clientSecret;
        private String tenantId;
        private String communityUrl;

        ViewModel() {
            reset();
        }

        void reset() {
            // TODO: use credentials from shared preferences if available
            clientName = MiscUtils.sanitize(getString(R.string.li_client_name));
            clientId = MiscUtils.sanitize(getString(R.string.li_client_id));
            clientSecret = MiscUtils.sanitize(getString(R.string.li_client_secret));
            tenantId = MiscUtils.sanitize(getString(R.string.li_tenant_id));
            communityUrl = MiscUtils.sanitize(getString(R.string.li_community_url));
        }

        boolean areCredentialsProvided() {
            return !TextUtils.isEmpty(clientName) && !TextUtils.isEmpty(clientId) &&
                    !TextUtils.isEmpty(clientSecret) && !TextUtils.isEmpty(tenantId) &&
                    !TextUtils.isEmpty(communityUrl);
        }

        boolean isInitialized() {
            return LiSDKManager.isInitialized();
        }

        String getClientName() {
            return clientName;
        }

        String getClientId() {
            return clientId;
        }

        String getClientSecret() {
            return clientSecret;
        }

        String getTenantId() {
            return tenantId;
        }

        String getCommunityUrl() {
            return communityUrl;
        }

        void update() {
            clientName = MiscUtils.sanitize(fieldClientName.getText().toString());
            clientId = MiscUtils.sanitize(fieldClientId.getText().toString());
            clientSecret = MiscUtils.sanitize(fieldClientSecret.getText().toString());
            tenantId = MiscUtils.sanitize(fieldTenantId.getText().toString());
            communityUrl = MiscUtils.sanitize(fieldCommunityUrl.getText().toString());
        }
    }
}
