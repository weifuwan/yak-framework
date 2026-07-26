package io.yak.framework.security.common.entity;

import io.yak.framework.security.common.entity.BaseEntity;

public class Message extends BaseEntity {
  private String title;
  private String content;
  private Boolean readTag;
  private Integer userId;
  private Integer oplogId;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Message)) {
      return false;
    }
    Message other = (Message)o;
    if (!other.canEqual(this)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Boolean this$readTag = this.getReadTag();
    Boolean other$readTag = other.getReadTag();
    if (this$readTag == null ? other$readTag != null
                             : !((Object)this$readTag).equals(other$readTag)) {
      return false;
    }
    Integer this$userId = this.getUserId();
    Integer other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null
                            : !((Object)this$userId).equals(other$userId)) {
      return false;
    }
    Integer this$oplogId = this.getOplogId();
    Integer other$oplogId = other.getOplogId();
    if (this$oplogId == null ? other$oplogId != null
                             : !((Object)this$oplogId).equals(other$oplogId)) {
      return false;
    }
    String this$title = this.getTitle();
    String other$title = other.getTitle();
    if (this$title == null ? other$title != null
                           : !this$title.equals(other$title)) {
      return false;
    }
    String this$content = this.getContent();
    String other$content = other.getContent();
    return !(this$content == null ? other$content != null
                                  : !this$content.equals(other$content));
  }

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof Message;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Boolean $readTag = this.getReadTag();
    result =
        result * 59 + ($readTag == null ? 43 : ((Object)$readTag).hashCode());
    Integer $userId = this.getUserId();
    result =
        result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
    Integer $oplogId = this.getOplogId();
    result =
        result * 59 + ($oplogId == null ? 43 : ((Object)$oplogId).hashCode());
    String $title = this.getTitle();
    result = result * 59 + ($title == null ? 43 : $title.hashCode());
    String $content = this.getContent();
    result = result * 59 + ($content == null ? 43 : $content.hashCode());
    return result;
  }

  public String getTitle() { return this.title; }

  public String getContent() { return this.content; }

  public Boolean getReadTag() { return this.readTag; }

  public Integer getUserId() { return this.userId; }

  public Integer getOplogId() { return this.oplogId; }

  public void setTitle(String title) { this.title = title; }

  public void setContent(String content) { this.content = content; }

  public void setReadTag(Boolean readTag) { this.readTag = readTag; }

  public void setUserId(Integer userId) { this.userId = userId; }

  public void setOplogId(Integer oplogId) { this.oplogId = oplogId; }

  @Override
  public String toString() {
    return "Message(title=" + this.getTitle() +
        ", content=" + this.getContent() + ", readTag=" + this.getReadTag() +
        ", userId=" + this.getUserId() + ", oplogId=" + this.getOplogId() +
        ")";
  }
}
