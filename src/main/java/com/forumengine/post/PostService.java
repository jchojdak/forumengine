package com.forumengine.post;

import com.forumengine.category.Category;
import com.forumengine.category.CategoryRepository;
import com.forumengine.exception.EntityNotFoundException;
import com.forumengine.post.dto.CreatePostDTO;
import com.forumengine.post.dto.PostCommentsDTO;
import com.forumengine.post.dto.PostDTO;
import com.forumengine.user.User;
import com.forumengine.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<PostDTO> getAllPosts(Integer page, Integer size, Sort.Direction sort) {
        int pageNumber = (page != null && page >= 0) ? page : 0;
        int pageSize = (size != null && size >= 1) ? size : 10;
        Sort.Direction sortDirection = (sort != null) ? sort : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortDirection, "createdAt"));

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
}
