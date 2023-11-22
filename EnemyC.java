import java.awt.*;

class EnemyC extends MovingObject{
    EnemyC(int apWidth,int apHeight){
        super(apWidth, apHeight);
        w = 60;
        h = 60;
        hp = 0;
    }

    void move(Graphics buf, int apWidth, int apHeight){
        buf.setColor(Color.red);
        if(hp>0){
            //buf.fillOval(x-w,y-w,50,50);
            x = x + dx;
            y = y + dy;
            if(y>apHeight+h)
        hp = 0;
        }
    }
     void revive(int apWidth, int apHeight) { // 敵を新たに生成（再利用）
    x = (int)(Math.random()*(apWidth-2*w)+w);
    y = -h;
    dy = 8;
    if (x<apWidth/2)
      dx = (int)(Math.random()*2)*5;
    else
      dx = -(int)(Math.random()*2)*5;
    hp = 8;
  }
}

    