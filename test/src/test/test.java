package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class test extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Circe Desktop");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

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
            MenuItem itemLeftTurn = new MenuItem("左回転");
            MenuItem itemRightTurn = new MenuItem("右回転");
            MenuItem itemReverse = new MenuItem("左右反転");
            MenuItem itemCanvasSize = new MenuItem("キャンバスサイズの変更");
            MenuItem itemGroup = new MenuItem("グループ化");
             menuEdit.getItems().addAll(
                    itemUndo,new SeparatorMenuItem(),
                    itemCut,itemCopy,itemPaste,itemTrim,new SeparatorMenuItem(),
                    itemLeftTurn,itemRightTurn,itemReverse,new SeparatorMenuItem(),
                    itemCanvasSize,itemGroup);

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
            MenuItem itemEraser = new MenuItem("消しゴム");
            MenuItem itemScal = new MenuItem("定規");
            MenuItem itemFinger = new MenuItem("指");
            MenuItem itemNuno = new MenuItem("布");
            MenuItem itemPastel = new MenuItem("パステル");
            MenuItem itemChork = new MenuItem("チョーク");
            MenuItem itemPen = new MenuItem("ペン");
            MenuItem itemHude = new MenuItem("筆");
            MenuItem itemAir = new MenuItem("エアブラシ");
            MenuItem spoit = new MenuItem("スポイト");
            menuTool.getItems().addAll(itemPencil,itemEraser,itemScal,
            itemFinger,itemNuno,itemPastel,itemChork,itemPen,itemHude,
            itemAir,spoit);
        Menu menuConfig = new Menu("設定");
        Menu menuWindow = new Menu("ウィンドウ");
        Menu menuHelp = new Menu("ヘルプ");

        menuBar.getMenus().addAll(menuFile, menuEdit, 
                menuSelect, menuDisplay, menuTool,
                 menuConfig, menuWindow, menuHelp);

        root.setTop(menuBar);

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}