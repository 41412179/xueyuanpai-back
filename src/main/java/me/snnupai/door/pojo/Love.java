package me.snnupai.door.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class Love implements Serializable {
    private Long id;

    private Long userId;

    /**
     * 0表示匿名,1表示不匿名
     */
    private Integer anonymous;

    private Date createdDate;

    private Date updatedDate;

    private Integer status;

    /**
     * 如果是匿名发布，则保留匿名名称
     */
    private String anonymousNickName;

    /**
     * 如果是匿名发布，保留匿名发布时的头像url
     */
    private String anonymousHeadUrl;

    private String content;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Integer anonymous) {
        this.anonymous = anonymous;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAnonymousNickName() {
        return anonymousNickName;
    }

    public void setAnonymousNickName(String anonymousNickName) {
        this.anonymousNickName = anonymousNickName;
    }

    public String getAnonymousHeadUrl() {
        return anonymousHeadUrl;
    }

    public void setAnonymousHeadUrl(String anonymousHeadUrl) {
        this.anonymousHeadUrl = anonymousHeadUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Love other = (Love) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getAnonymous() == null ? other.getAnonymous() == null : this.getAnonymous().equals(other.getAnonymous()))
            && (this.getCreatedDate() == null ? other.getCreatedDate() == null : this.getCreatedDate().equals(other.getCreatedDate()))
            && (this.getUpdatedDate() == null ? other.getUpdatedDate() == null : this.getUpdatedDate().equals(other.getUpdatedDate()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getAnonymousNickName() == null ? other.getAnonymousNickName() == null : this.getAnonymousNickName().equals(other.getAnonymousNickName()))
            && (this.getAnonymousHeadUrl() == null ? other.getAnonymousHeadUrl() == null : this.getAnonymousHeadUrl().equals(other.getAnonymousHeadUrl()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getAnonymous() == null) ? 0 : getAnonymous().hashCode());
        result = prime * result + ((getCreatedDate() == null) ? 0 : getCreatedDate().hashCode());
        result = prime * result + ((getUpdatedDate() == null) ? 0 : getUpdatedDate().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getAnonymousNickName() == null) ? 0 : getAnonymousNickName().hashCode());
        result = prime * result + ((getAnonymousHeadUrl() == null) ? 0 : getAnonymousHeadUrl().hashCode());
        result = prime * result + ((getContent() == null) ? 0 : getContent().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", anonymous=").append(anonymous);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", updatedDate=").append(updatedDate);
        sb.append(", status=").append(status);
        sb.append(", anonymousNickName=").append(anonymousNickName);
        sb.append(", anonymousHeadUrl=").append(anonymousHeadUrl);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}