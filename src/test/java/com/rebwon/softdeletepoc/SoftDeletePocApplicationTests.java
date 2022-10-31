package com.rebwon.softdeletepoc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

        em.flush();
        em.clear();

        // deleteById를 호출하면, cascade 삭제 전파 이벤트가 발생하여
        // 게시글과 댓글을 모두 삭제할 수 있다.
        articleRepository.deleteById(1L);

        // 아래에 로직처럼 삭제하게 된다면, Article만 삭제하고 연관된 Comment는 삭제하지 않는다.
        // 왜냐하면 Comment가 프록시 상태이기 때문.
        //Article article = articleRepository.findById(1L).get();
        //article.deleted();

        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("BeforeEach 절에서 article.deleted로 삭제하면 EntityNotFoundException이 발생한다.")
    void name() {
        List<Comment> comments = commentRepository.findAll();
        for(Comment comment : comments) {
            System.out.println(comment.getArticle().getContent());
        }
    }
}
