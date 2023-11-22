/**
 *  敵Ａ　のクラス
 *
 * @author fukai
 */

import java.awt.*;
class EnemyA extends MovingObject {

  // コンストラクタ(初期値設定)
  EnemyA(int apWidth, int apHeight) {
    super(apWidth, apHeight); // スーパークラス(ObjBase)のコンストラクタの呼び出し
    w = 12;
    h = 12;
    hp = 0;  // 初期状態では全て死亡
  }
  // ○を描き更新するメソッド
  void move(Graphics buf, int apWidth, int apHeight) {
    buf.setColor(Color.black); 
    if (hp>0) { // もし生きていれば
      //buf.drawOval(x - w, y - h, 21,21); // gc を使って○を描く
      x = x + dx; // 座標値の更新
      y = y + dy; // 座標値の更新
      if (y>apHeight+h)
	hp = 0;
    }
  }
  void revive(int apWidth, int apHeight) { // 敵を新たに生成（再利用）
    x = (int)(Math.random()*(apWidth-2*w)+w);
    y = -h;
    dy = 5;
    if (x<apWidth/2)
      dx = (int)(Math.random()*2);
    else
      dx = -(int)(Math.random()*2);
    hp = 1;
  }
}


