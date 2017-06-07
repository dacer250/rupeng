package com.rupeng.pojo;

import java.io.Serializable;
import java.util.Date;

//表示学生的学习记录
public class UserSegment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;//学生
    private Long segmentId;//学习卡
    private Date createTime;//学习时间

    public UserSegment() {

    }

    public UserSegment(Long userId, Long segmentId) {
        super();
        this.userId = userId;
        this.segmentId = segmentId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserSegment other = (UserSegment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

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

    public Long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(Long segmentId) {
        this.segmentId = segmentId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
