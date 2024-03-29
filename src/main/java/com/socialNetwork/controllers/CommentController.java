package com.socialNetwork.controllers;

import com.socialNetwork.dto.post.comment.CommentInfo;
import com.socialNetwork.dto.post.EditRequest;
import com.socialNetwork.dto.post.comment.CreateCommentRequest;
import com.socialNetwork.dto.response.SuccessResponse;
import com.socialNetwork.dto.response.SuccessResponseWithData;
import com.socialNetwork.exceptions.DeveloperException;
import com.socialNetwork.security.CustomUserDetails;
import com.socialNetwork.services.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/comment")
public class CommentController {

    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponseWithData<CommentInfo>> createComment(@RequestBody CreateCommentRequest commentInfo) throws DeveloperException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        logger.info("[createComment] RequestBody CreateCommentRequest: {}, userId from token: {}", commentInfo, userId);
        CommentInfo comment = commentService.create(userId, commentInfo);
        logger.info("Comment created successfully: {}", comment);
        return ResponseEntity.ok(new SuccessResponseWithData<>(comment));
    }

    @GetMapping
    public ResponseEntity<SuccessResponseWithData<List<CommentInfo>>> getComments(@RequestParam("postId") Long postId){
        logger.info("[getComments] RequestParam postId: {}", postId);
        List<CommentInfo> comments = commentService.getComments(postId);
        return ResponseEntity.ok(new SuccessResponseWithData<>(comments));
    }

    @DeleteMapping
    public ResponseEntity<SuccessResponse> deleteComment(@RequestParam("commentId") Long commentId) throws DeveloperException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        logger.info("[deleteComment] RequestParam commentId: {}, userId from token: {}", commentId.toString(), userId.toString());
        commentService.delete(userId, commentId);
        logger.info("Comment deleted successfully");
        return ResponseEntity.ok(new SuccessResponse("Comment deleted successfully"));
    }

    @PostMapping("/edit")
    public ResponseEntity<SuccessResponseWithData<String>> editComment(@RequestBody EditRequest commentEditRequest) throws DeveloperException {
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        Long userId = userDetails.getId();
        logger.info("[editComment], RequestBody EditRequest: {}, userId from token: {}", commentEditRequest, userId);
        String editedText = commentService.edit(userId, commentEditRequest);
        logger.info("Comment edited successfully");
        return ResponseEntity.ok(new SuccessResponseWithData<>(editedText));
    }

}
