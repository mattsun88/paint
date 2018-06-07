package test;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Window extends Application {
	static int mode=0;
	static int x,y;
    @Override
    public void start(Stage stage) {
        stage.setTitle("ペイント");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);
        final Canvas canvas = new Canvas(800,600);
        final Canvas canvas2 = new Canvas(800,600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext gc2 = canvas2.getGraphicsContext2D();
        gc.setGlobalAlpha(1.0);
        Pane pane = new Pane();
        pane.getChildren().add(canvas2);
        pane.getChildren().add(canvas);
        Image image = canvas.snapshot(null, null);
        WritableImage wImage = new WritableImage(image.getPixelReader(),
		        0,0,800,600);
		PixelWriter pw = wImage.getPixelWriter();

		for(int i=0;i<800;i++) {
			for(int j=0;j<600;j++) {
				pw.setColor(i, j, Color.WHITE);
			}
		}
		gc.drawImage(wImage, 0, 0);
        //こっからメニューバーの設定
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("ファイル");
            MenuItem itemCanvas = new MenuItem("新規作成");
            MenuItem itemOpen = new MenuItem("開く");
            MenuItem itemSave = new MenuItem("保存");
            MenuItem itemSaveAsName = new MenuItem("名前をつけて保存");
            MenuItem itemPrint = new MenuItem("印刷");
            MenuItem itemClose = new MenuItem("閉じる");
            menuFile.getItems().addAll(
                    itemCanvas,itemOpen,new SeparatorMenuItem(),
                    itemSave,itemSaveAsName,new SeparatorMenuItem(),
                    itemPrint,new SeparatorMenuItem(),
                    itemClose);
        Menu menuEdit = new Menu("編集");
            MenuItem itemUndo = new MenuItem("取り消し");
            MenuItem itemCut = new MenuItem("切り取り");
            MenuItem itemCopy = new MenuItem("コピー");
            MenuItem itemPaste = new MenuItem("貼り付け");
            MenuItem itemTrim = new MenuItem("トリミング");
             menuEdit.getItems().addAll(
                    itemUndo,new SeparatorMenuItem(),
                    itemCut,itemCopy,itemPaste,itemTrim,new SeparatorMenuItem(),
                    new SeparatorMenuItem());

        Menu menuSelect = new Menu("選択範囲");
            MenuItem itemSelect = new MenuItem("選択");
            itemSelect.addEventHandler( ActionEvent.ACTION , e -> modechange(2));
            MenuItem itemSelectClear = new MenuItem("解除");
            MenuItem itemSelectReverse = new MenuItem("反転");
            MenuItem itemSelectZoom = new MenuItem("拡大");
            MenuItem itemSelectZoomOut = new MenuItem("縮小");
            menuSelect.getItems().addAll(itemSelect);

        Menu menuDisplay = new Menu("表示");
            MenuItem DisplayZoom = new MenuItem("拡大表示");
            MenuItem DisplayZoomOut = new MenuItem("縮小表示");
            menuDisplay.getItems().addAll(DisplayZoom,DisplayZoomOut);
        Menu menuTool = new Menu("ツール");
            MenuItem itemPencil = new MenuItem("鉛筆");
            itemPencil.addEventHandler( ActionEvent.ACTION , e -> modechange(0));
            MenuItem itemStrait = new MenuItem("直線");
            itemStrait.addEventHandler( ActionEvent.ACTION , e -> modechange(1));
            menuTool.getItems().addAll(itemPencil,itemStrait);


        menuBar.getMenus().addAll(menuFile, menuEdit,
                menuSelect, menuDisplay, menuTool);
        //メニューバー設定ここまで

        //メニューバーとキャンバスを画面に


        //描画は黒に
		gc.setStroke(Color.BLACK);
		gc2.setStroke(Color.RED);
		//イベント検出と動作


        scene.setOnMousePressed((event) -> {
        	if(mode==0) {
        		paint(event,gc);
        	}
        	else if(mode==2) {
        		setXY(event);
        	}
        	else if(mode==1){
        		setXY(event);

        	}
        });
        scene.setOnMouseDragged((event)->{
        	if(mode==0) {
        		canvas.toFront(); //描画キャンパスを前面に
        		paint(event,gc);
        	}
        	else if(mode==2) {
        		canvas2.toFront();//予測線描画キャンパスを前面に
        		square(event,gc2);
        	}
        	else if(mode==1){
        		canvas2.toFront();//予測線描画キャンパスを前面に
        		line2(event,gc2);
        	}
        });
        scene.setOnMouseReleased((event)->{

        	if(mode==0) {
        		gc.beginPath();//マウス離すと始点リセット
        	}
        	else if(mode==1) {
        		line(event,gc,gc2);
        		gc.beginPath();//マウス離すと始点リセット
        	}
        	else if(mode==2) {
        		canvas2.toFront();
        		setNodes(event,pane,canvas,canvas2,gc,gc2);
        		modechange(4);
        	}

        });
        root.setTop(menuBar);
        root.setCenter(pane);
        stage.setScene(scene);
        stage.show();

    }



	public void paint(MouseEvent event,GraphicsContext gc){
		int x = (int)event.getX();
		int y = (int)event.getY();

		gc.lineTo(x, y-25);

		gc.stroke();
	}
	public void line(MouseEvent event,GraphicsContext gc,GraphicsContext gc2){ //ラバーバンド確定描画用
		int x1 = (int)event.getX();
		int y1 = (int)event.getY();
		gc2.clearRect(0, 0, 800, 600);
		gc.strokeLine(x,y,x1,y1);
	}
	public void line2(MouseEvent event,GraphicsContext gc){//ラバーバンド描画フィードバック用
		int x1 = (int)event.getX();
		int y1 = (int)event.getY();
		gc.clearRect(0, 0, 800, 600);
		gc.strokeLine(x,y,x1,y1);
	}
	public void square(MouseEvent event,GraphicsContext gc) {
		int x1 = (int)event.getX();
		int y1 = (int)event.getY();
		gc.clearRect(0, 0, 800, 600);
		gc.strokeLine(x,y,x,y1);
		gc.strokeLine(x,y,x1,y);
		gc.strokeLine(x1,y,x1,y1);
		gc.strokeLine(x,y1,x1,y1);
	}
	private void hsquare(MouseEvent event, GraphicsContext gc, int X, int Y) {
		// TODO 自動生成されたメソッド・スタブ
		int x1 = (int)event.getX();
		int y1 = (int)event.getY();
		gc.clearRect(0, 0, 800, 600);
		gc.strokeLine(x,y1,X,y1);
		gc.strokeLine(x,Y,x,y1);
		gc.strokeLine(X,y1,X,Y);
		gc.strokeLine(x,Y,X,Y);
	}
	public void setNodes(MouseEvent event, Pane pane,Canvas canvas, Canvas canvas2, GraphicsContext gc, GraphicsContext gc2) {
		int x1 = (int)event.getX();
		int y1 = (int)event.getY();
		int origx = x;
		int origy = y;
		Circle circle = new Circle(x,y,5);
		Circle circle2 = new Circle(x1,y,5);
		Circle circle3 = new Circle(x,y1,5);
		Circle circle4 = new Circle(x1,y1,5);
		Circle circle5 = new Circle((x+x1)/2,y,5);
		Circle circle6 = new Circle(x,(y+y1)/2,5);
		Circle circle7 = new Circle(x1,(y+y1)/2,5);
		Circle circle8 = new Circle((x+x1)/2,y1,5);
        circle.setOnMouseDragged((e)->{
        		setXY(x1,y1);
        		square(e,gc2);
        });
        circle.setOnMouseEntered((e)->{
        	circle.setCursor( Cursor.HAND );
        });
        circle.setOnMouseReleased((e)->{

        	big(e,gc,canvas,x,y,x1,y1,(int)e.getX(),(int)e.getY(),pane);
        	pane.getChildren().removeAll(circle,circle2,circle3,circle4,circle5,circle6,circle7,circle8);
        	gc2.clearRect(0, 0, 800, 600);
        	modechange(0);
        });
        circle.toFront();

        circle2.setOnMouseEntered((e)->{
        	circle2.setCursor( Cursor.HAND );
        });
        circle2.setOnMouseDragged((e)->{
    		setXY(x,y1);
    		square(e,gc2);
    });
        circle2.setOnMouseReleased((e)->{

        	big(e,gc,canvas,x,origy,x1,y1,(int)e.getX(),(int)e.getY(),pane);
        	pane.getChildren().removeAll(circle,circle2,circle3,circle4,circle5,circle6,circle7,circle8);
        	gc2.clearRect(0, 0, 800, 600);
        	modechange(0);
        });
        circle2.toFront();

        circle3.setOnMouseEntered((e)->{
        	circle3.setCursor( Cursor.HAND );
        });
        circle4.setOnMouseEntered((e)->{
        	circle4.setCursor( Cursor.HAND );
        });

        circle5.setOnMouseDragged((e)->{
    		hsquare(e,gc2,x1,y1);
        });
        circle5.setOnMouseReleased((e)->{

        	big(e,gc,canvas,x,y,x1,y1,x,(int)e.getY(), pane);
        	pane.getChildren().removeAll(circle,circle2,circle3,circle4,circle5,circle6,circle7,circle8);
        	gc2.clearRect(0, 0, 800, 600);
        	modechange(0);
        });
        circle5.setOnMouseEntered((e)->{
        	circle5.setCursor( Cursor.HAND );
        });
        circle6.setOnMouseEntered((e)->{
        	circle6.setCursor( Cursor.HAND );
        });
        circle7.setOnMouseEntered((e)->{
        	circle7.setCursor( Cursor.HAND );
        });
        circle8.setOnMouseEntered((e)->{
        	circle8.setCursor( Cursor.HAND );
        });

        pane.getChildren().addAll();
		pane.getChildren().addAll(circle,circle2,circle3,circle4,circle5,circle6,circle7,circle8);
	}





	public void setXY(MouseEvent e) {
		x = (int)e.getX();
		y=(int)e.getY();
	}
	public void setXY(int x1,int y1) {
		x = x1;
		y= y1;
	}
	public void big(MouseEvent event,GraphicsContext gc,Canvas canvas, int origx, int origy,int X, int Y, int x1, int y1, Pane pane){
		//int x1 = (int)event.getX();
		//int y1 = (int)event.getY();
		Image image = canvas.snapshot(null, null);
		//選択範囲をwritableimageに取り込む
		WritableImage resizedImage = new WritableImage(image.getPixelReader(),
		        origx,origy,X-origx,Y-origy);
		WritableImage voidImage = new WritableImage(X-origx,Y-origy);//今まで描画されてた部分に上書きする用の画像
		PixelWriter pw = voidImage.getPixelWriter();

		//真っ白い画像をつくる
		for(int i=0;i<X-origx;i++) {
			for(int j=0;j<Y-origy;j++) {
				pw.setColor(i, j, Color.WHITE);
			}
		}
		gc.drawImage(voidImage, origx, origy); //前の部分を白で上書き
		gc.drawImage(resizedImage,x1,y1,X-x1,Y-y1);//サイズを変更して貼り付け
	}
    public static void modechange(int i) {
        mode=i;
    }
    public static void main(String[] args) {
        launch(args);
    }

}