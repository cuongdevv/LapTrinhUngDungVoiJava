package vn.edu.gdu.lab6.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Cau truc JSON phan hoi loi chung cho toan bo API (Bai 4 - Chuong 6).
 * Dong bo dinh dang loi giup Client xu ly nhat quan.
 */
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<String> details; // Chua loi validation cu the tung truong

    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ErrorResponse(int status, String error, String message, String path, List<String> details) {
        this(status, error, message, path);
        this.details = details;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
    public List<String> getDetails() { return details; }
}
