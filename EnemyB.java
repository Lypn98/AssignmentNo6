import java.awt.*;
class EnemyB extends MovingObject {

  // コンストラクタ(初期値設定)
  EnemyB(int apWidth, int apHeight) {
    super(apWidth, apHeight); // スーパークラス(ObjBase)のコンストラクタの呼び出し
    w = 30;
    h = 30;
    hp = 0;  // 初期状態では全て死亡
  }
  // ○を描き更新するメソッド
  void move(Graphics buf, int apWidth, int apHeight) {
    buf.setColor(Color.black); // gc の色を黒に
    if (hp>0) { // もし生きていれば
      //buf.fillOval(x - w, y - h, 33, 33); // gc を使って○を描く
      x = x + dx; // 座標値の更新
      y = y + dy; // 座標値の更新
      if (y>apHeight+h)
	hp = 0;
    }
  }
  void revive(int apWidth, int apHeight) { // 敵を新たに生成（再利用）
    x = (int)(Math.random()*(apWidth-2*w)+w);
    y = -h;
    dy = 7;
    if (x<apWidth/2)
      dx = (int)(Math.random()*2)*2;
    else
      dx = -(int)(Math.random()*2)*2;
    hp = 3;
  }
}


