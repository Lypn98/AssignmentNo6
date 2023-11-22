/**
 * ゲームの進行自体を取り仕切るクラス
 * ・キーボード入力
 * ・ゲーム内の各オブジェクトの管理
 * ・ゲーム画面の描画
 * 
 * @author fukai
 */
import java.awt.*;
import java.awt.event.*;

public class GameMaster extends Canvas implements KeyListener {
  // ■ フィールド変数
  Image        buf;   // 仮の画面としての buffer に使うオブジェクト(Image クラス)
  Graphics     buf_gc;// buffer のグラフィックスコンテキスト (gc) 用オブジェクト
  Dimension    d;     // アプレットの大きさを管理するオブジェクト
  private int  imgW, imgH; // キャンバスの大きさ

  private int enmyCnum = 1; //ボスの数
  private int enmyAnum  = 20; // 敵Ａの数
  private int enmyBnum = 10;
  private int ftrBltNum = 10; // 自機の弾の数
  private int mode      = -1; // -1: タイトル画面，-2: ゲームオーバー，1〜 ゲームステージ
  private int i, j  =0;

  Fighter       ftr;  // 自機
  FighterBullet ftrBlt[] = new FighterBullet[ftrBltNum]; // 自機の弾
  EnemyA        enmyA[]  = new EnemyA[enmyAnum];         // 敵機Ａ
  EnemyC          enmyC[] = new EnemyC[enmyCnum]; 
  EnemyB        enmyB[] = new EnemyB[enmyBnum];
  boolean effect = false;

  int DeathEnmy  = 0;

  Image img1 = this.getToolkit().getImage("Bakuhatsu2.jpg");
  Image img2 = this.getToolkit().getImage("Ocean2.jpg");
  Image img3 = this.getToolkit().getImage("vehicle_submarine_01.png");
  Image img4 = this.getToolkit().getImage("other_rocket_01.png");
  Image img5 = this.getToolkit().getImage("other_sun_01.png");
  Image img6 = this.getToolkit().getImage("omoro_alien_06.png");
  Image img7 = this.getToolkit().getImage("Sunahama1.jpg");
  Image img8 = this.getToolkit().getImage("Black.png");


  Font TytleFont = new Font("Serif",Font.PLAIN,30);
  Font ResultFont = new Font("Serif",Font.PLAIN,45);
  
  
   // ■ コンストラクタ
  /**
   * ゲームの初期設定
   * ・描画領域(Image)とGC(Graphics)の作成
   * ・敵，自機，弾オブジェクトの作成
   */

  GameMaster(int imgW, int imgH) { // コンストラクタ （アプレット本体が引数． ゲームの初期化を行う）
    this.imgW = imgW; // 引数として取得した描画領域のサイズをローカルなプライベート変数に代入
    this.imgH = imgH; // 引数として取得した描画領域のサイズをローカルなプライベート変数に代入
    setSize(imgW, imgH); // 描画領域のサイズを設定

    addKeyListener(this);

    ftr = new Fighter(imgW, imgH); // 自機のオブジェクトを実際に作成
    for (i = 0; i < ftrBltNum; i++)       // 自機弾のオブジェクトを実際に作成
      ftrBlt[i] = new FighterBullet();
    for (i = 0; i < enmyAnum; i++)        // 敵Ａのオブジェクトを実際に作成
      enmyA[i] = new EnemyA(imgW, imgH);
    for(i = 0; i <  enmyCnum; i++)
      enmyC[i] = new EnemyC(imgW, imgH);
    for(i=0;i < enmyBnum; i++)
      enmyB[i] = new EnemyB(imgW, imgH);
  }

  // ■ メソッド
  // コンストラクタ内で createImage を行うと peer の関連で 
  // nullpointer exception が返ってくる問題を回避するために必要
  public void addNotify(){
    super.addNotify();
    buf = createImage(imgW, imgH); // buffer を画面と同サイズで作成
    buf_gc = buf.getGraphics();
  }

  // ■ メソッド (Canvas)
  public void paint(Graphics g) {
    buf_gc.setColor(Color.white);      // gc の色を白に
    buf_gc.fillRect(0, 0, imgW, imgH); // gc を使って白の四角を描く（背景の初期化）
    
    switch (mode) {
    
    case -2: // ゲームオーバー画面（スペースキーを押されたらタイトル画面）
      buf_gc.drawImage(img8, 0, 0, imgW, imgH, this);
      buf_gc.setFont(ResultFont);
      buf_gc.setColor(Color.red); // ゲームオーバー画面を描く
      buf_gc.drawString("      == Game over ==      ", 100, 180);
      buf_gc.setColor(Color.white);
      buf_gc.drawString("       Hit SPACE bar       ", 100, 240);
      buf_gc.drawString("        Score:        " + DeathEnmy, 100,300);
      
      break;
    case -1: // タイトル画面（スペースキーを押されたらゲーム開始）
      buf_gc.setColor(Color.black); // タイトル画面を描く
      buf_gc.setFont(TytleFont);
      buf_gc.drawImage(img7, 0, 0, imgW, imgH, this);
      buf_gc.drawImage(img4, 70, 360, 50, 50, this);
      buf_gc.drawString("  1 pt ", 65, 350);
      buf_gc.drawImage(img6, 310, 360, 50, 50, this);
      buf_gc.drawString("  3 pt ", 305, 350);
      buf_gc.drawImage(img5,500 , 345, 80, 80, this);
      buf_gc.drawString("  5 pt ", 510, 350);
      buf_gc.drawString(" == Marine Battle  == ", 200, 180);
      buf_gc.drawString("    Hit SPACE bar     ", 200, 210);
      DeathEnmy = 0;
      break;
    default: // ゲーム中
      buf_gc.drawImage(img2, 0, 0, imgW, imgH, this);
      buf_gc.drawString("        Score:        " + DeathEnmy, 200,200);
      
      // *** ランダムに敵を生成 *** 
      makeEnmy: if (Math.random() < 0.1) { // １０％の確率で一匹生成
	for (i = 0; i < enmyAnum; i++) {
	  if (enmyA[i].hp == 0) {
	    enmyA[i].revive(imgW, imgH);
	    break makeEnmy;
	  }
	}
}

  makeEnmyB: if (Math.random() < 0.05) { // 5％の確率で一匹生成
	for (i = 0; i < enmyBnum; i++) {
	  if (enmyB[i].hp == 0) {
	    enmyB[i].revive(imgW, imgH);
	    break makeEnmyB;
	  }
	}
}

     makeEnmyC: if (Math.random() < 0.01) { // 1％の確率で一匹生成
	for (i = 0; i < enmyCnum; i++) {
	  if (enmyC[i].hp == 0) {
	    enmyC[i].revive(imgW, imgH);
	    break makeEnmyC;
	  }
	}
}


      // *** 自分の弾を発射 ***
      if (ftr.sflag == true && ftr.delaytime == 0) { // もしスペースキーが押されていて＆待ち時間がゼロなら
	for (i = 0; i < ftrBltNum; i++) {      // 全部の弾に関して前から探査して
	  if (ftrBlt[i].hp == 0) {             // 非アクティブの（死んでいる）弾があれば
	    ftrBlt[i].revive(ftr.x, ftr.y);    // 自機から弾を発射して，
	    ftr.delaytime = 5;                 // 自機の弾発射待ち時間を元に戻して，
	    break;                             // for loop を抜ける
	  }
	}
      } else if (ftr.delaytime > 0)      // 弾を発射しない(出来ない)場合は
	ftr.delaytime--;                       // 待ち時間を１減らす
      // *** 各オブジェクト間の衝突チェック ***/
      for (i = 0; i < enmyAnum; i++){           // すべての敵に関しss，
	if (enmyA[i].hp > 0) {                 // 敵が生きていたら
	  ftr.collisionCheck(enmyA[i]);        // 自機と衝突チェック
	  for (j = 0; j < ftrBltNum; j++)      // 全ての自弾に関して
	    if (ftrBlt[j].hp > 0)              // 自弾が生きていたら
	     // 自弾との衝突チェック
      if(ftrBlt[j].collisionCheck(enmyA[i])){
        enmyA[i].count = 7;
        DeathEnmy++;
      }
	  }
    if(enmyA[i].count >0){
        buf_gc.drawImage(img1,enmyA[i].x - 30,enmyA[i].y,35,35,this);
        enmyA[i].count --;
    }
  }

      for (i = 0; i < enmyCnum ; i++)           // すべての敵に関し，
	if (enmyC[i].hp > 0) {                 // 敵が生きていたら
	  ftr.collisionCheck(enmyC[i]);        // 自機と衝突チェック
	  for (j = 0; j < ftrBltNum; j++)      // 全ての自弾に関して
	    if (ftrBlt[j].hp > 0){              // 自弾が生きていたら
	      if(ftrBlt[j].collisionCheck(enmyC[i]) && enmyC[i].hp == 0){   // 自弾との衝突チェック
          DeathEnmy = DeathEnmy + 5;
        } 

      }
	}

    for (i = 0; i < enmyBnum; i++)           // すべての敵に関し，
	if (enmyB[i].hp > 0) {                 // 敵が生きていたら
	  ftr.collisionCheck(enmyB[i]);        // 自機と衝突チェック
	  for (j = 0; j < ftrBltNum; j++)      // 全ての自弾に関して
	    if (ftrBlt[j].hp > 0){              // 自弾が生きていたら
	      if(ftrBlt[j].collisionCheck(enmyB[i]) && enmyB[i].hp == 0 ){  // 自弾との衝突チェック
          DeathEnmy = DeathEnmy + 3 ;
        } 
      }
	}


    

      // *** 自機の生死を判断 ***
      if (ftr.hp < 1)
	mode = -2; // ゲーム終了
 
    //bossの出現
  //  if (DeathEnmy>150)
  //   mode = 1;

      // *** オブジェクトの描画＆移動
       //***
    for (i = 0; i < enmyAnum; i++){
	    enmyA[i].move(buf_gc, imgW, imgH);
      if(enmyA[i].hp>0){
        buf_gc.drawImage(img4, enmyA[i].x -17 ,enmyA[i].y-24,35,35,this);
      }
    }

    for (i = 0; i < ftrBltNum; i++){
	    ftrBlt[i].move(buf_gc, imgW, imgH);
      if(enmyB[i].hp>0){
        buf_gc.drawImage(img6, enmyB[i].x - 31,enmyB[i].y- 30,35,35,this);
      }
    }
    
    for (i = 0; i < enmyCnum; i++){
	    enmyC[i].move(buf_gc, imgW, imgH);
      if(enmyC[i].hp>0){
        buf_gc.drawImage(img5, enmyC[i].x - 77,enmyC[i].y- 85,85,85,this);
      }
    }

    for (i = 0; i < enmyBnum; i++){
	    enmyB[i].move(buf_gc, imgW, imgH);
    }

  ftr.move(buf_gc, imgW, imgH);
  buf_gc.drawImage(img3, ftr.x - 20,ftr.y - 25,35,35,this);    

      // 状態チェック
//      for (i = 0; i < enmyAnum; i++) {
//	System.out.print(enmyA[i].hp + " ");
//      }
//      System.out.println("");
    }
    g.drawImage(buf, 0, 0, this); // 表の画用紙に裏の画用紙 (buffer) の内容を貼り付ける
  }

  // ■ メソッド (Canvas)
  public void update(Graphics gc) { // repaint() に呼ばれる
    paint(gc);
  }

  // ■ メソッド (KeyListener)
  public void keyTyped(KeyEvent ke) {
  } // 今回は使わないが実装は必要

  public void keyPressed(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_LEFT: // [←]キーが押されたら
      ftr.lflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_RIGHT: // [→]キーが押されたら
      ftr.rflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_UP: // [↑]キーが押されたら
      ftr.uflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_DOWN: // [↓]キーが押されたら
      ftr.dflag = true; // フラグを立てる
      break;
    case KeyEvent.VK_SPACE: // スペースキーが押されたら
      ftr.sflag = true; // フラグを立てる
      if (this.mode != 1){
	this.mode++;
      }
      ftr.hp = 4;
      break;
    }
  }

  // ■ メソッド (KeyListener)
  public void keyReleased(KeyEvent ke) {
    int cd = ke.getKeyCode();
    switch (cd) {
    case KeyEvent.VK_LEFT: // [←]キーが離されたら
      ftr.lflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_RIGHT: // [→]キーが離されたら
      ftr.rflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_UP: // [↑]キーが離されたら
      ftr.uflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_DOWN: // [↓]キーが離されたら
      ftr.dflag = false; // フラグを降ろす
      break;
    case KeyEvent.VK_SPACE: // スペースキーが離されたら
      ftr.sflag = false; // フラグを降ろす
      break;
    }
  }
}

