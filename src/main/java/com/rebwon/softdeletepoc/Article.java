package com.rebwon.softdeletepoc;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "article")
@SQLDelete(sql = "update article set deleted = true where id = ?")
@Where(clause = "deleted = false")
@Getter
@EqualsAndHashCode(of = "id")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String content;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    protected Article() {}

    public Article(Member member, String content) {
        this.member = member;
        this.content = content;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void deleted() {
        this.deleted = true;
    }
}
