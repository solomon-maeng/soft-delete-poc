package com.rebwon.softdeletepoc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "comment")
@SQLDelete(sql = "update comment set deleted = true where id = ?")
@Where(clause = "deleted = false")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    protected Comment() {}

    public Comment(String content) {
        this.content = content;
    }
}
