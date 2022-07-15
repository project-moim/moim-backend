package avocado.moim.service;

import avocado.moim.dto.PostListResponseDto;
import avocado.moim.dto.PostResponseDto;
import avocado.moim.dto.PostSaveRequestDto;
import avocado.moim.dto.PostUpdateRequestDto;

import java.util.List;

public interface PostService {

    Long save(PostSaveRequestDto requestDto);

    Long update(Long id, PostUpdateRequestDto requestDto);

    PostResponseDto findById(Long id);

    List<PostListResponseDto> findAllDesc();

    void delete(Long id);
}
