package brushbash;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JFileChooser;

public class Panel extends JPanel implements MouseMotionListener, MouseListener, ActionListener {
    // Draw's Coordinates
    private int startX, startY;
    private int endX, endY;

    // Boolean To Check Which Event Work
    private boolean isRecDraw = false;
    private boolean isOvalDraw = false;
    private boolean isLineDraw = false;
    private boolean isDotted = false;
    private boolean isFilled = false;
    private boolean isCleared = false;
    private boolean isFreeHand=false;
    private boolean isEraser=false;
    // Buttons && CheckBox
    private  JButton rectangleButton;
    private  JButton ovalButton;
    private  JButton lineButton;
    private  JButton clearButton;
    private  JButton saveButton;
    private  JButton openButton;
    private JButton freeHand;
    private JButton eraser;
    // Colors Button
    private Color currentColor = Color.BLACK;
    private  JButton blueButton;
    private  JButton orangeButton;
    private  JButton greenButton;
    private  JButton redButton;
    private  JButton defaultColorButton;
   private  JButton undoButton;

    // CheckBoxes
    private JCheckBox dottedCheckBox;
    private JCheckBox filledCheckBox;

    // List to store all shapes
    private  List<Shape> shapes;
    private ArrayList <ArrayList <Point>> lines;
    private ArrayList <Point> points;
    private ArrayList <Color> prevColors;
    // Image Variables
    private BufferedImage loadedImage;
    private boolean imageLoaded = false;

    // Shape currently being drawn
    private Shape currentShape = null;

    public Panel() {
        shapes = new ArrayList<>();

        rectangleButton = new JButton("Rectangle");
        ovalButton = new JButton("Oval");
        lineButton = new JButton("Line");
        clearButton = new JButton("Clear All");
       undoButton = new JButton("Undo");
        saveButton = new JButton("Save");
        openButton = new JButton("Open");
       
       rectangleButton.setBounds(200, 500, 100, 50);
       ovalButton.setBounds(200, 500, 100, 50);
       lineButton.setBounds(200, 500, 100, 50);
       clearButton.setBounds(200, 500, 100, 50);
       undoButton.setBounds(200, 500, 100, 50);
       saveButton.setBounds(200, 500, 100, 50);
       openButton.setBounds(200, 500, 100, 50);

        addMouseMotionListener(this);
        addMouseListener(this);
        
        rectangleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isOvalDraw = false;
                isRecDraw = true;
                isLineDraw = false;
                isFreeHand = false;
                isEraser = false;
                startX = 0;
                startY = 0;
                endX = 0;
                endY = 0;
                repaint();
            }
        });
        this.add(rectangleButton);

        ovalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isOvalDraw = true;
                isRecDraw = false;
                isLineDraw = false;
                isFreeHand = false;
                isEraser = false;                
            }
        });
        this.add(ovalButton);

        lineButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isOvalDraw = false;
                isRecDraw = false;
                isLineDraw = true;
                isFreeHand = false;
                isEraser = false;
            }
        });
        this.add(lineButton);
        
        freeHand=new JButton("Free Hand");
        lines=new ArrayList<ArrayList<Point>>();
        points=new ArrayList<Point>();
        prevColors=new ArrayList<Color>();
        freeHand.setBounds(200, 500, 100, 50);

        freeHand.addActionListener(new ActionListener() {
    
            @Override
            public void actionPerformed(ActionEvent e) {
                isFreeHand=true;
                isOvalDraw = false;
                isRecDraw = false;
                isLineDraw = false;
                isEraser = false;
            }
            
        });
        add(freeHand);

        eraser=new JButton("Eraser");
        eraser.setBounds(200, 500, 100, 50);
        eraser.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor=getBackground();
                isOvalDraw = false;
                isRecDraw = false;
                isLineDraw = false;
                isFreeHand = false;
                isEraser = true;
            }
            
        });
        add(eraser);
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shapes.clear();
                lines.clear();
                points.clear();
                currentShape = null;
                isCleared= !isCleared;
                repaint();
            }
        });
        this.add(clearButton);
       
        undoButton.addActionListener(new ActionListener()
       {
           public void actionPerformed(ActionEvent e)
           {
            if (!shapes.isEmpty()) {
               undoLastAction(); 
                currentShape = null;               
            } else if (!lines.isEmpty()) {
                lines.remove(lines.size() - 1);
                prevColors.remove(prevColors.size() - 1);
            }
            repaint();               
           }
       });
     this.add(undoButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveImage();
            }
        });
        this.add(saveButton);

        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openImage();
            }
        });
        this.add(openButton);

        // All Colors Things
        blueButton = new JButton();
        blueButton.setBackground(Color.BLUE);
        blueButton.setBorderPainted(false);
        blueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.BLUE;
            }
        });
        this.add(blueButton);

        orangeButton = new JButton();
        orangeButton.setBackground(Color.ORANGE);
        orangeButton.setBorderPainted(false);
        orangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.ORANGE;
            }
        });
        this.add(orangeButton);

        greenButton = new JButton();
        greenButton.setBackground(Color.GREEN);
        greenButton.setBorderPainted(false);
        greenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.GREEN;
            }
        });
        this.add(greenButton);

        redButton = new JButton();
        redButton.setBackground(Color.RED);
        redButton.setBorderPainted(false);
        redButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.RED;
            }
        });
        this.add(redButton);

        defaultColorButton = new JButton();
        defaultColorButton.setBackground(Color.BLACK);
        defaultColorButton.setBorderPainted(false);
        defaultColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentColor = Color.BLACK;
            }
        });
        this.add(defaultColorButton);

        dottedCheckBox = new JCheckBox("Dotted");
        dottedCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDotted = dottedCheckBox.isSelected();
            }
        });
        this.add(dottedCheckBox);

        filledCheckBox = new JCheckBox("Filled");
        filledCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isFilled = filledCheckBox.isSelected();
            }
        });
        this.add(filledCheckBox);

        setFocusable(true);
    }

     private void undoLastAction()
    {
       if (!shapes.isEmpty())
       {
           shapes.remove(shapes.size() - 1);  
           repaint();  
       }
    }

     @Override
    public void mousePressed(MouseEvent e) {
        if (isRecDraw || isOvalDraw || isLineDraw) {
            startX = e.getX();
            startY = e.getY();
        }
        if (isFreeHand || isEraser) {
            prevColors.add(currentColor);
            points.add(e.getPoint());
            repaint();
        }
        currentShape = new Shape(isRecDraw, isOvalDraw, isLineDraw, startX, startY, startX, startY, currentColor, isDotted, isFilled, isFreeHand, new ArrayList<>(points));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isRecDraw || isOvalDraw || isLineDraw) {
            endX = e.getX();
            endY = e.getY();
            shapes.add(new Shape(isRecDraw, isOvalDraw, isLineDraw, startX, startY, endX, endY, currentColor, isDotted, isFilled, false, null));
            repaint();
        }
        if (isFreeHand || isEraser) {
            Shape freehandShape = new Shape(false, false, false, 0, 0, 0, 0, currentColor, false, false, true, new ArrayList<>(points));
            shapes.add(freehandShape);
            points.clear();
        }
        if (isEraser){
        isEraser = false;
        currentColor = Color.BLACK;
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
    if (isFreeHand || isEraser) {
        points.add(e.getPoint());
        currentShape = new Shape(false, false, false, 0, 0, 0, 0, currentColor, false, false, true, new ArrayList<>(points));
        repaint();
    } 
    else if (isRecDraw || isOvalDraw || isLineDraw) {
        endX = e.getX();
        endY = e.getY();
        if (currentShape != null) {
            currentShape.x2 = endX;
            currentShape.y2 = endY;
        }
        repaint();
    }

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (imageLoaded) {
            g.drawImage(loadedImage, 0, 0, getWidth(), getHeight(), this);
        }

        for (Shape shape : shapes) {
            shape.draw(g);
        }
        
        if (currentShape != null) {
            currentShape.draw(g);
        }
     
}

    // Method to save the current drawing as an image
    private void saveImage() {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        paint(g2d);
        g2d.dispose();

        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                ImageIO.write(image, "PNG", file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Method to open an image and load it into the panel
    private void openImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                loadedImage = ImageIO.read(file);
                imageLoaded = true;
                shapes.clear();
                repaint();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    // Shape class to store shape information
    private class Shape {
        boolean isRectangle;
        boolean isOval;
        boolean isLine;
        boolean isFreeHand;
        int x1, y1, x2, y2;
        Color color;
        boolean isDotted;
        boolean isFilled;
        List<Point>points;
        
        public Shape(boolean isRectangle, boolean isOval, boolean isLine, int x1, int y1, int x2, int y2, Color color, boolean isDotted, boolean isFilled,boolean isFreeHand , List<Point>points) {
            this.isRectangle = isRectangle;
            this.isOval = isOval;
            this.isLine = isLine;
            this.isFreeHand = isFreeHand;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.color = color;
            this.isDotted = isDotted;
            this.isFilled = isFilled;
            this.points=points;
        }

        public void draw(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(color);

            Stroke oldStroke = g2d.getStroke();
            if (isDotted) {
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, new float[]{2}, 0));
            }
            if (isFreeHand && points != null) {
                for (int i = 1; i < points.size(); i++) {
                Point p1 = points.get(i - 1);
                Point p2 = points.get(i);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
            if (isRectangle) {
                int width = Math.abs(x2 - x1);
                int height = Math.abs(y2 - y1);
                int x = Math.min(x1, x2);
                int y = Math.min(y1, y2);
                if (isFilled) {
                    g2d.fillRect(x, y, width, height);
                } else {
                    g2d.drawRect(x, y, width, height);
                }
            } else if (isOval) {
                int width = Math.abs(x2 - x1);
                int height = Math.abs(y2 - y1);
                int x = Math.min(x1, x2);
                int y = Math.min(y1, y2);
                if (isFilled) {
                    g2d.fillOval(x, y, width, height);
                } else {
                    g2d.drawOval(x, y, width, height);
                }
            } else if (isLine) {
                g2d.drawLine(x1, y1, x2, y2);
            }

            g2d.setStroke(oldStroke);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}