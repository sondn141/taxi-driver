import edu.hust.soict.cbls.algorithm.Solution;
import edu.hust.soict.cbls.algorithm.Solver;
import edu.hust.soict.cbls.common.config.Properties;

public class Test extends Solver {

    public Test(Properties props) {
        super(props);
    }

    public static void main(String[] args) {
        Test test = new Test(new Properties());
        System.out.println("");
    }

    @Override
    public Solution solve() {
        return null;
    }
}
