package control;

import entity.Ant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI {
    private static int antNum = 5;//蚂蚁的总数量
    private static Ant[] ants;//所有ant对象的数组
    private static int[] antPosition;//蚂蚁的初始位置
    private static int stickLen = 300;//木杆的长度
    private static int tickLen = 5;//每个tick的时间(单位时间内蚂蚁移动的距离 即蚂蚁的速度）

    private CreepingGame creepingGame;

    //GUI的frame
    Frame mainFrame = new Frame("蚂蚁游戏");

    //GUI上的控件
    Label antsNumlbel = new Label("蚂蚁数量：");
    TextField antsNumtxt = new TextField("", 50);//蚂蚁的数量
    Label antsPoslbel = new Label("蚂蚁位置：");
    TextField antsPostxt = new TextField("", 50);//蚂蚁的位置
    Label antsVeclbel = new Label("蚂蚁速度：");
    TextField antsVectxt = new TextField("", 50);//蚂蚁的速度 -1表示向左，1表示向右
    Label stickLenlbel = new Label("木杆长度：");
    TextField stickLentxt = new TextField("", 50);//木杆的长度


    Button startButton = new Button("开始这场游戏");//开始游戏的按钮
    Button endGameButton = new Button("结束整场游戏");//结束游戏的按钮


    //初始化构造函数
    public void GUIinit() {
        //定义frame
        this.creepingGame = new CreepingGame(antNum, tickLen, stickLen, ants);
        creepingGame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                mainFrame.setVisible(true);
            }
        });
//        this.creepingGame.drivingGame();
        this.creepingGame.init();
    }

    //设置输入数据的frame
    public void prepareGUI() {
        //制定窗口的位置和大小
        mainFrame.setLayout(new BorderLayout(300, 10));
        mainFrame.setLocation(500, 300);
        mainFrame.setSize(2000, 2000);
        mainFrame.setFont(new Font("Times New Roman", Font.BOLD, 30));

        /* -----提示文字部分------*/
        //新建panel容器，显示提示语
        //对于每一个label标签和textfield文本框对应 新建一个box容器 调整水平布局
        Panel p = new Panel();


        Box box2 = Box.createHorizontalBox();
        box2.add(antsNumlbel);
        box2.add(antsNumtxt);
        antsNumtxt.setText("5");

        Box box3 = Box.createHorizontalBox();
        box3.add(antsPoslbel);
        box3.add(antsPostxt);
        antsPostxt.setText("30 80 110 160 250");

        Box box4 = Box.createHorizontalBox();
        box4.add(antsVeclbel);
        box4.add(antsVectxt);
        antsVectxt.setText("5");

        Box box6 = Box.createHorizontalBox();
        box6.add(stickLenlbel);
        box6.add(stickLentxt);
        stickLentxt.setText("300");
        //对于所有的box容器采用竖直布局
        Box totbox = Box.createVerticalBox();
        totbox.add(box2);
        totbox.add(box3);
        totbox.add(box4);
        totbox.add(box6);

        Box alignbox = Box.createHorizontalBox();
        alignbox.add(new Label(""));
        totbox.add(alignbox);

        totbox.add(startButton);
        totbox.add(endGameButton);
        p.add(totbox);
        mainFrame.add(p, BorderLayout.NORTH);

        //开始游戏的注意监听
        startButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 读数据
                String input_num = antsNumtxt.getText();
                String input_pos = antsPostxt.getText();
                String input_stickLen = stickLentxt.getText();
                String input_velocity = antsVectxt.getText();

                boolean isvalid = true;//验证在上方的输入中是否有输入不合法现象存在

                ants = new Ant[antNum];
                antPosition = new int[antNum];

                try {
                    antNum = Integer.parseInt(input_num);
                    if (antNum > 5) {
                        throw new Exception();
                    }
                } catch (Exception ep) {
                    isvalid = false;
                    Dialog d1 = new Dialog(mainFrame, "警告", true);
                    d1.setBounds(600, 600, 800, 300);
                    Box tb1 = Box.createVerticalBox();
                    tb1.add(new Label("请检查蚂蚁数量的输入是否合法且蚂蚁的数量不能超过5！"));
                    d1.add(tb1);
                    d1.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent windowEvent) {
                            d1.setVisible(false);
                        }
                    });
                    d1.setVisible(true);
                }


                try {
                    tickLen = Integer.parseInt(input_velocity);//输入蚂蚁的速度
                } catch (Exception ep) {
                    isvalid = false;
                    Dialog d1 = new Dialog(mainFrame, "警告", true);
                    d1.setBounds(600, 600, 700, 200);
                    Box tb1 = Box.createVerticalBox();
                    tb1.add(new Label("请检查蚂蚁的速度输入是否合法！"));
                    d1.add(tb1);
                    d1.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent windowEvent) {
                            d1.setVisible(false);
                        }
                    });
                    d1.setVisible(true);
                }

                System.out.println(input_pos);
                try {
                    String[] tmp = input_pos.split(" ");
                    for (int i = 0; i < tmp.length; i++) {
                        antPosition[i] = Integer.parseInt(tmp[i]);
                        if (i == 0 && antPosition[i] % tickLen != 0) {
                            throw new Exception();
                        } else if (i != 0 && (antPosition[i] - antPosition[i - 1]) % tickLen != 0) {
                            throw new Exception();
                        }
                    }
                    //更新蚂蚁对象
                    for (int i = 0; i < antNum; i++) {
                        ants[i] = new Ant(i, tickLen, antPosition[i]);
                    }
                } catch (Exception ep) {
                    isvalid = false;
                    Dialog d1 = new Dialog(mainFrame, "警告", true);
                    d1.setBounds(600,600, 1000, 200);
                    Box tb1 = Box.createVerticalBox();
                    tb1.add(new Label("蚂蚁位置的输入不合法！请注意检查蚂蚁的位置是否是速度的整数倍。"));
                    d1.add(tb1);
                    d1.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent windowEvent) {
                            d1.setVisible(false);
                        }
                    });
                    d1.setVisible(true);
                }

                try {
                    stickLen = Integer.parseInt(input_stickLen);
                    for (int i = 0; i < antNum; i++) {
                        if (stickLen <= ants[i].getPosition()) {
                            throw new Exception();
                        }
                    }
                } catch (Exception ep) {
                    isvalid = false;
                    Dialog d1 = new Dialog(mainFrame, "警告", true);
                    d1.setBounds(600, 600, 700, 200);
                    Box tb1 = Box.createVerticalBox();
                    tb1.add(new Label("请检查木杆的输入是否正确！"));
                    d1.add(tb1);
                    d1.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent windowEvent) {
                            d1.setVisible(false);
                        }
                    });
                    d1.setVisible(true);
                }
                if (isvalid) {
                    GUIinit();

                }
            }
        });

        //结束游戏的注意监听
        endGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        //设置退出按钮
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        mainFrame.pack();
        //使窗口可见
        mainFrame.setVisible(true);
    }



}
