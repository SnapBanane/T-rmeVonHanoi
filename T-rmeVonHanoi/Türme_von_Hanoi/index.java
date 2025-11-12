
/**
 * Klasse index 
 *
 * Collien, Celine, Timo, Julius
 * version 0.0.1
 */

//Import
import basis.*;
import java.awt.*;

public class index extends Fenster implements KnopfLauscher
{
    //Deklaration
    private Knopf ende, tower1, tower2, tower3;
    private Knopf disc1 = new Knopf("1");
    private Knopf disc2 = new Knopf("2");
    private Knopf disc3 = new Knopf("3");
    private Knopf disc4 = new Knopf("4");
    private ZahlenFeld count;

    //positions
    int[] t_pos = {40, 240, 440};
    int[] heights = {100, 130, 160, 190};

    //Disc positions
    int[] x_pos = {40, 40, 40, 40};
    int[] y_pos = {100, 130, 160, 190};

    //which disc is on which tower
    int[] towers = {1, 1, 1, 1};
    
    //moves
    int moveCount = 0;

    int lastClickedButton;

    // Konstruktor
    public index()
    {
        System.out.println("Startup-------------------------------------------------------");
        this.setzeGroesse(600,500);
        this.setzeTitel("index");
        ende = new Knopf("Ende",490,460,100,30);
        ende.setzeKnopfLauscher(this);
        tower1 = new Knopf("tower 1",40,220,120,30);
        tower1.setzeKnopfLauscher(this);
        tower2 = new Knopf("tower 2",240,220,120,30);
        tower2.setzeKnopfLauscher(this);
        tower3 = new Knopf("tower 3",440,220,120,30);
        tower3.setzeKnopfLauscher(this);
        disc1.setzeKnopfLauscher(this);
        disc2.setzeKnopfLauscher(this);
        disc3.setzeKnopfLauscher(this);
        disc4.setzeKnopfLauscher(this);
        count = new ZahlenFeld(240,280,120,30);

        this.updateGui();
    }

    public void updateGui() 
    {
        disc1.setzePosition(x_pos[0]+45, y_pos[0]);
        disc1.setzeGroesse(30,30);
        disc2.setzePosition(x_pos[1]+30, y_pos[1]);
        disc2.setzeGroesse(60,30);
        disc3.setzePosition(x_pos[2]+15, y_pos[2]);
        disc3.setzeGroesse(90,30);
        disc4.setzePosition(x_pos[3], y_pos[3]);
        disc4.setzeGroesse(120,30);
        count.setzeZahl(moveCount);
    }

    @Override
    public void bearbeiteKnopfDruck(Knopf k)
    {
        if (k ==ende)
        {
            this.gibFrei();
        } 
        else if (k ==disc1)
        {
            lastClickedButton = 1;
        } 
        else if (k ==disc2)
        {
            lastClickedButton = 2;
        }
        else if (k ==disc3)
        {
            lastClickedButton = 3;
        }
        else if (k ==disc4)
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
    }

    public void updatePosition(int disc, int r_tower){
        x_pos[disc-1] = t_pos[r_tower-1];
        int q = 0;
        for(int i = disc+1; i <= 4; i++){
            if(towers[i-1] == r_tower){
                q++;
            }
        }
        y_pos[disc-1] = heights[3-q];
        updateGui();
        System.out.println(y_pos[0]+","+y_pos[1]+","+y_pos[2]+","+y_pos[3]);
        System.out.println(heights[0]+","+heights[1]+","+heights[2]+","+heights[3]);
    }

    public void move(int disc, int r_tower){
        if(checkRules(disc, r_tower))
        {
            towers[disc-1] = r_tower;
            System.out.println(towers[0]+","+towers[1]+","+towers[2]+","+towers[3]);
            moveCount++;
            System.out.println("Move Count: " + moveCount);
            updatePosition(disc, r_tower);
        }
    }

    public boolean checkRules(int disc, int r_tower){
        int s_tower;

        s_tower = towers[disc-1];
        
        for(int i = disc-1; i >= 1; i--){
            if(towers[i-1] == s_tower){
                System.out.println("false -> not topmost disc");
                return false;
            }

            if(towers[i-1] == r_tower){
                System.out.println("false -> smaller disc on recieving tower");
                return false;
            }
        }

        System.out.println("true");
        return true;
    }
}
