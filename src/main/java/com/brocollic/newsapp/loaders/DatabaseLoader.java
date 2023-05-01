package com.brocollic.newsapp.loaders;

import com.brocollic.newsapp.entities.*;
import com.brocollic.newsapp.services.ArticleService;
import com.brocollic.newsapp.services.CategoryService;
import com.brocollic.newsapp.services.CommentService;
import com.brocollic.newsapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserService userService;
    private final CategoryService categoryService;
    private final ArticleService articleService;
    private final CommentService commentService;
    private final PasswordEncoder passwordEncoder;

    //Users
    private static final String[] userEmails = {"andrej.brocic@gmail.com", "user1@gmail.com", "user2@gmail.com"};
    private static final String[] userFirstNames = {"andrej", "user1", "user2"};
    private static final String[] userLastnames = {"brocic", "useric1", "useric2"};
    private static final Role[] userRoles = {Role.ADMIN, Role.CONTENT_CREATOR, Role.CONTENT_CREATOR};
    private static final String[] userPasswords = {"brocicpass", "useric1", "useric2"};

    //Categories
    private static final String[] categoryLabels = {"World news", "Entertainment", "Music"};
    private static final String[] categoryDescriptions = {"World news from all around the world",
            "News from entertainment industry", "Find all about your favourite music act"};

    //Articles
    private static final String[] articleTitles = {"Title1", "Title2", "Title3"};
    private static final String[] articleContents = {"Content1", "Content2", "Content3"};

    //Comments
    private static final String[] commentContents = {"Comment1", "Comment2", "Comment3"};


    @Override
//    @ConditionalOnProperty(
//            name = {"spring.jpa.hibernate.dll-auto"},
//            havingValue = "create-drop")
    public void run(String... args) throws Exception {
        for (int i = 0; i < 3; i++) {
            User newUser = User.builder()
                    .email(userEmails[i])
                    .firstname(userFirstNames[i])
                    .lastname(userLastnames[i])
                    .password(passwordEncoder.encode(userPasswords[i]))
                    .role(userRoles[i]).build();
            userService.save(newUser);

            Category newCategory = Category.builder()
                    .label(categoryLabels[i])
                    .description(categoryDescriptions[i])
                    .build();
            categoryService.save(newCategory);

            Article newArticle = Article.builder()
                    .user(newUser)
                    .creationTime(LocalDateTime.now())
                    .content(articleContents[i])
                    .title(articleTitles[i])
                    .category(newCategory)
                    .build();
            articleService.save(newArticle);

            Comment newComment = Comment.builder()
                    .content(commentContents[i])
                    .article(newArticle)
                    .user(newUser)
                    .creationTime(LocalDateTime.now())
                    .build();
            commentService.save(newComment);
        }
    }
}
