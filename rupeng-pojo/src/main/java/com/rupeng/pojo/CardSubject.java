package com.rupeng.pojo;

import java.io.Serializable;

//表示 学习卡——学科 关联关系
public class CardSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private Long cardId;//学习卡
    private Long subjectId;//学科
    private Integer seqNum;//序号(学科中的学习卡排序)

    public CardSubject() {

    }

    public CardSubject(Long cardId, Long subjectId) {
        this.cardId = cardId;
        this.subjectId = subjectId;
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
        CardSubject other = (CardSubject) obj;
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

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

}
