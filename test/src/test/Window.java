package test;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

public class Window extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("ペイント");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600,Color.WHITE);
        final Canvas canvas = new Canvas(800,600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Path path = new Path();
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
            MenuItem itemSelectAll = new MenuItem("全て選択");
            MenuItem itemSelectClear = new MenuItem("解除");
            MenuItem itemSelectReverse = new MenuItem("反転");
            MenuItem itemSelectZoom = new MenuItem("拡大");
            MenuItem itemSelectZoomOut = new MenuItem("縮小");
            menuSelect.getItems().addAll(itemSelectAll,itemSelectClear,itemSelectReverse,
                    itemSelectZoom,itemSelectZoom,itemSelectZoomOut);

        Menu menuDisplay = new Menu("表示");
            MenuItem DisplayZoom = new MenuItem("拡大表示");
            MenuItem DisplayZoomOut = new MenuItem("縮小表示");
            menuDisplay.getItems().addAll(DisplayZoom,DisplayZoomOut);
        Menu menuTool = new Menu("ツール");
            MenuItem itemPencil = new MenuItem("鉛筆");
            itemPencil.addEventHandler( ActionEvent.ACTION , e -> System.out.println( "selected" ));
            MenuItem itemStrait = new MenuItem("直線");

            menuTool.getItems().addAll(itemPencil,itemStrait);


        menuBar.getMenus().addAll(menuFile, menuEdit,
                menuSelect, menuDisplay, menuTool);
        //メニューバー設定ここまで

        //メニューバーとキャンバスを画面に


        //描画は黒に
		gc.setStroke(Color.BLACK);

		//イベント検出と動作
        //scene.setOnMouseClicked(event -> paint(event,gc));
        //scene.setOnMouseDragged(event -> paint(event,gc));
        //scene.setOnMouseReleased(event -> paint(event,gc));

        scene.setOnMousePressed((event) -> {
            path.getElements().add(new MoveTo(event.getX(),event.getY()));
        });
        scene.setOnMouseDragged((event)->{
            path.getElements().add(new LineTo(event.getX(),event.getY()));
        });
        int i=0;
        root.setTop(menuBar);

        //root.setCenter(canvas);
        final Path lastpath=path;
        //lastpath.setScaleX(3.5);
        itemStrait.addEventHandler( ActionEvent.ACTION , e ->big(e,path) );
        root.getChildren().add(path);
        System.out.println( i++ );
        stage.setScene(scene);
        stage.show();

    }

	public void paint(MouseEvent event,GraphicsContext gc){
		int x = (int)event.getX();
		int y = (int)event.getY();
		gc.setStroke(Color.BLACK);
		gc.strokeLine(x,y-25,x,y-25);
	}

	public void big(ActionEvent event,Path path){
		Affine affine = new Affine(1, 0, 0, 0, 1, 0);
		//Point2D dataA = new Point2D(160, 100);
		//affine.transform(dataA);
		path.getTransforms().add(affine);
	}

    public static void main(String[] args) {
        launch(args);
    }

}