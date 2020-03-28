package com.ivan1pl.minecraft.modelgenerator

import java.awt.Color

class BufferedImage {
    @Delegate
    java.awt.image.BufferedImage bufferedImage

    BufferedImage(java.awt.image.BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage
    }

    int getAlpha(int x, int y) {
        if (x < 0 || y < 0 || x >= bufferedImage.width || y >= bufferedImage.height) return 0
        int argb = bufferedImage.getRGB(x, y)
        (argb >> 24) & 0xFF
    }
}
