package entity;

import java.awt.*;

public class Ant {
    private int velocity;//每个时间片移动的距离
    private Image img;//每个蚂蚁的图片
    private int id;//每个蚂蚁的编号

    public int getDirection() {
        return direction;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getVelocity() {
        return velocity;
    }

    private int direction;//每个蚂蚁的方向，用1表示向右，用-1表示向左
    private int position;//每个蚂蚁当前的位置

    public Ant(int id, int velocity, int position) {
        this.velocity = velocity;
        this.id = id;
        this.position = position;
    }

    public int preCreeping() {
        return velocity * direction + position;
    }

    //蚂蚁移动到下一个时间片后的位置（实现position+=direction*velocity）
    public void creeping() {
        this.position = this.position + this.velocity * this.direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getPosition() {
        return position;
    }



    //改变蚂蚁的 direction
    public void changeDirection() {
        this.direction = -this.direction;

    }

    //判断蚂蚁是否处于退出状态（极限位置+朝向）
    public boolean isExit(int stickLen) {
        if (this.position <= 0 || this.position >= stickLen)
            return true;
        return false;
    }
}
