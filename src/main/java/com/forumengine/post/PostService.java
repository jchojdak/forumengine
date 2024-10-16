package com.forumengine.post;

import com.forumengine.category.Category;
import com.forumengine.category.CategoryRepository;
import com.forumengine.exception.EntityNotFoundException;
import com.forumengine.post.dto.CreatePostDTO;
import com.forumengine.post.dto.PostDTO;
import com.forumengine.user.User;
import com.forumengine.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

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
}
