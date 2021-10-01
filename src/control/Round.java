package control;

import entity.Ant;
import entity.Stick;

public class Round {
    private int id;//本轮的序号
    private int tick;//单位时间片
    private int allStatus;//五位二进制，记录所有蚂蚁是否在木板上，如果蚂蚁在木板上为 1，否则为 0
    private int allDirections;//用一个五位二进制来表示所有蚂蚁的朝向（1表示右边，0表示左边）
    private boolean roundStatus;//用一个布尔值表示该round是否已经结束 false表示结束
    private Ant[]ants;//所有ant对象的数组
    private Stick stick;//木杆对象
    private boolean []mrk;//表示每个时间片里面第i只蚂蚁是否需要掉头



    //Round的构造函数
    public Round(int id, int allStatus, int allDirections, Ant[] ants, Stick stick) {
        this.id = id;
        this.allStatus = allStatus;
        this.allDirections = allDirections;
        this.ants=new Ant[ants.length];
        this.ants = ants;
        this.stick = stick;
        this.roundStatus=true;
    }

    //round结束调用该方法判断该round是否结束
    public boolean isRoundStatus() {
        return roundStatus;
    }

    //返回本轮游戏所用时间
    public int roundResult() {
        return tick;
    }


    //初始化每一轮的tick
    public void init() {
        this.tick=0;
        this.roundStatus=true;
        this.mrk=new boolean[ants.length];
        for(int i=0;i<ants.length;i++){
            //allDirections中0表示向左，1表示向右
            //初始化蚂蚁的方向，对于蚂蚁的direction，为-1是向左，为1是向右。
            int dir=1;
            if(((1<<i)&this.allDirections)==0)dir=-1;
            ants[i].setDirection(dir);
        }

    }
    //进行每一时间片的操作，包括检测碰撞和之后的蚂蚁运动、退出
    public void play(){
            this.tick++;
            //mrk[i]=false将所有蚂蚁的是否改变方向的标记值改为false
            for(int i=0;i<ants.length;i++)this.mrk[i]=false;
            //通过每两只蚂蚁的运动轨迹判断其是否会碰撞（不讨论三只蚂蚁碰撞的问题）
            for(int i=0;i<ants.length;i++){
                for(int j=i+1;j<ants.length;j++){
                    //如果蚂蚁已经在杆子的外边，则不讨论碰撞
                    if(ants[i].isExit(stick.getLength()) || ants[j].isExit(stick.getLength()))continue;
                    //预先模拟两只蚂蚁前进1tick的位置
                    int k1=ants[i].preCreeping();
                    int k2=ants[j].preCreeping();
                    //如果两只蚂蚁方向不同并且在同一个位置，则两只蚂蚁肯定为碰撞（方向为背离之前一定存在碰撞）
                    if(ants[i].getPosition()==ants[j].getPosition()&&ants[i].getDirection()!=ants[j].getDirection()){
                        mrk[i]=!mrk[i];
                        mrk[j]=!mrk[j];
                    }
                    //如果两只蚂蚁方向不同并且不在同一个位置，则判断两个蚂蚁的之后的位置和之前的位置大小是否相反
                    else if(ants[i].getDirection()!=ants[j].getDirection()&&(ants[i].getPosition()-ants[j].getPosition())*(k1-k2)<0){
                        mrk[i]=!mrk[i];
                        mrk[j]=!mrk[j];
                    }
                }
            }
            //如果标记值为true，则代表蚂蚁需要改变方向；并且向之后的方向爬行。
            for(int i=0;i<ants.length;i++){
                if(ants[i].isExit(stick.getLength()))continue;
                if(mrk[i]){
                    ants[i].changeDirection();
                }
                ants[i].creeping();
                //继续向正确方向处爬行
            }

            //更新前端的数据
            for(int i=0;i<ants.length;i++){
                if(ants[i].isExit(this.stick.getLength())&&((this.allStatus&(1<<i))!=0)){
                    this.allStatus=this.allStatus-(1<<i);
                }
            }
            if(this.allStatus==0)this.roundStatus=false;
    }

}
