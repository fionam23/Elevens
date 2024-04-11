package CardDemo_src;


import org.w3c.dom.css.Rect;


import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Arrays;


class DrawPanel extends JPanel implements MouseListener {


   private ArrayList<Card> hand;
   private Rectangle button;
   private Rectangle replaceButton;


   private ArrayList<Card> Highlighted;
   private int cardsLeft;


   public DrawPanel() {
       button = new Rectangle(300, 100, 160, 26);
       replaceButton = new Rectangle(100,300, 160, 26);
       this.addMouseListener(this);
       hand = Card.buildHand();
       Highlighted = new ArrayList<>();
       cardsLeft = 43;
   }


   protected void paintComponent(Graphics g) {
       super.paintComponent(g);
       drawArray(g);
       g.setFont(new Font("Courier New", Font.BOLD, 20));
       if(lose()){
           g.drawString("No available moves! Try again!", (int) (replaceButton.getX()), (int) (replaceButton.getY()+replaceButton.getHeight()+20));
       }

       g.drawString("PLAY AGAIN", (int) button.getX(), (int) (button.getY()+button.getHeight()));
       g.drawRect((int)button.getX(), (int)button.getY(), (int)button.getWidth(), (int)button.getHeight()-3);
       g.drawString("REPLACE CARDS", (int) replaceButton.getX(), (int) (replaceButton.getY()+replaceButton.getHeight()-3));
       g.drawRect((int)replaceButton.getX(),(int)replaceButton.getY(),(int)replaceButton.getWidth(), (int)replaceButton.getHeight());
       g.drawString("Cards left: "+cardsLeft,100, (int) this.getBounds().getHeight()-10);


   }


   public void mousePressed(MouseEvent e) {


       Point clicked = e.getPoint();


       if (e.getButton() == 1) {
           if (button.contains(clicked)) {
               hand = Card.buildHand();
               cardsLeft = 47;
           }
           if(replaceButton.contains(clicked)){
               if(sumsToEleven(Highlighted)){
                   cardsLeft-=Highlighted.size();
               }
               for(int c = 0 ; c<Highlighted.size(); c++){
                   Highlighted.get(c).replaceCard();
                   Highlighted.get(c).flipHighlight();
                   Highlighted.remove(c);
                   c--;
               }
               if(lose()){
                   System.out.println("Lose");
               }
           }


           for (int i = 0; i < hand.size(); i++) {
               Rectangle box = hand.get(i).getCardBox();
               if (box.contains(clicked)) {
                   hand.get(i).flipCard();
               }
           }
       }


       if (e.getButton() == 3) {
           for (int i = 0; i < hand.size(); i++) {
               Rectangle box = hand.get(i).getCardBox();
               if (box.contains(clicked)) {


                   if(hand.get(i).getHighlight()){
                       hand.get(i).replaceCard();
                   } else{
                       if(Highlighted.size()<3){
                           hand.get(i).flipHighlight();
                           Highlighted.add(hand.get(i));
                       }


                   }
               }
           }
       }




   }


   public void mouseReleased(MouseEvent e) { }
   public void mouseEntered(MouseEvent e) { }
   public void mouseExited(MouseEvent e) { }
   public void mouseClicked(MouseEvent e) { }

    private boolean sumsToEleven(ArrayList<Card> selected){
       int sum = 0;
       int j = 0;
       int q = 0;
       int k = 0;
       for(Card c:selected){
           if(c.getValue().equals("J")|| c.getValue().equals("Q")||c.getValue().equals("K")){
               if(c.getValue().equals("J")){
                   j = 1;
               }
               if(c.getValue().equals("Q")){
                   q = 1;
               }
               if(c.getValue().equals("K")){
                   k = 1;
               }
           }
           else if(selected.size()==2){
               if(c.getValue().equals("A")){
                   sum+=1;
               } else {
                   sum+=Integer.parseInt(c.getValue());
               }
           }
       }
        if(j == 1 && q == 1 && k == 1){
            return true;
        } else if (sum== 11) {
            return true;
        }
        return false;
    }
    private boolean lose(){
       for(int i = 0; i< hand.size()-1;i++){
           ArrayList<Card> possibleMatch = new ArrayList<>();
           possibleMatch.add(hand.get(i));
           for(int a = i+1; a< hand.size();a++){
               possibleMatch.add(hand.get(a));
               if(sumsToEleven(possibleMatch)){
                   return false;
               }
               possibleMatch.remove(possibleMatch.size()-1);
           }
       }
       for(int i = 0; i< hand.size()-2; i++){
           ArrayList<Card> possibleMatch = new ArrayList<>();
           possibleMatch.add(hand.get(i));
           for(int a = i+1; a< hand.size()-1;a++){
               possibleMatch.add(hand.get(a));
               for(int b = a+1; b< hand.size(); b++){
                   possibleMatch.add(hand.get(b));
                   if(sumsToEleven(possibleMatch)){
                       return false;
                   }
               }
               possibleMatch.remove(possibleMatch.size()-2);
           }
       }
       return true;
    }
    private void drawArray(Graphics g){
        int x = 50;
        int y = 10;
        int columns = 0;
        for (int i = 0; i < hand.size(); i++) {
            columns++;
            Card c = hand.get(i);
            if (c.getHighlight()) {
                g.drawRect(x, y, c.getImage().getWidth(), c.getImage().getHeight());
            }
            c.setRectangleLocation(x, y);
            g.drawImage(c.getImage(), x, y, null);
            x = x + c.getImage().getWidth() + 10;
            if (columns == 3){
                x = 50;
                y = y + c.getImage().getHeight();
                columns = 0;
            }
        }
    }
}