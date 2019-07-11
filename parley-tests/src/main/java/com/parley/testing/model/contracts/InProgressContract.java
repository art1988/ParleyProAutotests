package com.parley.testing.model.contracts;

public class InProgressContract {
    private String link;
    private String title;
    private String counterparty;
    private String chiefNegotiator;
    private String stage;
    private Integer discussionCount;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getChiefNegotiator() {
        return chiefNegotiator;
    }

    public void setChiefNegotiator(String chiefNegotiator) {
        this.chiefNegotiator = chiefNegotiator;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Integer getDiscussionCount() {
        return discussionCount;
    }

    public void setDiscussionCount(Integer discussionCount) {
        this.discussionCount = discussionCount;
    }
}
