package com.tokubase.util;

import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Set;

/**
 * Utility service for validating and processing base64-encoded images.
 *
 * Rules enforced:
 * - Supported MIME types: image/jpeg, image/png, image/webp, image/gif
 * - Maximum decoded size: 5 MB (configurable via MAX_SIZE_BYTES)
 */
@Service
public class ImageValidationService {

    /** Supported image MIME types encoded in the base64 data-URI prefix. */
    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    /** Maximum allowed decoded image size: 5 MB. */
    private static final long MAX_SIZE_BYTES = 5 * 1024 * 1024L;

    // ── Public API ────────────────────────────────────────────────────────

    /**
     * Validates a base64 image string.
     * Accepts both raw base64 and data-URI format (data:image/png;base64,<data>).
     *
     * @param base64Image the raw or data-URI base64 string to validate
     * @throws IllegalArgumentException if the image is null/blank, unsupported type, or too large
     */
    public void validate(String base64Image) {
        if (base64Image == null || base64Image.isBlank()) {
            throw new IllegalArgumentException("Image data must not be empty");
        }

        String mimeType = extractMimeType(base64Image);
        if (mimeType != null && !ALLOWED_MIME_TYPES.contains(mimeType.toLowerCase())) {
            throw new IllegalArgumentException(
                    "Unsupported image type '" + mimeType
                            + "'. Allowed types: " + ALLOWED_MIME_TYPES);
        }

        byte[] decoded = decodeBase64(base64Image);
        if (decoded.length > MAX_SIZE_BYTES) {
            long sizeMb = decoded.length / (1024 * 1024);
            throw new IllegalArgumentException(
                    "Image size " + sizeMb + " MB exceeds the maximum allowed size of "
                            + (MAX_SIZE_BYTES / (1024 * 1024)) + " MB");
        }
    }

    /**
     * Validates the image if it is present (non-null / non-blank), otherwise no-ops.
     * Use this for optional image fields on update requests.
     */
    public void validateIfPresent(String base64Image) {
        if (base64Image != null && !base64Image.isBlank()) {
            validate(base64Image);
        }
    }

    /**
     * Strips the data-URI prefix (if any) and returns the raw base64 string.
     * Input: "data:image/png;base64,iVBORw0KGgo..."
     * Output: "iVBORw0KGgo..."
     */
    public String stripDataUriPrefix(String base64Image) {
        if (base64Image == null) return null;
        int commaIndex = base64Image.indexOf(',');
        if (commaIndex >= 0) {
            return base64Image.substring(commaIndex + 1);
        }
        return base64Image;
    }

    /**
     * Returns the decoded byte length of the image without fully allocating a second copy.
     * Useful for quick size checks.
     */
    public long decodedSizeBytes(String base64Image) {
        return decodeBase64(base64Image).length;
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    /**
     * Extracts the MIME type from a data-URI prefix.
     * Returns null if the string is plain base64 without a prefix.
     *
     * E.g. "data:image/png;base64,..." → "image/png"
     */
    private String extractMimeType(String base64Image) {
        if (base64Image.startsWith("data:")) {
            int semicolonIdx = base64Image.indexOf(';');
            if (semicolonIdx > 5) {
                return base64Image.substring(5, semicolonIdx); // after "data:"
            }
        }
        // Try to infer from magic bytes after decoding
        try {
            byte[] bytes = decodeBase64(base64Image);
            return detectMimeFromBytes(bytes);
        } catch (IllegalArgumentException e) {
            return null; // can't infer type
        }
    }

    /** Detects common image MIME types by inspecting the first few magic bytes. */
    private String detectMimeFromBytes(byte[] bytes) {
        if (bytes.length < 4) return null;

        // JPEG: FF D8 FF
        if (bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8 && bytes[2] == (byte) 0xFF) {
            return "image/jpeg";
        }
        // PNG: 89 50 4E 47
        if (bytes[0] == (byte) 0x89 && bytes[1] == 0x50
                && bytes[2] == 0x4E && bytes[3] == 0x47) {
            return "image/png";
        }
        // GIF: 47 49 46 38
        if (bytes[0] == 0x47 && bytes[1] == 0x49
                && bytes[2] == 0x46 && bytes[3] == 0x38) {
            return "image/gif";
        }
        // WebP: RIFF....WEBP
        if (bytes.length >= 12
                && bytes[0] == 0x52 && bytes[1] == 0x49   // RI
                && bytes[2] == 0x46 && bytes[3] == 0x46   // FF
                && bytes[8] == 0x57 && bytes[9] == 0x45   // WE
                && bytes[10] == 0x42 && bytes[11] == 0x50) { // BP
            return "image/webp";
        }
        return null;
    }

    private byte[] decodeBase64(String base64Image) {
        String raw = stripDataUriPrefix(base64Image);
        try {
            return Base64.getDecoder().decode(raw);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid base64 image data: " + e.getMessage(), e);
        }
    }
}

