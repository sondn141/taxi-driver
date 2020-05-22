package edu.hust.soict.cbls.worker;

import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.common.utils.Reflects;
import edu.hust.soict.cbls.visualization.Visualizer;

public class DrawerFactory {

    private Properties props;
    private Visualizer viz;

    public DrawerFactory(Properties props){
        this.props = props;
    }

    public void draw(){
        String clazz = props.getProperty(Const.VISUALIZER_CLASS);
        viz = Reflects.newInstance(clazz, new Class[]{Properties.class}, props);
        viz.drawAndSave();
    }

    public static void main(String[] args){
        DrawerFactory drawer = new DrawerFactory(new Properties());
        drawer.draw();
    }
}
