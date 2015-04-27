package conf;

/**
 *
 * @author Acep / D.Tsogtbayar
 */
public interface Config {
    
    public final int block_size = 25;
    public final int width_block = 15;
    public final int height_block = 20;
    
    public final int WIDTH = width_block * block_size;
    public final int HEIGHT = height_block * block_size;
    public final boolean RESIZABLE = false;
    public final boolean FULLSCREEN = false;
    public final String TITLE = "Block Monster";
    
    public final int CELLSIZE = block_size;
    public final int RECTANGLES = (WIDTH / CELLSIZE) * (HEIGHT / CELLSIZE);
    
}
