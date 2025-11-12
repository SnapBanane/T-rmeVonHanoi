/*
 * Klasse index 
 *
 * Collien, Celine, Timo, Julius
 * version 0.0.2
 */

// Import
import basis.*;

public class index extends Fenster implements KnopfLauscher
{

    // Declaration
    private final Knopf ende, tower1, tower2, tower3;
    private final Knopf aiButton; // neuer AI-Knopf
    private final BeschriftungsFeld count = new BeschriftungsFeld("Züge: 0", 250, 70, 100, 30);
    private final Knopf disc0 = new Knopf("1");
    private final Knopf disc1 = new Knopf("2");
    private final Knopf disc2 = new Knopf("3");
    private final Knopf disc3 = new Knopf("4");


    //tower positions
    private final int[] t_pos = {40, 240, 440};
    
    // Disc positions
    int[] x_pos = {40, 40, 40, 40};
    int[] y_pos = {100, 130, 160, 190};

    int[] heights = {190, 160, 130, 100};
    
    // Which disc is on which tower?
    int[] positions = {1, 1, 1, 1};

    int lastClickedButton;
    int moveCount = 0;

    // Letzte ausgeführte Bewegung: Disc und deren Quell-Tower (wird nach move() gesetzt)
    int lastMoveDisc = 0;
    int lastMoveSourceTower = 0;

    // Constructor
    public index()
    {
        this.setzeGroesse(600,500);
        this.setzeTitel("index");
        ende = new Knopf("Ende",490,460,100,30);
        ende.setzeKnopfLauscher(this);
        tower1 = new Knopf("macht nix",40,220,120,30);
        tower1.setzeKnopfLauscher(this);
        tower2 = new Knopf("macht nix",240,220,120,30);
        tower2.setzeKnopfLauscher(this);
        tower3 = new Knopf("macht nix",440,220,120,30);
        tower3.setzeKnopfLauscher(this);

        aiButton = new Knopf("AI", 490, 420, 100, 30); // Position oberhalb "Ende"
        aiButton.setzeKnopfLauscher(this);

        disc0.setzeKnopfLauscher(this);
        disc1.setzeKnopfLauscher(this);
        disc2.setzeKnopfLauscher(this);
        disc3.setzeKnopfLauscher(this);

        this.updateGui();
    }

    public void updateGui() 
    {
        disc0.setzePosition(x_pos[0], y_pos[0]);
        disc0.setzeGroesse(120,30);
        disc1.setzePosition(x_pos[1], y_pos[1]);
        disc1.setzeGroesse(120,30);
        disc2.setzePosition(x_pos[2], y_pos[2]);
        disc2.setzeGroesse(120,30);
        disc3.setzePosition(x_pos[3], y_pos[3]);
        disc3.setzeGroesse(120,30);
        count.setzeText("Züge: " + moveCount);
    }
    
    @Override
    public void bearbeiteKnopfDruck(Knopf k)
    {
        if (k ==ende)
        {
            this.gibFrei();
        } 
        else if (k ==disc0)
        {
            lastClickedButton = 1;
        } 
        else if (k ==disc1)
        {
            lastClickedButton = 2;
        }
        else if (k ==disc2)
        {
            lastClickedButton = 3;
        }
        else if (k ==disc3)
        {
            lastClickedButton = 4;
        }
        else if (k ==tower1)
        {
            move(lastClickedButton, 1);
        }
        else if (k ==tower2)
        {
            move(lastClickedButton, 2);
        }
        else if (k ==tower3)
        {
            move(lastClickedButton, 3);
        }
        else if (k == aiButton)
        {
            // Starte die AI in einem separaten Thread, damit die GUI nicht blockiert
            new Thread(() -> {
                System.out.println("AI: starte ai(this)");
                new Ai().ai(this);
            }).start();
        }
    }

    /**
     * Update the position of a disc
     * @param x final x pos
     * @param y final y pos
     */
    public void updatePosition(int x, int y){
        x_pos[x-1] = t_pos[y-1];
        // Count how many discs are currently on tower y (excluding the moving disc)
        int count = 0;
        for (int i = 0; i < 4; i++) {
            if (i == x-1) continue;
            if (positions[i] == y) count++;
        }
        // Place the moving disc on the appropriate height based on count
        y_pos[x-1] = heights[count];
        updateGui();
        System.out.println(x_pos[0]+","+x_pos[1]+","+x_pos[2]+","+x_pos[3]);
    }

    /**
     * Move a disc to a tower
     */
    public void move(int x, int y){
        int s_tower = positions[x-1]; // Quell-Turm vor der Bewegung
        if(checkRules(x, y)){
            positions[x-1] = y;
            // speichere die Bewegung (Scheibe und deren vorherigen Turm), damit AI keine Umkehr macht
            lastMoveDisc = x;
            lastMoveSourceTower = s_tower;

            System.out.println(positions[0]+","+positions[1]+","+positions[2]+","+positions[3]);
            moveCount++;
            updatePosition(x, y);
        }
    }

    /**
     * Check the rules of the game
     * @param disc for which disc should the rules be checked?
     * @param r_tower the tower to which the disc should be moved
     * @return boolean true if the move is valid, false otherwise
     */
    public boolean checkRules(int disc, int r_tower){
        int s_tower = positions[disc-1];

        // describe attempted move for logging
        String moveDesc = "attempted move: disc " + disc + " -> tower " + r_tower + " (from tower " + s_tower + ")";

        // can't move to the same tower
        if (s_tower == r_tower) {
            System.out.println(moveDesc + " => false (same tower)");
            return false;
        }

        // 1) disc must be the topmost on its source tower:
        //    no disc with smaller index (i.e. smaller number = smaller size) may be on the same source tower
        for (int i = 0; i < disc - 1; i++) {
            if (positions[i] == s_tower) {
                System.out.println(moveDesc + " => false (not topmost on source, blocked by disc " + (i+1) + ")");
                return false;
            }
        }

        // 2) target tower: find the top disc (smallest index) on r_tower, if any
        int topDiscOnTarget = Integer.MAX_VALUE;
        for (int i = 0; i < positions.length; i++) {
            if (positions[i] == r_tower && (i + 1) < topDiscOnTarget) {
                topDiscOnTarget = i + 1;
            }
        }
        // if there's a disc on target, and it's smaller than the moving disc -> illegal
        if (topDiscOnTarget < disc) {
            System.out.println(moveDesc + " => false (can't place onto smaller disc " + topDiscOnTarget + ")");
            return false;
        }

        System.out.println(moveDesc + " => true");
        return true;
    }
}
