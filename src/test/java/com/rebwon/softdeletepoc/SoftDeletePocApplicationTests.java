package com.rebwon.softdeletepoc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@SpringBootTest
@Transactional
class SoftDeletePocApplicationTests {

    @Autowired
    private EntityManager em;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    void setUp() {
        Member member = transactionTemplate.execute(
            status -> memberRepository.save(new Member("Solomon")));

        for (int i = 0; i < 10; i++) {
            Article article = articleRepository.save(new Article(member, "sample" + i));
            Comment comment = new Comment("sample" + i);
            comment.setArticle(article);
            article.addComment(comment);
        }
        // 게시글 10건, 댓글 10건
        // 게시글 1건 당 댓글 1건

        em.flush();
        em.clear();

        articleRepository.deleteById(1L);
        //Article article = articleRepository.findById(1L).get();
        //System.out.println(article.getComments().get(1).getContent());
        //article.deleted();

        em.flush();
        em.clear();

        // 10건의 게시글 중 1번 게시글을 삭제한 경우.
        // 1번 게시글에 1번 댓글은 살아있는거.
        // soft delete -> cascade delete
        // article, comment
        //
    }

    @Test
    void nam11e() {
//        List<Comment> comments = em.createQuery("select c from Comment c where c.deleted = :deleted",
//            Comment.class)
//            .setParameter("deleted", true)
//            .getResultList();
        List<Comment> comments = em.createQuery("select c from Comment c", Comment.class)
            .getResultList();
        for (Comment comment : comments) {
            System.out.println(comment.getArticle().getContent());
        }
    }

    @Test
    void contextLoads() {
        Optional<Comment> comment = commentRepository.findById(1L);
        comment.ifPresent(com -> System.out.println(com.getArticle().getContent()));
    }

    @Test
    void name() {
        List<Comment> comments = commentRepository.findAll();
        for(Comment comment : comments) {
            System.out.println(comment.getArticle().getContent());
        }
    }
}
