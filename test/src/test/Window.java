package test;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
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

        Pane pane = new Pane();
        pane.getChildren().add(canvas2);
        pane.getChildren().add(canvas);
        //gc.setFill(Color.WHITE);

        //int mode=0;
       // Path lastpath= new Path();
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
        	else {
        		setXY(event);

        	}
        });
        scene.setOnMouseDragged((event)->{
        	if(mode==0) {
        		canvas.toFront(); //念のために前面に
        		paint(event,gc);
        	}
        	else if(mode==2) {
        		square(event,gc2);
        	}
        	else {
        		canvas2.toFront();
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
        		setNodes(event,pane);
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

	public void setNodes(MouseEvent event, Pane pane) {
		int x1 = (int)event.getX();
		int y1 = (int)event.getY();
		Circle circle = new Circle(x,y,5);
		circle.setOnMouseClicked((e) -> {
			System.out.print("c1");
		});
		Circle circle2 = new Circle(x1,y,5);
		Circle circle3 = new Circle(x,y1,5);
		Circle circle4 = new Circle(x1,y1,5);
		Circle circle5 = new Circle((x+x1)/2,y,5);
		Circle circle6 = new Circle(x,(y+y1)/2,5);
		Circle circle7 = new Circle(x1,(y+y1)/2,5);
		Circle circle8 = new Circle((x+x1)/2,y1,5);


		pane.getChildren().addAll(circle,circle2,circle3,circle4,circle5,circle6,circle7,circle8);
	}

	public void setXY(MouseEvent e) {
		x = (int)e.getX();
		y=(int)e.getY();
	}
	public void big(ActionEvent event,GraphicsContext gc,Canvas canvas){
		WritableImage   wImg        = new WritableImage( (int) 800 , (int) 600 );
		PixelWriter writer = gc.getPixelWriter();
		//PixelWriter     writer      = wImg.getPixelWriter();
		Image image = canvas.snapshot(null, null);
		WritableImage resizedImage = new WritableImage(image.getPixelReader(),
		        0, 0, (int) (image.getWidth() / 2), (int) (image.getHeight() / 2));
		gc.drawImage(image, 400, 200);
	    File f = new File("tes.png");
	    try {
			ImageIO.write(SwingFXUtils.fromFXImage(resizedImage, null), "png", f);

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
    public static void modechange(int i) {
        mode=i;
    }
    public static void main(String[] args) {
        launch(args);
    }

}