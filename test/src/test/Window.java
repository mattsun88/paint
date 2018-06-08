package test;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Window extends Application {
	static int mode=0,flg=0;
	static int x,y,tx,ty;
	WritableImage histry1,histry2,histry;
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
        WritableImage wImage = new WritableImage(800,600);
		PixelWriter pw = wImage.getPixelWriter();
		for(int i=0;i<800;i++) {
			for(int j=0;j<600;j++) {
				pw.setColor(i, j, Color.WHITE);
			}
		}
		gc.drawImage(wImage, 0, 0);
 		Image image1 = canvas.snapshot(null, null);
 		histry =  new WritableImage(image1.getPixelReader(),
					0, 0,800, 600);
        //こっからメニューバーの設定
        Image       icon        = new Image( new File( "img/1498.jpg" ).toURI().toString() );
        ImageView   iconView1   = new ImageView( icon );
        Image       icon2        = new Image( new File( "img/pencil.jpg" ).toURI().toString() );
        ImageView   iconView2   = new ImageView( icon2 );
        Image       icon3        = new Image( new File( "img/strait.jpg" ).toURI().toString() );
        ImageView   iconView3   = new ImageView( icon3 );
        Image       icon4        = new Image( new File( "img/big.jpg" ).toURI().toString() );
        ImageView   iconView4   = new ImageView( icon4 );
        Alert alert1 = new Alert(AlertType.CONFIRMATION);
        alert1.setContentText("現在の画像を削除して新規に作成してよろしいですか？");
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("ファイル");
            MenuItem itemCanvas = new MenuItem("新規作成");
            itemCanvas.addEventHandler(ActionEvent.ACTION, e->{
            	final Optional<ButtonType> result = alert1.showAndWait();
            	if(result.get()  == ButtonType.OK){
            		reset(gc);
            	} else {

            	}
            });
            MenuItem itemSaveAsName = new MenuItem("名前をつけて保存");
            itemSaveAsName.addEventHandler(ActionEvent.ACTION, e->{
            	saveimage(stage,canvas);
            });
            menuFile.getItems().addAll(itemCanvas,itemSaveAsName);
        Menu menuSelect = new Menu("操作");
        	MenuItem unDo = new MenuItem("やり直し",iconView4);
        	unDo.addEventHandler( ActionEvent.ACTION , e -> {
        		if(unDo(gc,canvas)==0) {
        			unDo.setDisable(true);
        		}
        	});
        	unDo.setDisable(true);

            MenuItem itemSelect = new MenuItem("選択モード",iconView4);
            itemSelect.addEventHandler( ActionEvent.ACTION , e -> modechange(2));
            MenuItem itemDelete = new MenuItem("削除");
            itemDelete.addEventHandler( ActionEvent.ACTION , e -> {
            	delete(gc,tx,ty);
            	gc2.clearRect(0, 0, 800, 600);
            	modechange(0);
            	itemDelete.setDisable(true);
            	pane.getChildren().remove(2,10);
            });
    		itemDelete.setDisable(true);
            menuSelect.getItems().addAll(unDo,itemSelect,itemDelete);
        Menu menuTool = new Menu("ツール",iconView1);
            MenuItem itemPencil = new MenuItem("フリーライン",iconView2);
            itemPencil.addEventHandler( ActionEvent.ACTION , e -> modechange(0));
            MenuItem itemStrait = new MenuItem("直線",iconView3);
            itemStrait.addEventHandler( ActionEvent.ACTION , e -> modechange(1));
            menuTool.getItems().addAll(itemPencil,itemStrait);
        menuBar.getMenus().addAll(menuFile,menuSelect, menuTool);
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
        		settXY(event);
        		itemDelete.setDisable(false);
        	}
     		Image image = canvas.snapshot(null, null);

     			histry1 =histry;
     			histry=new WritableImage(image.getPixelReader(),
     					0, 0,800, 600);
     			//System.out.println("here");
     			unDo.setDisable(false);

        });
        scene.setOnKeyPressed( e-> {

    		switch(e.getCode()) {
    		case DELETE:
    			if(mode==4) {
    				delete(gc,tx,ty);
    				gc2.clearRect(0, 0, 800, 600);
    				modechange(0);
    				itemDelete.setDisable(true);
    				pane.getChildren().remove(2,10);
    			}
    			break;
    		case CONTROL:
    			flg=1;
    			break;
    		case Z:
    			if(flg==1&&histry1!=null) {
    				unDo(gc,canvas);
    				//System.out.println("here");
    			}
    			break;
    		default:
    			break;
    		}
    	});
        scene.setOnKeyReleased(e->{
        	switch(e.getCode()) {
    		case CONTROL:
    			flg=0;
    			break;
    		default:
    			break;
        	}
        });
        root.setTop(menuBar);
        root.setCenter(pane);
        stage.setScene(scene);
        stage.show();

    }



	private int unDo(GraphicsContext gc,Canvas canvas) {

 		WritableImage hImage = new WritableImage(histry1.getPixelReader(),
 		        0, 0,800, 600);

 		gc.drawImage(hImage, 0, 0);
 		Image image1 = canvas.snapshot(null, null);
 		histry =  new WritableImage(image1.getPixelReader(),
					0, 0,800, 600);
		histry1=null;
		//histry=null;
		return 0;
	}



	private void settXY(MouseEvent event) {
		// TODO 自動生成されたメソッド・スタブ
		tx = (int)event.getX();
		ty=(int)event.getY();
	}



	private void delete(GraphicsContext gc, int tx, int ty) {
		//真っ白い画像をつくる
		WritableImage voidImage = new WritableImage(tx-x,ty-y);//今まで描画されてた部分に上書きする用の画像
		PixelWriter pw = voidImage.getPixelWriter();
		for(int i=0;i<tx-x;i++) {
			for(int j=0;j<ty-y;j++) {
				pw.setColor(i, j, Color.WHITE);
			}
		}
		gc.drawImage(voidImage, x, y); //前の部分を白で上書き
	}



	private void saveimage(Stage stage, Canvas canvas) {
		// TODO 自動生成されたメソッド・スタブ
		FileChooser fileChooser = new FileChooser();
 		Image image = canvas.snapshot(null, null);
 		WritableImage resizedImage = new WritableImage(image.getPixelReader(),
 		        0, 0,800, 600);
		fileChooser.setInitialFileName("pic.png");
		File file = fileChooser.showSaveDialog(stage);
 	    try {
 			ImageIO.write(SwingFXUtils.fromFXImage(resizedImage, null), "png", file);
 		} catch (IOException e) {
 			// TODO 自動生成された catch ブロック
 			e.printStackTrace();
 		}
	}



	private void reset(GraphicsContext gc) {
        WritableImage wImage = new WritableImage(800,600);
		PixelWriter pw = wImage.getPixelWriter();

		for(int i=0;i<800;i++) {
			for(int j=0;j<600;j++) {
				pw.setColor(i, j, Color.WHITE);
			}
		}
		gc.drawImage(wImage, 0, 0);
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

        	big(e,gc,canvas,origx,origy,x1,y1,(int)e.getX(),(int)e.getY(),pane);
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
		System.out.println(origx);
				System.out.println(origy);
						System.out.println(X);
						System.out.println(Y);
		System.out.println(X-origx);
		System.out.println(Y-origy);
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