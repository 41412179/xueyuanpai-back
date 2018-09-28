package me.snnupai.door.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 
 */
public class Trade implements Serializable {
    /**
     * trade_id
     */
    private String id;

    /**
     * 是否被发布者删除
     */
    private Integer delFlag;

    /**
     * 出售状态，0表示未出售，1表示已出售，2表示已过期
     */
    private Integer status;

    private String title;

    /**
     * 发布信息人的id
     */
    private Long ownerId;

    /**
     * 0表示长安校区，1表示雁塔校区
     */
    private Integer xiaoqu;

    /**
     * 0表示匿名,1表示不匿名
     */
    private Integer anonymous;

    /**
     * qq
     */
    private String qq;

    /**
     * weixin
     */
    private String weixin;

    /**
     * 联系人
     */
    private String contacter;

    private Date createdDate;

    private Integer price;

    private Date updatedDate;

    /**
     * 匿名时发布者的昵称
     */
    private String anonymousNickName;

    /**
     * 匿名时的头像url
     */
    private String anonymousHeadUrl;

    /**
     * 交易内容
     */
    private String content;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getXiaoqu() {
        return xiaoqu;
    }

    public void setXiaoqu(Integer xiaoqu) {
        this.xiaoqu = xiaoqu;
    }

    public Integer getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Integer anonymous) {
        this.anonymous = anonymous;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getContacter() {
        return contacter;
    }

    public void setContacter(String contacter) {
        this.contacter = contacter;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
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
        Trade other = (Trade) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getDelFlag() == null ? other.getDelFlag() == null : this.getDelFlag().equals(other.getDelFlag()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getTitle() == null ? other.getTitle() == null : this.getTitle().equals(other.getTitle()))
            && (this.getOwnerId() == null ? other.getOwnerId() == null : this.getOwnerId().equals(other.getOwnerId()))
            && (this.getXiaoqu() == null ? other.getXiaoqu() == null : this.getXiaoqu().equals(other.getXiaoqu()))
            && (this.getAnonymous() == null ? other.getAnonymous() == null : this.getAnonymous().equals(other.getAnonymous()))
            && (this.getQq() == null ? other.getQq() == null : this.getQq().equals(other.getQq()))
            && (this.getWeixin() == null ? other.getWeixin() == null : this.getWeixin().equals(other.getWeixin()))
            && (this.getContacter() == null ? other.getContacter() == null : this.getContacter().equals(other.getContacter()))
            && (this.getCreatedDate() == null ? other.getCreatedDate() == null : this.getCreatedDate().equals(other.getCreatedDate()))
            && (this.getPrice() == null ? other.getPrice() == null : this.getPrice().equals(other.getPrice()))
            && (this.getUpdatedDate() == null ? other.getUpdatedDate() == null : this.getUpdatedDate().equals(other.getUpdatedDate()))
            && (this.getAnonymousNickName() == null ? other.getAnonymousNickName() == null : this.getAnonymousNickName().equals(other.getAnonymousNickName()))
            && (this.getAnonymousHeadUrl() == null ? other.getAnonymousHeadUrl() == null : this.getAnonymousHeadUrl().equals(other.getAnonymousHeadUrl()))
            && (this.getContent() == null ? other.getContent() == null : this.getContent().equals(other.getContent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getDelFlag() == null) ? 0 : getDelFlag().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getTitle() == null) ? 0 : getTitle().hashCode());
        result = prime * result + ((getOwnerId() == null) ? 0 : getOwnerId().hashCode());
        result = prime * result + ((getXiaoqu() == null) ? 0 : getXiaoqu().hashCode());
        result = prime * result + ((getAnonymous() == null) ? 0 : getAnonymous().hashCode());
        result = prime * result + ((getQq() == null) ? 0 : getQq().hashCode());
        result = prime * result + ((getWeixin() == null) ? 0 : getWeixin().hashCode());
        result = prime * result + ((getContacter() == null) ? 0 : getContacter().hashCode());
        result = prime * result + ((getCreatedDate() == null) ? 0 : getCreatedDate().hashCode());
        result = prime * result + ((getPrice() == null) ? 0 : getPrice().hashCode());
        result = prime * result + ((getUpdatedDate() == null) ? 0 : getUpdatedDate().hashCode());
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
        sb.append(", delFlag=").append(delFlag);
        sb.append(", status=").append(status);
        sb.append(", title=").append(title);
        sb.append(", ownerId=").append(ownerId);
        sb.append(", xiaoqu=").append(xiaoqu);
        sb.append(", anonymous=").append(anonymous);
        sb.append(", qq=").append(qq);
        sb.append(", weixin=").append(weixin);
        sb.append(", contacter=").append(contacter);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", price=").append(price);
        sb.append(", updatedDate=").append(updatedDate);
        sb.append(", anonymousNickName=").append(anonymousNickName);
        sb.append(", anonymousHeadUrl=").append(anonymousHeadUrl);
        sb.append(", content=").append(content);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}