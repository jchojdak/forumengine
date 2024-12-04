package com.forumengine.post;

import com.forumengine.category.Category;
import com.forumengine.category.CategoryRepository;
import com.forumengine.comment.Comment;
import com.forumengine.comment.CommentRepository;
import com.forumengine.exception.AccessDeniedException;
import com.forumengine.exception.EntityNotFoundException;
import com.forumengine.post.dto.CreatePostDTO;
import com.forumengine.post.dto.PostCommentsDTO;
import com.forumengine.post.dto.PostDTO;
import com.forumengine.post.dto.UpdatePostRequest;
import com.forumengine.security.CustomUserDetails;
import com.forumengine.user.User;
import com.forumengine.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final String ACCESS_DENIED_MESSAGE = "You do not have permission to delete this post.";
    private static final String ACCESS_DENIED_MESSAGE_UPDATE = "You do not have permission to update this post.";
    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String SORT_PROPERTIES = "createdAt";

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public PostDTO createPost(CreatePostDTO createPostDTO, String authorName) {
        Long categoryId = createPostDTO.getCategoryId();

        Optional<User> author = userRepository.findByUsername(authorName);
        if (author.isEmpty()) {
            throw new EntityNotFoundException(authorName);
        }

        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            throw new EntityNotFoundException(categoryId.toString());
        }

        Post post = postMapper.toPost(createPostDTO);
        post.setCategory(category.get());
        post.setAuthor(author.get());

        Post savedPost = postRepository.save(post);
        return postMapper.toPostDTO(savedPost);
    }

    public List<PostDTO> getAllPosts(Integer page, Integer size, Sort.Direction sort) {
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size >= 1) ? size : 10;
        Sort.Direction sortDirection = (sort != null) ? sort : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, SORT_PROPERTIES));

        Page<Post> postsPage = postRepository.findAll(pageable);

        return postMapper.toPostDTOs(postsPage.getContent());
    }

    public PostCommentsDTO getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new EntityNotFoundException(id.toString());
        }

        return postMapper.toPostCommentsDTO(post.get());
    }

    public void deletePostById(Long id, Authentication auth) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isEmpty()) {
            throw new EntityNotFoundException(id.toString());
        }

        Long loggedInUserId = getUserIdFromAuthentication(auth);
        Long authorId = post.get().getAuthor().getId();

        if (isAuthor(loggedInUserId, authorId) || isAdmin(auth)) {
            List<Comment> comments = post.get().getComments();
            comments.forEach(comment -> commentRepository.delete(comment));

            postRepository.delete(post.get());
        } else {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }

    public PostDTO updatePostById(Long id, String username, UpdatePostRequest request) {
        Optional<Post> findPost = postRepository.findById(id);
        if (findPost.isEmpty()) throw new EntityNotFoundException(id.toString());
        Post post = findPost.get();

        Optional<User> findUser = userRepository.findByUsername(username);
        if (findUser.isEmpty()) throw new EntityNotFoundException(username);
        User loggedInUser = findUser.get();

        if (!isAuthor(loggedInUser.getId(), post.getAuthor().getId())) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE_UPDATE);
        }

        String newTitle = request.getTitle();
        if (newTitle != null && !newTitle.isEmpty()) {
            post.setTitle(newTitle);
        }

        String newContent = request.getContent();
        if (newContent != null && !newContent.isEmpty()) {
            post.setContent(newContent);
        }

        post.setUpdatedAt(LocalDateTime.now());

        post = postRepository.save(post);

        return postMapper.toPostDTO(post);
    }

    private boolean isAuthor(Long userId, Long authorId) {
        return userId.equals(authorId);
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(authentication -> authentication.getAuthority().equals(ROLE_ADMIN));
    }

}
