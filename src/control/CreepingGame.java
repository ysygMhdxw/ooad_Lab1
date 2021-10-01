package control;

import entity.Ant;
import entity.Stick;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class CreepingGame extends Frame {
    private static int antNum;//蚂蚁的总数量
    private static int antVelocity = 5;//蚂蚁的速度
    private static int stickLen;//木杆的长度
    private static int[] antPosition;//蚂蚁的初始位置
    private static int roundNum;//代表局数
    private static Round round;//代表当前正在进行的局

    private Ant[] ants;//所有ant对象的数组
    private final Stick stick;//木杆对象
    private int[] roundTimes = new int[32];//一个数据记录所有轮数用时
    //蚂蚁的图片（两种方向）
    Image[] img1 = new Image[5];//表示向右走的蚂蚁
    Image[] img2 = new Image[5];//表示向左走的蚂蚁

    private int minTime = Integer.MAX_VALUE;//最短用时
    private int maxTime = Integer.MIN_VALUE;//最长用时

    private boolean gameOver=false;
    private static Thread thread;//表示新建的画图线程


    public CreepingGame(int antNum, int antVelocity, int stickLen, Ant[] ants) {
        CreepingGame.antNum = antNum;
        CreepingGame.antVelocity = antVelocity;
        CreepingGame.stickLen = stickLen;
        this.ants = new Ant[antNum];
        this.ants = ants;
        this.stick = new Stick(stickLen);
        roundNum = 0;
        this.roundTimes = new int[(1 << antNum)];
        antPosition = new int[antNum];
        for (int i = 0; i < antNum; i++)
            antPosition[i] = this.ants[i].getPosition();
    }

    private void initImg() {
        //读取蚂蚁图片
        //朝向左边的蚂蚁
        for (int i = 1; i <= 5; i++) {
            try {
                this.img1[i - 1] = ImageIO.read(new File("resources/ant" + i + "-1.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //朝向右边的蚂蚁
        for (int i = 1; i <= 5; i++) {
            try {
                this.img2[i - 1] = ImageIO.read(new File("resources/ant" + i + "-2.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //生成所有的蚂蚁对象
    public void init() {
        //扩展：对常量antNum, antVelocity, antPosition的处理改成输入
        //新建frame框架
        setSize(800, 600); // 设置窗口大小
        setTitle("蚂蚁游戏"); // 设置窗口标题
        setLocation(590, 300); // 窗口初始位置
        setFont(new Font("宋体", Font.BOLD, 22));

        setResizable(false); // 设置窗口大小不可变

        // 添加关闭窗口事件（监听窗口发生的事件，派发给参数对象，参数对象调用对应的方法）
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setVisible(true); // 窗口默认为不可见，设置为可见
        //初始化图片
        initImg();
        gameOver=false;
        //新建蚂蚁对象
        ants = new Ant[antNum];
        for (int i = 0; i < antNum; i++) {
            ants[i] = new Ant(i, antVelocity, antPosition[i]);
        }
        round = new Round(roundNum, (1 << antNum) - 1, 0, ants, stick);
        round.init();

        thread=new Thread(() -> {
            while (true) {
                if (!round.isRoundStatus()) {
                    this.roundTimes[roundNum] = round.roundResult();
                    updatedata(round.roundResult());
                    if (roundNum != (1 << antNum) - 1) {
                        roundNum++;
                        for (int i = 0; i < antNum; i++) {
                            ants[i] = new Ant(i, antVelocity, antPosition[i]);
                        }
                        round = new Round(roundNum, (1 << antNum) - 1, roundNum, ants, stick);
                        round.init();
                    }
                }
                //所有蚂蚁走一个时间片
                if(!gameOver){
                    round.play();
                }
                repaint(); // 通过调用repaint(),让JVM调用update()

                try {
                    Thread.sleep(40);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();

    }
    //更新最大时间和最小时间
    public void updatedata(int newtime) {
        this.minTime = min(newtime, minTime);
        this.maxTime = max(newtime, maxTime);
    }


    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (roundNum + 1 < (1 << antNum)) {
            g.drawString("局数为：" + roundNum, 50, 100);
            g.drawString("蚂蚁数量为：" + antNum, 50, 130);
            StringBuilder totpos = new StringBuilder();
            for (int i = 0; i < antNum; i++)
                totpos.append(this.ants[i].getPosition()).append(" ");
            g.drawString("每一只蚂蚁的位置为：" + totpos, 50, 160);
            g.drawString("蚂蚁的速度为：" + ants[0].getVelocity(), 50, 190);
            StringBuilder totdir = new StringBuilder();
            for (int i = 0; i < antNum; i++)
                totdir.append(this.ants[i].getDirection()).append(" ");
            g.drawString("蚂蚁的方向为：" + totdir, 50, 220);
            g.drawString("木杆的长度为：" + stickLen, 50, 250);


            String mini = String.valueOf(this.minTime);
            if (this.minTime == Integer.MAX_VALUE) mini += "(初始值)";
            g.drawString("目前最短时间为：" + mini, 50, 280);


            String maxi = String.valueOf(this.maxTime);
            if (this.maxTime == Integer.MIN_VALUE) maxi += "(初始值)";
            g.drawString("目前最长时间为：" + maxi, 50, 310);

            String time = "";
            for (int i = 0; i < roundTimes.length; i++) {
                time += roundTimes[i] + " ";
            }
            if(time.length()>50){
                g.drawString("每一局的时间：" + time.substring(0,40), 50, 340);
                g.drawString( time.substring(41), 50, 370);
            }


            if (ants != null) {
                for (int i = 0; i < antNum; i++) {
                    if (!ants[i].isExit(stickLen)) {
                        if (ants[i].getDirection() == -1) {
                            g.drawString("蚂蚁" + i, ants[i].getPosition() * 2+50, 390);
                            g.drawImage(img1[i], ants[i].getPosition() * 2+50, 400, this);
                        } else {
                            g.drawString("蚂蚁" + i, ants[i].getPosition() * 2+50, 390);
                            g.drawImage(img2[i], ants[i].getPosition() * 2+50, 400, this);
                        }
                    }
                }
            }

            g.setColor(Color.ORANGE);
            g.fillRect(50, 460, stickLen*2, 10);
        } else {
            gameOver=true;
            g.drawString("游戏结束！", 100, 200);
        }
    }


}
