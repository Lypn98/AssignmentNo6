/**
 *  自機のクラス
 *
 * @author fukai
 */

import java.awt.*;

class Fighter extends MovingObject  {
  
  boolean lflag; // ←が押されているか否か
  boolean rflag; // →が押されているか否か
  boolean uflag; // ↑が押されているか否か
  boolean dflag; // ↓が押されているか否か
  boolean sflag; // スペースキーが押されているか否か
  int delaytime; // 次の弾を発射するまでの待ち時間

  // コンストラクタ
  Fighter(int apWidth, int apHeight) {
    // super(); //省略可
    x  = (int)(apWidth/2);    // 画面の真中
    y  = (int)(apHeight*0.9); // 画面の下の方
    dx = 5;
    dy = 5;
    w  = 10;
    h  = 10;
    lflag = false;
    rflag = false;
    uflag = false;
    dflag = false;
    sflag = false;
    delaytime = 5; // 弾の発射待ち時間．毎回１ずつ減り，０で発射可能になる
  }
  void revive(int apWidth, int apHeight) {
  }
  void move(Graphics buf, int apWidth, int apHeight) {
    buf.setColor(Color.blue);                 // gc の色を青に
    buf.fillRect(x - w, y - h, 10, 10); // gc を使って□を描く
    if (lflag && !rflag && x > w) // 左キーＯＮ，右キーＯＦＦ，左の壁にあたっていない場合
      x = x - dx;               // オブジェクトを左に動かす
    if (rflag && !lflag && x < apWidth - w) // 右キーＯＮ，左キーＯＦＦ，右の壁にあたっていない場合
      x = x + dx;               // オブジェクトを右に動かす
    if (uflag && !dflag && y > h)
      y = y - dy;
    if (dflag && !uflag && y < apHeight - h)
      y = y + dy;
    // System.out.println("flags: " + lflag + rflag + delaytime ); // 状況チェック
    // System.out.println("(x, y)=(" + x + ", " + y + ")"); // 状態確認用
  }
}


