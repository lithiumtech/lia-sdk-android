/*
 * LiRanking.java
 * Created on Dec 27, 2016
 *
 * Copyright 2016 Lithium Technologies, Inc.
 * San Francisco, California, U.S.A.  All Rights Reserved.
 *
 * This software is the  confidential and proprietary information
 * of  Lithium  Technologies,  Inc.  ("Confidential Information")
 * You shall not disclose such Confidential Information and shall
 * use  it  only in  accordance  with  the terms of  the  license
 * agreement you entered into with Lithium.
 */

package lithium.community.android.sdk.model.helpers;

import com.google.gson.annotations.SerializedName;

import lithium.community.android.sdk.model.LiBaseModelImpl;

/**
 * A rank model represents a way to recognize and reward community members' achievements.
 */
public class LiRanking extends LiBaseModelImpl {
    private String href;
    private String name;
    private Long id;

    @SerializedName("display")
    private LiRankingDisplay lithiumRankingDisplay;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public LiBaseModelImpl.LiString getNameAsLithiumString() {
        final LiBaseModelImpl.LiString ret = new LiBaseModelImpl.LiString();
        ret.setValue(getName());
        return ret;
    }

    public String getName() {
        return name;
    }

    public void setName(LiBaseModelImpl.LiString name) {
        this.name = name.getValue();
    }

    public LiBaseModelImpl.LiInt getIdAsLithiumInt() {
        final LiBaseModelImpl.LiInt ret = new LiBaseModelImpl.LiInt();
        ret.setValue(getId());
        return ret;
    }

    public Long getId() {
        return id;
    }

    public void setId(LiBaseModelImpl.LiInt id) {
        this.id = id.getValue();
    }

    public LiRankingDisplay getLithiumRankingDisplay() {
        return lithiumRankingDisplay;
    }

    public void setLithiumRankingDisplay(LiRankingDisplay lithiumRankingDisplay) {
        this.lithiumRankingDisplay = lithiumRankingDisplay;
    }
}