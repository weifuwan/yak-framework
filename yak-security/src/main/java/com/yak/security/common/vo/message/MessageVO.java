/*
 * Decompiled with CFR 0.153-SNAPSHOT (a3c0321).
 * 
 * Could not load the following classes:
 *  io.swagger.annotations.ApiModel
 *  io.swagger.annotations.ApiModelProperty
 */
package com.yak.security.common.vo.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description="\u6d88\u606f\u4e2d\u5fc3\u4fe1\u606f")
public class MessageVO {
    @ApiModelProperty(value="\u6d88\u606fid", dataType="Integer", required=false)
    private Integer id;
    @ApiModelProperty(value="\u6807\u9898", dataType="String", required=false)
    private String title;
    @ApiModelProperty(value="\u5185\u5bb9\u4fe1\u606f", dataType="String", required=false)
    private String content;
    @ApiModelProperty(value="\u662f\u5426\u5df2\u8bfb", dataType="Boolean", required=false)
    private Boolean readTag;
    @ApiModelProperty(value="\u521b\u5efa\u65f6\u95f4", dataType="Long", required=false)
    private Long createTime;
    @ApiModelProperty(value="\u64cd\u4f5c\u65e5\u5fd7id", dataType="Integer", required=false)
    private Integer oplogId;

    public Integer getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public Boolean getReadTag() {
        return this.readTag;
    }

    public Long getCreateTime() {
        return this.createTime;
    }

    public Integer getOplogId() {
        return this.oplogId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReadTag(Boolean readTag) {
        this.readTag = readTag;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setOplogId(Integer oplogId) {
        this.oplogId = oplogId;
    }

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
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Boolean this$readTag = this.getReadTag();
        Boolean other$readTag = other.getReadTag();
        if (this$readTag == null ? other$readTag != null : !((Object)this$readTag).equals(other$readTag)) {
            return false;
        }
        Long this$createTime = this.getCreateTime();
        Long other$createTime = other.getCreateTime();
        if (this$createTime == null ? other$createTime != null : !((Object)this$createTime).equals(other$createTime)) {
            return false;
        }
        Integer this$oplogId = this.getOplogId();
        Integer other$oplogId = other.getOplogId();
        if (this$oplogId == null ? other$oplogId != null : !((Object)this$oplogId).equals(other$oplogId)) {
            return false;
        }
        String this$title = this.getTitle();
        String other$title = other.getTitle();
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) {
            return false;
        }
        String this$content = this.getContent();
        String other$content = other.getContent();
        return !(this$content == null ? other$content != null : !this$content.equals(other$content));
    }

    protected boolean canEqual(Object other) {
        return other instanceof MessageVO;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Boolean $readTag = this.getReadTag();
        result = result * 59 + ($readTag == null ? 43 : ((Object)$readTag).hashCode());
        Long $createTime = this.getCreateTime();
        result = result * 59 + ($createTime == null ? 43 : ((Object)$createTime).hashCode());
        Integer $oplogId = this.getOplogId();
        result = result * 59 + ($oplogId == null ? 43 : ((Object)$oplogId).hashCode());
        String $title = this.getTitle();
        result = result * 59 + ($title == null ? 43 : $title.hashCode());
        String $content = this.getContent();
        result = result * 59 + ($content == null ? 43 : $content.hashCode());
        return result;
    }

    public String toString() {
        return "MessageVO(id=" + this.getId() + ", title=" + this.getTitle() + ", content=" + this.getContent() + ", readTag=" + this.getReadTag() + ", createTime=" + this.getCreateTime() + ", oplogId=" + this.getOplogId() + ")";
    }
}

