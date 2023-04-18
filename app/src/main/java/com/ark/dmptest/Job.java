package com.ark.dmptest;

import com.google.gson.annotations.SerializedName;

public class Job {
    @SerializedName("id")
    public String id;
    @SerializedName("type")
    public String type;
    @SerializedName("url")
    public String url;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("company")
    public String company;
    @SerializedName("company_url")
    public String companyUrl;
    @SerializedName("location")
    public String location;
    @SerializedName("title")
    public String title;
    @SerializedName("description")
    public String description;
    @SerializedName("how_to_apply")
    public String howToApply;
    @SerializedName("company_logo")
    public String companyLogo;
}
