
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
    
       
    //tower positions
    int[] t_pos = {40, 240, 440};
    
    //Disc positions
    int[] d_pos = {40, 40, 40, 40};
    
    //which disc is on which tower
    int[] towers = {1, 1, 1, 1};
    
    int lastClickedButton;

    
    // Konstruktor
    
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
        disc1.setzeKnopfLauscher(this);
        disc2.setzeKnopfLauscher(this);
        disc3.setzeKnopfLauscher(this);
        disc4.setzeKnopfLauscher(this);

        this.updateGui();
    }

    public void updateGui() 
    {
        disc1.setzePosition(d_pos[0], 100);
        disc1.setzeGroesse(120,30);
        disc2.setzePosition(d_pos[1], 130);
        disc2.setzeGroesse(120,30);
        disc3.setzePosition(d_pos[2], 160);
        disc3.setzeGroesse(120,30);
        disc4.setzePosition(d_pos[3], 190);
        disc4.setzeGroesse(120,30);
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
    
    public void updatePosition(int x, int y){
        d_pos[x-1] = t_pos[y-1];
        updateGui();
        System.out.println(d_pos[0]+","+d_pos[1]+","+d_pos[2]+","+d_pos[3]);
    }
    
    public void move(int x, int y){
        if(checkRules(x, y)){
            towers[x-1] = y;
            System.out.println(towers[0]+","+towers[1]+","+towers[2]+","+towers[3]);
            updatePosition(x, y);
        }
    }
    
    public boolean checkRules(int disc, int r_tower){
        int s_tower;
        
        if(disc == 4)s_tower = towers[3];
        else s_tower = towers[disc];
        
        for(int i = disc-1; i >= 1; i--){
            if(towers[i-1] == s_tower){
                System.out.println("false");
                return false;
            }
            
            if(towers[i-1] == r_tower){
                System.out.println("false");
                return false;
            }
        }
        
        System.out.println("true");
        return true;
    }
}
