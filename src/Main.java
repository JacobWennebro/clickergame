import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class Task extends TimerTask {
    // run is a abstract method that defines task performed at scheduled time.
    public void run() {
        if(ClickerGame.CPS > ClickerGame.CPSHighscore || ClickerGame.CPSHighscore.equals(0)) {
            ClickerGame.CPSHighscore = ClickerGame.CPS;
        }

        ClickerGame.CPS = 0;
        ClickerGame.button.setText("CPS: " + ClickerGame.CPS);
    }
}

class TaskScheduling {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new Task(), 1000, 1000);
    }
}

class ClickerGame implements ActionListener {
    JFrame frame=new JFrame();
    static JButton button=new JButton("Click!");
    int clicks=0;
    String OS=System.getProperty("os.name");
    static Byte CPS = 0;
    static Byte CPSHighscore = 0;
    static JPanel panel = new JPanel();
    static JButton reset = new JButton("RESET");
    static JButton stop = new JButton("STOP");

    ClickerGame(){
        URL iconURL = getClass().getResource("/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);

        frame.setIconImage(icon.getImage());
        prepareGUI();
        uiProperties();
        TaskScheduling.main(null);
    }

    public static void createSaveFile() {
        try {
            File myObj = new File(".cgsave");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
                writeToSaveFile(0, 0);
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void writeToSaveFile(int score, int highscore) {
        try {
            FileWriter myWriter = new FileWriter(".cgsave");
            myWriter.write("s="+ score);
            myWriter.write(",cpshs="+ highscore);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static String readSaveFile() {
        try {
            File myObj = new File(".cgsave");
            Scanner myReader = new Scanner(myObj);
            return myReader.nextLine();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return "error";
        }
    }

    public Color ColorRandomizer() {
        Random rand = new Random();

        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        return Color.getHSBColor(r, g, b);
    }

    public void prepareGUI(){
        createSaveFile();
        String savedata = readSaveFile();
        String[] saveParameters = savedata.split(",");

        for(int i=0; i < saveParameters.length; i++) {
            String type = saveParameters[i].substring(0, saveParameters[i].indexOf("="));
            String value = saveParameters[i].substring(saveParameters[i].indexOf("=")+1);

            switch(type) {
                case "s":
                    clicks = Integer.parseInt(value);
                    break;
                case "cpshs":
                    CPSHighscore = Byte.parseByte(value);
                    break;
            }
        }

        frame.setTitle("Clicker Game : " + clicks + " | Best CPS : " + CPSHighscore + " [" + OS + " Edition]");
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);
        frame.setBounds(200,200,600,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public void uiProperties(){
        button.setBounds(0,0,frame.getBounds().width, frame.getBounds().height-100);
        frame.add(button);
        button.setForeground(Color.WHITE);
        button.setBackground(this.ColorRandomizer());
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 65));
        button.addActionListener(this::actionPerformed);//Registering ActionListener to the button

        panel.add(reset);
        reset.addActionListener(this::resetGame);
        reset.setFont(new Font("Comic Sans MS", Font.BOLD, 10));
        reset.setBackground(Color.RED);
        reset.setForeground(Color.WHITE);

        frame.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.SOUTH);
    }

    public void resetGame(ActionEvent e) {
        if(clicks > 0) {
            clicks = 0;
            CPSHighscore = 0;
            button.setBackground(Color.BLUE);
            frame.setTitle("Clicker Game : " + clicks + " | Best CPS : " + CPSHighscore + " [" + OS + " Edition]");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clicks++;
        writeToSaveFile(clicks, CPSHighscore);
        CPS++;

        if(CPS > 20) {
            frame.setTitle("Fuck you cheater | ClickerGame AC");
            frame.remove(button);
            return;
        }

        button.setText("CPS: " + CPS);
        button.setBackground(this.ColorRandomizer());
        button.setBounds(0,0,frame.getBounds().width, frame.getBounds().height-100);
        frame.setTitle("Clicker Game : " + clicks + " | Best CPS : " + CPSHighscore + " [" + OS + " Edition]");
    
    }
}

public class Main {

    public static void main(String[] args) {
        new ClickerGame();
    }

}