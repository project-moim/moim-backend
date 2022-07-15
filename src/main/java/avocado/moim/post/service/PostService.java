package avocado.moim.post.service;

import avocado.moim.post.dto.PostListResponseDto;
import avocado.moim.post.dto.PostResponseDto;
import avocado.moim.post.dto.PostSaveRequestDto;
import avocado.moim.post.dto.PostUpdateRequestDto;

import java.util.List;

public interface PostService {

    Long save(PostSaveRequestDto requestDto);

    Long update(Long id, PostUpdateRequestDto requestDto);

    PostResponseDto findById(Long id);

    List<PostListResponseDto> findAllDesc();

    void delete(Long id);
}
