package edu.hust.soict.cbls.visualization;

import edu.hust.soict.cbls.algorithm.Input;
import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.common.config.Const;
import edu.hust.soict.cbls.common.config.Properties;
import edu.hust.soict.cbls.data.Reader;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SolutionDrawer implements Visualizer{

    private Properties props;
    private Input input;
    private Map<String, Solution> solutions;

    private static final Logger logger = LoggerFactory.getLogger(SolutionDrawer.class);

    public SolutionDrawer(Properties props){
        this.props = props;
        this.input = Reader.read(props.getProperty(Const.VISUALIZATION_INPUT_FILE));
        this.solutions = Reader.readSolution(props.getProperty(Const.VISUALIZATION_RESULT_FILE));
    }

    @Override
    public void drawAndSave(){
        for(Map.Entry<String, Solution> kv : solutions.entrySet()){
            String solverName = props.getProperty(Const.VISUALIZATION_IMAGE_FOLDER) +
                    kv.getKey().substring(kv.getKey().lastIndexOf(".") + 1);
            VisualizationImageServer visualizer = drawSingleSolution(kv.getValue());
            save(visualizer, solverName + ".png");
        }
    }

    private VisualizationImageServer drawSingleSolution(Solution solution){
        Graph<String, String> graph = new DirectedSparseGraph<>();
        List<List<Integer>> routes = solution.convert();
        for(int i = 0 ; i < input.size() ; i ++){
            String name = String.valueOf(i);
            graph.addVertex(name);
        }
        for(int i = 0 ; i < routes.size() ; i ++){
            List<Integer> route = routes.get(i);
            for(int j = 1 ; j < route.size() ; j ++){
                String name = String.format("%d-%d", i, j);
                graph.addEdge(name, String.valueOf(route.get(j - 1)), String.valueOf(route.get(j)));
            }
        }

        Layout<String, String> layout = new CircleLayout<>(graph);
        layout.setSize(new Dimension(800,800));

        VisualizationImageServer<String,String> visualizer =
                new VisualizationImageServer<>(layout, layout.getSize());
        visualizer.setPreferredSize(new Dimension(
                props.getIntProperty(Const.VISUALIZATION_FIGURE_WIDTH, 900),
                props.getIntProperty(Const.VISUALIZATION_FIGURE_WIDTH, 900)));

        visualizer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        visualizer.getRenderContext().setVertexShapeTransformer((String s) -> {
            assert s != null;
            int index = Integer.valueOf(s);
            int spotType = input.pointType(index);
            switch (spotType){
                case 0:
                    return new Ellipse2D.Double(-30, -15, 60, 30);
                case 1:
                case 3:
                    return new Ellipse2D.Double(-15, -15, 30, 30);
                case 2:
                case 4:
                    return new Rectangle(-15, -15, 30, 30);

                default:
                    throw new RuntimeException();
            }
        });
        visualizer.getRenderContext().setVertexFillPaintTransformer((String s) -> {
            assert s != null;
            int index = Integer.valueOf(s);
            int spotType = input.pointType(index);
            switch (spotType){
                case 0:
                case 1:
                case 2:
                    return Const.COLORS[index];
                case 3:
                case 4:
                    return Const.COLORS[index - input.getPassengers().size() - input.getCommodities().size()];

                default:
                    throw new RuntimeException();
            }
        });

        return visualizer;
    }

    private void save(VisualizationImageServer vv, String file){
        JPanel panel = new JPanel ();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.WHITE);
        panel.add(vv);

        BufferedImage image = (BufferedImage) vv.getImage(
                new Point2D.Double(
                        vv.getGraphLayout().getSize().getWidth() / 2,
                        vv.getGraphLayout().getSize().getHeight() / 2),
                new Dimension(vv.getGraphLayout().getSize()));

        try {
            ImageIO.write(image, "png", new File(file));
        } catch (IOException e) {
            logger.error("Error while saving picture", e);
        }
    }
}
