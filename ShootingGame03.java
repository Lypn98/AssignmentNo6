/**
 * シューティングゲームのサンプルプログラム 03
 * このクラスでは、
 * 1. フレームの作成
 * 2. スレッドの逐次進行
 * のみをを行う。ゲームの内容の更新等は GameMaster クラスで行う
 *
 * @author fukai
 */

 import java.awt.*;

 public class ShootingGame03 extends Frame implements Runnable {
   // ■ フィールド変数
   Thread     th;   // Thread クラスのオブジェクトを宣言
   GameMaster gm;   // ゲームの進行を担当するクラス
 
   // ■ main メソッド（スタート地点）
   public static void main(String[] args) {
     new ShootingGame03(); // 自分自身のオブジェクトを作成
   }
 
   // ■ コンストラクタ
   ShootingGame03() { // init()は Applet クラスのメソッド
     super("Shooting Game (Sample)"); // 親クラスのコンストラクタを呼び出す
     int cW=640, cH=480;      // キャンバスのサイズ
     this.setSize(cW+30, cH+40);   // フレームのサイズを指定
     this.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // キャンバスをフレームに配置
 
     gm = new GameMaster(cW,cH);// GameMaster クラスのオブジェクトを作成
     this.add(gm);              // キャンバスをフレームに配置
     this.setVisible(true);     // 可視化
 
     th = new Thread(this);     // Thread クラスのオブジェクトの作成
     th.start();                // 最後にスレッドを start メソッドで開始
 
     requestFocusInWindow();    // フォーカスを得る
   }
 
   // ■ メソッド (Runnable インターフェース用）
   public void run() {
     try {
       while (true) { // 無限ループ
     Thread.sleep(20); // ウィンドウを更新する前に指定時間だけ休止
     gm.repaint();     // 再描画を要求する． repaint() は update() を呼び出す
       }
     }
     catch (Exception e) {System.out.println("Exception: " + e);}
   }
 }
 
 
 
 