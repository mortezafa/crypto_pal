package org.project.cryptopal.respones;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.project.cryptopal.model.Asset;

import java.util.List;

public class GetAssetsResponse {
    @JsonProperty("code")
    private String status;

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private List<Asset> result;

    @JsonProperty("next_page")
    private Integer nextPage;
    @JsonProperty("count")
    private int count;


    public Integer getNextPage() {
        return nextPage;
    }
    public String getStatus() {
        return status;
    }

    public int getCount() {
        return count;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Asset> getResult() {
        return result;
    }

    public void setResult(List<Asset> result) {
        this.result = result;
    }
}
