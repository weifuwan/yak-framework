package io.yak.framework.security.common.dto.message;

public class MessageDTO {
  private String title;
  private String content;
  private Integer oplogId;
  private Integer userId;

  public MessageDTO(Integer userId, Integer oplogId) {
    this.userId = userId;
    this.oplogId = oplogId;
  }

  public String getTitle() { return this.title; }

  public String getContent() { return this.content; }

  public Integer getOplogId() { return this.oplogId; }

  public Integer getUserId() { return this.userId; }

  public void setTitle(String title) { this.title = title; }

  public void setContent(String content) { this.content = content; }

  public void setOplogId(Integer oplogId) { this.oplogId = oplogId; }

  public void setUserId(Integer userId) { this.userId = userId; }

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof MessageDTO)) {
      return false;
    }
    MessageDTO other = (MessageDTO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Integer this$oplogId = this.getOplogId();
    Integer other$oplogId = other.getOplogId();
    if (this$oplogId == null ? other$oplogId != null
                             : !((Object)this$oplogId).equals(other$oplogId)) {
      return false;
    }
    Integer this$userId = this.getUserId();
    Integer other$userId = other.getUserId();
    if (this$userId == null ? other$userId != null
                            : !((Object)this$userId).equals(other$userId)) {
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

  protected boolean canEqual(Object other) {
    return other instanceof MessageDTO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Integer $oplogId = this.getOplogId();
    result =
        result * 59 + ($oplogId == null ? 43 : ((Object)$oplogId).hashCode());
    Integer $userId = this.getUserId();
    result =
        result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
    String $title = this.getTitle();
    result = result * 59 + ($title == null ? 43 : $title.hashCode());
    String $content = this.getContent();
    result = result * 59 + ($content == null ? 43 : $content.hashCode());
    return result;
  }

  public String toString() {
    return "MessageDTO(title=" + this.getTitle() +
        ", content=" + this.getContent() + ", oplogId=" + this.getOplogId() +
        ", userId=" + this.getUserId() + ")";
  }
}
