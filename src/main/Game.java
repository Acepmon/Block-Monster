package main;

import conf.Config;
import static conf.Config.HEIGHT;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Shape;

/**
 *
 * @author Acep
 */
public class Game implements Config, EventHandler<KeyEvent> {

    private Scene scene;
    private StackPane stack;
    private Group backGroup;
    private Pane environment;
    private Group mainGroup;
    private Pane canvas;

    private ObservableList<model.Shape> list = FXCollections.observableArrayList();
    private ObservableList<Rectangle> every_blocks = FXCollections.observableArrayList();

    private int current = -1;

    private ObservableList<MediaPlayer> sounds;

    private Timer autoDown;
    private boolean cancelAutoDown = false;
    private long difficulty = 800;
    private boolean overTheLimit = false;

    public Game(Stage parent, ObservableList<MediaPlayer> sounds) {
        this.sounds = sounds;
        this.sounds.get(1).play();
        this.canvas = new Pane();
        this.canvas.setPrefSize(WIDTH, HEIGHT);
        this.mainGroup = new Group(canvas);

        this.environment = new Pane();
        this.environment.setPrefSize(WIDTH, HEIGHT);
        this.backGroup = new Group(environment);

        this.stack = new StackPane();
        this.stack.getChildren().addAll(backGroup, mainGroup);

        this.scene = new Scene(stack, WIDTH, HEIGHT);

        this.scene.setOnKeyPressed(this);
        this.setupEnvironment();
        this.addShape();

        this.autoDown = new Timer();
        this.autoDown.schedule(new TimerTask() {

            @Override
            public void run() {
                int y = list.get(current).downLimitY();
                Platform.runLater(() -> {
                    if (y == Config.HEIGHT) {
                        addShape();
                    } else {
                        list.get(current).shiftDown();
                    }
                });
                if (cancelAutoDown) {
                    this.cancel();
                }
            }
        }, difficulty, 1000);
        parent.setOnCloseRequest((e) -> {
//            this.cancelAutoDown = true;
            autoDown.cancel();
        });
    }

    private void paintingBlocks() {
        if (current != -1) {
            canvas.getChildren().addAll(list.get(current).getBlocks());
        }
    }

    private void addShape() {
        int random = (int) (Math.random() * 7) + 1;
        list.add(new Shape(random));
        findCurrent();
        paintingBlocks();
    }

    private void findCurrent() {
        this.current = list.size() - 1;
    }

    private void setupEnvironment() {
        RadialGradient rg = new RadialGradient(0,
                .1,
                WIDTH / 2,
                HEIGHT / 2,
                270,
                false,
                CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#132487")),
                new Stop(1, Color.web("#060C2E")));
        Rectangle background = new Rectangle(WIDTH, HEIGHT);
        background.setFill(rg);
        environment.getChildren().add(background);
        Rectangle[] grids = new Rectangle[RECTANGLES];
        double x = 0, y = 0;
        for (Rectangle rec : grids) {
            rec = new Rectangle(x, y, CELLSIZE, CELLSIZE);
            rec.setFill(Color.TRANSPARENT);
            rec.setStroke(Color.web("#293BA3"));
            environment.getChildren().add(rec);

            x += Config.CELLSIZE;
            if (x >= Config.WIDTH) {
                x = 0;
                y += Config.CELLSIZE;
            }
            if (y > Config.HEIGHT) {
                break;
            }
        }
    }

    public Scene getScene() {
        return scene;
    }

    @Override
    public void handle(KeyEvent event) {
        int x, y, value, dly;
        if (current != -1) {
            switch (event.getCode()) {
                case UP:
                    this.list.get(this.current).shiftUp();
                    break;
                case LEFT:
                    x = this.list.get(this.current).leftLimitX();
                    dly = this.list.get(this.current).downLimitY();
                    if ((x - Config.CELLSIZE) >= 0) {
                        boolean cango = true;
                        for (Rectangle rec : every_blocks) {
                            if ((int) (rec.getX() + Config.CELLSIZE) == x && dly > rec.getY()) {
                                cango = false;
                            }
                        }
                        if (cango) {
                            this.list.get(this.current).shiftLeft();
                        }
                    }
                    break;
                case RIGHT:
                    x = this.list.get(this.current).rightLimitX();
                    dly = this.list.get(this.current).downLimitY();
                    if ((x + Config.CELLSIZE) <= Config.WIDTH) {
                        boolean cango = true;
                        for (Rectangle rec : every_blocks) {
                            if ((int) (rec.getX()) == x && dly > rec.getY()) {
                                cango = false;
                            }
                        }
                        if (cango) {
                            this.list.get(this.current).shiftRight();
                        }
                    }
                    break;
                case DOWN:
                    y = this.list.get(this.current).downLimitY();
                    if ((y + Config.CELLSIZE) <= Config.HEIGHT) {
                        this.list.get(this.current).shiftDown();
                    }
                    break;
                case SPACE:
                    List<Integer> spaceValues = new ArrayList<>();
                    for (Point p : list.get(current).allDownLimitY()) {
                        int upstartx = p.x;
                        int upendx = upstartx + Config.CELLSIZE;

                        int dlimit = p.y + Config.CELLSIZE;
                        int ulimit = Config.HEIGHT;
                        boolean gfirst = false;
                        for (int a = 0; a < every_blocks.size(); a++) {
                            int startx = (int) every_blocks.get(a).getX();
                            int endx = startx + Config.CELLSIZE;
                            if (startx == upstartx && endx == upendx) {
                                int tmp = (int) every_blocks.get(a).getY();
                                if (!gfirst) {
                                    ulimit = tmp;
                                    gfirst = true;
                                }
                                if (ulimit > tmp) {
                                    ulimit = tmp;
                                }
                            }
                        }
                        int val = ulimit > dlimit ? ulimit - dlimit : dlimit - ulimit;
                        spaceValues.add(val);
                    }
                    Collections.sort(spaceValues);
                    value = spaceValues.get(0);
                    this.list.get(this.current).shiftSpace(value);
                    every_blocks.addAll(this.list.get(current).getBlocks());
                    removeBlocks();

                    sounds.get(0).play();
                    sounds.get(0).stop();
                    
                    addShape();

                    break;
            }
        }
    }

    private void removeBlocks() {
        int[] arr = new int[every_blocks.size()];
        List<Point> ls = new ArrayList<>();
        for (int a = 0; a < arr.length; a++) {
            int y = (int) (every_blocks.get(a).getY());
            int x = (int) (every_blocks.get(a).getX());
            ls.add(new Point(x, y));
        }

        Collections.sort(ls, (Point c1, Point c2) -> {
            return Double.compare(c1.getY(), c2.getY());
        });

        int i = 0;
        int j = 0;
        int count;
        int[] unique = new int[ls.size()];
        int[] times = new int[ls.size()];
        while (i < ls.size()) {
            int w = (int) ls.get(i).getY();
            count = 1;
            while (++i < ls.size() && (int) (ls.get(i).getY()) == w) {
                ++count;
            }
            unique[j] = w;
            times[j++] = count;
        }
        unique = Arrays.copyOf(unique, j);
        times = Arrays.copyOf(times, j);

        boolean removedFromList = false;
        for (i = 0; i < unique.length; ++i) {
            System.out.println(unique[i] + " " + times[i]);
            if (times[i] == Config.width_block) {
                for (int a = 0; a < every_blocks.size(); a++) {
                    int list_y = (int) every_blocks.get(a).getY();
                    if (list_y == unique[i] && list_y < unique[i]+Config.CELLSIZE) {
                        every_blocks.remove(a);
                        removedFromList = true;
                    }
                }
                for (Rectangle rec : every_blocks) {
                    if ((int) (rec.getY()) < unique[i]) {
                        rec.setY(rec.getY()+Config.CELLSIZE);
                        System.out.println("Rec X: "+rec.getX()+" | Rec Y: "+rec.getY());
                    }
                }
            }
        }
        if (removedFromList) {
            canvas.getChildren().clear();
            canvas.getChildren().addAll(every_blocks);
        }
    }

    private int BinarySearch(int A[], int n, int x, boolean searchFirst) {
        int low = 0, high = n - 1, result = -1;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (A[mid] == x) {
                result = mid;
                if (searchFirst) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else if (x < A[mid]) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return result;
    }

}
