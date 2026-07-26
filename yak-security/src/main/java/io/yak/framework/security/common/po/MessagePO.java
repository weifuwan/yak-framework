package io.yak.framework.security.common.po;

import com.baomidou.mybatisplus.annotation.TableName;
import io.yak.framework.security.common.po.BasePO;

@TableName(value = "yak_security_message")
public class MessagePO extends BasePO {
  private String title;
  private String content;
  private Boolean readTag;
  private Long oplogId;
  private Long userId;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof MessagePO)) {
      return false;
    }
    MessagePO other = (MessagePO)o;
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
    Long this$oplogId = this.getOplogId();
    Long other$oplogId = other.getOplogId();
    if (this$oplogId == null ? other$oplogId != null
                             : !((Object)this$oplogId).equals(other$oplogId)) {
      return false;
    }
    Long this$userId = this.getUserId();
    Long other$userId = other.getUserId();
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

  @Override
  protected boolean canEqual(Object other) {
    return other instanceof MessagePO;
  }

  @Override
  public int hashCode() {
    int PRIME = 59;
    int result = super.hashCode();
    Boolean $readTag = this.getReadTag();
    result =
        result * 59 + ($readTag == null ? 43 : ((Object)$readTag).hashCode());
    Long $oplogId = this.getOplogId();
    result =
        result * 59 + ($oplogId == null ? 43 : ((Object)$oplogId).hashCode());
    Long $userId = this.getUserId();
    result =
        result * 59 + ($userId == null ? 43 : ((Object)$userId).hashCode());
    String $title = this.getTitle();
    result = result * 59 + ($title == null ? 43 : $title.hashCode());
    String $content = this.getContent();
    result = result * 59 + ($content == null ? 43 : $content.hashCode());
    return result;
  }

  public String getTitle() { return this.title; }

  public String getContent() { return this.content; }

  public Boolean getReadTag() { return this.readTag; }

  public Long getOplogId() { return this.oplogId; }

  public Long getUserId() { return this.userId; }

  public void setTitle(String title) { this.title = title; }

  public void setContent(String content) { this.content = content; }

  public void setReadTag(Boolean readTag) { this.readTag = readTag; }

  public void setOplogId(Long oplogId) { this.oplogId = oplogId; }

  public void setUserId(Long userId) { this.userId = userId; }

  @Override
  public String toString() {
    return "MessagePO(title=" + this.getTitle() +
        ", content=" + this.getContent() + ", readTag=" + this.getReadTag() +
        ", oplogId=" + this.getOplogId() + ", userId=" + this.getUserId() +
        ")";
  }
}
