package io.yak.framework.security.common.vo.message;

import lombok.Data;
/**
 * 消息视图对象。
 *
 * @author weifuwan
 */
@Data
public class MessageVO {
  /** 主键标识。 */
  private Long id;
  /** 消息标题。 */
  private String title;
  /** 消息内容。 */
  private String content;
  /** 是否已读。 */
  private Boolean readTag;
  /** 创建时间。 */
  private Long createTime;
  /** 关联的操作日志标识。 */
  private Long oplogId;

  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof MessageVO)) {
      return false;
    }
    MessageVO other = (MessageVO)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Long this$id = this.getId();
    Long other$id = other.getId();
    if (this$id == null ? other$id != null
                        : !((Object)this$id).equals(other$id)) {
      return false;
    }
    Boolean this$readTag = this.getReadTag();
    Boolean other$readTag = other.getReadTag();
    if (this$readTag == null ? other$readTag != null
                             : !((Object)this$readTag).equals(other$readTag)) {
      return false;
    }
    Long this$createTime = this.getCreateTime();
    Long other$createTime = other.getCreateTime();
    if (this$createTime == null
            ? other$createTime != null
            : !((Object)this$createTime).equals(other$createTime)) {
      return false;
    }
    Long this$oplogId = this.getOplogId();
    Long other$oplogId = other.getOplogId();
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

  protected boolean canEqual(Object other) {
    return other instanceof MessageVO;
  }

  public int hashCode() {
    int PRIME = 59;
    int result = 1;
    Long $id = this.getId();
    result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
    Boolean $readTag = this.getReadTag();
    result =
        result * 59 + ($readTag == null ? 43 : ((Object)$readTag).hashCode());
    Long $createTime = this.getCreateTime();
    result = result * 59 +
             ($createTime == null ? 43 : ((Object)$createTime).hashCode());
    Long $oplogId = this.getOplogId();
    result =
        result * 59 + ($oplogId == null ? 43 : ((Object)$oplogId).hashCode());
    String $title = this.getTitle();
    result = result * 59 + ($title == null ? 43 : $title.hashCode());
    String $content = this.getContent();
    result = result * 59 + ($content == null ? 43 : $content.hashCode());
    return result;
  }

  public String toString() {
    return "MessageVO(id=" + this.getId() + ", title=" + this.getTitle() +
        ", content=" + this.getContent() + ", readTag=" + this.getReadTag() +
        ", createTime=" + this.getCreateTime() +
        ", oplogId=" + this.getOplogId() + ")";
  }
}
