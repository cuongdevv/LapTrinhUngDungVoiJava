package vn.edu.gdu.lab6.exception;

/**
 * Custom Exception nem ra khi khong tim thay thuc the (Bai 4 - Chuong 6).
 * Duoc GlobalExceptionHandler bat va tra ve HTTP 404 Not Found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
