package com.example.nannyap.model;

public class CommentModel {
    private String commentId,parentId, nannyId, comment, date;

    public CommentModel() {
    }

    public CommentModel(String commentId, String parentId, String nannyId, String comment, String date) {
        this.commentId = commentId;
        this.parentId = parentId;
        this.nannyId = nannyId;
        this.comment = comment;
        this.date = date;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getParentId() {
        return parentId;
    }

    public String getNannyId() {
        return nannyId;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }
}
