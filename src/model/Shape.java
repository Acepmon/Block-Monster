package model;

import conf.Config;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Acep / D.Tsogtbayar
 */


public class Shape implements conf.Config {

    public final static int SHAPE_I = 1;
    public final static int SHAPE_T = 2;
    public final static int SHAPE_Z = 3;
    public final static int SHAPE_REVERSE_Z = 4;
    public final static int SHAPE_L = 5;
    public final static int SHAPE_REVERSE_L = 6;
    public final static int SHAPE_O = 7;
    public final static int SHAPE_S = 8;
    public final static int SHAPE_REVERSE_S = 9;

    private Color fillColor = Color.BLUE;
    private Color strokeColor = Color.web("#0B3838");

    private int[][] dots = new int[3][3];

    private ArrayList<Rectangle> blocks = new ArrayList<>();

    private final int size = CELLSIZE;

    private boolean lock = false;

    private String name;

    private int type;

    private int bottomBlock;

    // State changing variables
    boolean istate = true;
    byte tstate = 1;
    boolean zstate = true;
    byte lstate = 1;
    boolean sstae = true;

    public Shape(int shapeType) {
//        this.dots = dots;

        if (shapeType > 0 & shapeType < 10) {
            switch (shapeType) {
                case SHAPE_I:
                    this.dots = new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 0, 0}};
                    this.fillColor = Color.web("#C4FFF9");
                    this.name = "I";
                    this.type = SHAPE_I;
                    break;
                case SHAPE_T:
                    this.dots = new int[][]{{0, 1, 0}, {1, 1, 1}, {0, 0, 0}};
                    this.fillColor = Color.web("#FF5CD9");
                    this.type = SHAPE_T;
                    this.name = "T";
                    break;
                case SHAPE_Z:
                    this.dots = new int[][]{{0, 1, 0}, {1, 1, 0}, {1, 0, 0}};
                    this.fillColor = Color.web("#FF4242");
                    this.type = SHAPE_Z;
                    this.name = "Z";
                    break;
                case SHAPE_REVERSE_Z:
                    this.dots = new int[][]{{1, 0, 0}, {1, 1, 0}, {0, 1, 0}};
                    this.fillColor = Color.web("#93FF6B");
                    this.type = SHAPE_Z;
                    this.name = "Reverse Z";
                    break;
                case SHAPE_L:
                    this.dots = new int[][]{{1, 0, 0}, {1, 0, 0}, {1, 1, 0}};
                    this.fillColor = Color.web("#FFBD24");
                    this.type = SHAPE_L;
                    this.name = "L";
                    break;
                case SHAPE_REVERSE_L:
                    this.dots = new int[][]{{0, 1, 0}, {0, 1, 0}, {1, 1, 0}};
                    this.fillColor = Color.web("#697FFF");
                    this.type = SHAPE_L;
                    this.name = "Reverse L";
                    break;
                case SHAPE_O:
                    this.dots = new int[][]{{0, 0, 0}, {1, 1, 0}, {1, 1, 0}};
                    this.fillColor = Color.web("#FFFF42");
                    this.type = SHAPE_O;
                    this.name = "O";
                    break;
            }
            this.constructShape(this.dots);
        }
    }

    public int getBottomBlock() {
        return bottomBlock;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void shiftLeft() {
        for (Rectangle rec : this.blocks) {
            int tmpX = (int) rec.getX();
            rec.setX((tmpX -= this.size));
        }
    }

    public void shiftRight() {
        for (Rectangle rec : this.blocks) {
            int tmpX = (int) rec.getX();
            rec.setX((tmpX += this.size));
        }
    }

    public void shiftDown() {
        for (Rectangle rec : this.blocks) {
            int tmpY = (int) rec.getY();
            rec.setY(tmpY += this.size);
        }
    }

    public void shiftSpace(int valueToBeAdded) {
        for (Rectangle rec : this.blocks) {
            int tmpY = (int) rec.getY();
            rec.setY((tmpY += valueToBeAdded));
        }
    }

    public void shiftUp() {
        switch (type) {
            case SHAPE_I:
                if (this.leftLimitX() - Config.CELLSIZE < 0) {
                    this.shiftRight();
                } else if (this.rightLimitX() + Config.CELLSIZE > Config.WIDTH) {
                    this.shiftLeft();
                }
                rotate((int) (blocks.get(1).getX()), (int) (blocks.get(1).getY()));
                break;
            case SHAPE_T:
                if (this.rightLimitX() - Config.CELLSIZE * 2 == 0) {
                    this.shiftRight();
                } else if (this.leftLimitX() + Config.CELLSIZE * 2 == Config.WIDTH) {
                    this.shiftLeft();
                }
                rotate((int) (blocks.get(2).getX()), (int) (blocks.get(2).getY()));
                break;
            case SHAPE_Z:
                if (this.leftLimitX() - Config.CELLSIZE < 0) {
                    this.shiftRight();
                } else if (this.rightLimitX() + Config.CELLSIZE > Config.WIDTH) {
                    this.shiftLeft();
                }
                rotate((int) (blocks.get(1).getX()), (int) (blocks.get(1).getY()));
                break;
            case SHAPE_REVERSE_Z:
                if (this.leftLimitX() - Config.CELLSIZE < 0) {
                    this.shiftRight();
                } else if (this.rightLimitX() + Config.CELLSIZE > Config.WIDTH) {
                    this.shiftLeft();
                }
                rotate((int) (blocks.get(1).getX()), (int) (blocks.get(1).getY()));
                break;
            case SHAPE_L:
                if (this.leftLimitX() == 0) {
                        this.shiftRight();
                } else if (this.rightLimitX() == Config.WIDTH) {
                        this.shiftLeft();
                }
                rotate((int) (blocks.get(1).getX()), (int) (blocks.get(1).getY()));
                break;
            case SHAPE_REVERSE_L:
                if (this.leftLimitX() == 0) {
                        this.shiftRight();
                } else if (this.rightLimitX() == Config.WIDTH) {
                        this.shiftLeft();
                }
                rotate((int) (blocks.get(1).getX()), (int) (blocks.get(1).getY()));
                break;
        }
    }

    private void rotate(int ox, int oy) {
        int originX = ox / Config.CELLSIZE;
        int originY = oy / Config.CELLSIZE;

        for (int a = 0; a < blocks.size(); a++) {
            int x = (int) (blocks.get(a).getX()) / Config.CELLSIZE;
            int y = (int) (blocks.get(a).getY()) / Config.CELLSIZE;

            x -= originX;
            y -= originY;

            x = -x;
            y = -y;
            
            int temp = x;
            x = -y;
            y = temp;

            x = -x;
            y = -y;

            x += originX;
            y += originY;

            blocks.get(a).setX(x * Config.CELLSIZE);
            blocks.get(a).setY(y * Config.CELLSIZE);
        }
    }

    public int leftLimitX() {
        int val = -1;
        for (int a = 0; a < this.blocks.size(); a++) {
            int x = (int) this.blocks.get(a).getX();
            if (a == 0) {
                val = x;
            }
            if (val > x) {
                val = x;
            }
        }
        return val;
    }

    public int rightLimitX() {
        int val = -1;
        for (int a = 0; a < this.blocks.size(); a++) {
            int x = (int) this.blocks.get(a).getX() + this.size;
            if (a == 0) {
                val = x;
            }
            if (val < x) {
                val = x;
            }
        }
        return val;
    }

    public int downLimitY() {
        int val = -1;
        for (int a = 0; a < this.blocks.size(); a++) {
            int y = (int) this.blocks.get(a).getY() + this.size;
            if (a == 0) {
                val = y;
            }
            if (val < y) {
                val = y;
            }
            bottomBlock = a;
        }
        return val;
    }
    
    public List<Point> allDownLimitY() {
        List<Point> list = new ArrayList<>();
        List<Point> newList= new ArrayList<>();
        for (Rectangle block : blocks)
            list.add(new Point((int) block.getX(), (int) block.getY()));
        
        Collections.sort(list, (Point c1, Point c2) -> Double.compare(c1.getX(), c2.getX()));
        for (int a = 0, tx = 0, ty = 0; a<list.size(); a++) {
            if (a == 0) {
                tx = list.get(a).x;
                ty = list.get(a).y;
            }
            if (tx != list.get(a).x) {
                newList.add(new Point(tx, ty));
                tx = list.get(a).x;
                ty = list.get(a).y;
            } else {
                if (ty < list.get(a).y) {
                    ty = list.get(a).y;
                }
            }
            if (a == list.size() - 1) {
                newList.add(new Point(list.get(a).x, list.get(a).y));
            }
        }
        
        return newList;
    }

    public int upLimitY() {
        int val = -1;
        for (int a = 0; a < this.blocks.size(); a++) {
            int y = (int) this.blocks.get(a).getY() + this.size;
            if (a == 0) {
                val = y;
            }
            if (val > y) {
                val = y;
            }
        }
        return val;
    }

    public ArrayList<Rectangle> getBlocks() {
        return this.blocks;
    }

    public void paintBlocks(Pane container) {
        container.getChildren().addAll(this.blocks);
    }

    private void constructShape(int[][] dots) {
        for (int a = 0; a < dots.length; a++) {
            for (int b = 0; b < dots[a].length; b++) {
                if (dots[a][b] == 1) {
                    Rectangle tmp = new Rectangle(getPos(centerX(), b, CELLSIZE), getPos(0, a, CELLSIZE), CELLSIZE, CELLSIZE);
                    tmp.setFill(this.fillColor);
                    tmp.setStroke(this.strokeColor);
                    tmp.setStrokeWidth(3);
                    tmp.setSmooth(true);
                    tmp.setArcWidth(Config.CELLSIZE / 5);
                    tmp.setArcHeight(Config.CELLSIZE / 5);
                    blocks.add(tmp);
                }
            }
        }
    }

    private int centerX() {
        int val = 0;
        switch (this.type) {
            case SHAPE_I:
                val = (Config.width_block % 2 == 0) ? width_block / 2 - 1 : width_block / 2;
                break;
            case SHAPE_L:
                val = (Config.width_block % 2 == 0) ? width_block / 2 - 1 : width_block / 2;
                break;
            case SHAPE_T:
                val = (Config.width_block % 2 == 0) ? width_block / 2 - 2 : width_block / 2 - 1;
                break;
            case SHAPE_Z:
                val = (Config.width_block % 2 == 0) ? width_block / 2 - 1 : width_block / 2;
                break;
            case SHAPE_O:
                val = (Config.width_block % 2 == 0) ? width_block / 2 - 1 : width_block / 2 - 1;
                break;
            case SHAPE_S:
                val = (Config.width_block % 2 == 0) ? width_block / 2 - 1 : width_block / 2 - 1;
                break;
        }
        val = val * Config.CELLSIZE;
        return val;
    }

    private int getPos(int startPos, int endPos, int size) {
        int returnValue = startPos;
        for (int a = 0; a < endPos; a++) {
            returnValue += size;
        }
        return returnValue;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }
}
