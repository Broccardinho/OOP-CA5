package org.example.utils;

import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageUtils {
    private static final Map<String, Image> imageCache = new HashMap<>();

    public static Image loadImage(String url) {
        return imageCache.computeIfAbsent(url, k -> {
            try {
                Image img = new Image(url, true); // true for background loading

                // Add error handling
                img.errorProperty().addListener((obs, wasError, isNowError) -> {
                    if (isNowError) {
                        System.err.println("Failed to load image: " + url);
                    }
                });

                return img;
            } catch (Exception e) {
                System.err.println("Error loading image: " + url);
                return new Image("/error.png"); // Fallback image
            }
        });
    }

    public static void preloadImages(List<String> urls) {
        urls.forEach(ImageUtils::loadImage);
    }
}