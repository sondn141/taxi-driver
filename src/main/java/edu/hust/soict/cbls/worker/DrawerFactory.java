package edu.hust.soict.cbls.worker;

import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.utils.Reflects;
import edu.hust.soict.cbls.visualization.Visualizer;

import java.util.LinkedList;
import java.util.List;

public class DrawerFactory {

    private Properties props;

    public DrawerFactory(Properties props){
        this.props = props;
    }

    public void draw(){
        List<String> visualizerClazz = props.getCollection(Const.VISUALIZER_CLASS);
        List<Visualizer> visualisers = new LinkedList<>();
        for(String clazz : visualizerClazz){
            visualisers.add(Reflects.newInstance(clazz, new Class[]{Properties.class}, props));
        }

        for(Visualizer viz : visualisers){
            viz.drawAndSave();
        }
    }

    public static void main(String[] args){
        DrawerFactory drawer = new DrawerFactory(new Properties());
        drawer.draw();
    }
}
