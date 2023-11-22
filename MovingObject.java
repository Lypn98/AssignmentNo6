/**
 *  ゲームに登場する全ての移動オブジェクトに共通の親クラス（抽象クラス）
 * 　変数：　    位置 (x, y), 速度 (vx, vy), Hit Point (hp), 横幅 w, 縦幅 h
 * 　メソッド：　move, collisionCheck, revive
 * 　他，もし共通に出来るものがあれば随時追加する
 *
 * @author fukai
 */
import java.awt.Graphics;

abstract class MovingObject { // 抽象クラス
  // フィールド変数
  int x;       // 中心座標
  int y;       // 中心座標
  int dx;      // 速度
  int dy;      // 速度
  int w;       // 横幅の半分
  int h;       // 縦幅の半分
  int hp;      // ヒットポイント（ゼロ以下で死亡）
  int count;

  // コンストラクタ１　引数が無い場合は初期設定しない（フィールド変数は初期値０をもつ）
  MovingObject () {}
  // コンストラクタ２　描画領域の大きさを引数に，出現の初期値をランダムに設定
  MovingObject (int width, int height) {
    x  = (int) (Math.random()*width);  // 画面の内部でランダム
    y  = (int) (Math.random()*height); // 画面の内部でランダム
    dx = (int) ((Math.random()*6-3)*5);    // -3から+3の範囲でランダム
    dy = (int) ((Math.random()*6-3)*5);    // -3から+3の範囲でランダム
    w  = 2;
    h  = 2;
    hp = 10;
  }

  abstract void move(Graphics buf, int apWidth, int apHeight);
  // オブジェクトを動かし，位置を更新する抽象メソッド
  // MovingObject を継承する全てのクラスに同名での実装を強制
  // move では　１．まず描いてから　２．vx, vy だけ移動させる
  // move を呼び出す前に衝突判定し，合格したものだけ描く
    
  abstract void revive(int w, int h);
  // オブジェクトを生き返らす抽象メソッド（MovingObject を継承する全てのクラスに同名での実装を強制）
  // 引数は，通常はアプレットの縦横の大きさ．弾の場合は，オブジェクトの位置
    
  boolean collisionCheck(MovingObject obj) { 
    // 引数は相手のオブジェクト
    // 衝突判定の共通メソッド    	
    if (Math.abs(this.x-obj.x) <= (this.w+obj.w) && Math.abs(this.y-obj.y) <= (this.h+obj.h)) {
      this.hp--;   // 自分のhpを減らす
      obj.hp--;    // 相手のhpを減らす
      return true;
    } else
      return false;
  }
}

