package view;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import javax.imageio.ImageIO;
import model.enums.TileID;

/**
 * Class to get the appropriate image for any Tile by its TileID.
 *
 * @author Shane Menzies
 * @version 11/24/24
 */
public final class SpriteMap extends EnumMap<TileID, Image> {

    /**
     * Directory to find the tile images in.
     */
    private static final String IMAGE_DIRECTORY = "resources" + File.separator
            + "images"  + File.separator + "tiles" + File.separator;

    /**
     * Extension for tile images.
     */
    private static final String IMAGE_EXTENSION = ".png";

    /**
     * File name for the fallback tile image.
     */
    private static final String FALLBACK_IMAGE_NAME = "fallback";

    /**
     * Error message for when the fallback image can't be found.
     */
    private static final String FALLBACK_IMAGE_NOT_FOUND =
            "Fallback image \"" + IMAGE_DIRECTORY + FALLBACK_IMAGE_NAME + IMAGE_EXTENSION
                    + "\" not found!";

    /**
     * The single instance of this class.
     */
    private static final SpriteMap MY_INSTANCE = new SpriteMap();

    /**
     * Fallback image for any tiles whose images can't be found.
     */
    private final Image myFallBackImage;

    /**
     * Private constructor, initializes the single instance of SpriteMap.
     */
    private SpriteMap() {
        super(TileID.class);

        try {
            myFallBackImage = ImageIO.read(
                    new File(IMAGE_DIRECTORY + FALLBACK_IMAGE_NAME + IMAGE_EXTENSION)
            );
        } catch (final IOException exception) {
            throw new RuntimeException(FALLBACK_IMAGE_NOT_FOUND);
        }

        fillMap();
    }

    /**
     * Gets the (single) instance of SpriteMap.
     *
     * @return The instance of SpriteMap.
     */
    public static SpriteMap getInstance() {
        return MY_INSTANCE;
    }

    /**
     * Fills the sprite map with the correct images for each TileID.
     */
    private void fillMap() {
        for (final TileID nextTile : TileID.values()) {
            Image sprite;
            try {
                sprite = ImageIO.read(
                        new File(IMAGE_DIRECTORY + nextTile.toString() + IMAGE_EXTENSION)
                );
            } catch (final IOException exception) {
                sprite = myFallBackImage;
            }

            put(nextTile, sprite);
        }
    }
}